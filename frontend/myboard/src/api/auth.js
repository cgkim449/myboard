import {instanceWithoutToken} from "@/api/index";

/**
 * 회원가입 API
 *
 * @param user
 * @returns {*}
 */
const createUser = (user) => {
    return instanceWithoutToken.post("users", user);
}

/**
 * 로그인 API
 *
 * @param user
 * @returns {*}
 */
const loginUser = (user) => {
    return instanceWithoutToken.post("users/login", user);
}

export {
    createUser,
    loginUser
}