import {instanceWithoutToken} from "@/api/index";

/**
 * 관리자 로그인 API
 */
const adminLogin = (user) => {
    return instanceWithoutToken.post("admin/login", user);
}

export {
    adminLogin
}