import router from "@/router";
import Vue from "vue";
import store from "@/store";

export function setInterceptors(axiosInstance) {
    axiosInstance.interceptors.request.use(
        function (config) {
            //TODO: 다시
            const token = store.state.token;
            if(token !== null) { //로그인시에만 헤더에 토큰 넣어 요청
                config.headers.Authorization = "Bearer " + token;
            }
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
        async function (error) {
            if(error.response.data.errorCode === "A002") { //토큰 만료
                alert(error.response.data.errorMessage)
                await store.dispatch("MEMBER_LOGOUT");
                router.go();
            }
            return Promise.reject(error);
        },
    );
    return axiosInstance;
}

