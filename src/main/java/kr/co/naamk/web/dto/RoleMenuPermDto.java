package kr.co.naamk.web.dto;

import lombok.Data;

import java.util.List;


@Data
public class RoleMenuPermDto {
    @Data
    public static class MenuPermRequest {
        private Long menuId;
        private List<MenuPermResponse> perms;
    }


    @Data
    public static class MenuPermResponse {
        private Long permId;
        private boolean activated;
    }

}
