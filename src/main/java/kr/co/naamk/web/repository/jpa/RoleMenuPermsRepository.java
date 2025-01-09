package kr.co.naamk.web.repository.jpa;


import kr.co.naamk.domain.TbRoleMenuPerm;
import kr.co.naamk.domain.TbRoleMenuPermIds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleMenuPermsRepository extends JpaRepository<TbRoleMenuPerm, TbRoleMenuPermIds> {}
