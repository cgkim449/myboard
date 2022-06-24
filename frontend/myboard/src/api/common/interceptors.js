import router from "@/router";
import store from "@/store";

export function setInterceptors(axiosInstance) {
    axiosInstance.interceptors.request.use(
        function (config) {

            const token = store.state.token;

            if(token != null) {
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
            //TODO: enum
            if(error.response.data.errorCode === "A002") { //토큰 만료
                alert(error.response.data.errorMessage)
                await store.dispatch("LOGOUT");
                router.go();
            }

            return Promise.reject(error);
        },
    );
    return axiosInstance;
}

