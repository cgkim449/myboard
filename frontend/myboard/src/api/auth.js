import {instanceWithoutToken} from "@/api/index";

/**
 * 회원가입 API
 */
const createMember = (member) => {
    return instanceWithoutToken.post("members", member);
}

/**
 * 로그인 API
 */
const loginMember = (member) => {
    return instanceWithoutToken.post("members/login", member);
}

export {
    createMember,
    loginMember
}