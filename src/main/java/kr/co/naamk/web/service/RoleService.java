package kr.co.naamk.web.service;


import kr.co.naamk.web.dto.RoleDto;

import java.util.List;

public interface RoleService {
    void createRole(RoleDto.RoleRequest role);

    List<RoleDto.RoleResponse> getRoles(Long roleId);
}
