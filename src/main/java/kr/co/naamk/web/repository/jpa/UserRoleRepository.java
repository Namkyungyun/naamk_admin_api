package kr.co.naamk.web.repository.jpa;

import kr.co.naamk.domain.TbUserRole;
import kr.co.naamk.domain.TbUserRoleIds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<TbUserRole, TbUserRoleIds> {
}