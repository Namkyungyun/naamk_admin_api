package kr.co.naamk.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Timestamp;


@Data
public class PermDto {
    @Data
    public static class PermCreateRequest {
        private String permCd;
        private String permNm;
        private String permDesc;
    }

    // cd는 unique값이므로, 유저가 삭제하고 다시 만들도록하는 것이 나을 것 같음.
    @Data
    public static class PermUpdateRequest {
        private String permNm;
        private String permDesc;
    }


    @Data
    public static class PermListResponse {
        private Long id;
        private String permCd;
        private String permNm;
    }

    @Data
    public static class PermDetailResponse {
        private Long id;
        private String permNm;
        private String permCd;
        private String permDesc;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp updatedAt;

    }
}
