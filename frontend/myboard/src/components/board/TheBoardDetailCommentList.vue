<template>
    <div>
        <v-card-title class="text-subtitle-1 grey--text">
            댓글 {{ commentTotalCount }}개
        </v-card-title>

        <v-card-text v-for="comment in fetchedCommentList">
            <v-row dense>
                <v-col
                    cols="2"
                >
                    <template v-if="comment.guestNickname != null">
                        {{ comment.guestNickname }}
                    </template>

                    <template v-else-if="comment.memberNickname != null">
                        {{ comment.memberNickname }}
                    </template>

                    <template v-else-if="comment.adminNickname != null">
                        {{ comment.adminNickname }}
                    </template>
                </v-col>
                <v-col>
                    {{ comment.content }}
                </v-col>

                <v-col
                    cols="auto"
                >
                    <!--익명 댓글 삭제는 모달 띄움-->
                    <template v-if="comment.guestNickname !== null">
                        <v-btn
                            icon
                            x-small
                            color="red lighten-2"
                            class="mb-1"
                            von="on"

                            @click.stop="showDeleteGuestCommentDialog = true"
                            @click="clickDeleteGuestCommentDialog(comment.commentId)"
                        >
                            x
                        </v-btn>
                    </template>

                    <!-- 로그인사용자 '내댓글 삭제'는 모달 안띄움-->
                    <template
                        v-else-if="comment.memberNickname !== null && $store.state.nickname === comment.memberNickname">
                        <v-btn
                            icon
                            x-small
                            color="red lighten-2"
                            class="mb-1"
                            @click="clickDeleteCommentBtn(comment.commentId)"
                        >
                            x
                        </v-btn>
                    </template>

                    <!--TODO: 관리자 댓글은 삭제 버튼 안보임-->
                </v-col>

                <v-col
                    cols="auto"
                >
                    {{ comment.registerDate }}
                </v-col>
            </v-row>

            <v-divider></v-divider>
        </v-card-text>

        <!--익명댓글 삭제 모달-->
        <v-dialog
            v-model="showDeleteGuestCommentDialog"
            max-width="290"
        >
            <v-card>
                <v-card-title>
                    <span class="text-h5">비밀번호 확인</span>
                </v-card-title>

                <v-card-text>
                    <v-container>
                        <v-row>
                            <v-col
                                cols="12"
                            >
                                <v-text-field
                                    clearable
                                    type="password"
                                    prepend-icon="mdi-lock-outline"
                                    v-model="deleteCommentRequest.guestPassword"
                                    label="비밀번호를 입력해주세요."
                                    v-on:keyup.enter="clickDeleteCommentBtn(deleteCommentRequest.commentId)"
                                ></v-text-field>
                            </v-col>

                        </v-row>
                    </v-container>
                </v-card-text>

                <v-card-actions>
                    <v-spacer></v-spacer>

                    <v-btn
                        color="blue darken-1"
                        text
                        @click="showDeleteGuestCommentDialog = false"
                    >
                        취소
                    </v-btn>

                    <v-btn
                        color="blue darken-1"
                        text
                        @click="clickDeleteCommentBtn(deleteCommentRequest.commentId)"
                    >
                        확인
                    </v-btn>
                </v-card-actions>

            </v-card>
        </v-dialog>
    </div>
</template>

<script>
export default {
    name: "TheBoardDetailCommentList",
    props: {
        fetchedCommentList: {
            type: Array,
        },

        commentDeleteResponseStatus: {
            type: Number,
        },
    },
    data() {
        return {
            showDeleteGuestCommentDialog: false,
            deleteCommentRequest: {
                guestPassword: "",
                commentId: 0,
            },
        }
    },
    computed: {
        commentTotalCount() {
            if (this.fetchedCommentList === undefined) {
                return
            }
            return this.fetchedCommentList.length
        },
    },
    watch: {
        showDeleteGuestCommentDialog() {
            if (!this.showDeleteGuestCommentDialog) {
                this.initDeleteCommentRequest();
            }
        },
        commentDeleteResponseStatus() {
            if (this.commentDeleteResponseStatus === 204) {
                this.showDeleteGuestCommentDialog = false
                this.deleteCommentRequest = {};
                this.$emit("initCommentDeleteResponseStatus")
            }
        },
    },
    methods: {
        initDeleteCommentRequest(dialog) {
            if (!dialog) {
                this.deleteCommentRequest = {};
            }
        },
        async clickDeleteGuestCommentDialog(commentId) {
            console.log(commentId)
            this.deleteCommentRequest.commentId = commentId;
        },

        clickDeleteCommentBtn(commentId) {
            this.deleteCommentRequest.commentId = commentId;

            this.$emit("deleteCommentBtnClick", this.deleteCommentRequest);


        },
    },
}
</script>

<style scoped>

</style>