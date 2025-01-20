package kr.co.naamk.domain.admin;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="perm_grp_id")
    private TbPermGrps permGrp;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="perm_id")
    private TbPerms perm;

    @Column(name="activated")
    @Comment("활성여부")
    private boolean activated;
}
