import {loginMember, createMember} from "@/api/auth";

export const memberServicePlugin = {
    signUp: (user) => {
        return createMember(user);
    },
    login: (user) => {
        return loginMember(user);
    },
};

export default {
    install(Vue) {
        Vue.prototype.$_MemberService = memberServicePlugin;
    },
};
