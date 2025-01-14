package kr.co.naamk.domain;

import jakarta.persistence.*;
import kr.co.naamk.domain.audit.AuditEntity;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tb_menus")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class TbMenus extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    @Comment("메뉴 PK")
    private Long id;

    @Column(name="menu_cd", unique = true, nullable = false, length = 10)
    @Comment("메뉴 코드")
    private String menuCd;

    @Column(name="menu_nm", nullable = false, length = 20)
    @Comment("메뉴명")
    private String menuNm;

    @Column(name="menu_desc", nullable = false, length = 50)
    @Comment("메뉴 설명")
    private String menuDesc;

    @Column(name="parent_id")
    @Comment("상위 메뉴 ID")
    private Long parentId;

    @Column(name="path_url", nullable = false, length = 20)
    @Comment("메뉴 클릭 시 이동 url")
    private String pathUrl;

    @Column(name="order_num", nullable = false)
    @Comment("메뉴 정렬 순서 번호")
    private Integer orderNum;

    @Column(name="activated")
    @Comment("활성여부")
    private boolean activated;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, fetch= FetchType.LAZY, orphanRemoval = true)
    private List<TbRoleMenuPerm> roleMenuPerms = new ArrayList<>();

    @Transient // JPA가 관리하지 않는 필드
    private List<TbMenus> children;
}
