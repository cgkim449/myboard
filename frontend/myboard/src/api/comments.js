import {commentsInstance} from "@/api/index";

/**
 * 회원 댓글 작성 API
 */
const createMemberComment = (comment) => {
    let form = new FormData();
    form.append('boardId', comment.boardId);
    form.append('content', comment.content);

    return commentsInstance.post(
        "/member",
        form,
        {headers:{"Content-Type" : "multipart/form-data"}}
    )
}

/**
 * 익명 댓글 작성 API
 */
const createGuestComment = (comment) => {
    let form = new FormData();
    form.append('boardId', comment.boardId);
    form.append('content', comment.content);
    form.append('guestPassword', comment.guestPassword);
    form.append('guestPasswordConfirm', comment.guestPassword);
    form.append('guestNickname', comment.guestNickname);
    return commentsInstance.post(
        "/guest",
        form,
        {headers:{"Content-Type" : "multipart/form-data"}}
    )
}

/**
 * 댓글 목록 조회 API
 */
const getCommentList = (boardId) => {
    return commentsInstance.get(
        "/", { params: {boardId: boardId} });
}

/**
 * 댓글 삭제 API
 */
const deleteComment = (request) => {
    console.log(request.guestPassword)
    return commentsInstance.delete(
        `/${request.commentId}`,
        {data: {guestPassword: request.guestPassword}});
};

export {
    createGuestComment,
    createMemberComment,
    getCommentList,
    deleteComment
}