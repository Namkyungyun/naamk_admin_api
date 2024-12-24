package kr.co.naamkbank.api.repository;

import kr.co.naamkbank.domain.TbMenuPerm;
import kr.co.naamkbank.domain.TbMenuPermIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuPermRepository extends JpaRepository<TbMenuPerm, TbMenuPermIds> {

}
