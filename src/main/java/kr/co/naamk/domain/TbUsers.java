package kr.co.naamk.domain;

import jakarta.persistence.*;
import kr.co.naamk.domain.audit.AuditEntity;
import lombok.*;

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
    private Long id;

    @Column(name="user_nm", nullable = false, length = 10)
    private String userNm;

    @Column(name="user_email", nullable = false, length = 30)
    private String userEmail;

    @Column(name="login_id", unique = true, nullable = false, length = 10)
    private String loginId;

    @Column(name="login_pwd", nullable = false, length = 60)
    private String loginPwd;

    @Column(name = "login_pwd_fail_cnt", length = 1, nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int loginPwdFailCount;

    @Column(name="activated")
    private boolean activated;

    @Column(name="last_login_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp lastLoginAt;

    @Column(name="pwd_expired_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP + INTERVAL '90 days'")
    private Timestamp pwdExpiredAt;

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
