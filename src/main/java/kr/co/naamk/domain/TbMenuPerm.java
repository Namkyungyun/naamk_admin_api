package kr.co.naamk.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity @Table(name="tb_menu_perm")
@IdClass(TbMenuPermIds.class)
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class TbMenuPerm {
    @Id
    @ManyToOne
    @JoinColumn(name = "menu_id")
    @Comment("메뉴 ID")
    private TbMenus menu;


    @Id
    @ManyToOne
    @JoinColumn(name="perm_id")
    @Comment("권한 ID")
    private TbPerms perm;

    @Id
    @ManyToOne
    @JoinColumn(name="role_id")
    @Comment("역할 ID")
    private TbRoles role;
}
