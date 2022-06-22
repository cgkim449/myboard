import {boardsInstance} from "@/api/index";

/**
 * 게시글 목록 조회
 */
const getBoardList = (searchCondition) => {
    return boardsInstance.get("/", { params: searchCondition});
};

/**
 * 게시글 상세 조회
 */
const getBoardDetail = (boardId) => {
    return boardsInstance.get(
        `/${boardId}`
    );
};

/**
 * 관리자 게시글 작성
 */
const createBoard = (formData) => {
    return boardsInstance.post(
        "/",
        formData,
        {headers:{"Content-Type" : "multipart/form-data"}}
    )
}

/**
 * 게시글 삭제
 */
const deleteBoard = (request) => {
    console.log(request)
    if(request.guestPassword === undefined) { //회원이 자기 글 삭제 요청
        return boardsInstance.delete(
            `/${request.boardId}`
        );
    }

    return boardsInstance.delete( //익명 글 삭제 요청
        `/${request.boardId}`,
        {data: {guestPassword: request.guestPassword}});
};

/**
 * 게시글 수정
 */
const patchBoard = (formData) => {
    return boardsInstance.patch(
        `/${formData.get('boardId')}`,
        formData,
        {tableHeaders: {"Content-Type" : "multipart/form-data"},
        });
};

/**
 * 게시글 익명 작성자 비밀번호 확인
 */
const checkBoardPw = (board) => {
    console.log(board.guestPassword)
    //TODO: json stringify 로 바꾸기?
    return boardsInstance.post(
            `/${board.boardId}/password-check`,
        {
            'guestPassword': board.guestPassword
        },
    );
}

export {
    getBoardList,
    checkBoardPw,
    deleteBoard,
    getBoardDetail,
    patchBoard,
    createBoard,
}
