import {

    deleteBoard,
    getBoardDetail,
    getBoardList,
    patchBoard,
    createBoard
} from "@/api/boards";
import {getCommentList, deleteComment, createComment} from "@/api/comments";
import {downloadAttach} from "@/api/boardAttaches";

export const boardServicePlugin = {
    fetchBoardList: (searchCondition) => {
        return getBoardList(searchCondition);
    },
    fetchBoard: (boardId) => {
        return getBoardDetail(boardId);
    },
    writeBoard(formData) {
        return createBoard(formData);
    },
    removeBoard(board) {
        return deleteBoard(board);
    },
    updateBoard(formData) {
        return patchBoard(formData);
    },
    writeComment(formData) {
        return createComment(formData);
    },
    fetchCommentList(boardId) {
        try {
            return getCommentList(boardId);
        } catch (error) {
            console.log(error.response.data);
        }
    },
    deleteComment(comment) {
        return deleteComment(comment);
    },
    async downloadAttach(attachId) {
        try {
            const response = await downloadAttach(attachId);

            //TODO: 이 방식 다시 생각해보기
            let fileURL = window.URL.createObjectURL(new Blob([response.data]));
            console.log(response);
            let filename = decodeURI(response.headers['content-disposition'].split("UTF-8''")[1])
            let fileLink = document.createElement('a');
            fileLink.href = fileURL;
            fileLink.setAttribute('download', filename);

            document.body.appendChild(fileLink);

            fileLink.click();
        } catch (error) {
            console.log(error);
        }
    },

};

export default {
    install(Vue) {
        Vue.prototype.$_BoardService = boardServicePlugin;
    },
};
