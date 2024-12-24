package kr.co.naamkbank.api.dto;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class RoleDto {
    private String roleNm;
    private String roleCd;
    private RolePermRequest rolePerms;


    @Data @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class RolePermRequest implements Serializable {
        private List<Long> permIds;
    }

    @Data
    @NoArgsConstructor
    public static class RoleResponse implements Serializable {
        private Long id;
        private String roleNm;
        private String roleCd;
        private List<PermDto.PermResponse> perms = new ArrayList<>();
    }
}
