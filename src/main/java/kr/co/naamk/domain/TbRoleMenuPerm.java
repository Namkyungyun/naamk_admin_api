package kr.co.naamk.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name="tb_role_menu_perm")
@IdClass(TbRoleMenuPermIds.class)
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class TbRoleMenuPerm {

    @Id
    @ManyToOne
    @JoinColumn(name="role_id")
    private TbRoles role;


    @Id
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private TbMenus menu;


    @Id
    @ManyToOne
    @JoinColumn(name="perm_id")
    private TbPerms perm;


    @Column(name="activated", nullable = false)
    @Comment("활성 여부")
    private boolean activated;
}
