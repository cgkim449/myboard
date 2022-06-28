## password 는 전부 '1a@@'를 SHA256으로 암호화한 것 https://emn178.github.io/online-tools/sha256.html

## 관리자, 회원, 자유게시판 게시글/첨부파일/댓글 데이터 전체 삭제
DELETE FROM tbl_comment;
DELETE FROM tbl_board_attach;
DELETE FROM tbl_board;
# DELETE FROM tbl_member;
# DELETE FROM tbl_admin;

## auto increment 값 초기화
ALTER TABLE tbl_admin AUTO_INCREMENT=1;
SET @COUNT = 0;
UPDATE tbl_admin SET admin_id = @COUNT:=@COUNT+1;

ALTER TABLE tbl_member AUTO_INCREMENT=1;
SET @COUNT = 0;
UPDATE tbl_member SET member_id = @COUNT:=@COUNT+1;

ALTER TABLE tbl_comment AUTO_INCREMENT=1;
SET @COUNT = 0;
UPDATE tbl_comment SET comment_id = @COUNT:=@COUNT+1;

ALTER TABLE tbl_board_attach AUTO_INCREMENT=1;
SET @COUNT = 0;
UPDATE tbl_board_attach SET attach_id = @COUNT:=@COUNT+1;

ALTER TABLE tbl_board AUTO_INCREMENT=1;
SET @COUNT = 0;
UPDATE tbl_board SET board_id = @COUNT:=@COUNT+1;

## 카테고리 insert
INSERT INTO tbl_category(category_id, name)
VALUES(1, 'Java');

INSERT INTO tbl_category(category_id, name)
VALUES(2, 'JavaScript');

INSERT INTO tbl_category(category_id, name)
VALUES(3, 'Database');

## 관리자 insert
INSERT INTO tbl_admin(username, nickname, password)
VALUES('admin@admin.com', '관리자' ,'adcca5581f71d0add132aff599282c3b2c1e48307a9d9cdf6da0d4ba3d27ca10');

INSERT INTO tbl_admin(username, nickname, password)
VALUES('admin2@admin.com', '관리자2' ,'adcca5581f71d0add132aff599282c3b2c1e48307a9d9cdf6da0d4ba3d27ca10');


## 회원 insert
# DELIMITER $$
# DROP PROCEDURE IF EXISTS insertMembers $$
#
# CREATE PROCEDURE insertMembers()
# BEGIN
#     DECLARE i INT DEFAULT 1;
#
#     WHILE i <= 4 DO
#             INSERT INTO tbl_member(username, nickname, password)
#             VALUES(concat(i,'@',i,'.com'), concat('회원', i), 'adcca5581f71d0add132aff599282c3b2c1e48307a9d9cdf6da0d4ba3d27ca10');
#             SET i = i + 1;
#         END WHILE;
# END$$
# DELIMITER $$
#
# CALL insertMembers();

## 자유게시판 익명 게시물 insert
DELIMITER $$
DROP PROCEDURE IF EXISTS insertGuestBoards $$

CREATE PROCEDURE insertGuestBoards()
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i <= 63 DO
            INSERT INTO tbl_board(category_id, title, content, guest_nickname, guest_password)
            VALUES(MOD(i, 3) + 1, concat('제목제목', i), concat('내용내용', i), concat('익명', MOD(i, 5) + 1), 'adcca5581f71d0add132aff599282c3b2c1e48307a9d9cdf6da0d4ba3d27ca10');

            SET i = i + 1;
        END WHILE;
END$$
DELIMITER $$

CALL insertGuestBoards();

## 자유게시판 회원 게시물 insert
DELIMITER $$
DROP PROCEDURE IF EXISTS insertMemberBoards $$

CREATE PROCEDURE insertMemberBoards()
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i <= 63 DO
            INSERT INTO tbl_board(category_id, title, content, member_id)
            VALUES(MOD(i, 3) + 1, concat('제목제목', i), concat('내용내용', i), MOD(i, 4) + 1);

            SET i = i + 1;
        END WHILE;
END$$
DELIMITER $$

CALL insertMemberBoards();

## 자유게시판 관리자 게시물 insert
DELIMITER $$
DROP PROCEDURE IF EXISTS insertAdminBoards $$

CREATE PROCEDURE insertAdminBoards()
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i <= 3 DO
            INSERT INTO tbl_board(category_id, title, content, admin_id)
            VALUES(MOD(i, 3) + 1, concat('제목제목', i), concat('내용내용', i), MOD(i, 2) + 1);

            SET i = i + 1;
        END WHILE;
END$$
DELIMITER $$

CALL insertAdminBoards();