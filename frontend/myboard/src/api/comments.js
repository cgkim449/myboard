import {commentsInstance} from "@/api/index";

/**
 * 댓글 작성 API
 *
 * @param comment
 * @returns {*}
 */
const createComment = (comment) => {
    return commentsInstance.post(
        "/",
        comment);
}

/**
 * 댓글 목록 조회 API
 *
 * @param boardId
 * @returns {*}
 */
const getCommentList = (boardId) => {
    return commentsInstance.get(
        "/", { params: {boardId: boardId} });
}

export {
    createComment,
    getCommentList
}