import { getBoardList } from "@/api";

export const boardServicePlugin = {
    fetchBoardList: (searchCondition) => {
        return getBoardList(searchCondition);
    },
};

export default {
    install(Vue) {
        Vue.prototype.$_BoardService = boardServicePlugin;
    },
};
