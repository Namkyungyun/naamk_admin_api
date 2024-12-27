package kr.co.naamk.web.repository.jpa;

import kr.co.naamk.domain.TbRolePerm;
import kr.co.naamk.domain.TbRolePermIds;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermRepository extends JpaRepository<TbRolePerm, TbRolePermIds> {
    List<TbRolePerm> findAllByRoleId(Long roleId);
}


