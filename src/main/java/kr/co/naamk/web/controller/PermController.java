package kr.co.naamk.web.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.PermDto;
import kr.co.naamk.web.dto.apiResponse.APIResponseEntityBuilder;
import kr.co.naamk.web.service.PermService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/permission")
public class PermController {

    private final PermService permService;

    @RequestMapping(value="", method = RequestMethod.POST)
    public Object createPermission(@RequestBody PermDto permDto,
                                   HttpServletRequest request) {
        permService.createPermission(permDto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Object getPermissions(@RequestParam @Nullable Long permId,
                                 HttpServletRequest request) {
        List<PermDto.PermResponse> permissions = permService.getPermissions(permId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(permissions)
                .build();
    }


}
