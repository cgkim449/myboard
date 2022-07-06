import {ERROR_CODE} from "@/api/common/errorCode";
import store from "@/store";
import router from "@/router";

export const globalExceptionHandler = {

    async handleException({ response }) {

        const errorCode = response.data.errorCode;
        const errorMessage = response.data.errorMessage;
        const fieldErrorDetails = response.data.fieldErrorDetails;


        if(errorCode === ERROR_CODE.TOKEN_EXPIRED) {
            await handleTokenExpiredException(errorMessage);
        }

        if(errorCode === ERROR_CODE.INVALID_INPUT_VALUE) {
            await handleInvalidInputValueException(fieldErrorDetails);
        }

        if(errorCode === ERROR_CODE.INTERNAL_SERVER_ERROR) {
            await handleInternalServerErrorException(errorMessage);
        }

        if(errorCode === ERROR_CODE.GUEST_PASSWORD_MISMATCH) {
            await handleGuestPasswordMismatchException(errorMessage);
        }


        async function handleGuestPasswordMismatchException(errorMessage) {
            alert(errorMessage);
        }

        async function handleTokenExpiredException(errorMessage) {
            alert(errorMessage)
            await store.dispatch("LOGOUT");
            router.go();
        }

        async function handleInvalidInputValueException(fieldErrorDetails) {
            const firstFieldErrorMessage = fieldErrorDetails[0].fieldErrorMessage;
            alert(firstFieldErrorMessage)
        }

        async function handleInternalServerErrorException(errorMessage) {
            alert(errorMessage)
        }
    }
}