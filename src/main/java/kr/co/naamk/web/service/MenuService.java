package kr.co.naamk.web.service;

import kr.co.naamk.web.dto.MenuDto;

import java.util.List;

public interface MenuService {

    void createMenu(MenuDto.CreateRequest dto);

    void deleteMenu(Long id);

    List<MenuDto.MenuTreeResponse> getDisplayTreeByUserId(Long userId);

    List<MenuDto.MenuTreeResponse> getManagementTreeBySearch(String menuNm, String menuCd);

    MenuDto.MenuDetailResponse getMenuDetailById(Long id);

    // 수정도 필요
    // createRequestDto와 동일하게 들어가는 규격인데
    // 사용 권한 수정 (Group code를 select 옵션으로 가져간다고 생각하기)
}
