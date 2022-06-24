import axios from "axios";
import {setInterceptors} from "@/api/common/interceptors";

/**
 * 토큰을 전송하는 axios 인스턴스 생성
 */
function createInstanceWithToken(url) {
    return setInterceptors(axios.create({baseURL: `${process.env.VUE_APP_API_URL}${url}`,}));
}

/**
 * 토큰을 전송하지 않는 axios 인스턴스 생성
 */
//TODO: 할필요없음
function createInstanceWithoutToken() {
    return axios.create({baseURL: process.env.VUE_APP_API_URL,});
}

export const instanceWithoutToken = createInstanceWithoutToken()

export const adminInstance = createInstanceWithToken("admin");

export const boardsInstance = createInstanceWithToken("admin/boards");
export const boardAttachesInstance = createInstanceWithToken("admin/attaches");
export const commentsInstance = createInstanceWithToken("admin/comments");

export const questionsInstance = createInstanceWithToken("admin/questions");
export const questionAttachesInstance = createInstanceWithToken("admin/question-attaches");
export const answersInstance = createInstanceWithToken("admin/answers");
export const answerAttachesInstance = createInstanceWithToken("admin/answer-attaches");

export const faqsInstance = createInstanceWithToken("admin/faqs");

export const noticesInstance = createInstanceWithToken("admin/notices");
export const noticeAttachesInstance = createInstanceWithToken("admin/notice-attaches");