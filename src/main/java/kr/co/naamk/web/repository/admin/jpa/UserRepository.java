package kr.co.naamk.web.repository.admin.jpa;

import kr.co.naamk.domain.admin.TbUsers;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<TbUsers, Long> {
}
