package kr.co.naamk.web.service.admin;

import kr.co.naamk.domain.admin.TbMenus;
import kr.co.naamk.web.dto.admin.MenuDto;

import java.util.List;

public interface MenuService {

    TbMenus getMenu(Long menuId);

    void createMenu(MenuDto.MenuCreateRequest dto);

    void updateMenu(Long menuId, MenuDto.MenuUpdateRequest dtos);

    void deleteMenu(Long id);

    List<MenuDto.MenuTreeResponse> getDisplayTreeByUserId(Long userId);

    List<MenuDto.MenuTreeResponse> getManagementTreeBySearch(String menuNm, String menuCd);

    MenuDto.MenuDetailResponse getMenuDetailById(Long id);

}
