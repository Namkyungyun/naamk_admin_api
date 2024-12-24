package kr.co.naamkbank.api.dto;

import lombok.*;


@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class PermDto {
    private String permCd;
    private String permNm;


    @Data @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class PermResponse {
        private Long id;
        private String permCd;
        private String permNm;
    }


}
