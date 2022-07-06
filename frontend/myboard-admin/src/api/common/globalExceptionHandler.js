import {ERROR_CODE} from "@/api/common/errorCode";
import store from "@/store";
import router from "@/router";

export const globalExceptionHandler = {

    async handleException(error) {

        const errorCode = error.response.data.errorCode;
        const errorMessage = error.response.data.errorMessage;


        if(errorCode === ERROR_CODE.TOKEN_EXPIRED) {
            await handleTokenExpiredException(errorMessage);
        }

        async function handleTokenExpiredException(errorMessage) {

            alert(errorMessage)

            await store.dispatch("LOGOUT");

            router.go();
        }
    }
}