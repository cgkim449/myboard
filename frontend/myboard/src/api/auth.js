import {instance} from "@/api/index";

/**
 * 회원가입 API
 */
const createMember = (member) => {
    return instance.post("signup", member);
}

/**
 * 회원 로그인 API
 */
const memberLogin = (member) => {
    return instance.post("login", member);
}

export {
    createMember,
    memberLogin,
}