package kr.co.naamk.api.repository.queryDSL;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.naamk.api.dto.UserDto;
import kr.co.naamk.api.dto.mapstruct.UserMapper;
import kr.co.naamk.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<UserDto.ListResponse> findUsersWithRoles(UserDto.SearchParam search, Pageable pageable) {
        QTbRoles roles = QTbRoles.tbRoles;
        QTbUsers user = QTbUsers.tbUsers;
        QTbUserRole userRole = QTbUserRole.tbUserRole;

        BooleanBuilder predicate = new BooleanBuilder();

        if(search.getUserEmail() != null && !search.getUserEmail().isBlank()) {
            predicate.and(user.userEmail.like("%" + search.getUserEmail() + "%"));
        }

        if(search.getUserNm() != null && !search.getUserNm().isBlank()) {
            predicate.and(user.userNm.like("%" + search.getUserNm() + "%"));
        }

        if(search.getLoginId() != null && !search.getLoginId().isBlank()) {
            predicate.and(user.loginId.like("%" + search.getLoginId() + "%"));
        }

        if(search.getRoleId() != null) {
            predicate.and(userRole.role.id.eq(search.getRoleId()));
        }


        // user entity list
        List<TbUsers> content = queryFactory.select(user)
                .from(user)
                .join(user.userRoles, userRole)
                .join(userRole.role, roles)
                .where(predicate)
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(user.createdAt.desc())
                .fetch();

        // user total count
        long total = queryFactory.selectFrom(user)
                .where(predicate)
                .stream().count();


        return PageableExecutionUtils.getPage(
                UserMapper.INSTANCE.entitiesToListResponseDtos(content),
                pageable,
                () -> total
        );
    }
}
