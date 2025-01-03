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

    @Data @Builder
    public static class MenuPermissionSearch {
        private Long roleId;
        private Long parentId;
        private boolean isDisplay;
    }

    @Data
    public static class MenuPermissionRequest {
        private Long menuId;
        private Long roleId;
        private List<Long> permIds = new ArrayList<>();
    }


    @Data
    public static class CreateOrUpdateRequest {
        private String menuCd;
        private String menuNm;
        private String menuDesc;
        private Integer orderNum;
        private Long parentId;
        private String pathUrl;
        private boolean activated = true;
    }

    @Data
    public static class ManagementTreeResponse { // 메뉴 관리의 리스트로 사용.
        private Long id;
        private Long parentId;
        private String menuNm;
        private boolean activated;
        private List<ManagementTreeResponse> children = new ArrayList<>();
    }

    @Data
    public static class DisplayTreeResponse { // 로그인 이후 메뉴탭에 사용
        private Long id;
        private String menuNm;
        private String pathUrl;
        private List<String> permCds = new ArrayList<>();
        private List<DisplayTreeResponse> children = new ArrayList<>();
    }

    @Data
    public static class PermissionTreeResponse { // 역할별 메뉴 권한 관리에서 사용
        private Long id;
        private String menuNm;
        private String menuCd;
        private boolean activated;
        private List<PermDto.PermStatusByRole> perms = new ArrayList<>();
        private List<PermissionTreeResponse> children = new ArrayList<>();
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
