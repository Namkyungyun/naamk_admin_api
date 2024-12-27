package kr.co.naamk.web.controller;

import kr.co.naamk.web.dto.MenuDto;
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
    public ResponseEntity<Object> createMenu(@RequestBody MenuDto.MenuRequest dto) {
        try {
            menuService.createMenu(dto);
            return ResponseEntity.ok("OK");
        } catch(Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Object> getMenusByMenuId(@RequestParam(required = false) Long menuId) {
        try {
            List<MenuDto.MenuTreeDetailResponse> menus = menuService.getMenusByMenuId(menuId);
            return ResponseEntity.ok(menus);
        } catch(Exception e) {
            throw e;
        }
    }

    @RequestMapping(value="/tree", method = RequestMethod.GET)
    public ResponseEntity<Object> getMenusTree(@RequestParam(required = true) Long userId) {
        List<MenuDto.MenuTreeResponse> menusByUserId = menuService.getDisplayMenuTreeByUserId(userId);

        return ResponseEntity.ok(menusByUserId);
    }

}
