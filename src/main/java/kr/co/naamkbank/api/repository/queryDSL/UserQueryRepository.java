package kr.co.naamkbank.api.repository.queryDSL;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.naamkbank.api.dto.UserDto;
import kr.co.naamkbank.domain.QTbUserRole;
import kr.co.naamkbank.domain.QTbUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<UserDto.ListResponse> findUsersByRoleId() {
        QTbUserRole userRole = QTbUserRole.tbUserRole;
        QTbUsers users = QTbUsers.tbUsers;

        // TODO 완성시키기
        queryFactory.selectFrom(users);
        return List.of();
    }

}
