package kr.co.naamkbank.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class TbMenuPermIds implements Serializable {
    private Long menuId;
    private Long permId;
}
