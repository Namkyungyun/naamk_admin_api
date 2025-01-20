package kr.co.naamk.web.repository.admin.jpa;

import kr.co.naamk.domain.admin.TbPermGrpPerm;
import kr.co.naamk.domain.admin.TbPermGrpPermIds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermGrpPermRepository extends JpaRepository<TbPermGrpPerm, TbPermGrpPermIds> {
}
