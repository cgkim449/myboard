<template>
    <v-row justify="center">
        <v-col
            cols="12"
        >
            <div class="text-center">
                <v-pagination
                    color="secondary"
                    v-model="page"
                    @input="clickPageBtn"
                    :length="paginationLength"
                    :total-visible="10"
                ></v-pagination>
            </div>
        </v-col>
    </v-row>
</template>

<script>
export default {
    name: "Pagination",
    data() {
        return {
            page: 1,
        }
    },
    props: ["updatedPage", "itemTotalCount"],
    created() {
        this.page = this.updatedPage;
    },
    //TODO: computed 로 바꾸기
    watch: {
        updatedPage() {
            console.log("sindomeits")
            this.page = this.updatedPage;
        },
    },
    computed: {
        paginationLength() {
            return Math.ceil(this.itemTotalCount / 12);
        }
    },
    methods: {
        clickPageBtn() {
            this.$emit("pageBtnClick", this.page)
        }
    }
}
</script>

<style scoped>

</style>