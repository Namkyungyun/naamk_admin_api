package kr.co.naamk.web.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.PermDto;
import kr.co.naamk.web.dto.apiResponse.APIResponseEntityBuilder;
import kr.co.naamk.web.service.PermService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/perms")
public class PermController {

    private final PermService permService;

    @RequestMapping(value="", method = RequestMethod.POST)
    public Object createPermission(@RequestBody PermDto.PermCreateRequest dto,
                                   HttpServletRequest request) {
        permService.createPermission(dto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    // updatePermission
    @RequestMapping(value = "/{permId}", method = RequestMethod.PUT)
    public Object updatePermission(@PathVariable("permId") Long permId,
                                   @RequestBody PermDto.PermUpdateRequest dto,
                                   HttpServletRequest request) {
        permService.updatePermission(permId, dto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    // deletePermission
    @RequestMapping(value = "/{permId}", method = RequestMethod.DELETE)
    public Object deletePermission(@PathVariable("permId") Long permId,
                                   HttpServletRequest request) {
        permService.deletePermissionById(permId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Object getAll(HttpServletRequest request) {
        List<PermDto.PermListResponse> result = permService.getAll();

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(result)
                .build();
    }

    // getDetail
    @RequestMapping(value = "/{permId}", method = RequestMethod.GET)
    public Object getDetail(@PathVariable("permId") Long permId,
                            HttpServletRequest request) {
        PermDto.PermDetailResponse result = permService.getDetailById(permId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(result)
                .build(); 
    }


}
