import router from "@/router";

export const routerPushPlugin = {
    /**
     * 게시물 상세조회로 이동
     *
     * @param boardId
     * @param query
     * @returns {Promise<void>}
     */
    async goToBoardDetail(boardId, query) {

        await router.push({
            name: "BoardDetailView",
            params: {boardId},
            query: query
        });
    },

    /**
     * 게시물 작성 페이지로 이동
     *
     * @param query
     * @returns {Promise<void>}
     */
    async goToBoardWrite(query) {

        await router.push({
            name: "BoardWriteView",
            query: query,
        });
    },

    /**
     * 게시물 목록 페이지로 이동
     *
     * @returns {Promise<void>}
     */
    async goToBoardList(query) {
        await router.push({
            name: "BoardListView",
            query: query
        });
    },

    /**
     * 게시물 수정 페이지로 이동
     */
    async goToBoardModify(boardId, query) {
        await router.push({
            name: "BoardModifyView",
            params: {
                boardId: boardId,
            },
            query: query,
        });
    },
};

export default {
    install(Vue) {
        Vue.prototype.$_routerPush = routerPushPlugin;
    },
};
