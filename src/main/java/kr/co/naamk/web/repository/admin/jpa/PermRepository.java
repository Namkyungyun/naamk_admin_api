package kr.co.naamk.web.repository.admin.jpa;

import kr.co.naamk.domain.admin.TbPerms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermRepository extends JpaRepository<TbPerms, Long> {
    boolean existsByPermCd(String permCd);
    Optional<TbPerms> findByPermCd(String permCd);
}
