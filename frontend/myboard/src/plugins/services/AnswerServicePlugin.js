import {createAnswer, getAnswerDetail} from "@/api/answers";

export const answerServicePlugin = {
    fetchAnswer: (id) => {
        return getAnswerDetail(id);
    },
    writeAnswer(formData) {
        return createAnswer(formData);
    },
};

export default {
    install(Vue) {
        Vue.prototype.$_AnswerService = answerServicePlugin;
    },
};
