import {
    getFAQList
} from "@/api/faqs";

export const faqServicePlugin = {
    fetchFAQList: (categoryId) => {
        return getFAQList(categoryId);
    },
};

export default {
    install(Vue) {
        Vue.prototype.$_faqService = faqServicePlugin;
    },
};
