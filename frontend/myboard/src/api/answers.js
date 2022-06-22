import {answersInstance} from "@/api/index";

const getAnswerDetail = (id) => {
    return answersInstance.get(
        `/${id}`
    );
};


export {
    getAnswerDetail
}
