function setValueToCookie(key, value) {
    document.cookie = `${key}=${value}`;
}

function getValueFromCookie(key) {
    for (const cookie of document.cookie.split("; ")) {
        const arr = cookie.split("=");
        if(arr[0] === key) {
            return arr[1];
        }
    }
}

function deleteCookie(value) {
    document.cookie = `${value}=; max-age=0`;
}

export {
    getValueFromCookie,
    setValueToCookie,
    deleteCookie,
};