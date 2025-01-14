package kr.co.naamk.domain;

import jakarta.persistence.*;
import kr.co.naamk.domain.audit.AuditEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tb_perm_grps")
@Setter @Getter
@AllArgsConstructor @NoArgsConstructor @Builder
public class TbPermGrps extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="perm_grp_cd", nullable = false, unique = true, length = 10)
    private String permGrpCd;

    @Column(name="perm_grp_nm", nullable = false, length = 20)
    private String permGrpNm;

    @Column(name="perm_grp_desc", length = 30)
    private String permGrpDesc;

    @OneToMany(mappedBy = "permGrp", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TbPermGrpPerm> permGrpPerm = new ArrayList<>();
}
