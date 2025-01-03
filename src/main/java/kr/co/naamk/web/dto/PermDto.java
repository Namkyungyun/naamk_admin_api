package kr.co.naamk.web.dto;

import lombok.*;


@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class PermDto {
    private String permCd;
    private String permNm;


    @Data
    public static class PermResponse {
        private Long id;
        private String permCd;
        private String permNm;
    }

    @Data
    public static class PermStatusByRole {
        private Long id;
        private String permCd;
        private String permNm;
        private boolean activated = true;
    }



}
