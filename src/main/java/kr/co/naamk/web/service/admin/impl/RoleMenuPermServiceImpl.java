package kr.co.naamk.web.service.admin.impl;

import kr.co.naamk.domain.admin.*;
import kr.co.naamk.exception.ServiceException;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.admin.MenuDto;
import kr.co.naamk.web.dto.admin.RoleMenuPermDto.*;
import kr.co.naamk.web.dto.admin.mapstruct.MenuMapper;
import kr.co.naamk.web.dto.admin.type.BasePermGrpType;
import kr.co.naamk.web.dto.admin.type.BaseRoleType;
import kr.co.naamk.web.repository.admin.jpa.MenuRepository;
import kr.co.naamk.web.repository.admin.jpa.PermGrpRepository;
import kr.co.naamk.web.repository.admin.jpa.RoleMenuPermsRepository;
import kr.co.naamk.web.repository.admin.jpa.RoleRepository;
import kr.co.naamk.web.repository.admin.queryDSL.MenuQueryRepository;
import kr.co.naamk.web.service.admin.RoleMenuPermService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleMenuPermServiceImpl implements RoleMenuPermService {

    private final PermGrpRepository permGrpRepository;
    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;

    private final MenuQueryRepository menuQueryRepository;
    private final RoleMenuPermsRepository roleMenuPermsRepository;


    /** [조회] 역할별 메뉴 권한 트리 */
    @Override
    @Transactional(readOnly = true)
    public List<MenuDto.MenuTreeResponse> getPermissionTreeByRoleId(Long roleId) {
        // 역할 조회
        TbRoles role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ServiceException(ServiceMessageType.ROLE_NOT_FOUND));

        // search 생성
        MenuDto.Search search = new MenuDto.Search();
        search.setRoleIds(List.of(role.getId()));

        // 메뉴트리 재귀 조회
        List<Map<String, Object>> menuTreeMap = menuRepository.nativeFindMenuTreeExceptionRoot();
        List<MenuDto.MenuTree> menuTreeList = MenuMapper.INSTANCE.objsToMenuTreeDTOS(menuTreeMap);

        // 하위 메뉴 조회
        List<TbMenus> childMenus = menuQueryRepository.findMenusBySearch(search);

        // 전체 루트 메뉴 검색
        search.setRootMenu(true);
        List<TbMenus> rootMenus = menuQueryRepository.findMenusBySearch(search);

        // 메뉴트리 만들기
        Map<Long, MenuDto.MenuTreeResponse> rootMenuMap = MenuMapper.INSTANCE.entityToMap(rootMenus, roleId);

        rootMenus.addAll(childMenus);
        Map<Long, MenuDto.MenuTreeResponse> allMenuMap = MenuMapper.INSTANCE.entityToMap(rootMenus, roleId);

        return MenuMapper.INSTANCE.generateMenuTree(rootMenuMap, allMenuMap, menuTreeList);
    }



    /** [수정] 역할별 메뉴 트리 권한 수정 */
    @Override
    @Transactional
    public void updateActivated(Long roleId, List<MenuPermRequest> roleMenuPerms) {
        List<TbRoleMenuPerm> saveList = new ArrayList<>();

        for(MenuPermRequest roleMenuPerm : roleMenuPerms) {
            Long menuId = roleMenuPerm.getMenuId();
            List<MenuPermResponse> perms = roleMenuPerm.getPerms(); // { "permId": "value", "activated": true }

            for (MenuPermResponse perm : perms) {
                Long permId = perm.getPermId();

                TbRoleMenuPermIds compositeKey = new TbRoleMenuPermIds();
                compositeKey.setRole(roleId);
                compositeKey.setMenu(menuId);
                compositeKey.setPerm(permId);

                TbRoleMenuPerm entity = roleMenuPermsRepository.findById(compositeKey)
                        .orElseThrow(() -> new ServiceException(ServiceMessageType.ROLE_MENU_PERMIT_NOT_FOUND,
                                "not found roleId, menuId, permId [" + roleId + "," + menuId + "," + permId + "]"));

                entity.setActivated(perm.isActivated());
                saveList.add(entity);
            }
        }

        roleMenuPermsRepository.saveAll(saveList);
    }

    /** [수정] 메뉴의 root, el 변화에 따른 role-menu-perm 수정 */
    @Transactional
    public void updatePermByMenu(TbMenus menu) {
        // 메뉴가 root, el 확인
        boolean isRootMenu = menu.getParentId() == null;

        // 변경될 메뉴에 넣을 권한
        List<TbPerms> menuRootPerms = findPermByPermGrpCd(BasePermGrpType.MENU_ROOT.getCode());
        List<TbPerms> menuBasePerms = findPermByPermGrpCd(BasePermGrpType.MENU_BASE.getCode());
        List<TbPerms> permsForMenu = isRootMenu ? menuRootPerms : menuBasePerms;

        // menu 따른 모든 role-menu-perm data
        List<TbRoleMenuPerm> roleMenuPerms = menu.getRoleMenuPerms();
        if(isRootMenu) {
            // root의 perm이 아닌 요소는 제거
            List<TbRoleMenuPerm> list = roleMenuPerms.stream()
                    .filter(el -> !permsForMenu.contains(el.getPerm()))
                    .toList();

            roleMenuPerms.removeAll(list); // cascade delete

        } else {
            // base perm이 아닌 요소는 제거
            List<TbRoles> roles = menu.getRoleMenuPerms().stream().map(TbRoleMenuPerm::getRole).toList();
            List<TbPerms> perms = menuBasePerms.stream().filter(el -> !el.equals(menuRootPerms)).toList();

            List<TbRoleMenuPerm> saveList = new ArrayList<>();
            for(TbRoles role : roles ) {
                for(TbPerms perm : perms) {
                    TbRoleMenuPerm entity = new TbRoleMenuPerm();
                    entity.setRole(role);
                    entity.setPerm(perm);
                    entity.setMenu(menu);
                    entity.setActivated(role.getRoleCd().equals(BaseRoleType.SUPER_ADMIN.getCode()));

                    saveList.add(entity);
                }
            }
            roleMenuPerms.addAll(saveList); // cascade
        }
    }


    /** [생성] 메뉴 단일 건 생성에 따른 role-menu-perm 추가하기 */
    @Override
    @Transactional
    public void createByMenu(TbMenus menu) {
        // menu가 루트메뉴인지
        boolean isRootMenu = menu.getParentId() == null;

        // 퍼미션 가져오기
        List<TbPerms> menuRootPerms = findPermByPermGrpCd(BasePermGrpType.MENU_ROOT.getCode());
        List<TbPerms> menuBasePerms = findPermByPermGrpCd(BasePermGrpType.MENU_BASE.getCode());
        List<TbPerms> permsForMenu = isRootMenu ? menuRootPerms : menuBasePerms;


        // roles 에서 슈퍼어드민과 그외 역할들로 분리
        List<TbRoles> roles = roleRepository.findAll();

        TbRoles superAdmin = findSuperAdmin(roles);

        List<TbRoles> elseRoles = new ArrayList<>(roles);
        elseRoles.remove(superAdmin);


        // tbRoleMenuPerm list 생성
        List<TbRoleMenuPerm> saveList = new ArrayList<>();

        // super admin에 대한 role-menu-perm 만들기
        List<TbRoleMenuPerm> menuPermsBySuperAdmin =  generateRoleMenuPermByPermList(superAdmin, menu, permsForMenu, true);
        saveList.addAll(menuPermsBySuperAdmin);

        // 그 외 역할에 대한 role-menu-perm 만들기
        List<TbRoleMenuPerm> menuPermsByElse = elseRoles.stream()
                        .flatMap(role -> generateRoleMenuPermByPermList(role, menu, permsForMenu, false).stream())
                        .toList();
        saveList.addAll(menuPermsByElse);

        // 저장
        roleMenuPermsRepository.saveAll(saveList);
    }




    /** [생성] 역할 단일 건 생성에 따른 role-menu-perm 추가 */
    @Override
    @Transactional
    public void createByRole(TbRoles role) {
        // role의 코드가 슈퍼어드민인지
        boolean isSuperAdmin = BaseRoleType.SUPER_ADMIN.getCode().equals(role.getRoleCd());

        // 메뉴에 대한 perms 가져오기
        List<TbPerms> menuRootPerms = findPermByPermGrpCd(BasePermGrpType.MENU_ROOT.getCode());
        List<TbPerms> menuBasePerms = findPermByPermGrpCd(BasePermGrpType.MENU_BASE.getCode());

        // menus 에서 루트메뉴와 그외 매뉴로 분리
        List<TbMenus> menus = menuRepository.findAll();

        List<TbMenus> rootMenus = menus.stream()
                .filter(menu -> menu.getParentId() == null)
                .toList();

        List<TbMenus> elseMenus = new ArrayList<>(menus);
        menus.removeAll(rootMenus);


        List<TbRoleMenuPerm> saveList = new ArrayList<>();

        // rootMenu에 대한 role-menu-perm 을 만들기
        rootMenus.forEach(menu -> {
            List<TbRoleMenuPerm> roleMenuPerms = generateRoleMenuPermByPermList(role, menu, menuRootPerms, isSuperAdmin);
            saveList.addAll(roleMenuPerms);
        });

        // 그 외 menus에 대한 role-menu-perm 을 만들기
        elseMenus.forEach(menu -> {
            List<TbRoleMenuPerm> roleMenuPerms = generateRoleMenuPermByPermList(role, menu, menuBasePerms, isSuperAdmin);
            saveList.addAll(roleMenuPerms);
        });

        // 저장
        roleMenuPermsRepository.saveAll(saveList);
    }



    private TbRoles findSuperAdmin(List<TbRoles> roles) {
        return roles.stream()
                .filter(role -> BaseRoleType.SUPER_ADMIN.getCode().equals(role.getRoleCd()))
                .findFirst()
                .orElseThrow(() -> new ServiceException(ServiceMessageType.ROLE_NOT_FOUND, "not found super admin"));
    }



    private List<TbPerms> findPermByPermGrpCd(String permGrpCd) {
        TbPermGrps permGrp = permGrpRepository.findByPermGrpCd(permGrpCd)
                .orElseThrow(() -> new ServiceException(ServiceMessageType.PERMISSION_NOT_FOUND));


        return permGrp.getPermGrpPerm().stream()
                .filter(TbPermGrpPerm::isActivated)
                .map(TbPermGrpPerm::getPerm)
                .toList();
    }


    private List<TbRoleMenuPerm> generateRoleMenuPermByPermList(TbRoles role, TbMenus menu,
                                                                List<TbPerms> perms,
                                                                boolean isSuperAdmin) {
        List<TbRoleMenuPerm> result = new ArrayList<>();

        for(TbPerms perm : perms) {
            TbRoleMenuPerm roleMenuPerm = new TbRoleMenuPerm();
            roleMenuPerm.setMenu(menu);
            roleMenuPerm.setRole(role);
            roleMenuPerm.setPerm(perm);
            roleMenuPerm.setActivated(isSuperAdmin);

            result.add(roleMenuPerm);
        }

        return result;
    }


}
