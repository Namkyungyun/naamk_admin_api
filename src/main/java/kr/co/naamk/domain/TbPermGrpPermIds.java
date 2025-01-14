package kr.co.naamk.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter
public class TbPermGrpPermIds {
    private Long perm;
    private Long permGrp;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        TbPermGrpPermIds that = (TbPermGrpPermIds) o;

        return Objects.equals(perm, that.perm) &&
                Objects.equals(permGrp, that.permGrp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(perm, permGrp);
    }
}
