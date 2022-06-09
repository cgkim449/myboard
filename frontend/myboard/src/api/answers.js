import {answersInstance} from "@/api/index";

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

export {
    getAnswerDetail,
    createAnswer
}
