package kr.co.naamk.domain;

import jakarta.persistence.*;
import kr.co.naamk.domain.audit.AuditEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tb_perms")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@ToString
public class TbPerms extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "perm_cd", unique = true, nullable = false, length = 10)
    private String permCd;

    @Column(name = "perm_nm", length = 20)
    private String permNm;

    @Column(name="perm_desc")
    private String permDesc;

    @OneToMany(mappedBy = "perm", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TbRoleMenuPerm> roleMenuPerms = new ArrayList<>();

    @OneToMany(mappedBy = "perm", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TbPermGrpPerm> permGrpPerm = new ArrayList<>();
}
