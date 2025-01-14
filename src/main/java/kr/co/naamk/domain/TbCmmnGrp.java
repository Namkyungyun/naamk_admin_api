package kr.co.naamk.domain;

import jakarta.persistence.*;
import kr.co.naamk.domain.audit.AuditEntity;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name="tb_cmmn_grp")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class TbCmmnGrp extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    @Comment("PK ID")
    private Long id;

    @Column(name="cmmn_grp_cd", nullable = false, unique = true)
    @Comment("공통 그룹 코드")
    private String cmmnGrpCd;

    @Column(name="cmmn_grp_nm", nullable = false)
    @Comment("공통 그룹 코드 명")
    private String cmmnGrpNm;

    @Column(name="cmmn_grp_desc", nullable = false)
    @Comment("공통 그룹 코드 설명")
    private String cmmnGrpDesc;

    @Column(name="activated")
    @Comment("활성 여부")
    private boolean activated;

}
