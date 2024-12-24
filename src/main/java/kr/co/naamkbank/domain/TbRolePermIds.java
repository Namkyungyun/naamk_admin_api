package kr.co.naamkbank.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class TbRolePermIds {
    private Long roleId;
    private Long permId;
}
