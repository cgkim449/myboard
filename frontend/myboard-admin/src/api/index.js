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

export const boardsInstance = createInstanceWithToken("boards");
export const boardAttachesInstance = createInstanceWithToken("attaches");
export const commentsInstance = createInstanceWithToken("comments");

export const questionsInstance = createInstanceWithToken("questions");
export const questionAttachesInstance = createInstanceWithToken("question-attaches");
export const answersInstance = createInstanceWithToken("answers");

export const faqsInstance = createInstanceWithToken("faqs");

export const noticesInstance = createInstanceWithToken("notices");