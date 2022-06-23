import {getFAQList, createFAQ} from "@/api/faqs";

export const faqServicePlugin = {
    fetchFAQList: (categoryId) => {
        return getFAQList(categoryId);
    },
    writeFAQ(formData) {
        return createFAQ(formData);
    },
};

export default {
    install(Vue) {
        Vue.prototype.$_FAQService = faqServicePlugin;
    },
};
