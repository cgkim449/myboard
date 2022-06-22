import {getNoticeDetail} from "@/api/notices";

export const noticeServicePlugin = {
    fetchNoticeDetail: () => {
        return getNoticeDetail();
    },
};

export default {
    install(Vue) {
        Vue.prototype.$_NoticeService = noticeServicePlugin;
    },
};
