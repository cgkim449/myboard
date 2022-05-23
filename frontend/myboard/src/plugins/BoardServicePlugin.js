import {getBoardList} from "@/api";

export const boardServicePlugin = {
    fetchBoardList: () => {
        return getBoardList();
    },
}

export default {
    install(Vue) {
        Vue.prototype.$_BoardService = boardServicePlugin;
    },
}