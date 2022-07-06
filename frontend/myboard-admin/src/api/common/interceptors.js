import store from "@/store";
import {globalExceptionHandler} from "@/api/common/globalExceptionHandler";

export function setInterceptors(axiosInstance) {
    axiosInstance.interceptors.request.use(

        function (config) {
            const token = store.state.token;

            if(token !== null) {
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

            await globalExceptionHandler.handleException(error);

            return Promise.reject(error);
        },
    );
    return axiosInstance;
}

