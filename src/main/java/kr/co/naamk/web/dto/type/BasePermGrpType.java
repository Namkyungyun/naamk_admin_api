package kr.co.naamk.web.dto.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BasePermGrpType {
    MENU_ROOT("MENU_ROOT", "메뉴 루트"),
    MENU_BASE("MENU_BASE", "메뉴 일반"),
    ;

    private String code;
    private String name;
}
