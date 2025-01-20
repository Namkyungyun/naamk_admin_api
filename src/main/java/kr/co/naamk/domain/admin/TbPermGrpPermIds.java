package kr.co.naamk.domain.admin;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.Objects;

@Getter @Setter
public class TbPermGrpPermIds {
    @Comment("권한 그룹 ID")
    private Long permGrp;

    @Comment("권한 ID")
    private Long perm;

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
