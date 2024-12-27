package kr.co.naamk.domain;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter @Setter
public class TbRolePermIds implements Serializable {
    private Long role;
    private Long perm;

    // Override equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbRolePermIds that = (TbRolePermIds) o;
        return Objects.equals(role, that.getRole()) &&
                Objects.equals(perm, that.getPerm());
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, perm);
    }
}
