function formatDate(value) {
    return value.substring(0, 16);
}

function formatBoardTitle(value) {
    if(value === undefined) {
        return;
    }
    return value.length > 80 ? value.substring(0, 40).concat("..."): value;
}

export {formatDate, formatBoardTitle}