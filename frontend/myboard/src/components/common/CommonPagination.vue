<template>
    <v-row justify="center">
        <v-col
            cols="12"
        >
            <div class="text-center">
                <v-pagination
                    v-model="page"
                    color="secondary"
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
    name: "CommonPagination",
    data() {
        return {
            page: 1,
        }
    },
    props: {
        updatedPage: {
            type: Number,
        },

        itemTotalCount: {
            type: Number,
        },
    },
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