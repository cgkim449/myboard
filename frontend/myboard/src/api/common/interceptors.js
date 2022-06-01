import store from "@/store";

export function setInterceptors(axiosInstance) {
    axiosInstance.interceptors.request.use(
        function (config) {
            if(store.state.token !== "") {
                config.headers.Authorization = "Bearer " + store.state.token;
            }
            console.log(config.headers.Authorization);

            return config;
        },
        function (error) {

        },
    );

    axiosInstance.interceptors.response.use(
        function (response) {
            return response;
        },
        function (error) {

        },
    );
    return axiosInstance;
}

