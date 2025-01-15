package kr.co.naamk.web.repository.jpa;

import kr.co.naamk.domain.TbCmmn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CmmnRepository extends JpaRepository<TbCmmn, Long> {
    boolean existsByCmmnCdAndCmmnGrp(String cd, Long cmmnGrpId);
}
