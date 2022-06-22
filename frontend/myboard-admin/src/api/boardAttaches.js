import {boardAttachesInstance} from "@/api/index";

/**
 * 첨부파일 다운로드
 */
const downloadAttach = (attachId) => {
    return boardAttachesInstance.get(
        `/${attachId}`,
        {responseType: "blob"}
    );
};

export {
    downloadAttach,
}