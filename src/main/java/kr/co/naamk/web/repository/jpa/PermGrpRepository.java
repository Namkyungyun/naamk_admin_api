package kr.co.naamk.web.repository.jpa;

import kr.co.naamk.domain.TbPermGrps;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermGrpRepository extends JpaRepository<TbPermGrps, Long> {
    boolean existsByPermGrpCd(String permGrpCd);
}
