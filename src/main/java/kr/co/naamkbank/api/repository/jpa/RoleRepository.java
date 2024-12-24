package kr.co.naamkbank.api.repository.jpa;

import kr.co.naamkbank.domain.TbRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<TbRoles, Long> {
}