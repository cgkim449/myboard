import store from "@/store";

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
            //TODO: 로그아웃시 공통 코드. alert 띄운다음에 리프래시 해야할듯.
            alert(error.response.data.errorMessage)
            return Promise.reject(error);
        },
    );
    return axiosInstance;
}

