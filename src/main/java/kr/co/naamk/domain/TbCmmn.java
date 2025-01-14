package kr.co.naamk.domain;

import jakarta.persistence.*;
import kr.co.naamk.domain.audit.AuditEntity;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name="tb_cmmn")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class TbCmmn extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    @Comment("PK ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cmmn_grp_id", referencedColumnName = "id")
    @Comment("공통 코드 그룹 아이디")
    private TbCmmnGrp cmmnGrp;

    @Column(name="cmmn_cd", nullable = false, unique = true)
    @Comment("공통 코드")
    private String cmmnCd;

    @Column(name="cmmn_nm", nullable = false)
    @Comment("공통 코드 명")
    private String cmmnNm;

    @Column(name="cmmn_desc", nullable = false)
    @Comment("공통 코드 설명")
    private String cmmnDesc;

    @Column(name="order_num")
    @Comment("정렬 번호")
    private Integer orderNum;

    @Column(name="activated")
    @Comment("활성 여부")
    private boolean activated;

}
