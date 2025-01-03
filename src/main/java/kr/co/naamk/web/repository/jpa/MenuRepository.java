package kr.co.naamk.web.repository.jpa;

import kr.co.naamk.domain.TbMenus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<TbMenus, Long> {
   List<TbMenus> findByParentIdIsNull();
   List<TbMenus> findByParentId(Long parentId);


   List<TbMenus> findByMenuCdIgnoreCaseOrMenuNmIgnoreCase(String menuCd, String menuNm);
}
