import {loginUser, createUser} from "@/api/auth";

export const userServicePlugin = {
    signUp: (user) => {
        return createUser(user);
    },
    login: (user) => {
        return loginUser(user);
    },
};

export default {
    install(Vue) {
        Vue.prototype.$_UserService = userServicePlugin;
    },
};
