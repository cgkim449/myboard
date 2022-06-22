import {answersInstance, boardsInstance} from "@/api/index";

const getAnswerDetail = (id) => {
    return answersInstance.get(
        `/${id}`
    );
};

const createAnswer = (formData) => {
    return answersInstance.post(
        "/",
        formData,
        {headers:{"Content-Type" : "multipart/form-data"}}
    )
}

const deleteAnswer = (answerId) => {
    return answersInstance.delete(`/${answerId}`,);
};

const patchAnswer = (formData) => {
    return answersInstance.patch(
        `/${formData.get('answerId')}`,
        formData,
        {tableHeaders: {"Content-Type" : "multipart/form-data"},
        });
};

export {
    getAnswerDetail,
    createAnswer,
    deleteAnswer,
    patchAnswer
}
