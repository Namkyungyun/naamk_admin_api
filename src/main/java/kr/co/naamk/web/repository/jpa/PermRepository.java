package kr.co.naamk.web.repository.jpa;

import kr.co.naamk.domain.TbPerms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermRepository extends JpaRepository<TbPerms, Long> {
}
