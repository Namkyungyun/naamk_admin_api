package kr.co.naamk.web.service;

import kr.co.naamk.web.dto.MenuDto;

import java.util.List;

public interface MenuService {

    void saveMenu(MenuDto.CreateOrUpdateRequest dto);

//    void saveMenuPermission(List<MenuDto.MenuPermissionRequest> dto);

    void deleteMenu(Long id);

    List<MenuDto.DisplayTreeResponse> getDisplayTreeByUserId(Long userId);

    List<MenuDto.ManagementTreeResponse> getManagementTreeBySearch(String menuNm, String menuCd);

    MenuDto.MenuDetailResponse getMenuDetailById(Long id);

//    List<MenuDto.PermissionTreeResponse> getPermissionTreeByRoleId(Long roleId);
}
