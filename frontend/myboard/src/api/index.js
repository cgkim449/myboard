import axios from "axios";

const axiosInstance = axios.create({
    baseURL: process.env.VUE_APP_API_URL,
});

const saveUser = (user) => {
    return axiosInstance.post("users", user);
}

const login = (user) => {
    return axiosInstance.post("users/login", user);
}

const getBoardList = (searchCondition) => {
    return axiosInstance.get("boards", { params: searchCondition});
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

const checkBoardPw = (board) => {
    console.log(board.guestPassword)
    return axiosInstance.post(
        `boards/${board.boardId}/pwCheck`,
        board);
}

const deleteBoard = (board) => {
    return axiosInstance.delete(
        `boards/${board.boardId}`,
        {data: board});
};

const patchBoard = (formData) => {
    return axiosInstance.patch(
        `boards/${formData.get('boardId')}`,
        formData,
        {headers: {"Content-Type" : "multipart/form-data"}
        });
};

export {
    getBoardList,
    getBoardDetail,
    postComment,
    getCommentList,
    saveBoard,
    downloadAttach,
    checkBoardPw,
    deleteBoard,
    patchBoard,
    saveUser,
    login
};
