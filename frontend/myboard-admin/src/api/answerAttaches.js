import {answerAttachesInstance} from "@/api/index";

/**
 * 첨부파일 다운로드
 */
const downloadAnswerAttach = (attachId) => {
    return answerAttachesInstance.get(
        `/${attachId}`,
        {responseType: "blob"}
    );
};

export {
    downloadAnswerAttach,
}