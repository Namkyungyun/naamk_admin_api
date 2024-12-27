package kr.co.naamk.api.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
public class UserDto {

    @Data
    public static class SearchParam {
        private String userNm;
        private String userEmail;
        private String loginId;
        private Long roleId;
    }

    @Data @Builder
    public static class CreateRequest {
        private String userNm;
        private String userEmail;
        private String loginId;
        private String loginPwd;
        private List<Long> roleIds;
    }

    @Data @Builder
    public static class ListResponse {
        private Long id;
        private String userNm;
        private String userEmail;
        private String loginId;
        private boolean activated;
        private Timestamp lastLoginAt;
        private Timestamp createdAt;
        private Timestamp updatedAt;
        private List<RoleDto.RoleResponse> roles;

    }

    @Data @Builder
    public static class DetailResponse {
        private Long id;
        private String userNm;
        private String userEmail;
        private String loginId;
        private Integer loginPwdFailCount;
        private boolean activated;
        private Timestamp lastLoginAt;
        private Timestamp pwdExpiredAt;
        private Timestamp createdAt;
        private Timestamp updatedAt;
        private List<RoleDto.RoleResponse> roles;
    }

    @Data @Builder
    public static class UpdateRequest {
        private String userEmail;
        private String loginPwd;
        private boolean activated;
        private List<Long> roleIds;
    }

}
