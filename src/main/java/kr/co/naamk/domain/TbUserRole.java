package kr.co.naamk.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name="tb_user_role")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@IdClass(TbUserRoleIds.class) // 복합키 매핑
public class TbUserRole {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private TbUsers user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="role_id")
    private TbRoles role;
}
