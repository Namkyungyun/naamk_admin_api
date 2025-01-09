package kr.co.naamk.domain;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter @Setter
public class TbRoleMenuPermIds implements Serializable {
    private Long menu;
    private Long perm;
    private Long role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbRoleMenuPermIds that = (TbRoleMenuPermIds) o;
        return Objects.equals(menu, that.getMenu()) &&
                Objects.equals(perm, that.getPerm()) &&
                Objects.equals(role, that.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(menu, perm, role);
    }
}
