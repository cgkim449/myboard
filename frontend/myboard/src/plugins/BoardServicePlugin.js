import {getBoardDetail, getBoardList, postComment, getCommentList, saveBoard, downloadAttach} from "@/api";

export const boardServicePlugin = {
    fetchBoardList: (searchCondition) => {
        return getBoardList(searchCondition);
    },
    fetchBoard: (boardId) => {
        return getBoardDetail(boardId);
    },
    writeBoard(formData) {
        return saveBoard(formData);
    },
    writeComment(comment) {
        return postComment(comment);
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
};

export default {
    install(Vue) {
        Vue.prototype.$_BoardService = boardServicePlugin;
    },
};
