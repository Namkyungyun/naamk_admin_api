package kr.co.naamkbank.api.repository.jpa;

import kr.co.naamkbank.domain.TbMenus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MenuRepository extends JpaRepository<TbMenus, Long> {
    //최상위 메뉴 가져오기
    @Query(nativeQuery = true,
            value = "SELECT tm.id, tm.menu_cd, tm.menu_nm, tm.menu_desc, " +
                    "       tm.path_url, tm.order_num, tm.activated, tm.parent_id, " +
                    "       tm.created_at, tm.updated_at " +
                    "FROM tb_menus tm " +
                    "INNER JOIN tb_menu_perm tmp ON tmp.menu_id = tm.id " +
                    "AND tm.parent_id IS NULL " +
                    "AND tmp.perm_id in (:permIds) " +
                    "GROUP BY tm.id, tm.menu_cd, tm.menu_nm, tm.menu_desc, " +
                    "         tm.path_url, tm.order_num, tm.activated, tm.parent_id, " +
                    "         tm.created_at, tm.updated_at " +
                    "ORDER BY tm.order_num"
    )
    List<TbMenus> nativeFindByParentIdNullAndPermIdIsIn(@Param("permIds") List<Long> permIds);


    @Query(nativeQuery = true,
            value = "SELECT tm.id, tm.menu_cd, tm.menu_nm, tm.menu_desc, " +
                    "       tm.path_url, tm.order_num, tm.activated, tm.parent_id, " +
                    "       tm.created_at, tm.updated_at " +
                    "FROM tb_menus tm " +
                    "INNER JOIN tb_menu_perm tmp ON tmp.menu_id = tm.id " +
                    "AND tm.parent_id = :parentId " +
                    "AND tmp.perm_id in (:permIds) " +
                    "GROUP BY tm.id, tm.menu_cd, tm.menu_nm, tm.menu_desc, " +
                    "         tm.path_url, tm.order_num, tm.activated, tm.parent_id, " +
                    "         tm.created_at, tm.updated_at " +
                    "ORDER BY tm.order_num"
    )
    List<TbMenus> nativeFindByParentIdAndPermIdIsIn(@Param("parentId") Long parentId, @Param("permIds") List<Long> permIds);
}
