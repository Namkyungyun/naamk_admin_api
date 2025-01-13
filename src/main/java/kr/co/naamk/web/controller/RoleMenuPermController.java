package kr.co.naamk.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.naamk.domain.TbMenus;
import kr.co.naamk.domain.TbRoles;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.MenuDto;
import kr.co.naamk.web.dto.RoleMenuPermDto;
import kr.co.naamk.web.dto.apiResponse.APIResponseEntityBuilder;
import kr.co.naamk.web.service.MenuService;
import kr.co.naamk.web.service.RoleMenuPermService;
import kr.co.naamk.web.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role-menu")
@RequiredArgsConstructor
public class RoleMenuPermController {

    private final RoleService roleService;
    private final MenuService menuService;
    private final RoleMenuPermService roleMenuPermService;

    /** 로그인 > 역할별 메뉴 권한 관리 > list */
    @RequestMapping(value="/{roleId}", method = RequestMethod.GET)
    public Object getPermissionMenuTree(@PathVariable("roleId") Long roleId, HttpServletRequest request) {
        List<MenuDto.MenuTreeResponse> list = roleMenuPermService.getPermissionTreeByRoleId(roleId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(list)
                .build();
    }

    /** 로그인 > 역할별 메뉴 권한 관리 > 권한 수정 */
    @RequestMapping(value="/{roleId}", method = RequestMethod.PUT)
    public Object updateRoleMenuPerm(@PathVariable("roleId") Long roleId,
                                     @RequestBody List<RoleMenuPermDto.MenuPermRequest> roleMenuPerms,
                                     HttpServletRequest request) {
        roleMenuPermService.updateActivated(roleId, roleMenuPerms);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    /** 하나의 역할에 대해 모든 메뉴 role-menu-perm 생성 */
    @RequestMapping(value="/role/{roleId}", method = RequestMethod.POST)
    public Object createRoleMenuPermByRole(@PathVariable("roleId") Long roleId,
                                     HttpServletRequest request) {

        TbRoles role = roleService.getRoleById(roleId);
        roleMenuPermService.createByRole(role);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    /** 하나의 메뉴에 대해 모든 역할에 role-menu-perm 생성*/
    @RequestMapping(value="/menu/{menuId}", method = RequestMethod.POST)
    public Object createRoleMenuPermByMenu(@PathVariable("menuId") Long menuId,
                                     HttpServletRequest request) {

        TbMenus menu = menuService.getMenu(menuId);
        roleMenuPermService.createByMenu(menu);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }
}
