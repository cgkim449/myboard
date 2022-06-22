import {commentsInstance} from "@/api/index";

/**
 * 댓글 작성
 */
const createComment = (comment) => {
    let form = new FormData();
    form.append('boardId', comment.boardId);
    form.append('content', comment.content);

    return commentsInstance.post(
        "/",
        form,
        {headers:{"Content-Type" : "multipart/form-data"}}
    )
}

/**
 * 댓글 목록 조회
 */
const getCommentList = (boardId) => {
    return commentsInstance.get("/", { params: {boardId: boardId} });
}

/**
 * 댓글 삭제
 */
const deleteComment = (request) => {
    return commentsInstance.delete(`/${request.commentId}`,);
};

export {
    createComment,
    getCommentList,
    deleteComment
}