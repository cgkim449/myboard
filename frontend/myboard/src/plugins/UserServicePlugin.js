import {saveUser, login} from "@/api";

export const userServicePlugin = {
    signUp: (user) => {
        return saveUser(user);
    },
    login: (user) => {
        return login(user);
    },
};

export default {
    install(Vue) {
        Vue.prototype.$_UserService = userServicePlugin;
    },
};
