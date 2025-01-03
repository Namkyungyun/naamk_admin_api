package kr.co.naamk.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.MenuDto;
import kr.co.naamk.web.dto.apiResponse.APIResponseEntityBuilder;
import kr.co.naamk.web.service.RoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role-menu")
@RequiredArgsConstructor
public class MenuPermController {

    private final RoleMenuService roleMenuService;

    /** 로그인 > 역할별 메뉴 권한 관리 > {역할}-{메뉴}-[권한] 생성 및 수정 */
    @RequestMapping(value="/role-perms", method = RequestMethod.POST)
    public Object saveMenuPermissionWithRole(@RequestBody List<MenuDto.MenuPermissionRequest> dto,
                                             HttpServletRequest request) {
        roleMenuService.saveMenuPermission(dto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    /** 로그인 > 역할별 메뉴 권한 관리 > list */
    @RequestMapping(value="/permission-tree", method = RequestMethod.GET)
    public Object getPermissionMenuTree(@RequestParam Long roleId, HttpServletRequest request) {
        List<MenuDto.PermissionTreeResponse> list = roleMenuService.getPermissionTreeByRoleId(roleId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(list)
                .build();
    }

}
