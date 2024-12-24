package kr.co.naamkbank.api.repository.jpa;

import kr.co.naamkbank.domain.TbUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<TbUsers, Long> {

    Optional<TbUsers> findUserWithRolesById(@Param("userId") Long userId);
}
