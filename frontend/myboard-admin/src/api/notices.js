import {noticesInstance} from "@/api/index";

const getNoticeDetail = () => {
    return noticesInstance.get("/");
};

export {
    getNoticeDetail,
}
