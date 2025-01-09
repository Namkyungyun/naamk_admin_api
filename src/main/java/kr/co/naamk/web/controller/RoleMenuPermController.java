package kr.co.naamk.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.MenuDto;
import kr.co.naamk.web.dto.RoleMenuPermDto;
import kr.co.naamk.web.dto.apiResponse.APIResponseEntityBuilder;
import kr.co.naamk.web.service.RoleMenuPermService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role-menu")
@RequiredArgsConstructor
public class RoleMenuPermController {

    private final RoleMenuPermService roleMenuPermService;

    /** 로그인 > 역할별 메뉴 권한 관리 > list */ // TODO !!!!
    @RequestMapping(value="/{roleId}", method = RequestMethod.GET)
    public Object getPermissionMenuTree(@PathVariable("roleId") Long roleId, HttpServletRequest request) {
        List<MenuDto.MenuTreeResponse> list = roleMenuPermService.getPermissionTreeByRoleId(roleId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(list)
                .build();
    }

}
