package kr.co.naamk.api.repository.jpa;

import kr.co.naamk.domain.TbMenuPerm;
import kr.co.naamk.domain.TbMenuPermIds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuPermRepository extends JpaRepository<TbMenuPerm, TbMenuPermIds> {

}
