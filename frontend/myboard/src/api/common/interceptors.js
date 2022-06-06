import router from "@/router";
import {cookiePlugin} from "@/plugins/CookiePlugin";

export function setInterceptors(axiosInstance) {
    axiosInstance.interceptors.request.use(
        function (config) {
            const token = cookiePlugin.getValueFromCookie("token");
            if(token !== "") { //로그인시에만 헤더에 토큰 넣어 요청
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
                cookiePlugin.deleteCookie("token");
                cookiePlugin.deleteCookie("username");
                cookiePlugin.deleteCookie("nickname");
                router.go();
            }
            return Promise.reject(error);
        },
    );
    return axiosInstance;
}

