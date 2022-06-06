import {boardsInstance} from "@/api/index";

/**
 * 게시글 목록 조회 API
 */
const getBoardList = (searchCondition) => {
    return boardsInstance.get("/", { params: searchCondition});
};

/**
 * 게시글 상세 조회 API
 */
const getBoardDetail = (boardId) => {
    return boardsInstance.get(
        `/${boardId}`
    );
};

/**
 * 회원 게시글 작성 API
 */
const createMemberBoard = (formData) => {
    return boardsInstance.post(
        "/member",
        formData,
        {headers:{"Content-Type" : "multipart/form-data"}}
    )
}

/**
 * 익명 게시글 작성 API
 */
const createGuestBoard = (formData) => {
    return boardsInstance.post(
        "/guest",
        formData,
        {headers:{"Content-Type" : "multipart/form-data"}}
    )
}

/**
 * 게시글 삭제 API
 */
const deleteBoard = (request) => {
    console.log(request.guestPassword)
    return boardsInstance.delete(
        `/${request.boardId}`,
        {data: {guestPassword: request.guestPassword}});
};

/**
 * 게시글 수정 API
 */
const patchBoard = (formData) => {
    return boardsInstance.patch(
        `/${formData.get('boardId')}`,
        formData,
        {headers: {"Content-Type" : "multipart/form-data"},
        });
};

/**
 * 게시글 익명 작성자 비밀번호 확인 API
 */
const checkBoardPw = (board) => {
    console.log(board.guestPassword)
    //TODO: json stringify 로 바꾸기?
    return boardsInstance.post(
            `/${board.boardId}/pwCheck`,
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
    createMemberBoard,
    createGuestBoard,
    patchBoard,
}
