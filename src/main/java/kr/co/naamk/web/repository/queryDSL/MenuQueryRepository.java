package kr.co.naamk.web.repository.queryDSL;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.naamk.domain.*;
import kr.co.naamk.web.dto.MenuDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<TbMenus> findMenuWithPermsBySearch(MenuDto.MenuPermissionSearch search) {
        QTbRoles role = QTbRoles.tbRoles;
        QTbMenus menus = QTbMenus.tbMenus;
        QTbPerms perms = QTbPerms.tbPerms;
        QTbMenuPerm  menuPerm = QTbMenuPerm.tbMenuPerm;


        // query
        JPAQuery<TbMenus> jpaQuery = queryFactory.selectFrom(menus)
                .leftJoin(menus.permRoles, menuPerm).on(menuPerm.role.id.eq(search.getRoleId()))
                .leftJoin(menuPerm.perm, perms)
                .leftJoin(menuPerm.role, role);

        // 검색 조건
        BooleanBuilder predicate = new BooleanBuilder(
                search.getParentId() != null ?
                        menus.parentId.eq(search.getParentId()) : menus.parentId.isNull()
        );

        if(search.isDisplay()) {
            predicate.and(perms.permCd.eq("R"));
        }


        return jpaQuery
                .where(predicate)
                .distinct()
                .fetch();
    }

}
