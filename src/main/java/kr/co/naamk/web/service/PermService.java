package kr.co.naamk.web.service;

import kr.co.naamk.domain.TbPerms;
import kr.co.naamk.web.dto.PermDto;

import java.util.List;

public interface PermService {
    // entity 반환
    List<TbPerms> getPermissions();

    // entity 반환
    TbPerms getPermissionById(Long id);

    void createPermission(PermDto.PermCreateRequest dto);

    // 삭제 -> 연관된 데이터를 삭제안한 경우 serviceException으로 처리
    void deletePermissionById(Long permId);

    // 수정
    void updatePermission(Long id, PermDto.PermUpdateRequest dto);

    // dto로 매핑된 값들로 모두 조회
    List<PermDto.PermListResponse> getAll();

    // 상세가져오기
    PermDto.PermDetailResponse getDetailById(Long permId);


}
