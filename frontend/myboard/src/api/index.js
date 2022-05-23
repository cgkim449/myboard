import axios from "axios";

const axiosInstance = axios.create({
    baseURL: process.env.VUE_APP_API_URL
});

const getBoardList = () => {
    return axiosInstance.get("boards");
};

export {
    getBoardList,
}