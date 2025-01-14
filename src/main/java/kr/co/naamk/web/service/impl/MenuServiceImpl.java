package kr.co.naamk.web.service.impl;

import kr.co.naamk.domain.*;
import kr.co.naamk.exception.ServiceException;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.MenuDto;
import kr.co.naamk.web.dto.mapstruct.MenuMapper;
import kr.co.naamk.web.dto.type.BasePermType;
import kr.co.naamk.web.repository.jpa.*;
import kr.co.naamk.web.repository.queryDSL.MenuQueryRepository;
import kr.co.naamk.web.service.MenuService;
import kr.co.naamk.web.service.RoleMenuPermService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.List;

import static kr.co.naamk.web.dto.MenuDto.*;


@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final PermRepository permRepository;
    private final MenuQueryRepository menuQueryRepository;

    private final RoleMenuPermService roleMenuPermService;


    @Override
    @Transactional(readOnly = true)
    public TbMenus getMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new ServiceException(ServiceMessageType.MENU_NOT_FOUND));
    }

    /** 메뉴 관리 > 생성
     *  메뉴 생성 및 역할-메뉴-권한 생성.
     * */
    @Override
    @Transactional
    public void createMenu(MenuCreateRequest dto) {
        // 메뉴 조회
        Optional<TbMenus> menu = menuRepository.findByMenuCdIgnoreCase(dto.getMenuCd());

        if(menu.isPresent()) throw new ServiceException(ServiceMessageType.ALREADY_EXIST);

        // 메뉴 저장
        TbMenus savedMenu = menuRepository.save(MenuMapper.INSTANCE.requestDtoToEntity(dto));

        // 새 메뉴를 역할별로 권한 생성
        roleMenuPermService.createByMenu(savedMenu);

    }

    /** 메뉴 관리 > 수정
     *  메뉴 수정 및 역할-메뉴-권한 또한 수정
     * */
    @Override
    @Transactional
    public void updateMenu(Long menuId, MenuUpdateRequest dto) {
        // !! save로 진행하지 않을 것.
        // 메뉴 조회
        TbMenus menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ServiceException(ServiceMessageType.MENU_NOT_FOUND, "menu id : " + menuId));

        // 메뉴 수정
        menu.setMenuCd(dto.getMenuCd());
        menu.setMenuNm(dto.getMenuNm());
        menu.setMenuDesc(dto.getMenuDesc());
        menu.setOrderNum(dto.getOrderNum());
        menu.setParentId(dto.getParentId());
        menu.setPathUrl(dto.getPathUrl());
        menu.setActivated(dto.isActivated());

        menuRepository.save(menu);

        // 권한 수정 (root -> el : role-menu-permit 데이터 삭제 필요 , el -> root : role-menu-permit 데이터 추가 필요)
        roleMenuPermService.updatePermByMenu(menu);
    }


    /** 메뉴 관리 > 삭제
     * 메뉴 삭제 및 role-menu-perm 연관 데이터 삭제
     * */
    @Override
    public void deleteMenu(Long id) {
        // 데이터 존재여부 조회
        TbMenus menu = menuRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceMessageType.MENU_NOT_FOUND));

        // 종속된 데이터 존재 확인
        List<TbMenus> childMenu = menuRepository.findByParentId(menu.getId());
        if(!childMenu.isEmpty()) {
            List<String> childMenuCds = childMenu.stream()
                    .map(TbMenus::getMenuCd)
                    .toList();

            throw new ServiceException(ServiceMessageType.HAVE_CHILDREN_DATA, "children menus = " + childMenuCds );
        }

        // 데이터 삭제
        menuRepository.delete(menu);
    }



    /**  메뉴 관리 상세
     * 존재하는 메뉴 인지 확인
     * */
    @Override
    @Transactional(readOnly = true)
    public MenuDetailResponse getMenuDetailById(Long id) {
        TbMenus menu = menuRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceMessageType.MENU_NOT_FOUND));

        return MenuMapper.INSTANCE.entityToDetailDTO(menu);
    }



    /**  메뉴 관리 > 조회 (검색 포함)
     * (1) search 생성 <- menuCd, menuNm,
     * (2) 전체 메뉴트리 recursive data 조회
     * (3) child menu 가져오기 <- search / search.isRootMenu = false,
     * (4) (3)번의 결과를 (2)번 메뉴트리에서 instance 추출
     * (5) (4)번의 메뉴트리에서 instancee들의 path를 통해 트리구조상 연관된 id 추출
     * (6) child menu 새로 가져오기 <- search.menuIds = (5)번
     * (7) (4)번의 트리 구조상의 root에 해당하는 id 추출
     * (8) root menu 가져오기 <- search.menuIds = (7)번, search.isRootMenu = true
     * (9) menu tree object 생성 -> return object
     * */
    @Override
    @Transactional(readOnly = true)
    public List<MenuTreeResponse> getManagementTreeBySearch(String menuCd, String menuNm) {

        // search 생성
        Search search = new Search();
        search.setMenuCd(menuCd);
        search.setMenuNm(menuNm);

        // 메느 튜리 조회
        List<Map<String, Object>> menuTreeMap = menuRepository.nativeFindMenuTreeExceptionRoot();
        List<MenuTree> menuTreeList = MenuMapper.INSTANCE.objsToMenuTreeDTOS(menuTreeMap);

        // 검색 조건을 가진 자식 메뉴 조회
        List<TbMenus> childMenus  = menuQueryRepository.findMenusBySearch(search);

        // 검색 결과로 나온 자식 메뉴를 메뉴 트리에서 추출
        List<MenuTree> menusInMenuTree = extractMenuInMenuTree(childMenus, menuTreeList);

        // associated id 필터
        List<Long> associatedIds = extractAssociatedIds(menusInMenuTree);

        // 위의 자식 조건을 가지고 연관된 모든 자식 메뉴들 가지고 오도로 재 조회
        search.setMenuIds(associatedIds);
        childMenus  = menuQueryRepository.findMenusBySearch(search);

        // 루트 메뉴 조회
        // 연관된 root id 필터
        List<Long> rootIds = extractRootIds(menusInMenuTree);

        search.setRootMenu(true);
        search.setMenuIds(rootIds);
        List<TbMenus> rootMenus = menuQueryRepository.findMenusBySearch(search);


        // 메뉴 트리 만들기
        Map<Long, MenuDto.MenuTreeResponse> rootMenuMap = MenuMapper.INSTANCE.entityToMap(rootMenus);

        rootMenus.addAll(childMenus);
        Map<Long, MenuDto.MenuTreeResponse> allMenuMap = MenuMapper.INSTANCE.entityToMap(rootMenus);

        return MenuMapper.INSTANCE.generateMenuTree(rootMenuMap, allMenuMap, menuTreeList);
    }

    // 검색 결과 조회된 메뉴를 메뉴트리 instance로 추출
    private List<MenuTree> extractMenuInMenuTree(List<TbMenus> searchedMenus, List<MenuTree> menuTree) {
        List<Long> ids = searchedMenus.stream().map(TbMenus::getId).toList();

        return menuTree.stream()
                .filter(menu -> ids.contains(menu.getId()))
                .toList();
    }

    // 메뉴 트리에 연관된 id 추출
    private List<Long> extractAssociatedIds(List<MenuTree> menuTree) {
        return menuTree.stream()
                .flatMap(menu -> Arrays.stream(menu.getPath().split(",")))
                .map(Long::parseLong)
                .distinct()
                .toList();
    }

    // 메뉴 트리에 연관된 root id 추출
    private List<Long> extractRootIds(List<MenuTree> menuTree) {
        return menuTree.stream()
                .map(MenuTree::getRootId)
                .distinct()
                .toList();
    }



    /**  메뉴탭 > 조회
     * (1) userId -> List<Long> roleIds
     * (2) roleIds -> perm id for read permission
     * (3) search 생성 <- roleIds, permIds, isDisplay
     * (4) 전체 메뉴트리 recursive data 조회
     * (5) child menu 가져오기 <- search / search.isRootMenu = false
     * (6) root menu 가져오기 <- search / search.isRootMenu = true
     * (7) menuTree object 생성 -> return object
     * */
    @Override
    @Transactional(readOnly = true)
    public List<MenuTreeResponse> getDisplayTreeByUserId(Long userId) {
        // 유저 조회
        TbUsers user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ServiceMessageType.USER_NOT_FOUND));

        // 해당 유저의 모든 role 조회
        List<Long> roleIds = user.getUserRoles().stream()
                .map(role -> role.getRole().getId())
                .toList();

        // 조회 권한 id 조회
        Long permId = permRepository.findByPermCd(BasePermType.READ.getCode())
                .orElseThrow(()-> new ServiceException(ServiceMessageType.PERMISSION_NOT_FOUND))
                .getId();

        // search 생성
        Search search = new Search();
        search.setRoleIds(roleIds);
        search.setPermIds(List.of(permId));
        search.setDisplay(true);
        search.setActivated(true);

        // 전체 메뉴 트리 재귀 쿼리 조회
        List<Map<String, Object>> menuTreeMap = menuRepository.nativeFindMenuTreeExceptionRoot();
        List<MenuTree> menuTreeList = MenuMapper.INSTANCE.objsToMenuTreeDTOS(menuTreeMap);

        // 하위 메뉴 조회
        List<TbMenus> childMenus = menuQueryRepository.findMenusBySearch(search);

        // 전체 root메뉴 조회
        search.setRootMenu(true);
        List<TbMenus> rootMenus = menuQueryRepository.findMenusBySearch(search);

        // 메뉴 트리 만들기
        Map<Long, MenuDto.MenuTreeResponse> rootMenuMap = MenuMapper.INSTANCE.entityToMap(rootMenus, roleIds);

        rootMenus.addAll(childMenus);
        Map<Long, MenuDto.MenuTreeResponse> allMenuMap = MenuMapper.INSTANCE.entityToMap(rootMenus, roleIds);

        return MenuMapper.INSTANCE.generateMenuTree(rootMenuMap, allMenuMap, menuTreeList);
    }
}
