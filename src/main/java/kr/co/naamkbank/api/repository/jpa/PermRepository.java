package kr.co.naamkbank.api.repository.jpa;

import kr.co.naamkbank.domain.TbPerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermRepository extends JpaRepository<TbPerms, Long> {
}
