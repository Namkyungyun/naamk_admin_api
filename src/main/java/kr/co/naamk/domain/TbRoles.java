package kr.co.naamk.domain;

import jakarta.persistence.*;
import kr.co.naamk.domain.audit.AuditEntity;
import lombok.*;

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
    private Long id;

    @Column(name="role_nm", nullable = false, length = 20)
    private String roleNm;

    @Column(name="role_cd", unique = true, nullable = false, length = 10)
    private String roleCd;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TbUserRole> userRoles = new ArrayList<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TbMenuPerm> menuPerms = new ArrayList<>();

}
