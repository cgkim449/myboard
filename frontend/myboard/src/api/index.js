import axios from "axios";

const axiosInstance = axios.create({
    baseURL: process.env.VUE_APP_API_URL,
});

const getBoardList = (searchCondition) => {
    return axiosInstance.get("boards", { params: searchCondition });
};

export { getBoardList };
