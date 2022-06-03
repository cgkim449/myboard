import {instanceWithoutToken} from "@/api/index";

/**
 * 회원가입 API
 */
const createUser = (user) => {
    return instanceWithoutToken.post("users", user);
}

/**
 * 로그인 API
 */
const loginUser = (user) => {
    return instanceWithoutToken.post("users/login", user);
}

export {
    createUser,
    loginUser
}