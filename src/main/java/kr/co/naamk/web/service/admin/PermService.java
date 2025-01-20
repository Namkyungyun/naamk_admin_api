package kr.co.naamk.web.service.admin;

import kr.co.naamk.domain.admin.TbPermGrps;
import kr.co.naamk.domain.admin.TbPerms;
import kr.co.naamk.web.dto.admin.PermDto;
import kr.co.naamk.web.dto.admin.PermGrpDto;

import java.util.List;

public interface PermService {

    /************ perm group CRUD ************/
    // [생성] 권한 그룹 생성
    void createPermGrp(PermGrpDto.PermGrpCreateRequest dto);

    // [수정] 권한 그룹 수정
    void updatePermGrp(Long id, PermGrpDto.PermGrpUpdateRequest dto);

    // [삭제] 권한 그룹 삭제 -> 물리 삭제 연결
    void deletePermGrpById(Long permGrpId);

    // [조회] 상세 조회 (상세 조회 소속 권한 까지 나오게 하기)
    PermGrpDto.PermGrpDetailResponse getDetailPermGrp(Long permGrpId);

    // [조회] 전체 조회
    List<PermGrpDto.PermGrpListResponse> getAllPermGrp();

    // [조회] return entity
    TbPermGrps getPermGrpById(Long permGrpId);

    List<TbPermGrps> getPermGrps();

    /************  perm CRUD ************/
    // entity 반환
    List<TbPerms> getPerms();

    // entity 반환
    TbPerms getPermById(Long id);

    void createPermission(PermDto.PermCreateRequest dto);

    // 삭제 -> 연관된 데이터를 삭제안한 경우 serviceException으로 처리
    void deletePermissionById(Long permId);

    // 수정
    void updatePermission(Long id, PermDto.PermUpdateRequest dto);

    // dto로 매핑된 값들로 모두 조회
    List<PermDto.PermListResponse> getAllPerm();

    // 상세가져오기
    PermDto.PermDetailResponse getDetailPermById(Long permId);


    /************ perm group, perm mapping CRUD ************/
    // [생성] perm 생성 시 -> 모든 permGrp의 하위값으로 추가
    void createMappingDataByPerm(TbPerms perm, List<TbPermGrps> permGrps);


    // [생성] permGrp 생성에 따른 모든 perm 하위값 추가
    void createMappingDataByPermGrp(TbPermGrps permGrp, List<TbPerms> perms);

    // [수정] perGrp에 속하는 perm activated 수정
    void updateMappingDataActivated(TbPermGrps permGrp, List<PermGrpDto.ChildPermStatus> childPermStatuses);

}
