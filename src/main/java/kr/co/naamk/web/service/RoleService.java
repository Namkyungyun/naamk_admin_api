package kr.co.naamk.web.service;


import kr.co.naamk.domain.TbRoles;
import kr.co.naamk.web.dto.RoleDto;

import java.util.List;

public interface RoleService {
    List<TbRoles> getRoles();

    TbRoles getRoleById(Long id);

    void createRole(RoleDto.RoleCreateRequest dto);

    void updateRole(Long id, RoleDto.RoleUpdateRequest dto);

    void deleteRoleById(Long id);

    RoleDto.RoleDetailResponse getDetailById(Long id);

    List<RoleDto.RoleListResponse> getAll();
}
