import {questionsInstance} from "@/api/index";

const getQuestionList = (searchCondition) => {
    return questionsInstance.get("/", { params: searchCondition});
};

const getQuestionDetail = (id) => {
    return questionsInstance.get(
        `/${id}`
    );
};

const createQuestion = (formData) => {
    return questionsInstance.post(
        "/",
        formData,
        {headers:{"Content-Type" : "multipart/form-data"}}
    )
}

// /**
//  * 게시글 삭제 API
//  */
// const deleteBoard = (request) => {
//     console.log(request.guestPassword)
//     return boardsInstance.delete(
//         `/${request.boardId}`,
//         {data: {guestPassword: request.guestPassword}});
// };
//
// /**
//  * 게시글 수정 API
//  */
// const patchBoard = (formData) => {
//     return boardsInstance.patch(
//         `/${formData.get('boardId')}`,
//         formData,
//         {headers: {"Content-Type" : "multipart/form-data"},
//         });
// };
//
// /**
//  * 게시글 익명 작성자 비밀번호 확인 API
//  */
// const checkBoardPw = (board) => {
//     console.log(board.guestPassword)
//     //TODO: json stringify 로 바꾸기?
//     return boardsInstance.post(
//             `/${board.boardId}/pwCheck`,
//         {
//             'guestPassword': board.guestPassword
//         },
//     );
// }

export {
    getQuestionList,
    createQuestion,
    getQuestionDetail
}
