package kr.co.naamk.web.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CmmnDto {

    @Data
    public static class CmmnGrpCreateRequest {
        private String cmmnGrpCd;
        private String cmmnGrpNm;
        private String cmmnGrpDesc;
        private boolean activated;
    }

    @Data @Builder
    public static class CmmnGrpCreateResponse {
        private Long id;
    }

    @Data
    public static class CmmnGrpUpdateRequest {
        private String cmmnGrpNm;
        private String cmmnGrpDesc;
        private boolean activated;
    }

    @Data
    public static class CmmnGrpListResponse {
        private Long id;
        private String cmmnGrpCd;
        private String cmmnGrpNm;
        private String activated;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp updatedAt;
    }

    @Data
    public static class CmmnGrpDetailResponse {
        private Long id;
        private String cmmnGrpCd;
        private String cmmnGrpNm;
        private String cmmnGrpDesc;
        private String activated;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp updatedAt;
    }

    @Data
    public static class CmmnCreateRequest {
        private String cmmnCd;
        private String cmmnNm;
        private String cmmnDesc;
        private Integer orderNum;
        private boolean activated;
    }

    @Data
    public static class CmmnUpdateRequest {
        private String cmmnCd;
        private String cmmnNm;
        private String cmmnDesc;
        private Integer orderNum;
        private boolean activated;
    }

    @Data
    public static class CmmnListResponse {
        private Long id;
        private Long cmmnGrpId;
        private String cmmnCd;
        private String cmmnNm;
        private Integer orderNum;
        private boolean activated;
    }

    @Data
    public static class CmmnDetailResponse{
        private Long id;
        private Long cmmnGrpId;
        private String cmmnCd;
        private String cmmnNm;
        private String cmmnDesc;
        private Integer orderNum;
        private boolean activated;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp updatedAt;
    }
}
