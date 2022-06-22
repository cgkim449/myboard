import {instanceWithoutToken, adminInstance} from "@/api/index";

/**
 * 관리자 로그인
 */
const adminLogin = (user) => {
    return instanceWithoutToken.post("admin/login", user);
}

const adminCheck = () => {
    return adminInstance.get("/check");
}

export {
    adminLogin,
    adminCheck
}