package kr.co.naamk.web.service.impl;


import kr.co.naamk.domain.*;
import kr.co.naamk.exception.ServiceException;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.MenuDto;
import kr.co.naamk.web.dto.mapstruct.MenuMapper;
import kr.co.naamk.web.repository.jpa.*;
import kr.co.naamk.web.repository.queryDSL.MenuQueryRepository;
import kr.co.naamk.web.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final MenuQueryRepository menuQueryRepository;

    /** 메뉴 생성*/
    @Override
    @Transactional
    public void saveMenu(MenuDto.CreateOrUpdateRequest dto) {
        TbMenus menu = MenuMapper.INSTANCE.requestDtoToEntity(dto);
        menuRepository.save(menu);
    }


    @Override
    public void deleteMenu(Long id) {
        // 데이터 존재여부 조회
        TbMenus menu = menuRepository.findById(id).orElseThrow(() -> new ServiceException(ServiceMessageType.MENU_NOT_FOUND));

        // 종속된 데이터 존재 확인
        List<TbMenus> childMenu = menuRepository.findByParentId(menu.getId());
        if(!childMenu.isEmpty()) {
            List<String> childMenuCds = childMenu.stream().map(TbMenus::getMenuCd).toList();
            throw new ServiceException(ServiceMessageType.HAVE_CHILDREN_DATA, "children menus = " + childMenuCds );
        }

        // 데이터 삭제
        menuRepository.delete(menu);
    }

    /** 로그인 유저 이후 메뉴탭 불러오기
     * : userId -> roleId -> 해당 role에 조회 권한이 없을 시,
     *   메뉴트리 내에서 해당 메뉴 확인불가 (생성, 삭제, 수정 권한이 있어도)
     * */
    @Override
    @Transactional(readOnly = true)
    public List<MenuDto.DisplayTreeResponse> getDisplayTreeByUserId(Long userId) {
        // 유저 확인
        TbUsers tbUsers = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ServiceMessageType.USER_NOT_FOUND));

        // TODO !!!! role을 list로 받아서 처리할 수 있도록 수정 필요
        Long roleId = tbUsers.getUserRoles().getFirst().getRole().getId();

        // MenuDto.MenuPermissionSearch 생성
        MenuDto.MenuPermissionSearch search = MenuDto.MenuPermissionSearch.builder()
                .roleId(roleId)
                .isDisplay(true)
                .build();

        // 1. parent 메뉴 조회
        List<TbMenus> menuWithPerms = menuQueryRepository.findMenuWithPermsBySearch(search);
        // 2. parent DTOs 변환
        List<MenuDto.DisplayTreeResponse> menuWithPermsDTOs
                = MenuMapper.INSTANCE.entitiesToDisplayTreeDTOs(menuWithPerms);
        // 3. 자식 메뉴 재귀
        menuWithPermsDTOs.forEach(rootMenu -> setChildrenMenuTree(rootMenu, rootMenu.getId(), search));

        return menuWithPermsDTOs;
    }

    /** 재귀 -> '메뉴탭 트리' 사용**/
    private void setChildrenMenuTree(Object parentDTO, Long parentId,
                                     MenuDto.MenuPermissionSearch parentSearch) {
        // findMenuWithPermsBySearch 인자를 위한  상위 메뉴 id값 넣기
        parentSearch.setParentId(parentId);

        // parentId = search.getParentId() 데이터 조회
        List<TbMenus> children = menuQueryRepository.findMenuWithPermsBySearch(parentSearch);

        // dto 변환
        List<MenuDto.DisplayTreeResponse> childrenDTOs =  MenuMapper.INSTANCE.entitiesToDisplayTreeDTOs(children);

        // 재귀 (차일드 데이터가 없다면 재귀가 동작 x)
        childrenDTOs.forEach(dto -> setChildrenMenuTree(dto, dto.getId(), parentSearch));

        // 부모 DTO에 childrenDTOS 넣기
        ((MenuDto.DisplayTreeResponse) parentDTO).setChildren(childrenDTOs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDto.ManagementTreeResponse> getManagementTreeBySearch(String menuCd, String menuNm) {

        // if menuNm, menuCd are null 인 경우 -> parentId is null인 root menu 전체 데이터 반환
        if((menuNm == null || menuNm.isBlank()) && (menuCd == null || menuCd.isBlank())) {
            List<TbMenus> rootMenus = menuRepository.findByParentIdIsNull();
            return MenuMapper.INSTANCE.entitiesToManagementTreeDTOs(rootMenus);
        }

        // if menuNm, menuCd are not null 인 경우 -> parentId로 역재귀로 데이터 조회.
        if(menuCd == null) menuCd = "";
        if(menuNm == null) menuNm = "";

        // 대소문자 구분없이 검색될 수 있도록 (Containing 적용 시, 중복 데이터 발생함)
        List<TbMenus> menus = menuRepository.findByMenuCdIgnoreCaseOrMenuNmIgnoreCase(menuCd, menuNm);
        // DTO 변환
        List<MenuDto.ManagementTreeResponse> dtos = MenuMapper.INSTANCE.entitiesToManagementTreeDTOs(menus);

        List<MenuDto.ManagementTreeResponse> result = new ArrayList<>();
        for(MenuDto.ManagementTreeResponse dto : dtos) {
            final Long parentId = dto.getParentId();
            // parentId가 null 인 경우 그대로 dto 민, 외 경우 역재귀(부모 찾기)
            result.add(parentId == null ? dto : setParentMenuTree(dto));
        }

        return result;
    }

    /** 역재귀(부모 찾기) */
    private MenuDto.ManagementTreeResponse setParentMenuTree(MenuDto.ManagementTreeResponse childMenu) {
        TbMenus parent = menuRepository.findById(childMenu.getParentId())
                .orElseThrow(() -> new ServiceException(ServiceMessageType.MENU_NOT_FOUND));

        MenuDto.ManagementTreeResponse parentDto = MenuMapper.INSTANCE.entityToManagementTreeDTO(parent);
        parentDto.getChildren().add(childMenu);

        if(parent.getParentId() != null) {
            return setParentMenuTree(parentDto);
        } else {
            return parentDto;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MenuDto.MenuDetailResponse getMenuDetailById(Long id) {
        TbMenus menu = menuRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceMessageType.MENU_NOT_FOUND));

        return MenuMapper.INSTANCE.entityToDetailDTO(menu);
    }
}
