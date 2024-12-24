package kr.co.naamkbank.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tb_role_perm")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@IdClass(TbRolePermIds.class)
public class TbRolePerm {

    @Id
    @ManyToOne
    @JoinColumn(name="role_id")
    private TbRoles role;

    @Id
    @ManyToOne
    @JoinColumn(name="perm_id")
    private TbPerms perm;


}
