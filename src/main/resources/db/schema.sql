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



