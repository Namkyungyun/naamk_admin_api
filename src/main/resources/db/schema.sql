--- user table
CREATE TABLE tb_users (
    id SERIAL PRIMARY KEY,
    user_nm VARCHAR(20) NOT NULL,
    user_email VARCHAR(50) NOT NULL,
    login_id VARCHAR(100) UNIQUE NOT NULL,
    login_pwd VARCHAR(100) NOT NULL,
    login_pwd_fail_cnt INT DEFAULT 0,
    activated BOOLEAN DEFAULT TRUE,
    last_login_at TIMESTAMP,
    pwd_expired_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP + INTERVAL '90 days',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_id ON tb_users(id);
COMMENT ON COLUMN tb_users.id IS '사용자 고유 ID';
COMMENT ON COLUMN tb_users.user_nm IS '사용자 이름';
COMMENT ON COLUMN tb_users.user_email IS '사용자 이메일';
COMMENT ON COLUMN tb_users.login_id IS '로그인 ID';
COMMENT ON COLUMN tb_users.login_pwd IS '로그인 패스워드';
COMMENT ON COLUMN tb_users.login_pwd_fail_cnt IS '패스워드 틀린 횟수';
COMMENT ON COLUMN tb_users.activated IS '활성여부';
COMMENT ON COLUMN tb_users.last_login_at IS '최근 로그인 일자';
COMMENT ON COLUMN tb_users.pwd_expired_at IS '패스워드 만료 일자';
COMMENT ON COLUMN tb_users.created_at IS '생성 일자';
COMMENT ON COLUMN tb_users.updated_at IS '수정 일자';


--- role table
CREATE TABLE tb_roles (
    id SERIAL PRIMARY KEY,
    role_nm VARCHAR(20) NOT NULL,
    role_cd VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON COLUMN tb_roles.id IS '역할 고유 ID';
COMMENT ON COLUMN tb_roles.role_nm IS '역할 명';
COMMENT ON COLUMN tb_roles.role_cd IS '역할 코드';
COMMENT ON COLUMN tb_roles.created_at IS '생성 일자';
COMMENT ON COLUMN tb_roles.updated_at IS '수정 일자';

--- user-role mapping
CREATE TABLE tb_user_role (
    user_id INT REFERENCES tb_users(id) ON DELETE CASCADE,
    role_id INT REFERENCES tb_roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);
COMMENT ON TABLE tb_user_role IS '사용자-역할 매핑 테이블';
COMMENT ON COLUMN tb_user_role.user_id IS '사용자 PK';
COMMENT ON COLUMN tb_user_role.role_id IS '역할 PK';


--- permission table
CREATE TABLE tb_perms (
    id SERIAL PRIMARY KEY,
    perm_cd VARCHAR(30) UNIQUE NOT NULL,
    perm_nm VARCHAR(30),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE tb_perms IS '권한 테이블';
COMMENT ON COLUMN tb_perms.id IS '권한 고유 ID';
COMMENT ON COLUMN tb_perms.perm_cd IS '권한 코드';
COMMENT ON COLUMN tb_perms.created_at IS '생성일';
COMMENT ON COLUMN tb_perms.updated_at IS '수정일';

--------- 역할-권한 테이블
CREATE TABLE tb_role_perm (
    role_id INT NOT NULL,
    perm_id INT NOT NULL,
    PRIMARY KEY (role_id, perm_id),
    FOREIGN KEY (role_id) REFERENCES tb_roles(id) ON DELETE CASCADE,
    FOREIGN KEY (perm_id) REFERENCES tb_perms(id) ON DELETE CASCADE
    );

COMMENT ON COLUMN tb_role_perm.role_id IS '역할 ID (tb_roles의 외래 키)';
COMMENT ON COLUMN tb_role_perm.perm_id IS '권한 ID (tb_perms의 외래 키)';
