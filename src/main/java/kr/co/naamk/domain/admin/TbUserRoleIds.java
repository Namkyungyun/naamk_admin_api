package kr.co.naamk.domain.admin;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.io.Serializable;
import java.util.Objects;

@Getter @Setter
public class TbUserRoleIds implements Serializable {
    @Comment("사용자 ID")
    private Long user;
    @Comment("역할 ID")
    private Long role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbUserRoleIds that = (TbUserRoleIds) o;
        return Objects.equals(role, that.getRole()) &&
                Objects.equals(user, that.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, user);
    }
}
