#TODO: pk, constraint 등을 alter table 로 수정

CREATE TABLE tbl_admin
(
    admin_id      INT AUTO_INCREMENT COMMENT '관리자 ID'
        PRIMARY KEY,
    username      VARCHAR(30)                           NOT NULL COMMENT '이메일',
    password      CHAR(64)                              NOT NULL COMMENT '비밀번호',
    nickname      VARCHAR(30)                           NOT NULL COMMENT '별명',
    register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT admin_nickname_uindex
        UNIQUE (nickname),
    CONSTRAINT admin_username_uindex
        UNIQUE (username)
)
    COMMENT '관리자';

CREATE TABLE tbl_category
(
    category_id   INT                                   NOT NULL COMMENT '카테고리 ID',
    name          VARCHAR(30)                           NOT NULL COMMENT '이름',
    register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    PRIMARY KEY (category_id)
)
    COMMENT '카테고리';

CREATE TABLE tbl_faq
(
    faq_id        INT AUTO_INCREMENT COMMENT 'FAQ ID'
        PRIMARY KEY,
    admin_id      INT                                    NOT NULL COMMENT '관리자 ID',
    category_id   INT                                    NOT NULL COMMENT '카테고리 ID',
    title         VARCHAR(100)                           NOT NULL COMMENT '제목',
    content       TEXT                                   NOT NULL COMMENT '내용',
    view_count    INT        DEFAULT 0                   NULL COMMENT '조회수',
    has_attach    TINYINT(1) DEFAULT 0                   NULL COMMENT '첨부파일 유무',
    thumbnail_uri VARCHAR(100)                           NULL COMMENT '썸네일 URI',
    register_date TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date   TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT faq_admin_admin_id_fk
        FOREIGN KEY (admin_id) REFERENCES tbl_admin (admin_id),
    CONSTRAINT faq_category_category_id_fk
        FOREIGN KEY (category_id) REFERENCES tbl_category (category_id)
)
    COMMENT 'FAQ';

CREATE TABLE tbl_faq_attach
(
    attach_id     INT AUTO_INCREMENT COMMENT '첨부파일 ID'
        PRIMARY KEY,
    faq_id        INT                                    NOT NULL COMMENT 'faq ID',
    upload_path   VARCHAR(200)                           NOT NULL COMMENT '저장 경로',
    uuid          VARCHAR(100)                           NOT NULL COMMENT 'UUID',
    name          VARCHAR(100)                           NOT NULL COMMENT '이름',
    extension     VARCHAR(20)                            NOT NULL COMMENT '확장자',
    is_image      TINYINT(1) DEFAULT 0                   NULL COMMENT '이미지 여부',
    size          INT                                    NOT NULL COMMENT '파일 크기',
    register_date TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date   TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT faq_attach_faq_faq_id_fk
        FOREIGN KEY (faq_id) REFERENCES tbl_faq (faq_id)
)
    COMMENT 'faq 첨부파일';

CREATE TABLE tbl_member
(
    member_id     INT AUTO_INCREMENT COMMENT '회원 ID'
        PRIMARY KEY,
    username      VARCHAR(30)                           NOT NULL COMMENT '이메일',
    password      CHAR(64)                              NOT NULL COMMENT '비밀번호',
    nickname      VARCHAR(30)                           NOT NULL COMMENT '별명',
    register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT member_nickname_uindex
        UNIQUE (nickname),
    CONSTRAINT member_username_uindex
        UNIQUE (username)
)
    COMMENT '회원';

CREATE TABLE tbl_board
(
    board_id       INT AUTO_INCREMENT COMMENT '게시물 ID'
        PRIMARY KEY,
    category_id    INT                                    NOT NULL COMMENT '카테고리 ID',
    member_id      INT                                    NULL COMMENT '회원 ID',
    admin_id       INT                                    NULL COMMENT '관리자 ID',
    guest_nickname VARCHAR(30)                            NULL COMMENT '익명 게시자 별명',
    guest_password CHAR(64)                               NULL COMMENT '익명 게시자 비밀번호',
    title          VARCHAR(100)                           NOT NULL COMMENT '제목',
    content        TEXT                                   NOT NULL COMMENT '내용',
    view_count     INT        DEFAULT 0                   NULL COMMENT '조회수',
    has_attach     TINYINT(1) DEFAULT 0                   NULL COMMENT '첨부파일 유무',
    thumbnail_uri  VARCHAR(100)                           NULL COMMENT '썸네일 URI',
    register_date  TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date    TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT board_admin_admin_id_fk
        FOREIGN KEY (admin_id) REFERENCES tbl_admin (admin_id),
    CONSTRAINT board_category_category_id_fk
        FOREIGN KEY (category_id) REFERENCES tbl_category (category_id),
    CONSTRAINT board_member_member_id_fk
        FOREIGN KEY (member_id) REFERENCES tbl_member (member_id)
)
    COMMENT '자유게시판';

CREATE TABLE tbl_board_attach
(
    attach_id     INT AUTO_INCREMENT COMMENT '첨부파일 ID'
        PRIMARY KEY,
    board_id      INT                                    NOT NULL COMMENT '게시글 ID',
    upload_path   VARCHAR(200)                           NOT NULL COMMENT '저장 경로',
    uuid          VARCHAR(100)                           NOT NULL COMMENT 'UUID',
    name          VARCHAR(100)                           NOT NULL COMMENT '이름',
    extension     VARCHAR(20)                            NOT NULL COMMENT '확장자',
    is_image      TINYINT(1) DEFAULT 0                   NULL COMMENT '이미지 여부',
    size          INT                                    NOT NULL COMMENT '파일 크기',
    register_date TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date   TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT board_attach_board_board_id_fk
        FOREIGN KEY (board_id) REFERENCES tbl_board (board_id)
)
    COMMENT '자유게시판 첨부파일';

CREATE TABLE tbl_comment
(
    comment_id     INT AUTO_INCREMENT COMMENT '댓글 ID'
        PRIMARY KEY,
    board_id       INT                                   NOT NULL COMMENT '게시글 ID',
    member_id      INT                                   NULL COMMENT '회원 ID',
    admin_id       INT                                   NULL COMMENT '관리자 ID',
    guest_nickname VARCHAR(30)                           NULL COMMENT '익명 댓글 작성자 별명',
    guest_password CHAR(64)                              NULL COMMENT '익명 댓글 작성자 비밀번호',
    content        VARCHAR(1000)                         NOT NULL COMMENT '내용',
    register_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT comment_admin_admin_id_fk
        FOREIGN KEY (admin_id) REFERENCES tbl_admin (admin_id),
    CONSTRAINT comment_board_board_id_fk
        FOREIGN KEY (board_id) REFERENCES tbl_board (board_id),
    CONSTRAINT comment_member_member_id_fk
        FOREIGN KEY (member_id) REFERENCES tbl_member (member_id)
)
    COMMENT '자유게시판 댓글';

CREATE TABLE tbl_notice
(
    notice_id     INT AUTO_INCREMENT COMMENT '공지사항 ID'
        PRIMARY KEY,
    admin_id      INT                                    NOT NULL COMMENT '관리자 ID',
    title         VARCHAR(100)                           NOT NULL COMMENT '제목',
    content       TEXT                                   NOT NULL COMMENT '내용',
    has_attach    TINYINT(1) DEFAULT 0                   NULL COMMENT '첨부파일 유무',
    thumbnail_uri VARCHAR(100)                           NULL COMMENT '썸네일 URI',
    register_date TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date   TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT notice_admin_admin_id_fk
        FOREIGN KEY (admin_id) REFERENCES tbl_admin (admin_id)
)
    COMMENT '공지사항';

CREATE TABLE tbl_notice_attach
(
    attach_id     INT AUTO_INCREMENT COMMENT '첨부파일 ID'
        PRIMARY KEY,
    notice_id     INT                                    NOT NULL COMMENT '공지사항 ID',
    upload_path   VARCHAR(200)                           NOT NULL COMMENT '저장 경로',
    uuid          VARCHAR(100)                           NOT NULL COMMENT 'UUID',
    name          VARCHAR(100)                           NOT NULL COMMENT '이름',
    extension     VARCHAR(20)                            NOT NULL COMMENT '확장자',
    is_image      TINYINT(1) DEFAULT 0                   NULL COMMENT '이미지 여부',
    size          INT                                    NOT NULL COMMENT '파일 크기',
    register_date TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date   TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT notice_attach_notice_notice_id_fk
        FOREIGN KEY (notice_id) REFERENCES tbl_notice (notice_id)
)
    COMMENT '공지사항 첨부파일';

CREATE TABLE tbl_question
(
    question_id   INT AUTO_INCREMENT COMMENT '질문 ID'
        PRIMARY KEY,
    category_id   INT                                    NOT NULL COMMENT '카테고리 ID',
    member_id     INT                                    NULL COMMENT '회원 ID',
    admin_id      INT                                    NULL COMMENT '관리자 ID',
    title         VARCHAR(100)                           NOT NULL COMMENT '제목',
    content       TEXT                                   NOT NULL COMMENT '내용',
    view_count    INT        DEFAULT 0                   NULL COMMENT '조회수',
    has_attach    TINYINT(1) DEFAULT 0                   NULL COMMENT '첨부파일 유무',
    thumbnail_uri VARCHAR(100)                           NULL COMMENT '썸네일 URI',
    is_secret     TINYINT(1) DEFAULT 0                   NULL COMMENT '비밀글 여부',
    register_date TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date   TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT question_admin_admin_id_fk
        FOREIGN KEY (admin_id) REFERENCES tbl_admin (admin_id),
    CONSTRAINT question_category_category_id_fk
        FOREIGN KEY (category_id) REFERENCES tbl_category (category_id),
    CONSTRAINT question_member_member_id_fk
        FOREIGN KEY (member_id) REFERENCES tbl_member (member_id)
)
    COMMENT 'Q&A 질문';

CREATE TABLE tbl_answer
(
    answer_id     INT AUTO_INCREMENT COMMENT '답변 ID'
        PRIMARY KEY,
    question_id   INT                                    NOT NULL COMMENT '질문 ID',
    admin_id      INT                                    NOT NULL COMMENT '관리자 ID',
    title         VARCHAR(100)                           NOT NULL COMMENT '제목',
    content       TEXT                                   NOT NULL COMMENT '내용',
    view_count    INT        DEFAULT 0                   NULL COMMENT '조회수',
    has_attach    TINYINT(1) DEFAULT 0                   NULL COMMENT '첨부파일 유무',
    thumbnail_uri VARCHAR(100)                           NULL COMMENT '썸네일 URI',
    register_date TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date   TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT answer_admin_admin_id_fk
        FOREIGN KEY (admin_id) REFERENCES tbl_admin (admin_id),
    CONSTRAINT answer_question_question_id_fk
        FOREIGN KEY (question_id) REFERENCES tbl_question (question_id)
)
    COMMENT 'Q&A 답변';

CREATE TABLE tbl_answer_attach
(
    attach_id     INT AUTO_INCREMENT COMMENT '첨부파일 ID'
        PRIMARY KEY,
    answer_id     INT                                    NOT NULL COMMENT '답변 ID',
    upload_path   VARCHAR(200)                           NOT NULL COMMENT '저장 경로',
    uuid          VARCHAR(100)                           NOT NULL COMMENT 'UUID',
    name          VARCHAR(100)                           NOT NULL COMMENT '이름',
    extension     VARCHAR(20)                            NOT NULL COMMENT '확장자',
    is_image      TINYINT(1) DEFAULT 0                   NULL COMMENT '이미지 여부',
    size          INT                                    NOT NULL COMMENT '파일 크기',
    register_date TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date   TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT answer_attach_answer_answer_id_fk
        FOREIGN KEY (answer_id) REFERENCES tbl_answer (answer_id)
)
    COMMENT '답변 첨부파일';

CREATE TABLE tbl_question_attach
(
    attach_id     INT AUTO_INCREMENT COMMENT '첨부파일 ID'
        PRIMARY KEY,
    question_id   INT                                    NOT NULL COMMENT '질문 ID',
    upload_path   VARCHAR(200)                           NOT NULL COMMENT '저장 경로',
    uuid          VARCHAR(100)                           NOT NULL COMMENT 'UUID',
    name          VARCHAR(100)                           NOT NULL COMMENT '이름',
    extension     VARCHAR(20)                            NOT NULL COMMENT '확장자',
    is_image      TINYINT(1) DEFAULT 0                   NULL COMMENT '이미지 여부',
    size          INT                                    NOT NULL COMMENT '파일 크기',
    register_date TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date   TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT question_attach_question_question_id_fk
        FOREIGN KEY (question_id) REFERENCES tbl_question (question_id)
)
    COMMENT 'Q&A 질문 첨부파일';