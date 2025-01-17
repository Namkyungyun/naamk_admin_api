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
- MockMvc는 MockHttpServletRequestBuilder (예: MockMvcRequestBuilders)를 사용해야 하고, MockServerHttpRequest는 WebTestClient와 함께 사용
  - MockMvcRequestBuilders.get(), MockMvcRequestBuilders.post(), MockMvcRequestBuilders.put(), MockMvcRequestBuilders.delete() 로 사용하기

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


## Rest Docs 설정하기
### build.gradle
#### (1) Asciidoctor를 사용하여 .adoc 파일을 html 또는 pdf 문서로 변환
```groovy
plugins {
    id "org.asciidoctor.jvm.convert" version '3.3.2'
}
```
- Asciidoctor 플러그인은 Gradle 태스크(asciidoctor)를 자동 생성하며, 이 태스크를 사용해 .adoc 파일을 변환.
- spring REST Docs가 생성한 스니펫(snippets)을 포함하는 .adoc 파일을 html로 변환하는데 사용됨.
  - HTML, PDF, DOC 등의 다양한 형식으로 변환 가능.
- 최신 버전(3.3.2)은 Gradle 7.x 이상과 호환됩니다.
#### (2) snippetsDir 정의
```groovy
ext {
    snippetsDir = file("build/generated-snippets")
}
```

#### (3) Javadoc 설정
```groovy
javadoc {
    options.encoding = 'UTF-8'
}
```
- options.encoding='UTF-8'설정을 통해 java doc에서 문서 파일의 문자 인코딩을 utf-8로 설정
- 이 설정은 Javadoc에서 기본적으로 ASCII만 처리하는 문제가 있으므로 Java 소스 코드 주석이 UTF-8로 작성된 경우에도 Javadoc 생성 중 깨지지 않도록 보장.
- 최신 Gradle에서는 생략해도 대부분 환경에서 UTF-8이 기본값으로 동작.
#### (4) Configurations 설정
```groovy
configurations {
    asciidoctorExtensions
    compileOnly {
        extendsFrom annotationProcessor
    }
}
```
- "asciidoctorExtensions"
    - 새로운 의존성 구성으로, asciidoctor의 확장을 위한 의존성 관리.
    - REST Docs 스니펫(.adoc 파일)에 포함할 확장 기능(예: 문서 스타일, 테이블 구조 등)을 정의하는 의존성을 추가하기 위해 사용.
    - 예: org.springframework.restdocs:spring-restdocs-asciidoctor와 같은 의존성이 포함
- "compileOnly"
    - 컴파일 단계에서만 필요한 의존성을 관리.
    - extendsFrom annotationProcessor는 annotationProcessor 구성의 의존성을 compileOnly 구성으로 확장하여, Lombok과 같은 애노테이션 프로세서를 컴파일 시에만 사용하도록 설정
#### (5) 의존성 설정
```groovy
dependencies {
    testImplementation 'org.junit.platform:junit-platform-launcher'
    testImplementation('org.junit.jupiter:junit-jupiter')
    testImplementation('org.junit.jupiter:junit-jupiter-api')
    testRuntimeOnly('org.junit.jupiter:junit-jupiter-engine')

    // Spring REST Docs
    asciidoctorExtensions 'org.springframework.restdocs:spring-restdocs-asciidoctor'
    testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'
}
```
- Junit 관련 의존성
    - junit-jupiter : Junit5 플랫폼 및 API를 사용하여 테스트를 작성
    - junit-jupiter-engine : Junit5 테스트 진행을 위한 엔진.
- Spring REST Docs 관련 의존성
    - spring-restdocs-mockmvc :
        - spring rest docs의 mockmvc 통합을 지원
        - 테스트를 실행하여 rest api 요청 및 응답 데이터를 캡처하고 스니펫을 생성
    - spring-restdocs-asciidoctor :
        - asciidoctor와 통합하여 rest docs 스니펫을 .adoc 파일에 포함시키는 데 사용.

#### (6) Test Task 설정
```groovy
test {
    outputs.dir snippetsDir
    useJUnitPlatform()
}
```
- outputs.dir snippetsDir
  - 테스트 실행 결과로 생성되는 REST Docs 스니펫 디렉토리(build/generated-snippets)를 설정.
  - build/generated-snippets는 REST Docs 스니펫이 저장되는 디렉토리.
  - 이 경로는 이후 asciidoctor 태스크에서 사용.
- useJUnitPlatform() 
  - JUnit5 플랫폼에서 테스트를 실행하도록 설정.


#### (7) Asciidoctor 상세 설정
```groovy
asciidoctor {
    configurations 'asciidoctorExtensions'
    inputs.dir snippetsDir
    dependsOn test
    sources {
        include("**/index.adoc")
    }
    // 경로를 baseDir로 맞춰준다!
    baseDirFollowsSourceFile()
}
```
- configurations 'asciidoctorExtensions' : asciidoctorExtensions 구성에서 정의된 의존성을 사용하여 Asciidoctor 확장을 추가합니다.
- inputs.dir snippetsDir : REST Docs 스니펫이 포함된 디렉토리를 Asciidoctor의 입력으로 설정합니다.
- dependsOn test : asciidoctor는 test 태스크가 실행되어 REST Docs 스니펫이 생성된 후 실행됩니다.
- sources : 변환할 .adoc 파일을 지정합니다. 여기서는 index.adoc만 포함되도록 설정합니다.
- baseDirFollowsSourceFile() : 소스 파일 경로를 기준으로 Asciidoctor의 기본 경로를 설정합니다
  - .adoc 파일의 경로를 기준으로 문서 생성의 기본 디렉토리를 설정.
  -  Asciidoctor가 상대 경로를 올바르게 처리하도록 도움.

- 문서화를 시작하기 전에, 이전에 생성된 HTML 문서를 삭제하여 새롭게 생성된 문서만 남도록 설정합니다.
#### (8) 문서 복사 태스크
```groovy
task copyDocument(type: Copy) { // (12)
    dependsOn asciidoctor
    from asciidoctor.outputDir
    into 'src/main/resources/static/docs'
}
```
- Copy 태스크:
  - Asciidoctor 태스크 실행 후 생성된 HTML 문서를 src/main/resources/static/docs 디렉토리로 복사합니다.
  - 문서를 애플리케이션의 정적 리소스로 배포할 수 있도록 설정합니다.

#### build.gradle 설정 값에 대한 주요 작업 정리
위 설정은 Spring REST Docs와 Asciidoctor를 사용하여 REST API 문서를 자동 생성 및 HTML로 변환하는 전체적인 빌드 과정입니다. 주요 작업은 다음과 같습니다:

1. 테스트 실행 → REST Docs 스니펫 생성
   - test 태스크에서 실행.
   - 생성된 스니펫은 build/generated-snippets 디렉토리에 저장.
2. 스니펫을 .adoc 파일에 포함 및 HTML 변환
   - asciidoctor 태스크를 통해 .adoc 파일을 HTML로 변환.
3. HTML 문서를 정적 리소스 디렉토리로 복사
   - copyDocument 태스크로 HTML 파일을 src/main/resources/static/docs에 복사.
   - Spring Boot 애플리케이션 실행 시 /docs 경로에서 문서 제공 가능.
   - 
이 설정을 통해 빌드 후 REST API 문서가 src/main/resources/static/docs에 HTML로 배포


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