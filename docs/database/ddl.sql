#TODO: pk, constraint 등을 alter table 로 수정
#TODO: 예약어는 대문자로 수정

create table tbl_admin
(
    admin_id      int auto_increment comment '관리자 ID'
        primary key,
    username      varchar(30)                           not null comment '이메일',
    password      char(64)                              not null comment '비밀번호',
    nickname      varchar(30)                           not null comment '별명',
    register_date timestamp default current_timestamp() null comment '등록일',
    update_date   timestamp default current_timestamp() null comment '수정일',
    constraint admin_nickname_uindex
        unique (nickname),
    constraint admin_username_uindex
        unique (username)
)
    comment '관리자' engine = InnoDB;

create table tbl_category
(
    category_id   int                                   not null comment '카테고리 ID'
        primary key,
    name          varchar(30)                           not null comment '이름',
    register_date timestamp default current_timestamp() null comment '등록일',
    update_date   timestamp default current_timestamp() null comment '수정일'
)
    comment '카테고리' engine = InnoDB;

create table tbl_faq
(
    faq_id        int auto_increment comment 'FAQ ID'
        primary key,
    admin_id      int                                    not null comment '관리자 ID',
    category_id   int                                    not null comment '카테고리 ID',
    title         varchar(100)                           not null comment '제목',
    content       text                                   not null comment '내용',
    view_count    int        default 0                   null comment '조회수',
    has_attach    tinyint(1) default 0                   null comment '첨부파일 유무',
    thumbnail_uri varchar(100)                           null comment '썸네일 URI',
    register_date timestamp  default current_timestamp() null comment '등록일',
    update_date   timestamp  default current_timestamp() null comment '수정일',
    constraint faq_admin_admin_id_fk
        foreign key (admin_id) references tbl_admin (admin_id),
    constraint faq_category_category_id_fk
        foreign key (category_id) references tbl_category (category_id)
)
    comment 'FAQ' engine = InnoDB;

create table tbl_faq_attach
(
    attach_id     int auto_increment comment '첨부파일 ID'
        primary key,
    faq_id        int                                    not null comment 'faq ID',
    upload_path   varchar(200)                           not null comment '저장 경로',
    uuid          varchar(100)                           not null comment 'UUID',
    name          varchar(100)                           not null comment '이름',
    extension     varchar(20)                            not null comment '확장자',
    is_image      tinyint(1) default 0                   null comment '이미지 여부',
    size          int                                    not null comment '파일 크기',
    register_date timestamp  default current_timestamp() null comment '등록일',
    update_date   timestamp  default current_timestamp() null comment '수정일',
    constraint faq_attach_faq_faq_id_fk
        foreign key (faq_id) references tbl_faq (faq_id)
)
    comment 'faq 첨부파일' engine = InnoDB;

create table tbl_member
(
    member_id     int auto_increment comment '회원 ID'
        primary key,
    username      varchar(30)                           not null comment '이메일',
    password      char(64)                              not null comment '비밀번호',
    nickname      varchar(30)                           not null comment '별명',
    register_date timestamp default current_timestamp() null comment '등록일',
    update_date   timestamp default current_timestamp() null comment '수정일',
    constraint member_nickname_uindex
        unique (nickname),
    constraint member_username_uindex
        unique (username)
)
    comment '회원' engine = InnoDB;

create table tbl_board
(
    board_id       int auto_increment comment '게시물 ID'
        primary key,
    category_id    int                                    not null comment '카테고리 ID',
    member_id      int                                    null comment '회원 ID',
    admin_id       int                                    null comment '관리자 ID',
    guest_nickname varchar(30)                            null comment '익명 게시자 별명',
    guest_password char(64)                               null comment '익명 게시자 비밀번호',
    title          varchar(100)                           not null comment '제목',
    content        text                                   not null comment '내용',
    view_count     int        default 0                   null comment '조회수',
    has_attach     tinyint(1) default 0                   null comment '첨부파일 유무',
    thumbnail_uri  varchar(100)                           null comment '썸네일 URI',
    register_date  timestamp  default current_timestamp() null comment '등록일',
    update_date    timestamp  default current_timestamp() null comment '수정일',
    constraint board_admin_admin_id_fk
        foreign key (admin_id) references tbl_admin (admin_id),
    constraint board_category_category_id_fk
        foreign key (category_id) references tbl_category (category_id),
    constraint board_member_member_id_fk
        foreign key (member_id) references tbl_member (member_id)
)
    comment '자유게시판' engine = InnoDB;

create table tbl_board_attach
(
    attach_id     int auto_increment comment '첨부파일 ID'
        primary key,
    board_id      int                                    not null comment '게시글 ID',
    upload_path   varchar(200)                           not null comment '저장 경로',
    uuid          varchar(100)                           not null comment 'UUID',
    name          varchar(100)                           not null comment '이름',
    extension     varchar(20)                            not null comment '확장자',
    is_image      tinyint(1) default 0                   null comment '이미지 여부',
    size          int                                    not null comment '파일 크기',
    register_date timestamp  default current_timestamp() null comment '등록일',
    update_date   timestamp  default current_timestamp() null comment '수정일',
    constraint board_attach_board_board_id_fk
        foreign key (board_id) references tbl_board (board_id)
)
    comment '자유게시판 첨부파일' engine = InnoDB;

create table tbl_comment
(
    comment_id     int auto_increment comment '댓글 ID'
        primary key,
    board_id       int                                   not null comment '게시글 ID',
    member_id      int                                   null comment '회원 ID',
    admin_id       int                                   null comment '관리자 ID',
    guest_nickname varchar(30)                           null comment '익명 댓글 작성자 별명',
    guest_password char(64)                              null comment '익명 댓글 작성자 비밀번호',
    content        varchar(1000)                         not null comment '내용',
    register_date  timestamp default current_timestamp() null comment '등록일',
    update_date    timestamp default current_timestamp() null comment '수정일',
    constraint comment_admin_admin_id_fk
        foreign key (admin_id) references tbl_admin (admin_id),
    constraint comment_board_board_id_fk
        foreign key (board_id) references tbl_board (board_id),
    constraint comment_member_member_id_fk
        foreign key (member_id) references tbl_member (member_id)
)
    comment '자유게시판 댓글' engine = InnoDB;

create table tbl_notice
(
    notice_id     int auto_increment comment '공지사항 ID'
        primary key,
    admin_id      int                                    not null comment '관리자 ID',
    title         varchar(100)                           not null comment '제목',
    content       text                                   not null comment '내용',
    has_attach    tinyint(1) default 0                   null comment '첨부파일 유무',
    thumbnail_uri varchar(100)                           null comment '썸네일 URI',
    register_date timestamp  default current_timestamp() null comment '등록일',
    update_date   timestamp  default current_timestamp() null comment '수정일',
    constraint notice_admin_admin_id_fk
        foreign key (admin_id) references tbl_admin (admin_id)
)
    comment '공지사항' engine = InnoDB;

create table tbl_notice_attach
(
    attach_id     int auto_increment comment '첨부파일 ID'
        primary key,
    notice_id     int                                    not null comment '공지사항 ID',
    upload_path   varchar(200)                           not null comment '저장 경로',
    uuid          varchar(100)                           not null comment 'UUID',
    name          varchar(100)                           not null comment '이름',
    extension     varchar(20)                            not null comment '확장자',
    is_image      tinyint(1) default 0                   null comment '이미지 여부',
    size          int                                    not null comment '파일 크기',
    register_date timestamp  default current_timestamp() null comment '등록일',
    update_date   timestamp  default current_timestamp() null comment '수정일',
    constraint notice_attach_notice_notice_id_fk
        foreign key (notice_id) references tbl_notice (notice_id)
)
    comment '공지사항 첨부파일' engine = InnoDB;

create table tbl_question
(
    question_id   int auto_increment comment '질문 ID'
        primary key,
    category_id   int                                    not null comment '카테고리 ID',
    member_id     int                                    null comment '회원 ID',
    admin_id      int                                    null comment '관리자 ID',
    title         varchar(100)                           not null comment '제목',
    content       text                                   not null comment '내용',
    view_count    int        default 0                   null comment '조회수',
    has_attach    tinyint(1) default 0                   null comment '첨부파일 유무',
    thumbnail_uri varchar(100)                           null comment '썸네일 URI',
    is_secret     tinyint(1) default 0                   null comment '비밀글 여부',
    register_date timestamp  default current_timestamp() null comment '등록일',
    update_date   timestamp  default current_timestamp() null comment '수정일',
    constraint question_admin_admin_id_fk
        foreign key (admin_id) references tbl_admin (admin_id),
    constraint question_category_category_id_fk
        foreign key (category_id) references tbl_category (category_id),
    constraint question_member_member_id_fk
        foreign key (member_id) references tbl_member (member_id)
)
    comment 'Q&A 질문' engine = InnoDB;

create table tbl_answer
(
    answer_id     int auto_increment comment '답변 ID'
        primary key,
    question_id   int                                    not null comment '질문 ID',
    admin_id      int                                    not null comment '관리자 ID',
    title         varchar(100)                           not null comment '제목',
    content       text                                   not null comment '내용',
    view_count    int        default 0                   null comment '조회수',
    has_attach    tinyint(1) default 0                   null comment '첨부파일 유무',
    thumbnail_uri varchar(100)                           null comment '썸네일 URI',
    register_date timestamp  default current_timestamp() null comment '등록일',
    update_date   timestamp  default current_timestamp() null comment '수정일',
    constraint answer_admin_admin_id_fk
        foreign key (admin_id) references tbl_admin (admin_id),
    constraint answer_question_question_id_fk
        foreign key (question_id) references tbl_question (question_id)
)
    comment 'Q&A 답변' engine = InnoDB;

create table tbl_answer_attach
(
    attach_id     int auto_increment comment '첨부파일 ID'
        primary key,
    answer_id     int                                    not null comment '답변 ID',
    upload_path   varchar(200)                           not null comment '저장 경로',
    uuid          varchar(100)                           not null comment 'UUID',
    name          varchar(100)                           not null comment '이름',
    extension     varchar(20)                            not null comment '확장자',
    is_image      tinyint(1) default 0                   null comment '이미지 여부',
    size          int                                    not null comment '파일 크기',
    register_date timestamp  default current_timestamp() null comment '등록일',
    update_date   timestamp  default current_timestamp() null comment '수정일',
    constraint answer_attach_answer_answer_id_fk
        foreign key (answer_id) references tbl_answer (answer_id)
)
    comment '답변 첨부파일' engine = InnoDB;

create table tbl_question_attach
(
    attach_id     int auto_increment comment '첨부파일 ID'
        primary key,
    question_id   int                                    not null comment '질문 ID',
    upload_path   varchar(200)                           not null comment '저장 경로',
    uuid          varchar(100)                           not null comment 'UUID',
    name          varchar(100)                           not null comment '이름',
    extension     varchar(20)                            not null comment '확장자',
    is_image      tinyint(1) default 0                   null comment '이미지 여부',
    size          int                                    not null comment '파일 크기',
    register_date timestamp  default current_timestamp() null comment '등록일',
    update_date   timestamp  default current_timestamp() null comment '수정일',
    constraint question_attach_question_question_id_fk
        foreign key (question_id) references tbl_question (question_id)
)
    comment 'Q&A 질문 첨부파일' engine = InnoDB;