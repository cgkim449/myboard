import {noticesInstance} from "@/api/index";

/**
 * 가장 최근 공지 조회
 */
const getLatestNoticeDetail = () => {
    return noticesInstance.get(
        `/latest`
    );
};

/**
 * 목록 조회
 */
const getNoticeList = (searchCondition) => {
    return noticesInstance.get("/", { params: searchCondition});
};

/**
 * 상세 조회
 */
const getNoticeDetail = (noticeId) => {
    return noticesInstance.get(
        `/${noticeId}`
    );
};

/**
 * 작성
 */
const createNotice = (formData) => {
    return noticesInstance.post(
        "/",
        formData,
        {headers:{"Content-Type" : "multipart/form-data"}}
    )
}

/**
 * 삭제
 */
const deleteNotice = (noticeId) => {
    return noticesInstance.delete( //익명 글 삭제 요청
        `/${noticeId}`,);
};

/**
 * 수정
 */
const patchNotice = (formData) => {
    return noticesInstance.patch(
        `/${formData.get('noticeId')}`,
        formData,
        {tableHeaders: {"Content-Type" : "multipart/form-data"},
        });
};





export {
    getLatestNoticeDetail,
    getNoticeList,
    getNoticeDetail,
    createNotice,
    deleteNotice,
    patchNotice,
}
