export const commonFormValidatorPlugin = {
//TODO: 메시지 분리
    validateMultipartFile(multipartFile) {
        let regex = new RegExp("(.*?)\.(jsp|jspx|jsw|jsv|jspf|htm|html)$");

        let maxSize = 10 * 1024 * 1024; // 10MB


        let fileSize = multipartFile.size;
        let fileName = multipartFile.name;

        if(fileSize >= maxSize) {
            return "10MB이상의 파일은 업로드할 수 없습니다.";
        }
        if(regex.test(fileName)) {
            return "해당 확장자의 파일은 업로드할 수 없습니다.";
        }

        return true;
    },

    validateMultipartFiles(multipartFiles) {
        if(multipartFiles.length === 0) {
            return true;
        }

        if(multipartFiles.length > 3) {

        return '첨부파일은 3개까지 업로드 가능합니다.';
        } else {
            const maxTotalSize = 30 * 1024 * 1024; // 30MB
            let totalSize = 0;

            for (const multipartFile of multipartFiles) {
                totalSize += multipartFile.size;

                if(!this.validateMultipartFile(multipartFile)) {
                    return false;
                }
            }

            if(totalSize >= maxTotalSize) {
                return "첨부파일은 총 30MB이상 업로드할 수 없습니다.";
            }

            return true;
        }
    },
    validateCategoryId(categoryId) {
        if(categoryId === null || categoryId === undefined || categoryId === "") {
            return "필수 항목입니다."
        }

        return true;
    },
    validateContent(content) {
        if(content === null || content === undefined || content === "") {
            return "필수 항목입니다."
        }

        if(!(4 <= content.length && content.length < 2000)) {
            return "내용은 4글자 이상, 2000글자 미만입니다.";
        }

        return true;
    },
    validateTitle(title) {
        if(title === null || title === undefined || title === "") {
            return "필수 항목입니다."
        }

        if(!(4 <= title.length && title.length < 20)) {
            return "제목은 4글자 이상, 20글자 미만입니다."
        }

        return true;
    },
    validateGuestNickname(guestNickname) {
        if(guestNickname === null || guestNickname === undefined || guestNickname === "") {
            return "필수 항목입니다."
        }

        if(!(3 <= guestNickname.length && guestNickname.length < 5) ) {
            return "3글자 이상, 5글자 미만입니다."
        }

        return true;
    },
    validateGuestPasswordConfirm(guestPasswordConfirm, guestPassword) {
        if(guestPasswordConfirm === null || guestPasswordConfirm === undefined || guestPasswordConfirm === "") {
            return "필수 항목입니다.";
        }

        if(guestPasswordConfirm !== guestPassword) {
            return "비밀번호가 일치하지 않습니다.";
        }

        return true;
    },
    validateGuestPassword(guestPassword) {
        if(guestPassword === null || guestPassword === undefined || guestPassword === "") {
            return "필수 항목입니다.";
        }

        if(!(4 <= guestPassword.length && guestPassword.length < 16)) {
            return "영문/숫자/특수문자 포함 4글자 이상, 16글자 미만입니다.";
        }

        let alphabet = 0;
        let number = 0;
        let specialSymbol = 0;

        for (let i = 0; i < guestPassword.length; i++) {
            let ascii = guestPassword.charCodeAt(i);

            if (!('!'.charCodeAt(0) <= ascii && ascii <= '~'.charCodeAt(0))) {
                return "영문/숫자/특수문자 포함 4글자 이상, 16글자 미만입니다.";
            } else if (('A'.charCodeAt(0) <= ascii && ascii <= 'Z'.charCodeAt(0)) || ('a'.charCodeAt(0) <= ascii && ascii <= 'z'.charCodeAt(0))) {
                alphabet++;
            } else if ('0'.charCodeAt(0) <= ascii && ascii <= '9'.charCodeAt(0)) {
                number++;
            } else {
                specialSymbol++;
            }
        }

        if(!(alphabet !== 0 && number !== 0 && specialSymbol !== 0)) {
            return "영문/숫자/특수문자 포함 4글자 이상, 16글자 미만입니다."
        }

        return true;
    }
};

export default {
    install(Vue) {
        Vue.prototype.$_commonFormValidator = commonFormValidatorPlugin;
    },
};
