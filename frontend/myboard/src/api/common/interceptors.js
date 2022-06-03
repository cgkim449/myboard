import store from "@/store";
import router from "@/router";
import {deleteCookie} from "@/utils/cookies";

export function setInterceptors(axiosInstance) {
    axiosInstance.interceptors.request.use(
        function (config) {
            if(store.state.token !== "") { //로그인시에만 헤더에 토큰 넣어 요청
                config.headers.Authorization = "Bearer " + store.state.token;
            }
            console.log(config.headers.Authorization);

            return config;
        },
        function (error) {
            return Promise.reject(error);
        },
    );

    axiosInstance.interceptors.response.use(
        function (response) {
            return response;
        },
        function (error) {
            if(error.response.data.errorCode === "A002") { //토큰 만료
                alert(error.response.data.errorMessage)
                store.commit("clearToken");
                store.commit("clearUsername");
                store.commit("clearNickname");
                deleteCookie("token");
                deleteCookie("username");
                deleteCookie("nickname");
                router.go();
            }
            return Promise.reject(error);
        },
    );
    return axiosInstance;
}

