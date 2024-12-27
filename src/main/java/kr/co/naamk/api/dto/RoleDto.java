package kr.co.naamk.api.dto;

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
        private List<Long> permIds;
    }

    @Data @Builder
    public static class RoleResponse implements Serializable {
        private Long id;
        private String roleNm;
        private String roleCd;
        private List<PermDto.PermResponse> perms = new ArrayList<>();
    }
}
