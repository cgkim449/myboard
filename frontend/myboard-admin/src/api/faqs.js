import {boardsInstance, faqsInstance} from "@/api/index";

const getFAQList = (categoryId) => {
    return faqsInstance.get("/", { params: {categoryId: categoryId}});
};

const createFAQ = (formData) => {
    return faqsInstance.post(
        "/",
        formData,
        {headers:{"Content-Type" : "multipart/form-data"}}
    )
}


export {
    getFAQList,
    createFAQ
}
