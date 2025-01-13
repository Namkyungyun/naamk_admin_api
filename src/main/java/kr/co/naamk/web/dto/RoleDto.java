package kr.co.naamk.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class RoleDto {

    @Data
    public static class RoleCreateRequest {
        private String roleNm;
        private String roleCd;
        private String roleDesc;
    }

    @Data
    public static class RoleUpdateRequest {
        private String roleNm;
        private String roleDesc;
    }

    @Data
    public static class RoleListResponse implements Serializable {
        private Long id;
        private String roleNm;
        private String roleCd;
    }

    @Data
    public static class RoleDetailResponse implements Serializable {
        private Long id;
        private String roleCd;
        private String roleNm;
        private String roleDesc;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp updatedAt;
    }
}
