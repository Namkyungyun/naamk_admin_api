package kr.co.naamk.web.repository.jpa;

import kr.co.naamk.domain.TbMenus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<TbMenus, Long> {
   List<TbMenus> findByParentId(Long parentId);

   /** menu create에서 사용 */
   Optional<TbMenus> findByMenuCdIgnoreCase(String menuCd);

   /** 전체 메뉴 트리 (검색x) && rootMenu 제외 */
   @Query(nativeQuery = true,
      value = """        
        WITH RECURSIVE menu_tree AS (
            -- 최상위 메뉴 가져오기
            SELECT
            	id as root_id,
            	id,
            	parent_id,
                menu_cd,
                menu_nm,
                order_num,
                0 AS level, -- 루트 노드는 level 0
                CAST(id AS TEXT) AS path -- 초기 path는 현재 노드의 id\s
            FROM naamk_bank.tb_menus
            WHERE parent_id IS null
        
            UNION ALL
        
            -- 하위 메뉴를 재귀적으로 추가
            SELECT
            	mt.root_id,
            	m.id,
            	m.parent_id,
                m.menu_cd,
                m.menu_nm,
                m.order_num,
                mt.level + 1 AS level, -- 부모의 level + 1
                mt.path || ',' || m.id AS path -- 부모의 path에 현재 노드의 id 추가\s
            FROM naamk_bank.tb_menus m
            INNER JOIN menu_tree mt ON m.parent_id = mt.id
        )
        -- 결과 출력
        SELECT
        	id,
        	root_id,
        	parent_id,
        	level,
        	order_num,
        	path
        FROM menu_tree
        WHERE parent_id is not null
        ORDER BY root_id, level, order_num
        """)
   List<Map<String, Object>> nativeFindMenuTreeExceptionRoot();

}
