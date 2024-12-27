package kr.co.naamk.web.repository.jpa;

import kr.co.naamk.domain.TbUsers;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<TbUsers, Long> {
}
