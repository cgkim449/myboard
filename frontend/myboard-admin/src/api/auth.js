import {instanceWithoutToken} from "@/api/index";

/**
 * 관리자 로그인 API
 */
const login = (user) => {
    return instanceWithoutToken.post("admin/login", user);
}

export {
    login
}