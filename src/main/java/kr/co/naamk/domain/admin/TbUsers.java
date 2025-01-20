package kr.co.naamk.domain.admin;

import jakarta.persistence.*;
import kr.co.naamk.domain.audit.AuditEntity;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@Entity @Table(name="tb_users")
public class TbUsers extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    @Comment("어드민 사용자 PK")
    private Long id;

    @Column(name="user_nm", nullable = false, length = 10)
    @Comment("어드민 사용자 명")
    private String userNm;

    @Column(name="user_email", nullable = false, length = 30)
    @Comment("어드민 사용자 이메일")
    private String userEmail;

    @Column(name="login_id", unique = true, nullable = false, length = 10)
    @Comment("어드민 사용자 로그인 아이디")
    private String loginId;

    @Column(name="login_pwd", nullable = false, length = 60)
    @Comment("어드민 사용자 로그인 패스워드")
    private String loginPwd;

    @Column(name = "login_pwd_fail_cnt", length = 1, nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    @Comment("어드민 사용자 로그인 패스워드 틀린 횟수")
    private int loginPwdFailCount;

    @Column(name="activated")
    @Comment("어드민 사용자 활성여부")
    private boolean activated;

    @Column(name="last_login_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Comment("마지막 로그인 일자")
    private Timestamp lastLoginAt;

    @Column(name="pwd_expired_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP + INTERVAL '90 days'")
    @Comment("로그인 패스워드 만료일자")
    private Timestamp pwdExpiredAt;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade= CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TbUserRole> userRoles = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.pwdExpiredAt == null) {
            this.pwdExpiredAt = Timestamp.valueOf(LocalDateTime.now().plusDays(90));
        }
    }

    public void removeUserRole(TbUserRole userRole) {
        userRoles.remove(userRole);
        userRole.setUser(null);
    }

}
