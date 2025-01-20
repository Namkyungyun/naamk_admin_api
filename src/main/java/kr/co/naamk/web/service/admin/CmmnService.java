package kr.co.naamk.web.service.admin;

import kr.co.naamk.web.dto.admin.CmmnDto.*;

import java.util.List;

public interface CmmnService {
    /******** 공통 그룹 코드 ********/
    // [생성] 공통 그룹 코드 생성
    CmmnGrpCreateResponse createCmmnGrp(CmmnGrpCreateRequest dto);

    // [수정] 공통 그룹 코드 수정
    CmmnGrpDetailResponse updateCmmnGrp(Long cmmnGrpId, CmmnGrpUpdateRequest dto);

    // [삭제] 공통 그룹 코드 삭제
    void deleteCmmnGrp(Long cmmnGrpId);

    // [조회] 공통 그룹 코드 전체 조회
    List<CmmnGrpListResponse> getAllCmmnGrps();

    // [조회] 공통 그룹 코드 상세 조회
    CmmnGrpDetailResponse getDetailCmmnGrp(Long cmmnGrpId);

    /******** 공통 코드 ********/
    // [생성] 공통 코드 생성
    void createCmmn(Long cmmnGrpId, CmmnCreateRequest dto);

    // [수정] 공통 코드 수정
    void updateCmmn(Long cmmnGrpId, CmmnUpdateRequest dto);

    // [삭제] 공통 코드 삭제
    void deleteCmmn(Long cmmnId);

    // [조회] 공통 코드 조회 (공통 그룹 코드 id에 의한 리스트)
    List<CmmnListResponse> getAllCmmnsByGrpId(Long cmmnGrpId);

    // [조회] 특정 그룹 내의 공통 코드 상세 조회
    CmmnDetailResponse getDetailCmmnByCmmnGrpId(Long cmmGrpId, Long cmmnId);

    // [조회] 공통 코드 상세 조회
    CmmnDetailResponse getDetailCmmn(Long cmmnId);
}
