import {saveUser} from "@/api";

export const userServicePlugin = {
    signUp: (user) => {
        return saveUser(user);
    },
};

export default {
    install(Vue) {
        Vue.prototype.$_UserService = userServicePlugin;
    },
};
