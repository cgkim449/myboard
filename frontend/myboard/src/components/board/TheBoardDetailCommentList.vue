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
                    <span v-if="comment.guestNickname">{{ comment.guestNickname }}</span>
                    <span v-else-if="comment.memberNickname">{{ comment.memberNickname }}</span>
                    <span v-else-if="comment.adminNickname"><{{ comment.adminNickname }}</span>
                </v-col>

                <v-col>
                    <div v-html="$options.filters.replaceCRLFWithBrTag(comment.content)"/>
                </v-col>

                <v-col
                    cols="auto"
                >
                    <!--익명 댓글 삭제는 모달 띄움-->
                    <template v-if="comment.guestNickname">
                        <v-btn
                            icon
                            x-small
                            color="red lighten-2"
                            class="mb-1"
                            von="on"

                            @click.stop="showDeleteGuestCommentDialog = true"
                            @click="clickDeleteGuestCommentBtn(comment.commentId)"
                        >
                            x
                        </v-btn>
                    </template>

                    <!-- 로그인사용자 '내댓글 삭제'는 모달 안띄움-->
                    <template v-else-if="isMyMemberComment(comment)">
                        <v-btn
                            icon
                            x-small
                            color="red lighten-2"
                            class="mb-1"
                            @click="clickDeleteMyCommentBtn(comment.commentId)"
                        >
                            x
                        </v-btn>
                    </template>
                </v-col>

                <v-col
                    cols="auto"
                >
                    {{ comment.registerDate }}
                </v-col>
            </v-row>

            <v-divider/>
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
                                    v-on:keyup.enter="clickDeleteMyCommentBtn(deleteCommentRequest.commentId)"
                                ></v-text-field>
                            </v-col>
                        </v-row>
                    </v-container>
                </v-card-text>

                <v-card-actions>
                    <v-spacer/>

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
                        @click="clickDeleteMyCommentBtn(deleteCommentRequest.commentId)"
                    >
                        확인
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
</template>

<script>
/**
 * 자유게시판 상세 조회 댓글 목록
 */
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
        /**
         * 댓글 총 갯수
         *
         * @returns {number|*}
         */
        commentTotalCount() {
            if (!this.fetchedCommentList) {
                return;
            }
            return this.fetchedCommentList.length
        },
    },

    watch: {
        /**
         * 익명 댓글 삭제 모달이 닫히면 입력했었던 비밀번호값 초기화
         */
        showDeleteGuestCommentDialog() {
            if (!this.showDeleteGuestCommentDialog) {

                this.deleteCommentRequest = {};
            }
        },

        /**
         * 익명 댓글 삭제 성공시 모달 창 닫음
         */
        commentDeleteResponseStatus() {
            if (this.commentDeleteResponseStatus === 204) {

                this.showDeleteGuestCommentDialog = false

                this.deleteCommentRequest = {};

                this.$emit("initCommentDeleteResponseStatus")
            }
        },
    },

    methods: {
        /**
         * 익명 댓글 삭제 버튼 클릭
         *
         * @param commentId
         */
        clickDeleteGuestCommentBtn(commentId) {
            this.deleteCommentRequest.commentId = commentId;
        },

        /**
         * (로그인 사용자) 내 댓글 삭제 버튼 클릭
         *
         * @param commentId
         */
        clickDeleteMyCommentBtn(commentId) {
            this.deleteCommentRequest.commentId = commentId;

            this.$emit("deleteCommentBtnClick", this.deleteCommentRequest);
        },

        /**
         * 로그인 사용자 본인의 댓글인지 여부
         *
         * @param comment
         * @returns {*|boolean}
         */
        isMyMemberComment(comment) {
            return comment.memberNickname && this.$store.state.nickname === comment.memberNickname;
        }
    },
}
</script>
