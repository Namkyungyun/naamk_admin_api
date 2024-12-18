package kr.co.naamkbank.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tb_users")
public class TbUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false)
    private Long id;

    @Column(name="user_nm")
    private String userNm;

    @Column(name="user_email")
    private String userEmail;

    @Column(name="login_id")
    private String loginId;

    @Column(name="login_pwd")
    private String loginPwd;

    @Column(name = "login_pwd_fail_cnt")
    private int loginPwdFailCount;

    @Column(name="activated")
    private boolean activated;

    @Column(name="last_login_at")
    private Timestamp lastLoginAt;

    @Column(name="pwd_expired_at")
    private Timestamp pwdExpiredAt;

}
