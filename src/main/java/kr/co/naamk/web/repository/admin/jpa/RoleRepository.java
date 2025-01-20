package kr.co.naamk.web.repository.admin.jpa;

import kr.co.naamk.domain.admin.TbRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<TbRoles, Long> {
    boolean existsByRoleCd(String roleCd);
}