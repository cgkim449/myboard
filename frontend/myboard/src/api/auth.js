import {instanceWithoutToken} from "@/api/index";

/**
 * 회원가입 API
 */
const createMember = (member) => {
    return instanceWithoutToken.post("members", member);
}

/**
 * 회원 로그인 API
 */
const memberLogin = (member) => {
    return instanceWithoutToken.post("members/login", member);
}

/**
 * 관리자 로그인 API
 */
const adminLogin = (user) => {
    return instanceWithoutToken.post("admin/login", user);
}

export {
    createMember,
    memberLogin,
    adminLogin
}