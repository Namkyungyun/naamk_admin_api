# Getting Started

## JPA CreatedAt, UpdatedAt 자동 생성
- @EnableJpaAuditing -> Main class
- @EntityListeners( value = { AuditingEntityListener.class} ) -> abstract AuditEntity 클래스에 넣고 다른 도메인들은 이를 상속받는 형식으로 진행
```java
@EntityListeners( value = { AuditingEntityListener.class} )
@MappedSuperclass
public abstract class AuditEntity {

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "created_at", updatable = false)
    @Comment("생성일자")
    private Timestamp createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "updated_at")
    @Comment("수정일자")
    private Timestamp updatedAt;

}
```

## Entity 칼럼으로 Enum 넣기
```java
@Table 
@Entity 
public class AEntity {
    // ...
    
    @Column
    @Enumerated(EnumType.STRING)
    private BEnum enumValue;
}
```


## TEST CODE
### 컨트롤러 테스트 
- @AutoConfigureMockMvc -> Mock(가짜) 환경에 MockMvc가 등록됨.
- @Autowired -> 가짜 환경에 등록된 MockMvc 주입(DI)함.
- application.yml의 설정된 환경으로 테스트 환경이 빌드되어지므로 주의할 것 (local, dev, prod)
- 'assertj.core.api.Assertions' library 이용해 assertThat()으로 테스트 진행 

```java
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class ATest {
    
    @Autowired
    private MockMvc mvc;
    
    @Test
    public void a_test() throws Exception {
        // given : dto와 같은 mock으로 넣을 데이터를 준비하는 단계
        
        // when
        ResultActions resultActions = mvc.perform(get("/api/menus"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int httpStatusCode = resultActions.andReturn().getResponse().getStatus();
        
        // then
        assertThat(httpStatusCode).isEqualTo(200);
    }
    
}
```
### 서비스 테스트 
- @ExtendWith(MockitoException.class) -> mockito 환경에서 테스트 진행
- @InjectMocks -> mock(가짜) 환경에서 테스트 주체가 되는 서비스에 어노테이션 부여
- @Mock -> 테스트 주체가 되는 서비스에 주입되어야하는 연관 빈들을 가져오는 역할 (가짜로)
- @Spy -> 실제 Spring context Ioc내에 등록된 객체를 가져오는 역할.

```java
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

// Spring 관련 Bean들이 하나도 없는 환경 !
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    
    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void createUser() {
        // given -> dto 준비
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUserName("냠크");
        userRequestDto.setUserEmail("naamk@test.com");
        userRequestDto.setLoginId("test");
        userRequestDto.setLoginPassword("pwdpwd");

        // stub1 -> 연관 빈들이 mock annotation으로 들어왔는지 검사
        when(userRepository.findByUserName(any())).thenReturn(Optional.empty());

        // stub2 
        TbUsers savedUser = new TbUsers();
        savedUser.setId(1L);
        savedUser.setUserName("냠크");
        savedUser.setUserEmail("naamk@test.com");
        savedUser.setLoginId("test");
        savedUser.setLoginPassword("pwdpwd");
        savedUser.createdAt(Timestamp.valueOf(LocalDateTime.now()));
        savedUser.upatedAt(Timestamp.valueOf(LocalDateTime.now()));
        
        when(userRepository.save(any())).thenReturn(savedUser);

        // when -> 테스트를 진행할 조건 넣기
        TbUsers entity = UserMapper.INSTANCE.createDtoToEntity(userRequestDto);
        UserResponseDto userResponseDto =  userService.createUser(entity);

        // then -> 테스트의 결과
        assetThat(userResponseDto.getId()).isEqualTo(savedUser.getId());
    }
}

```


## INIT DATA API 호출
#### 권한 일반
```
 curl -X POST 'localhost:28080/perms' \
-H 'Content-Type: application/json' \
-d '{
    "permCd": "C",
    "permNm": "생성"
}'; \
curl -X POST 'localhost:28080/perms' \
-H 'Content-Type: application/json' \
-d '{
    "permCd": "R",
    "permNm": "조회"
}'; \
curl -X POST 'localhost:28080/perms' \
-H 'Content-Type: application/json' \
-d '{
    "permCd": "D",
    "permNm": "삭제"
}'; \
curl -X POST 'localhost:28080/perms' \
-H 'Content-Type: application/json' \
-d '{
    "permCd": "U",
    "permNm": "수정"
}'
 
```


#### 권한 그룹
```
curl -X POST 'localhost:28080/perms/grp' \
-H 'Content-Type: application/json' \
-d '{
    "permGrpCd": "MENU_ROOT",
    "permGrpNm": "루트메뉴그룹권한",
    "permGrpDesc": "루트메뉴에 할당되는 권한의 그룹."
}'; \
curl -X PUT --request PUT 'localhost:28080/perms/grp/1' \
-H 'Content-Type: application/json' \
-d '{
    "permGrpCd": "MENU_ROOT",
    "permGrpNm": "루트메뉴그룹권한",
    "permGrpDesc": "루트메뉴에 할당되는 권한의 그룹.",
    "childPerms": [
        {
            "permId": 2,
            "activated": true
        }
    ]
}'; \

curl -X POST 'localhost:28080/perms/grp' \
-H 'Content-Type: application/json' \
-d '{
    "permGrpCd": "MENU_BASE",
    "permGrpNm": "일반메뉴그룹권한",
    "permGrpDesc": "루트메뉴에 하위로 들어가야하는 일반 메뉴 그룹."
}'; \
curl -X PUT --request PUT 'localhost:28080/perms/grp/2' \
-H 'Content-Type: application/json' \
-d '{
    "permGrpCd": "MENU_BASE",
    "permGrpNm": "일반메뉴그룹권한",
    "permGrpDesc": "루트메뉴에 하위로 들어가야하는 일반 메뉴 그룹.",
    "childPerms": [
        {
            "permId": 1,
            "activated": true
        },
         {
            "permId": 2,
            "activated": true
        },
         {
            "permId": 3,
            "activated": true
        },
         {
            "permId": 4,
            "activated": true
        }
    ]
}'
```

#### 역할
```
curl -X POST 'localhost:28080/roles' \
-H 'Content-Type: application/json' \
-d '{
    "roleNm": "슈퍼어드민",
    "roleCd": "SUPERADMIN"
}'; \
curl -X POST 'localhost:28080/roles' \
-H 'Content-Type: application/json' \
-d '{
    "roleNm": "어드민",
    "roleCd": "ADMIN"
}'; \
curl -X POST 'localhost:28080/roles' \
-H 'Content-Type: application/json' \
-d '{
    "roleNm": "뷰어",
    "roleCd": "VIEWER"
}'
```

#### 메뉴
```
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "ROOT1",
    "menuNm": "루트 메뉴1",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": null,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU1",
    "menuNm": "메뉴1",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": 1,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU1-1",
    "menuNm": "메뉴1-1",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": 2,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU1-2",
    "menuNm": "메뉴1-2",
    "menuDesc": "test",
    "orderNum": 2,
    "parentId": 2,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "ROOT2",
    "menuNm": "루트 메뉴2",
    "menuDesc": "test",
    "orderNum": 2,
    "parentId": null,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU2",
    "menuNm": "메뉴2",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": 5,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU2-1",
    "menuNm": "메뉴2-1",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": 6,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "ROOT3",
    "menuNm": "루트3",
    "menuDesc": "test",
    "orderNum": 3,
    "parentId": null,
    "pathUrl": "/"
}'
```


#### 사용자 
```
curl -X POST 'localhost:28080/users' \
-H 'Content-Type: application/json' \
-d '{
    "userNm": "테스터1",
    "userEmail": "test@test.com",
    "loginId": "test1",
    "loginPwd": "test",
    "roleIds": [1]
}'; \
curl -X POST 'localhost:28080/users' \
-H 'Content-Type: application/json' \
-d '{
    "userNm": "테스터2",
    "userEmail": "test@test.com",
    "loginId": "test2",
    "loginPwd": "test",
    "roleIds": [2]
}'; \
curl -X POST 'localhost:28080/users' \
-H 'Content-Type: application/json' \
-d '{
    "userNm": "테스터3",
    "userEmail": "test@test.com",
    "loginId": "test3",
    "loginPwd": "test",
    "roleIds": [3]
}'
```

### 전체 CMD
```
curl -X POST 'localhost:28080/perms' \
-H 'Content-Type: application/json' \
-d '{
    "permCd": "C",
    "permNm": "생성"
}'; \
curl -X POST 'localhost:28080/perms' \
-H 'Content-Type: application/json' \
-d '{
    "permCd": "R",
    "permNm": "조회"
}'; \
curl -X POST 'localhost:28080/perms' \
-H 'Content-Type: application/json' \
-d '{
    "permCd": "D",
    "permNm": "삭제"
}'; \
curl -X POST 'localhost:28080/perms' \
-H 'Content-Type: application/json' \
-d '{
    "permCd": "U",
    "permNm": "수정"
}'; \
curl -X POST 'localhost:28080/perms/grp' \
-H 'Content-Type: application/json' \
-d '{
    "permGrpCd": "MENU_ROOT",
    "permGrpNm": "루트메뉴그룹권한",
    "permGrpDesc": "루트메뉴에 할당되는 권한의 그룹."
}'; \
curl -X PUT --request PUT 'localhost:28080/perms/grp/1' \
-H 'Content-Type: application/json' \
-d '{
    "permGrpCd": "MENU_ROOT",
    "permGrpNm": "루트메뉴그룹권한",
    "permGrpDesc": "루트메뉴에 할당되는 권한의 그룹.",
    "childPerms": [
        {
            "permId": 2,
            "activated": true
        }
    ]
}'; \
curl -X POST 'localhost:28080/perms/grp' \
-H 'Content-Type: application/json' \
-d '{
    "permGrpCd": "MENU_BASE",
    "permGrpNm": "일반메뉴그룹권한",
    "permGrpDesc": "루트메뉴에 하위로 들어가야하는 일반 메뉴 그룹."
}'; \
curl -X PUT --request PUT 'localhost:28080/perms/grp/2' \
-H 'Content-Type: application/json' \
-d '{
    "permGrpCd": "MENU_BASE",
    "permGrpNm": "일반메뉴그룹권한",
    "permGrpDesc": "루트메뉴에 하위로 들어가야하는 일반 메뉴 그룹.",
    "childPerms": [
        {
            "permId": 1,
            "activated": true
        },
         {
            "permId": 2,
            "activated": true
        },
         {
            "permId": 3,
            "activated": true
        },
         {
            "permId": 4,
            "activated": true
        }
    ]
}'; \
curl -X POST 'localhost:28080/roles' \
-H 'Content-Type: application/json' \
-d '{
    "roleNm": "슈퍼어드민",
    "roleCd": "SUPERADMIN"
}'; \
curl -X POST 'localhost:28080/roles' \
-H 'Content-Type: application/json' \
-d '{
    "roleNm": "어드민",
    "roleCd": "ADMIN"
}'; \
curl -X POST 'localhost:28080/roles' \
-H 'Content-Type: application/json' \
-d '{
    "roleNm": "뷰어",
    "roleCd": "VIEWER"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "ROOT1",
    "menuNm": "루트 메뉴1",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": null,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU1",
    "menuNm": "메뉴1",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": 1,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU1-1",
    "menuNm": "메뉴1-1",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": 2,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU1-2",
    "menuNm": "메뉴1-2",
    "menuDesc": "test",
    "orderNum": 2,
    "parentId": 2,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "ROOT2",
    "menuNm": "루트 메뉴2",
    "menuDesc": "test",
    "orderNum": 2,
    "parentId": null,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU2",
    "menuNm": "메뉴2",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": 5,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "MENU2-1",
    "menuNm": "메뉴2-1",
    "menuDesc": "test",
    "orderNum": 1,
    "parentId": 6,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/menus' \
-H 'Content-Type: application/json' \
-d '{
    "menuCd": "ROOT3",
    "menuNm": "루트3",
    "menuDesc": "test",
    "orderNum": 3,
    "parentId": null,
    "pathUrl": "/"
}'; \
curl -X POST 'localhost:28080/users' \
-H 'Content-Type: application/json' \
-d '{
    "userNm": "테스터1",
    "userEmail": "test@test.com",
    "loginId": "test1",
    "loginPwd": "test",
    "roleIds": [1]
}'; \
curl -X POST 'localhost:28080/users' \
-H 'Content-Type: application/json' \
-d '{
    "userNm": "테스터2",
    "userEmail": "test@test.com",
    "loginId": "test2",
    "loginPwd": "test",
    "roleIds": [2]
}'; \
curl -X POST 'localhost:28080/users' \
-H 'Content-Type: application/json' \
-d '{
    "userNm": "테스터3",
    "userEmail": "test@test.com",
    "loginId": "test3",
    "loginPwd": "test",
    "roleIds": [3]
}'
```