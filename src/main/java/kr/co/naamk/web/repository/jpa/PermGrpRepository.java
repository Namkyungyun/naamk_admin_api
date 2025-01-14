package kr.co.naamk.web.repository.jpa;

import kr.co.naamk.domain.TbPermGrps;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermGrpRepository extends JpaRepository<TbPermGrps, Long> {
    boolean existsByPermGrpCd(String permGrpCd);
    Optional<TbPermGrps> findByPermGrpCd(String permGrpCd);
}
