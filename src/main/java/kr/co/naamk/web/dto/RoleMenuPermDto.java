package kr.co.naamk.web.dto;

import lombok.Data;


@Data
public class RoleMenuPermDto {

    @Data
    public static class MenuPerm {
        private Long permId;
        private boolean activated;
    }
}
