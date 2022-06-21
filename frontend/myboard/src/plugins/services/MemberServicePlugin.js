import {memberLogin, createMember} from "@/api/auth";

export const memberServicePlugin = {
    signUp: (user) => {
        return createMember(user);
    },
    login: (user) => {
        return memberLogin(user);
    },
};

export default {
    install(Vue) {
        Vue.prototype.$_MemberService = memberServicePlugin;
    },
};
