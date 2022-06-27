import axios from "axios";
import {setInterceptors} from "@/api/common/interceptors";

function createInstanceWith(url) {
    return setInterceptors(axios.create({baseURL: `${process.env.VUE_APP_API_URL}${url}`,}));
}

export const instance = createInstanceWith("");

export const boardsInstance = createInstanceWith("boards");
export const boardAttachesInstance = createInstanceWith("board-attaches");
export const commentsInstance = createInstanceWith("comments");

export const questionsInstance = createInstanceWith("questions");
export const questionAttachesInstance = createInstanceWith("question-attaches");
export const answersInstance = createInstanceWith("answers");
export const answerAttachesInstance = createInstanceWith("answer-attaches");

export const faqsInstance = createInstanceWith("faqs");

export const noticesInstance = createInstanceWith("notices");
export const noticeAttachesInstance = createInstanceWith("notice-attaches");