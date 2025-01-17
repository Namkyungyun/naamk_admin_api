package kr.co.naamk.web.dto.admin.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BasePermType {
    CREATE("C", "생성"),
    READ("R", "조회"),
    UPDATE("U", "수정"),
    DELETE("D", "삭제"),
    ;

    private String code;
    private String name;
}
