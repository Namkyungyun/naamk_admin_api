package kr.co.naamk.domain;

import lombok.*;
import org.hibernate.annotations.Comment;

import java.io.Serializable;
import java.util.Objects;

@Getter @Setter
public class TbRoleMenuPermIds implements Serializable {
    @Comment("역할 ID")
    private Long role;

    @Comment("메뉴 ID")
    private Long menu;

    @Comment("권한 ID")
    private Long perm;

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
