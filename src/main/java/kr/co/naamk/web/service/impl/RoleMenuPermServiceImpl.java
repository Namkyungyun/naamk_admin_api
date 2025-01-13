package kr.co.naamk.web.service.impl;

import kr.co.naamk.domain.*;
import kr.co.naamk.exception.ServiceException;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.MenuDto;
import kr.co.naamk.web.dto.RoleMenuPermDto.*;
import kr.co.naamk.web.dto.mapstruct.MenuMapper;
import kr.co.naamk.web.dto.type.PermActionType;
import kr.co.naamk.web.dto.type.RoleType;
import kr.co.naamk.web.repository.jpa.RoleMenuPermsRepository;
import kr.co.naamk.web.repository.jpa.MenuRepository;
import kr.co.naamk.web.repository.jpa.PermRepository;
import kr.co.naamk.web.repository.jpa.RoleRepository;
import kr.co.naamk.web.repository.queryDSL.MenuQueryRepository;
import kr.co.naamk.web.service.RoleMenuPermService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleMenuPermServiceImpl implements RoleMenuPermService {

    private final PermRepository permRepository;
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
        List<TbPerms> allPerm = permRepository.findAll();
        TbPerms readPerm = findReadPerm(allPerm);
        List<TbPerms> permsForMenu = isRootMenu ? List.of(readPerm) : allPerm;

        // menu 따른 모든 role-menu-perm data
        List<TbRoleMenuPerm> roleMenuPerms = menu.getRoleMenuPerms();
        if(isRootMenu) {
            List<TbRoleMenuPerm> list = roleMenuPerms.stream()
                    .filter(el -> !permsForMenu.contains(el.getPerm()))
                    .toList();

            roleMenuPerms.removeAll(list); // cascade delete

        } else {
            List<TbRoles> roles = menu.getRoleMenuPerms().stream().map(TbRoleMenuPerm::getRole).toList();
            List<TbPerms> perms = allPerm.stream().filter(el -> !el.equals(readPerm)).toList();

            List<TbRoleMenuPerm> saveList = new ArrayList<>();
            for(TbRoles role : roles ) {
                for(TbPerms perm : perms) {
                    TbRoleMenuPerm entity = new TbRoleMenuPerm();
                    entity.setRole(role);
                    entity.setPerm(perm);
                    entity.setMenu(menu);
                    entity.setActivated(role.getRoleCd().equals(RoleType.SUPER_ADMIN.getCode()));

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
        List<TbPerms> perms = permRepository.findAll();

        TbPerms readPerm = findReadPerm(perms);
        List<TbPerms> permsForMenu = isRootMenu ? List.of(readPerm) : perms;


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
        boolean isSuperAdmin = RoleType.SUPER_ADMIN.getCode().equals(role.getRoleCd());

        // 메뉴에 대한 perms 모든 가져오기 -> TODO 추후 permission 디벨롭할 때 변경할 것.
        List<TbPerms> perms = permRepository.findAll();
        TbPerms readPerm = findReadPerm(perms);


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
            List<TbRoleMenuPerm> roleMenuPerms = generateRoleMenuPermByPermList(role, menu, List.of(readPerm), isSuperAdmin);
            saveList.addAll(roleMenuPerms);
        });

        // 그 외 menus에 대한 role-menu-perm 을 만들기
        elseMenus.forEach(menu -> {
            List<TbRoleMenuPerm> roleMenuPerms = generateRoleMenuPermByPermList(role, menu, perms, isSuperAdmin);
            saveList.addAll(roleMenuPerms);
        });

        // 저장
        roleMenuPermsRepository.saveAll(saveList);
    }



    private TbRoles findSuperAdmin(List<TbRoles> roles) {
        return roles.stream()
                .filter(role -> RoleType.SUPER_ADMIN.getCode().equals(role.getRoleCd()))
                .findFirst()
                .orElseThrow(() -> new ServiceException(ServiceMessageType.ROLE_NOT_FOUND, "not found super admin"));
    }


    private TbPerms findReadPerm(List<TbPerms> perms) {
        return perms.stream()
                .filter(perm -> PermActionType.READ.getCode().equals( perm.getPermCd()))
                .findFirst()
                .orElseThrow(() -> new ServiceException(ServiceMessageType.ROLE_NOT_FOUND, "not found read perm"));
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
