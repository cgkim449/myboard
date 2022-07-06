/**
 * 게시물 등록일 가공
 *
 * @param registerDate
 * @returns {string|*}
 */
function formatRegisterDate(registerDate) {
    if (!registerDate) {
        return;
    }
    return registerDate.substring(0, 16);
}

/**
 * 게시물 수정일 가공
 *
 * @param registerDate
 * @param updateDate
 * @returns {string|*}
 */
function formatUpdateDate(updateDate, registerDate) {
    if (!updateDate || !registerDate) {
        return;
    }
    return registerDate === updateDate ? '-' : formatRegisterDate(updateDate);
}

/**
 * 게시물 제목 가공
 *
 * @param boardTitle
 * @returns {string|*}
 */
function formatBoardTitle(boardTitle) {
    if (!boardTitle) {
        return;
    }
    return boardTitle.length > 40 ? boardTitle.substring(0, 40).concat("...") : boardTitle;
}

/**
 * 질문 작성자 별명 가공
 *
 * @param questionNickname
 * @returns {string|*}
 */
function formatQuestionNickname(questionNickname) {
    if (!questionNickname) {
        return;
    }
    return questionNickname.substring(0, 1).concat("*").concat(questionNickname.substring(questionNickname.length - 1));
}

function replaceCRLFWithBrTag(content) {
    if (!content) {
        return;
    }

    return content.replaceAll("\r\n", " <br/> ").replaceAll("\r", " <br/> ").replaceAll("\n", " <br/> ");
}

export {formatRegisterDate, formatUpdateDate, formatBoardTitle, formatQuestionNickname, replaceCRLFWithBrTag}