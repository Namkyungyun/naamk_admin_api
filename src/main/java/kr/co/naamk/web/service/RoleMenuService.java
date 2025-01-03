package kr.co.naamk.web.service;

import kr.co.naamk.web.dto.MenuDto;

import java.util.List;

public interface RoleMenuService {
    void saveMenuPermission(List<MenuDto.MenuPermissionRequest> dto);

    List<MenuDto.PermissionTreeResponse> getPermissionTreeByRoleId(Long roleId);
}
