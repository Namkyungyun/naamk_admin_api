package kr.co.naamk.domain.admin;

import jakarta.persistence.*;
import kr.co.naamk.domain.audit.AuditEntity;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tb_roles")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class TbRoles extends AuditEntity {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("역할 PK")
    private Long id;

    @Column(name="role_cd", unique = true, nullable = false, length = 10)
    @Comment("")
    private String roleCd;

    @Column(name="role_nm", nullable = false, length = 20)
    @Comment("역할 명")
    private String roleNm;

    @Column(name="role_desc")
    @Comment("역할 설명")
    private String roleDesc;

    @Builder.Default
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TbUserRole> userRoles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TbRoleMenuPerm> menuPerms = new ArrayList<>();

}
