package kr.co.naamk.web.service.impl;

import kr.co.naamk.domain.*;
import kr.co.naamk.exception.ServiceException;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.MenuDto;
import kr.co.naamk.web.dto.PermDto;
import kr.co.naamk.web.dto.RoleMenuPermDto;
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

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RoleMenuPermPermServiceImpl implements RoleMenuPermService {

    private final PermRepository permRepository;
    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;

    private final MenuQueryRepository menuQueryRepository;
    private final RoleMenuPermsRepository roleMenuPermsRepository;


    /** [생성] 메뉴 단일 건 생성에 따른 role-menu-perm 추가하기 */
    @Override
    @Transactional
    public void createRoleMenuPermsByMenu(TbMenus menu) {

        // 모든 role 가져오기
        List<TbRoles> roles = roleRepository.findAll();

        // roles 에서 슈퍼어드민과 그외 역할들로 분리
        TbRoles superAdmin = findSuperAdmin(roles);
        List<TbRoles> elseRoles = new ArrayList<>(roles);
        elseRoles.remove(superAdmin);

        // 퍼미션 가져오기
        List<TbPerms> perms = permRepository.findAll();

        // tbRoleMenuPerm list 생성
        List<TbRoleMenuPerm> menuPermsBySuperAdmin =  generateRoleMenuPermByPermList(superAdmin, menu, perms, true);
        List<TbRoleMenuPerm> menuPermsByElse = elseRoles.stream()
                        .flatMap(role -> generateRoleMenuPermByPermList(role, menu, perms, false).stream())
                        .toList();

        List<TbRoleMenuPerm> saveList = new ArrayList<>();
        saveList.addAll(menuPermsBySuperAdmin);
        saveList.addAll(menuPermsByElse);

        // 저장
        roleMenuPermsRepository.saveAll(saveList);
    }


    @Override
    @Transactional
    public void createRoleMenuPermsByRole(TbRoles role) {
        // role의 코드가 슈퍼어드민인지
        boolean isSuperAdmin = RoleType.SUPER_ADMIN.getCode().equals(role.getRoleCd());

        // 모든 메뉴 가져오기
        List<TbMenus> menus = menuRepository.findAll();

        // menus 에서 루트메뉴와 그외 매뉴로 분리
        List<TbMenus> rootMenus = menus.stream()
                .filter(menu -> menu.getParentId() == null)
                .toList();

        List<TbMenus> elseMenus = new ArrayList<>(menus);
        menus.removeAll(rootMenus);

        // 메뉴에 대한 perms 모든 가져오기 -> TODO 추후 permission 디벨롭할 때 변경할 것.
        List<TbPerms> perms = permRepository.findAll();
        TbPerms readPerm = findReadPerm(perms);


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



    /** 역할별로 메뉴에 대한 권한을 생성 **/
    @Override
    @Transactional
    public void createRoleMenuPerms() {

    }

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
        return MenuMapper.INSTANCE.generateMenuTree(rootMenus, childMenus, menuTreeList, RoleMenuPermDto.MenuPerm.class);
    }
}
