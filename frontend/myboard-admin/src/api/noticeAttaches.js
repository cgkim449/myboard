import {noticeAttachesInstance} from "@/api/index";

/**
 * 첨부파일 다운로드
 */
const downloadAttach = (attachId) => {
    return noticeAttachesInstance.get(
        `/${attachId}`,
        {responseType: "blob"}
    );
};

export {
    downloadAttach,
}