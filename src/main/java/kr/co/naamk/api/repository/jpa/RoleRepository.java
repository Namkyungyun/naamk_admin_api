package kr.co.naamk.api.repository.jpa;

import kr.co.naamk.domain.TbRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<TbRoles, Long> {
}