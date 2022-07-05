import {
    getNoticeDetail,
    getNoticeList,
    createNotice,
    deleteNotice,
    patchNotice,
    getLatestNoticeDetail
} from "@/api/notices";

import {
    downloadAttach
} from "@/api/noticeAttaches";

export const noticeServicePlugin = {
    fetchNoticeList: (searchCondition) => {
        return getNoticeList(searchCondition);
    },
    writeNotice(formData) {
        return createNotice(formData);
    },
    removeNotice(board) {
        return deleteNotice(board);
    },
    updateNotice(formData) {
        return patchNotice(formData);
    },
    fetchNoticeDetail: (noticeId) => {
        return getNoticeDetail(noticeId);
    },
    fetchLatestNoticeDetail() {
        return getLatestNoticeDetail();
    },
    async downloadAttach(attachId) {
        try {
            const response = await downloadAttach(attachId);

            //TODO: 이 방식 다시 생각해보기
            let fileURL = window.URL.createObjectURL(new Blob([response.data]));
            console.log(response);
            let filename = decodeURI(response.headers['content-disposition'].split("UTF-8''")[1])
            let fileLink = document.createElement('a');
            fileLink.href = fileURL;
            fileLink.setAttribute('download', filename);

            document.body.appendChild(fileLink);

            fileLink.click();
        } catch (error) {
            console.log(error);
        }
    },
};

export default {
    install(Vue) {
        Vue.prototype.$_noticeService = noticeServicePlugin;
    },
};
