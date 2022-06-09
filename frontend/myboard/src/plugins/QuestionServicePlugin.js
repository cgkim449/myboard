import {createQuestion, getQuestionDetail, getQuestionList} from "@/api/questions";
import {downloadQuestionAttach} from "@/api/questionAttaches";

export const questionServicePlugin = {
    fetchItemList: (searchCondition) => {
        return getQuestionList(searchCondition);
    },
    fetchItem: (id) => {
        return getQuestionDetail(id);
    },
    writeQuestion(formData) {
        return createQuestion(formData);
    },
    // removeBoard(board) {
    //     return deleteBoard(board);
    // },
    // updateBoard(formData) {
    //     return patchBoard(formData);
    // },
    // checkBoardPw(board) {
    //     return checkBoardPw(board);
    // },
    // writeComment(comment) {
    //     return createComment(comment);
    // },
    // fetchCommentList(boardNo) {
    //     try {
    //         return getCommentList(boardNo);
    //     } catch (error) {
    //         console.log(error.response.data);
    //     }
    // },
    // removeComment(comment) {
    //     return deleteComment(comment);
    // },
    async downloadAttach(attachId) {
        try {
            const response = await downloadQuestionAttach(attachId);

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
        Vue.prototype.$_QuestionService = questionServicePlugin;
    },
};