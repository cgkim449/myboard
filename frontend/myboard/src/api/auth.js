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
const loginMember = (member) => {
    return instanceWithoutToken.post("members/login", member);
}

/**
 * 관리자 로그인 API
 */
const loginAdmin = (user) => {
    return instanceWithoutToken.post("admin/login", user);
}

export {
    createMember,
    loginMember,
    loginAdmin
}