package kr.co.naamkbank.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class UserDto {
    private String userNm;
    private String userEmail;
    private String loginId;
    private String loginPwd;
    private UserRoleRequest roles;

    @Data @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class UserRoleRequest {
        private List<Long> roleIds;
    }

    @Data
    @NoArgsConstructor
    public static class UserResponse {
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
}
