package kr.co.naamk.domain;

import jakarta.persistence.*;
import kr.co.naamk.domain.audit.AuditEntity;
import lombok.*;

@Entity
@Table(name="tb_perms")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@ToString
public class TbPerms extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "perm_nm", length = 20)
    private String permNm;

    @Column(name = "perm_cd", unique = true, nullable = false, length = 10)
    private String permCd;

    @Column(name="perm_desc")
    private String permDesc;
}
