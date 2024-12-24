package kr.co.naamkbank.api.repository.jpa;

import kr.co.naamkbank.domain.TbRolePerm;
import kr.co.naamkbank.domain.TbRolePermIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermRepository extends JpaRepository<TbRolePerm, TbRolePermIds> {
    List<TbRolePerm> findAllByRoleId(Long roleId);
}


