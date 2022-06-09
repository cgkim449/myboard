import router from "@/router";
import Vue from "vue";

export function setInterceptors(axiosInstance) {
    axiosInstance.interceptors.request.use(
        function (config) {
            //TODO: 다시
            const token = Vue.$cookies.get("token");
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
        function (error) {
            if(error.response.data.errorCode === "A002") { //토큰 만료
                alert(error.response.data.errorMessage)
                Vue.$cookies.remove("token");
                Vue.$cookies.remove("username");
                Vue.$cookies.remove("nickname");
                router.go();
            }
            return Promise.reject(error);
        },
    );
    return axiosInstance;
}

