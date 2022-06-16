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
 * 댓글 목록 조회 API
 */
const getCommentList = (boardId) => {
    return commentsInstance.get(
        "/", { params: {boardId: boardId} });
}

/**
 * 댓글 삭제 API
 */
const deleteComment = (commentId) => {
    return commentsInstance.delete(
        `/${commentId}`,
        );
};

export {
    createMemberComment,
    getCommentList,
    deleteComment
}