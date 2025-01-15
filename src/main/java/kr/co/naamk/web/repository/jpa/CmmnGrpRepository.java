package kr.co.naamk.web.repository.jpa;

import kr.co.naamk.domain.TbCmmnGrp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CmmnGrpRepository extends JpaRepository<TbCmmnGrp, Long> {
    Optional<TbCmmnGrp> findByCmmnGrpCd(String cmmnGrpCd);
    boolean existsByCmmnGrpCd(String cmmnGrpCd);
}
