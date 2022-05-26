import {getBoardDetail, getBoardList, postComment, getCommentList} from "@/api";

export const boardServicePlugin = {
    fetchBoardList: (searchCondition) => {
        return getBoardList(searchCondition);
    },
    fetchBoard: (boardId) => {
        return getBoardDetail(boardId);
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
};

export default {
    install(Vue) {
        Vue.prototype.$_BoardService = boardServicePlugin;
    },
};
