import axios from "axios";

const axiosInstance = axios.create({
    baseURL: process.env.VUE_APP_API_URL,
});

const saveUser = (user) => {
    return axiosInstance.post("users", user);
}

const getBoardList = (searchCondition) => {
    return axiosInstance.get("boards", { params: searchCondition });
};

const getBoardDetail = (boardId) => {
    return axiosInstance.get(
        `boards/${boardId}`
    );
};

const saveBoard = (formData) => {

    return axiosInstance.post(
        "boards",
        formData,
        {headers:{"Content-Type" : "multipart/form-data"}}
    )
}


const postComment = (comment) => {
    return axiosInstance.post(
        "comments",
        comment);
}

const getCommentList = (boardId) => {
    return axiosInstance.get(
        `comments`, { params: {boardId: boardId} });
}

const downloadAttach = (attachId) => {
    return axiosInstance.get(
        `attaches/${attachId}`,
        {responseType: "blob"}
    );
};

export { getBoardList, getBoardDetail, postComment, saveUser, getCommentList, saveBoard, downloadAttach };
