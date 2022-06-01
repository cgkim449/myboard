import {

} from "@/api";
import {checkBoardPw, deleteBoard, getBoardDetail, getBoardList, patchBoard, createBoard} from "@/api/boards";
import {getCommentList, createComment} from "@/api/comments";
import {downloadAttach} from "@/api/attaches";

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
    writeComment(comment) {
        return createComment(comment);
    },
    fetchCommentList(boardNo) {
        try {
            return getCommentList(boardNo);
        } catch (error) {
            console.log(error.response.data);
        }
    },
    async downloadAttach(attachId) {
        try {
            const response = await downloadAttach(attachId);

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
    removeBoard(board) {
        return deleteBoard(board);
    },
    updateBoard(formData) {
        return patchBoard(formData);
    },
    checkBoardPw(board) {
        return checkBoardPw(board);
    }
};

export default {
    install(Vue) {
        Vue.prototype.$_BoardService = boardServicePlugin;
    },
};
