//TODO: 정규식
function saveTokenToCookie(value) {
    document.cookie = `token=${value}`;
}

function saveUserNameToCookie(value) {
    document.cookie = `username=${value}`;
}

function getTokenFromCookie() {
    return document.cookie.replace(
        /(?:(?:^|.*;\s*)token\s*=\s*([^;]*).*$)|^.*$/,
        '$1',
    );
}

function getUserNameFromCookie() {
    return document.cookie.replace(
        /(?:(?:^|.*;\s*)username\s*=\s*([^;]*).*$)|^.*$/,
        '$1',
    );
}

function deleteCookie(value) {
    document.cookie = `${value}=; expires=Thu, 01 Jan 1970 00:00:01 GMT;`;
}

export {
    saveTokenToCookie,
    saveUserNameToCookie,
    getTokenFromCookie,
    getUserNameFromCookie,
    deleteCookie,
};