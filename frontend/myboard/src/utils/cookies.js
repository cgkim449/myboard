//TODO: 정규식
function saveTokenToCookie(value) {
    document.cookie = `token=${value}`;
}

function getTokenFromCookie() {
    return document.cookie.replace(
        /(?:(?:^|.*;\s*)token\s*=\s*([^;]*).*$)|^.*$/,
        '$1',
    );
}

function saveUsernameToCookie(value) {
    document.cookie = `username=${value}`;
}

function getUsernameFromCookie() {
    return document.cookie.replace(
        /(?:(?:^|.*;\s*)username\s*=\s*([^;]*).*$)|^.*$/,
        '$1',
    );
}

function saveNicknameToCookie(value) {
    document.cookie = `nickname=${value}`;
}

function getNicknameFromCookie() {
    return document.cookie.replace(
        /(?:(?:^|.*;\s*)nickname\s*=\s*([^;]*).*$)|^.*$/,
        '$1',
    );
}


function deleteCookie(value) {
    document.cookie = `${value}=; expires=Thu, 01 Jan 1970 00:00:01 GMT;`;
}

export {
    saveTokenToCookie,
    getTokenFromCookie,
    saveUsernameToCookie,
    getUsernameFromCookie,
    saveNicknameToCookie,
    getNicknameFromCookie,
    deleteCookie,
};