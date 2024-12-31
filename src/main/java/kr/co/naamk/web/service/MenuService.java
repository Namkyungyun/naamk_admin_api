package kr.co.naamk.web.service;

import kr.co.naamk.web.dto.MenuDto;
import java.util.List;

public interface MenuService {

    void createMenu(MenuDto.MenuRequest dto);

    List<MenuDto.MenuTreeDetailResponse> getMenusByMenuId(Long menuId);

    List<MenuDto.MenuTreeResponse> getDisplayMenuTreeByUserId(Long userId);

}
