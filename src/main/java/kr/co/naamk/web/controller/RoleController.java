package kr.co.naamk.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.RoleDto;
import kr.co.naamk.web.dto.apiResponse.APIResponseEntityBuilder;
import kr.co.naamk.web.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/role")
public class RoleController {

    private final RoleService roleService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object saveRole(@RequestBody RoleDto.RoleRequest roleDto,
                           HttpServletRequest request) {

        roleService.createRole(roleDto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public Object getRoles(@RequestParam(required = false) Long roleId,
                           HttpServletRequest request) {

        List<RoleDto.RoleResponse> list = roleService.getRoles(roleId);
        Object result = (roleId != null) ? list.getFirst() : list;

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(result)
                .build();
    }
}
