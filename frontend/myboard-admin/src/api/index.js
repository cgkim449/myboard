import axios from "axios";
import {setInterceptors} from "@/api/common/interceptors";

function createInstanceWith(url) {
    return setInterceptors(axios.create({baseURL: `${process.env.VUE_APP_API_URL}${url}`,}));
}

export const instance = createInstanceWith("");

export const adminInstance = createInstanceWith("admin");

export const boardsInstance = createInstanceWith("admin/boards");
export const boardAttachesInstance = createInstanceWith("admin/board-attaches");
export const commentsInstance = createInstanceWith("admin/comments");

export const questionsInstance = createInstanceWith("admin/questions");
export const questionAttachesInstance = createInstanceWith("admin/question-attaches");
export const answersInstance = createInstanceWith("admin/answers");
export const answerAttachesInstance = createInstanceWith("admin/answer-attaches");

export const faqsInstance = createInstanceWith("admin/faqs");

export const noticesInstance = createInstanceWith("admin/notices");
export const noticeAttachesInstance = createInstanceWith("admin/notice-attaches");