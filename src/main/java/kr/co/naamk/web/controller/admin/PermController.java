package kr.co.naamk.web.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.naamk.exception.ServiceException;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.admin.PermDto;
import kr.co.naamk.web.dto.admin.PermGrpDto;
import kr.co.naamk.web.dto.apiResponse.APIResponseEntityBuilder;
import kr.co.naamk.web.service.admin.PermService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/perms")
public class PermController {

    private final PermService permService;


    // 권한 그룹 만들기
    @RequestMapping(value="/grp", method = RequestMethod.POST)
    public Object createPermGrp(@RequestBody PermGrpDto.PermGrpCreateRequest dto,
                                HttpServletRequest request) throws ServiceException {
        permService.createPermGrp(dto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }


    
    // 권한 그룹 전체 조회
    @RequestMapping(value="/grp", method = RequestMethod.GET)
    public Object getAllPermGrp(HttpServletRequest request) {
        List<PermGrpDto.PermGrpListResponse> result = permService.getAllPermGrp();

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(result)
                .build();
    }



    // 권한 그룹 상세 조회
    @RequestMapping(value = "/grp/{permGrpId}", method = RequestMethod.GET)
    public Object getDetailPermGrp(@PathVariable("permGrpId") Long permGrpId,
                                   HttpServletRequest request) {
        PermGrpDto.PermGrpDetailResponse result = permService.getDetailPermGrp(permGrpId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(result)
                .build();
    }



    // 권한 그룹 수정하기
    @RequestMapping(value="/grp/{permGrpId}", method = RequestMethod.PUT)
    public Object updatePermGrp(@PathVariable("permGrpId") Long permGrpId,
                                @RequestBody PermGrpDto.PermGrpUpdateRequest dto,
                                HttpServletRequest request) {
        permService.updatePermGrp(permGrpId, dto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }



    // 권한 그룹 삭제하기
    // 데이터 삭제 후 삽입으로 인해 테이블 스페이스가 커질 수 있으므로,
    // "정기적인 정리 작업 (Vacuum, Optimize)" 해야 함.
    @RequestMapping(value="/grp/{permGrpId}", method = RequestMethod.DELETE)
    public Object deletePermGrp(@PathVariable("permGrpId") Long permGrpId,
                                HttpServletRequest request) {
        permService.deletePermGrpById(permGrpId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }



    // [생성] 권한 일반 생성
    @RequestMapping(value="", method = RequestMethod.POST)
    public Object createPerm(@RequestBody PermDto.PermCreateRequest dto,
                             HttpServletRequest request) {
        permService.createPermission(dto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }



    // [수정] 권한 일반 수정
    @RequestMapping(value = "/{permId}", method = RequestMethod.PUT)
    public Object updatePerm(@PathVariable("permId") Long permId,
                             @RequestBody PermDto.PermUpdateRequest dto,
                             HttpServletRequest request) {
        permService.updatePermission(permId, dto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }



    // [삭제] 권한 일반 삭제
    @RequestMapping(value = "/{permId}", method = RequestMethod.DELETE)
    public Object deletePerm(@PathVariable("permId") Long permId,
                             HttpServletRequest request) {
        permService.deletePermissionById(permId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }



    // [조회] 권한 일반 전체 조회
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Object getAllPerm(HttpServletRequest request) {
        List<PermDto.PermListResponse> result = permService.getAllPerm();

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(result)
                .build();
    }



    // [조회] 권한 일반 상세 조회
    @RequestMapping(value = "/{permId}", method = RequestMethod.GET)
    public Object getDetailPerm(@PathVariable("permId") Long permId,
                                HttpServletRequest request) {
        PermDto.PermDetailResponse result = permService.getDetailPermById(permId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(result)
                .build(); 
    }
}
