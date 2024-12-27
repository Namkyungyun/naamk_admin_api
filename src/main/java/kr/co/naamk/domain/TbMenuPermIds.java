package kr.co.naamk.domain;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter @Setter
public class TbMenuPermIds implements Serializable {
    private Long menu;
    private Long perm;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbMenuPermIds that = (TbMenuPermIds) o;
        return Objects.equals(menu, that.getMenu()) &&
                Objects.equals(perm, that.getPerm());
    }

    @Override
    public int hashCode() {
        return Objects.hash(menu, perm);
    }
}
