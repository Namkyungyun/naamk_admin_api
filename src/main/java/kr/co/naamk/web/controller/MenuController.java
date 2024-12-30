package kr.co.naamk.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.MenuDto;
import kr.co.naamk.web.dto.apiResponse.APIResponseEntityBuilder;
import kr.co.naamk.web.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @RequestMapping(value="", method = RequestMethod.POST)
    public Object createMenu(@RequestBody MenuDto.MenuRequest dto,
                             HttpServletRequest request) {
        menuService.createMenu(dto);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .build();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Object getMenusByMenuId(@RequestParam(required = false) Long menuId,
                                                   HttpServletRequest request) {

        List<MenuDto.MenuTreeDetailResponse> menus = menuService.getMenusByMenuId(menuId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(menus)
                .build();
    }

    @RequestMapping(value="/tree", method = RequestMethod.GET)
    public Object getMenusTree(@RequestParam Long userId,
                               HttpServletRequest request) {

        List<MenuDto.MenuTreeResponse> menusByUserId = menuService.getDisplayMenuTreeByUserId(userId);

        return APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SUCCESS)
                .entity(menusByUserId)
                .build();
    }

}
