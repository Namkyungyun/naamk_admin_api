package kr.co.naamk.web.repository.jpa;

import kr.co.naamk.domain.TbPermGrpPerm;
import kr.co.naamk.domain.TbPermGrpPermIds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermGrpPermRepository extends JpaRepository<TbPermGrpPerm, TbPermGrpPermIds> {
}
