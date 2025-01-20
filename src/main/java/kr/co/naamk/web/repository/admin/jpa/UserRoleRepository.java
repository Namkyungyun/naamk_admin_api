package kr.co.naamk.web.repository.admin.jpa;

import kr.co.naamk.domain.admin.TbUserRole;
import kr.co.naamk.domain.admin.TbUserRoleIds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<TbUserRole, TbUserRoleIds> {
}
