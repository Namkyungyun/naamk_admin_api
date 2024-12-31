package kr.co.naamk.web.dto;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data @Builder
public class RoleDto {

    @Data @Builder
    public static class RoleRequest {
        private String roleNm;
        private String roleCd;
    }

    @Data @Builder
    public static class RoleResponse implements Serializable {
        private Long id;
        private String roleNm;
        private String roleCd;
    }
}
