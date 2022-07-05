import {
    downloadAnswerAttach
} from "@/api/answerAttaches";

export const answerServicePlugin = {
    async downloadAttach(attachId) {
        try {
            const response = await downloadAnswerAttach(attachId);

            //TODO: 다른 방법으로 바꾸기
            const fileURL = window.URL.createObjectURL(new Blob([response.data]));
            const filename = decodeURI(response.headers['content-disposition'].split("UTF-8''")[1])
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
        Vue.prototype.$_answerService = answerServicePlugin;
    },
};
