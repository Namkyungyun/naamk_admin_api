package kr.co.naamk.web.repository.admin.jpa;


import kr.co.naamk.domain.admin.TbRoleMenuPerm;
import kr.co.naamk.domain.admin.TbRoleMenuPermIds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleMenuPermsRepository extends JpaRepository<TbRoleMenuPerm, TbRoleMenuPermIds> {}
