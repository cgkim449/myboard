import {questionsInstance} from "@/api/index";

/**
 * 질문 목록 조회
 */
const getQuestionList = (searchCondition) => {
    return questionsInstance.get("/", { params: searchCondition});
};

/**
 * 질문 상세 조회
 */
const getQuestionDetail = (id) => {
    return questionsInstance.get(
        `/${id}`
    );
};

/**
 * 질문 등록
 */
const createQuestion = (formData) => {
    return questionsInstance.post(
        "/",
        formData,
        {headers:{"Content-Type" : "multipart/form-data"}}
    )
}

/**
 * 질문 삭제
 */
const deleteQuestion = (questionId) => {
    return questionsInstance.delete(
        `/${questionId}`,);
};

/**
 * 질문 수정
 */
const patchQuestion = (formData) => {
    return questionsInstance.patch(
        `/${formData.get('questionId')}`,
        formData,
        {headers: {"Content-Type" : "multipart/form-data"},
        });
};

export {
    getQuestionList,
    createQuestion,
    getQuestionDetail,
    deleteQuestion,
    patchQuestion
}
