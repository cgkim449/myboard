<template>
    <v-container>
        <PageTitle>
            <h2 slot="title" @click="moveToNoticeList" v-bind:style="{ cursor: 'pointer' }">
                공지사항
            </h2>
        </PageTitle>

        <v-row justify="center">
            <v-col
                cols="12"
            >
                <ItemDetail
                    v-bind:fetchedItemDetail="noticeDetail"
                    v-bind:itemType="itemType"
                ></ItemDetail>

                <AttachList
                    v-if="noticeDetail.hasAttach"
                    v-bind:fetchedAttachList="noticeDetail.attachList"
                    v-bind:attachOf="attachOf"
                ></AttachList>

                <v-card elevation="0">
                    <v-card-text>
                        <v-row justify="center">
                            <v-col
                                cols="auto"
                            >
                                <v-btn
                                    @click="moveToNoticeList"
                                    color="secondary"
                                >
                                    목록
                                </v-btn>
                            </v-col>

                            <v-col
                                cols="auto"
                            >
                                <v-btn
                                    @click="moveToNoticeModify"
                                    outlined
                                    color="primary"
                                >
                                    수정
                                </v-btn>
                            </v-col>

                            <v-col
                                cols="auto"
                            >
                                <v-btn
                                    outlined
                                    color="primary"
                                    @click="deleteNotice"
                                >
                                    삭제
                                </v-btn>
                            </v-col>
                        </v-row>
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>

        <v-row>
            <v-col>

            </v-col>
        </v-row>

    </v-container>
</template>

<script>
import AttachList from "@/components/common/AttachList";
import ItemDetail from "@/components/common/ItemDetail";
import PageTitle from "@/components/common/PageTitle";

export default {
    name: "NoticeDetailView",
    components: {
        PageTitle,
        ItemDetail,
        AttachList,
    },
    data() {
        return {
            noticeDetail: {},
            attachOf: "notice",
            itemType: "notice",
        }
    },
    computed: {},
    async created() {

        let noticeId = this.$route.params.noticeId;

        await this.fetchNoticeDetail(noticeId);
    },
    methods: {
        isEmpty(obj) {
            return Object.keys(obj).length === 0 && obj.constructor === Object;
        },

        async fetchNoticeDetail(noticeId) {
            const {data} = await this.$_NoticeService.fetchNoticeDetail(noticeId);

            this.noticeDetail = data.noticeDetail;
        },

        async deleteNotice() {
            try {
                await this.$_NoticeService.removeNotice(this.noticeDetail.noticeId);
                alert('삭제되었습니다.');

                this.moveToNoticeList();
            } catch (error) {
                alert(error.response.data.errorMessage);
            }
        },

        moveToNoticeList() {
            this.$router.push({
                name: "NoticeListView",
                query: this.$route.query
            });
        },

        moveToNoticeModify() {
            this.$router.push({
                name: "NoticeModifyView",
                params: {
                    noticeId: this.noticeDetail.noticeId
                },
                query: this.$route.query,
            });
        },
    },
}
</script>

<style scoped>
.v-text-field >>> input {
    font-size: 0.875em;
}

.v-text-field >>> label {
    font-size: 0.875em;
}

.v-text-field >>> button {
    font-size: 0.875em;
}

.v-text-field--outlined >>> fieldset {
    border-color: rgba(209, 209, 209, 1);
}
</style>