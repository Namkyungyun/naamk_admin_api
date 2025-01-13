package kr.co.naamk.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data @Builder
@AllArgsConstructor
public class MenuDto {

    @Data
    public static class Search {
        private boolean isDisplay;
        private boolean isRootMenu;
        private boolean isActivated;

        private List<Long> permIds = new ArrayList<>();
        private List<Long> roleIds = new ArrayList<>();
        private List<Long> menuIds = new ArrayList<>();

        private String menuCd;
        private String menuNm;
    }

    @Data
    public static class MenuCreateRequest {
        private String menuCd;
        private String menuNm;
        private String menuDesc;
        private Integer orderNum;
        private Long parentId;
        private String pathUrl;
        private boolean activated = true;
    }

    @Data
    public static class MenuUpdateRequest {
        private String menuCd;
        private String menuNm;
        private String menuDesc;
        private Integer orderNum;
        private Long parentId;
        private String pathUrl;
        private boolean activated = true;
    }

    @Data
    public static class MenuAlignmentUpdateRequest {
        private Long id;
        private Long parentId;
        private Integer orderNum;
    }

    @Data
    public static class MenuTree {
        private Long rootId;
        private Long parentId;
        private Long id;
        private Integer level;
        private Integer orderNum;
        private String path;
    }

    @Data
    public static class MenuTreeResponse {
        private Long id;
        private String menuCd;
        private String menuNm;
        private String pathUrl;
        private boolean activated;
        private List<Object> perms = new ArrayList<>();
        private List<MenuTreeResponse> children = new ArrayList<>();
    }

    @Data
    public static class MenuDetailResponse {
        private Long id;
        private String menuNm;
        private String menuCd;
        private String menuDesc;
        private Integer orderNum;
        private Long parentId;
        private String pathUrl;
        private boolean activated;
        private Timestamp createdAt;
        private Timestamp updatedAt;
    }
}
