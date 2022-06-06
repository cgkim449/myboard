export const cookiePlugin = {
    setValueToCookie(key, value) {
        document.cookie = `${key}=${value}`;
    },
    getValueFromCookie(key) {
        for (const cookie of document.cookie.split("; ")) {
            const arr = cookie.split("=");
            if (arr[0] === key) {
                return arr[1];
            }
        }
        return "";
    },
    deleteCookie(value) {
        document.cookie = `${value}=; max-age=0`;
    },
}


export default {
    install(Vue) {
        Vue.prototype.$_Cookie = cookiePlugin;
    },
};
