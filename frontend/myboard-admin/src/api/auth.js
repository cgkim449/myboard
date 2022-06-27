import {instance, adminInstance} from "@/api/index";

/**
 * 관리자 로그인
 */
const adminLogin = (user) => {
    return instance.post("admin/login", user);
}

const adminCheck = () => {
    return adminInstance.get("/auth");
}

export {
    adminLogin,
    adminCheck
}