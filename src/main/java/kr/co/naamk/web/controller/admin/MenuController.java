package kr.co.naamk.web.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.admin.MenuDto;
import kr.co.naamk.web.dto.apiResponse.APIResponseEntityBuilder;
import kr.co.naamk.web.service.admin.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /** 로그인 > 메뉴탭  */
    @RequestMapping(value="/display-tree", method = RequestMethod.GET)
    public Object getDisplayMenuTree(@RequestParam Long userId, HttpServletRequest request) {
        List<MenuDto.MenuTreeResponse> list = menuService.getDisplayTreeByUserId(userId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(list)
                .build();
    }

    /** 로그인 > 메뉴 관리 > list */
    @RequestMapping(value="/management-tree", method = RequestMethod.GET)
    public Object getManagementMenuTree(@RequestParam(required = false) String menuNm,
                                        @RequestParam(required = false) String menuCd,
                                        HttpServletRequest request) {

        List<MenuDto.MenuTreeResponse> list = menuService.getManagementTreeBySearch(menuCd, menuNm);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(list)
                .build();
    }


    /** 로그인 > 메뉴 관리 > 메뉴 생성 */
    @RequestMapping(value="", method = RequestMethod.POST)
    public Object createMenu(@RequestBody MenuDto.MenuCreateRequest requestBody,
                             HttpServletRequest request) {
        menuService.createMenu(requestBody);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    /** 로그인 > 메뉴 관리 > 메뉴 정렬 수정 */
    @RequestMapping(value = "/alignment", method = RequestMethod.PUT)
    public Object updateMenuAlignment(@RequestBody List<MenuDto.MenuAlignmentUpdateRequest> requestBody,
                                      HttpServletRequest request) {
        // TODO
        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    /** 로그인 > 메뉴 관리 > 메뉴 상세 수정 */
    @RequestMapping(value = "/{menuId}", method = RequestMethod.PUT)
    public Object updateMenu(@PathVariable("menuId") Long menuId,
                             @RequestBody MenuDto.MenuUpdateRequest requestBody,
                             HttpServletRequest request) {
        menuService.updateMenu(menuId, requestBody);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    /** 로그인 > 메뉴 관리 > 메뉴 삭제*/
    @RequestMapping(value="/{menuId}", method = RequestMethod.DELETE)
    public Object deleteMenu(@PathVariable("menuId") Long menuId,
                             HttpServletRequest request) {

        menuService.deleteMenu(menuId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }


    /** 로그인 > 메뉴 관리 > 메뉴 상세*/
    @RequestMapping(value="/{menuId}", method = RequestMethod.GET)
    public Object getMenuById(@PathVariable Long menuId, HttpServletRequest request) {
        MenuDto.MenuDetailResponse menu = menuService.getMenuDetailById(menuId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(menu)
                .build();
    }
}
