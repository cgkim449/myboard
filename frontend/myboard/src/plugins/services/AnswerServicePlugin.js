import {getAnswerDetail} from "@/api/answers";
import {downloadAnswerAttach} from "@/api/answerAttaches";

export const answerServicePlugin = {
    fetchAnswer: (id) => {
        return getAnswerDetail(id);
    },

    async downloadAttach(attachId) {
        try {
            const response = await downloadAnswerAttach(attachId);

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
        Vue.prototype.$_AnswerService = answerServicePlugin;
    },
};
