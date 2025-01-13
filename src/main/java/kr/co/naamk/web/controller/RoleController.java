package kr.co.naamk.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.RoleDto;
import kr.co.naamk.web.dto.apiResponse.APIResponseEntityBuilder;
import kr.co.naamk.web.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/roles")
public class RoleController {

    private final RoleService roleService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object createRole(@RequestBody RoleDto.RoleCreateRequest roleDto,
                             HttpServletRequest request) {
        roleService.createRole(roleDto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    @RequestMapping(value="/{roleId}", method = RequestMethod.PUT)
    public Object updateRole(@PathVariable("roleId") Long roleId,
                             @RequestBody RoleDto.RoleUpdateRequest dto,
                             HttpServletRequest request) {
        roleService.updateRole(roleId, dto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    @RequestMapping(value="/{roleId}", method = RequestMethod.DELETE)
    public Object deleteRole(@PathVariable("roleId") Long roleId,
                             HttpServletRequest request) {
        roleService.deleteRoleById(roleId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public Object getAll(HttpServletRequest request) {
        List<RoleDto.RoleListResponse> result = roleService.getAll();

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(result)
                .build();
    }

    @RequestMapping(value="/{roleId}", method = RequestMethod.GET)
    public Object getDetail(@PathVariable("roleId") Long roleId,
                            HttpServletRequest request) {
        RoleDto.RoleDetailResponse result = roleService.getDetailById(roleId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(result)
                .build();
    }
}
