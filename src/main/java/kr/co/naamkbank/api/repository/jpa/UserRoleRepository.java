package kr.co.naamkbank.api.repository.jpa;

import kr.co.naamkbank.domain.TbUserRole;
import kr.co.naamkbank.domain.TbUserRoleIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRoleRepository extends JpaRepository<TbUserRole, TbUserRoleIds> {
}
