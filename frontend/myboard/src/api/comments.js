import {commentsInstance} from "@/api/index";

/**
 * 댓글 작성 API
 */
const createComment = (comment) => {
    let form = new FormData();
    form.append('boardId', comment.boardId);
    form.append('commentContent', comment.commentContent);
    form.append('guestPassword', comment.guestPassword);
    form.append('guestNickname', comment.guestNickname);
    return commentsInstance.post(
        "/",
        form);
}

/**
 * 댓글 목록 조회 API
 */
const getCommentList = (boardId) => {
    return commentsInstance.get(
        "/", { params: {boardId: boardId} });
}

export {
    createComment,
    getCommentList
}