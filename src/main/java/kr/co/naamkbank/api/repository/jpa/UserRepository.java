package kr.co.naamkbank.api.repository.jpa;

import kr.co.naamkbank.domain.TbUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<TbUsers, Long> {
}
