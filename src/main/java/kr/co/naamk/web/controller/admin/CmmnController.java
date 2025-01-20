package kr.co.naamk.web.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.admin.CmmnDto;
import kr.co.naamk.web.dto.apiResponse.APIResponseEntityBuilder;
import kr.co.naamk.web.service.admin.CmmnService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CmmnController {

    private final CmmnService cmmnService;

    final String COMMON_GROUP = "/cmmn-grps";
    final String COMMON = "/cmmns";

    /*************** 공통 그룹 코드 ****************/
    // [생성] 그룹 코드 생성
    @RequestMapping(value = COMMON_GROUP, method = RequestMethod.POST)
    public Object createCmmnGrp(@RequestBody CmmnDto.CmmnGrpCreateRequest dto,
                                HttpServletRequest request) {
        CmmnDto.CmmnGrpCreateResponse result = cmmnService.createCmmnGrp(dto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(result)
                .build();
    }

    // [수정] 그룹 코드 수정
    @RequestMapping(value = COMMON_GROUP + "/{grpId}", method = RequestMethod.PUT)
    public Object updateCmmnGrp(@PathVariable("grpId") Long grpId,
                            @RequestBody CmmnDto.CmmnGrpUpdateRequest dto,
                            HttpServletRequest request) {
        CmmnDto.CmmnGrpDetailResponse result = cmmnService.updateCmmnGrp(grpId, dto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(result)
                .build();
    }

    // [삭제] 그룹 모드 삭제
    @RequestMapping(value = COMMON_GROUP + "/{grpId}", method = RequestMethod.DELETE)
    public Object deleteCmmnGrp(@PathVariable("grpId") Long grpId,
                            HttpServletRequest request) {
        cmmnService.deleteCmmnGrp(grpId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    // [조회] 그룹 코드 전체 조회
    @RequestMapping(value = COMMON_GROUP, method = RequestMethod.GET)
    public Object getAllCmmnGrps(HttpServletRequest request) {
        List<CmmnDto.CmmnGrpListResponse> result = cmmnService.getAllCmmnGrps();

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(result)
                .build();
    }

    // [조회] 그룹 코드 상세 조회
    @RequestMapping(value = COMMON_GROUP + "/{grpId}", method = RequestMethod.GET)
    public Object getDetailCmmnGrp(@PathVariable("grpId") Long grpId,
                                   HttpServletRequest request) {
        CmmnDto.CmmnGrpDetailResponse result = cmmnService.getDetailCmmnGrp(grpId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(result)
                .build();
    }

    /*************** 공통 코드 ****************/
    // [생성] 특정 그룹 코드에 속할 공통 코드 생성
    @RequestMapping(value = COMMON_GROUP + "/{grpId}" + COMMON, method = RequestMethod.POST)
    public Object createCmmn(@PathVariable("grpId") Long grpId,
                             @RequestBody CmmnDto.CmmnCreateRequest dto,
                             HttpServletRequest request) {
        cmmnService.createCmmn(grpId, dto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    // [수정] 특정 그룹 상관없이 공통 코드 수정
    @RequestMapping(value = COMMON + "/{id}", method = RequestMethod.PUT)
    public Object updateCmmn(@PathVariable("id") Long id,
                             @RequestBody CmmnDto.CmmnUpdateRequest dto,
                             HttpServletRequest request) {
        cmmnService.updateCmmn(id, dto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    // [삭제] 특정 그룹 상관없이 공통 코드 수정
    @RequestMapping(value = COMMON + "/{id}", method = RequestMethod.DELETE)
    public Object deleteCmmn(@PathVariable("id") Long id,
                             HttpServletRequest request) {
        cmmnService.deleteCmmn(id);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    // [조회] 특정 그룹에 상관없이 common 코드 상세 조회
    @RequestMapping(value = COMMON + "/{id}", method = RequestMethod.GET)
    public Object getDetailCmmn(@PathVariable("id") Long id,
                                HttpServletRequest request) {
        CmmnDto.CmmnDetailResponse result = cmmnService.getDetailCmmn(id);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(result)
                .build();
    }


    // [조회] 특정 그룹 코드에 속하는 common 데이터 리스트 조회
    @RequestMapping(value = COMMON_GROUP + "/{grpId}" + COMMON, method = RequestMethod.GET)
    public Object getAllCmmByCmmnGrpId(@PathVariable("grpId") Long grpId,
                                       HttpServletRequest request) {
        List<CmmnDto.CmmnListResponse> result = cmmnService.getAllCmmnsByGrpId(grpId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(result)
                .build();
    }

    // [조회] 특정 그룹에 속하는 common 코드 상세 조회
    @RequestMapping(value = COMMON_GROUP + "/{grpId}" + COMMON + "/{id}", method = RequestMethod.GET)
    public Object getDetailCmmnInCmmnGrp(@PathVariable("grpId") Long grpId,
                                         @PathVariable("id") Long id,
                                         HttpServletRequest request) {
        CmmnDto.CmmnDetailResponse result = cmmnService.getDetailCmmnByCmmnGrpId(grpId, id);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(result)
                .build();
    }


}
