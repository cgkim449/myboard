function formatDate(value) {
    if (value === undefined) {
        return;
    }
    return value.substring(0, 16);
}

function formatBoardTitle(value) {
    if (value === undefined) {
        return;
    }
    return value.length > 40 ? value.substring(0, 40).concat("...") : value;
}

function formatQuestionNickname(value) {
    if (value === undefined) {
        return;
    }
    return value.substring(0, 1).concat("*").concat(value.substring(value.length - 1));
}

export {formatDate, formatBoardTitle, formatQuestionNickname}