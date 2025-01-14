package kr.co.naamk.web.dto.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BaseRoleType {
    SUPER_ADMIN("SUPERADMIN", "슈퍼어드민"),
    ADMIN("ADMIN", "어드민"),
    VIEWER("VIEWER", "뷰어"),
    ;

    private String code;
    private String name;
}
