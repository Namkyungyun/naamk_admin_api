package kr.co.naamkbank.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data @Builder
@AllArgsConstructor
public class MenuDto {

    @Data
    @NoArgsConstructor
    public static class MenuRequest {
        private String menuCd;
        private String menuNm;
        private String menuDesc;
        private Integer orderNum;
        private Long parentId;
        private String pathUrl;
        private boolean activated = true;
        private List<Long> permIds = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    public static class MenuTreeDetailResponse {
        private Long id;
        private String menuCd;
        private String menuNm;
        private String menuDesc;
        private Integer orderNum;
        private String pathUrl;
        private boolean activated;
        private List<PermDto.PermResponse> perms = new ArrayList<>();
        private List<MenuTreeDetailResponse> children = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    public static class MenuTreeResponse {
        private Long id;
        private String menuCd;
        private String menuNm;
        private String pathUrl;
        private List<MenuTreeResponse> children = new ArrayList<>();
    }
}
