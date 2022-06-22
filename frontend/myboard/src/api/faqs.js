import {faqsInstance} from "@/api/index";

const getFAQList = (categoryId) => {
    return faqsInstance.get("/", { params: {categoryId: categoryId}});
};


export {
    getFAQList,
}
