import {questionAttachesInstance} from "@/api/index";

/**
 * 첨부파일 다운로드
 */
const downloadQuestionAttach = (attachId) => {
    return questionAttachesInstance.get(
        `/${attachId}`,
        {responseType: "blob"}
    );
};

export {
    downloadQuestionAttach,
}