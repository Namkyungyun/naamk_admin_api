package kr.co.naamkbank.api.repository;

import kr.co.naamkbank.domain.TbUserRole;
import kr.co.naamkbank.domain.TbUserRoleIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<TbUserRole, TbUserRoleIds> {
    List<TbUserRole> findAllByUserId(Long userId);
}
