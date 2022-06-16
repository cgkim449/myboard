import {attachesInstance} from "@/api/index";

/**
 * 첨부파일 다운로드 API
 */
const downloadAttach = (attachId) => {
    return attachesInstance.get(
        `/${attachId}`,
        {responseType: "blob"}
    );
};

export {
    downloadAttach,
}