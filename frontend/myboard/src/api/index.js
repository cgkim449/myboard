import axios from "axios";

const axiosInstance = axios.create({
    baseURL: process.env.VUE_APP_API_URL,
});

const getBoardList = (searchCondition) => {
    return axiosInstance.get("boards", { params: searchCondition });
};

const getBoardDetail = (boardId) => {
    return axiosInstance.get(
        `boards/${boardId}`
    );
};

const postComment = (comment) => {
    return axiosInstance.post(
        "comments",
        comment);
}

const getCommentList = (boardId) => {
    return axiosInstance.get(
        `comments`, { params: {boardId: boardId} });
}

export { getBoardList, getBoardDetail, postComment, getCommentList };
