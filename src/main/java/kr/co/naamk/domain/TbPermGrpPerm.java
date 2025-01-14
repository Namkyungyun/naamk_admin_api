package kr.co.naamk.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

/** mapping table */

@Entity
@Table(name="tb_perm_grp_perm")
@IdClass(TbPermGrpPermIds.class)
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class TbPermGrpPerm {
    @Id
    @ManyToOne
    @JoinColumn(name="perm_grp_id")
    @Comment("권한 그룹 ID")
    private TbPermGrps permGrp;

    @Id
    @ManyToOne
    @JoinColumn(name="perm_id")
    @Comment("권한 ID")
    private TbPerms perm;

    @Column(name="activated")
    private boolean activated;
}
