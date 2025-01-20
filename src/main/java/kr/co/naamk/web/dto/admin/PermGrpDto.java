package kr.co.naamk.web.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class PermGrpDto {

    @Data
    public static class PermGrpCreateRequest {
        private String permGrpCd;
        private String permGrpNm;
        private String permGrpDesc;
    }

    @Data
    public static class PermGrpUpdateRequest {
        private String permGrpNm;
        private String permGrpDesc;
        private List<ChildPermStatus> childPerms = new ArrayList<>();
    }

    @Data
    public static class PermGrpListResponse {
        private Long id;
        private String permGrpCd;
        private String permGrpNm;
        private String permGrpDesc;
    }

    @Data
    public static class PermGrpDetailResponse {
        private Long id;
        private String permGrpCd;
        private String permGrpNm;
        private String permGrpDesc;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp updatedAt;
        private List<ChildPermStatus> childPerms = new ArrayList<>();
    }

    @Data
    @AllArgsConstructor
    public static class ChildPermStatus {
        private Long permId;
        private boolean activated;
    }
}
