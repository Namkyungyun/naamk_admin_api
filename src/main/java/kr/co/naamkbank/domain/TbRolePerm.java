package kr.co.naamkbank.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tb_role_perm")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class TbRolePerm {
    @EmbeddedId
    private TbRolePermIds id;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name="role_id")
    private TbRoles role;


    @ManyToOne
    @MapsId("permId")
    @JoinColumn(name="perm_id")
    private TbPerms perm;


}
