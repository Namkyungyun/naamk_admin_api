package kr.co.naamk.domain;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter @Setter
public class TbUserRoleIds implements Serializable {
    private Long user;
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
