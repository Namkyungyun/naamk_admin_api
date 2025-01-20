package kr.co.naamk.web.repository.admin.queryDSL;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.naamk.domain.admin.QTbMenus;
import kr.co.naamk.domain.admin.QTbRoleMenuPerm;
import kr.co.naamk.domain.admin.TbMenus;
import kr.co.naamk.web.dto.admin.MenuDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuQueryRepository {

    private final JPAQueryFactory queryFactory;

    /** findParent ? rootMenus : childMenus */
    public List<TbMenus> findMenusBySearch(MenuDto.Search search) {
        QTbMenus menus = QTbMenus.tbMenus;
        QTbRoleMenuPerm roleMenuPerm = QTbRoleMenuPerm.tbRoleMenuPerm;

        // 검색 조건
        BooleanBuilder predicate = new BooleanBuilder();

        if(search.isRootMenu()) {
            predicate.and(menus.parentId.isNull());
        } else {
            predicate.and(menus.parentId.isNotNull());
        }

        if(!search.getRoleIds().isEmpty()) {
            predicate.and(roleMenuPerm.role.id.in(search.getRoleIds()));
        }

        if(search.isActivated()) {
            predicate.and(roleMenuPerm.activated.eq(true));
        }

        if(!search.getPermIds().isEmpty()) {
            predicate.and(roleMenuPerm.perm.id.in(search.getPermIds()));
        }

        if(search.isDisplay()) {
            predicate.and(menus.activated.eq(true));
        }

        if(search.getMenuCd() != null) {
            predicate.and(menus.menuCd.startsWith(search.getMenuCd()));
        }

        if(search.getMenuNm() != null) {
            predicate.and(menus.menuNm.startsWith(search.getMenuNm()));
        }

        if(!search.getMenuIds().isEmpty()) {
            predicate.or(menus.id.in(search.getMenuIds()));
        }


        return queryFactory.selectFrom(menus)
                .join(menus.roleMenuPerms, roleMenuPerm)
                .where(predicate)
                .orderBy(menus.orderNum.asc())
                .distinct()
                .fetch();
    }
}
