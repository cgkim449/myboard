import {patchQuestion, createQuestion, deleteQuestion, getQuestionDetail, getQuestionList} from "@/api/questions";
import {downloadQuestionAttach} from "@/api/questionAttaches";

export const questionServicePlugin = {
    fetchQuestionList: (searchCondition) => {
        return getQuestionList(searchCondition);
    },
    fetchQuestion: (id) => {
        return getQuestionDetail(id);
    },
    writeQuestion(formData) {
        return createQuestion(formData);
    },
    removeQuestion(questionId) {
        return deleteQuestion(questionId);
    },
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
    updateQuestion(formData) {
        return patchQuestion(formData);
    }
};

export default {
    install(Vue) {
        Vue.prototype.$_QuestionService = questionServicePlugin;
    },
};
