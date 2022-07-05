<template>
    <!--  <v-container v-if="!isEmpty(boardDetail)">-->
    <v-container>
        <CommonPageTitle>
            <h2 slot="title" @click="moveToBoardList" v-bind:style="{ cursor: 'pointer' }">
                자유게시판
            </h2>
        </CommonPageTitle>

        <v-row justify="center">
            <v-col
                cols="12"
            >
                <CommonItemDetail
                    v-bind:fetchedItemDetail="boardDetail"
                ></CommonItemDetail>

                <CommonAttachList
                    v-if="boardDetail.hasAttach"
                    v-bind:fetchedAttachList="boardDetail.attachList"
                    v-bind:attachOf="attachOf"
                ></CommonAttachList>

                <v-card outlined class="px-1 pt-1 mt-3">
                    <TheBoardDetailCommentList
                        v-on:deleteCommentBtnClick="deleteComment"
                        v-on:initCommentDeleteResponseStatus="initCommentDeleteResponseStatus"
                        v-bind:commentDeleteResponseStatus="commentDeleteResponseStatus"
                        v-bind:fetchedCommentList="boardDetail.commentList"
                    ></TheBoardDetailCommentList>

                    <TheBoardDetailCommentWriteForm
                        v-on:saveCommentBtnClick="writeComment"
                        v-on:initCommentSaveResponseStatus="initCommentSaveResponseStatus"
                        v-bind:commentSaveResponseStatus="commentSaveResponseStatus"
                    ></TheBoardDetailCommentWriteForm>
                </v-card>

<!--TODO: 컴포넌트 분리-->
                <v-card elevation="0">
                    <v-card-text>
                        <v-row justify="center">
                            <v-col
                                cols="auto"
                            >
                                <v-btn
                                    @click="moveToBoardList"
                                    color="secondary"
                                >
                                    목록
                                </v-btn>
                            </v-col>

                            <!--              1.(로그인 사용자)본인 글이면 수정 삭제 버튼 보임.-->
                            <template
                                v-if="boardDetail.memberNickname !== null && $store.state.username === boardDetail.memberUsername">
                                <v-col
                                    cols="auto"
                                >
                                    <v-btn
                                        @click="moveToBoardModify"
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
                                        @click="deleteMyBoard"
                                    >
                                        삭제
                                    </v-btn>
                                </v-col>
                            </template>

                            <!--                2. 익명 글이면 누구나 수정 삭제 버튼 눌렀을시 모달 뜸-->
                            <template v-else-if="boardDetail.guestNickname !== null">

                                <v-col
                                    cols="auto"
                                >
                                    <v-dialog
                                        v-model="showModifyGuestBoardDialog"
                                        max-width="400px"
                                    >
                                        <template v-slot:activator="{ on, attrs }">
                                            <v-btn
                                                outlined
                                                color="primary"
                                                v-bind="attrs"
                                                v-on="on"
                                            >
                                                수정
                                            </v-btn>
                                        </template>

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
                                                                type="password"
                                                                clearable
                                                                prepend-icon="mdi-lock-outline"
                                                                v-model="guestPasswordCheckRequest.guestPassword"
                                                                label="비밀번호를 입력해주세요."
                                                                v-on:keyup.enter="guestPasswordCheck('modify')"
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
                                                    @click="showModifyGuestBoardDialog = false"
                                                >
                                                    취소
                                                </v-btn>

                                                <v-btn
                                                    color="blue darken-1"
                                                    text
                                                    @click="guestPasswordCheck('modify')"
                                                >
                                                    확인
                                                </v-btn>
                                            </v-card-actions>
                                        </v-card>
                                    </v-dialog>
                                </v-col>

                                <v-col
                                    cols="auto"
                                >
                                    <v-dialog
                                        v-model="showDeleteGuestBoardDialog"
                                        max-width="400px"
                                    >
                                        <template v-slot:activator="{ on, attrs }">
                                            <v-btn
                                                outlined
                                                color="primary"
                                                v-bind="attrs"
                                                v-on="on"
                                            >
                                                삭제
                                            </v-btn>
                                        </template>

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
                                                                v-model="guestPasswordCheckRequest.guestPassword"
                                                                label="비밀번호를 입력해주세요."
                                                                v-on:keyup.enter="guestPasswordCheck('delete')"
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
                                                    @click="showDeleteGuestBoardDialog = false"
                                                >
                                                    취소
                                                </v-btn>

                                                <v-btn
                                                    color="blue darken-1"
                                                    text
                                                    @click="guestPasswordCheck('delete')"
                                                >
                                                    확인
                                                </v-btn>
                                            </v-card-actions>
                                        </v-card>
                                    </v-dialog>
                                </v-col>
                            </template>
                        </v-row>
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import CommonAttachList from "@/components/common/CommonAttachList";
import TheBoardDetailCommentList from "@/components/board/TheBoardDetailCommentList";
import TheBoardDetailCommentWriteForm from "@/components/board/TheBoardDetailCommentWriteForm";
import CommonItemDetail from "@/components/common/CommonItemDetail";
import CommonPageTitle from "@/components/common/CommonPageTitle";

export default {
    name: "BoardDetailView",
    components: {
        CommonPageTitle,
        CommonItemDetail,
        CommonAttachList,
        TheBoardDetailCommentList,
        TheBoardDetailCommentWriteForm
    },
    data() {
        return {
            boardDetail: {},
            attachOf: "board",

            commentSaveResponseStatus: 0,
            commentDeleteResponseStatus: 0,

            guestPasswordCheckRequest: {
                guestPassword: "",
            },

            showModifyGuestBoardDialog: false,
            showDeleteGuestBoardDialog: false,
        }
    },
    computed: {},
    //TODO: coumputed 로 변경
    watch: {
        showModifyGuestBoardDialog() {
            if (this.showModifyGuestBoardDialog === false) {
                this.guestPasswordCheckRequest.guestPassword = "";
            }
        },
        showDeleteGuestBoardDialog() {
            if (this.showDeleteGuestBoardDialog === false) {
                this.guestPasswordCheckRequest.guestPassword = "";
            }
        }
    },
    async created() {

        let boardId = this.$route.params.boardId;

        await this.fetchBoardDetail(boardId);
    },
    methods: {
        isEmpty(obj) {
            return Object.keys(obj).length === 0 && obj.constructor === Object;
        },

        async fetchBoardDetail(boardId) {
            const {data} = await this.$_boardService.fetchBoard(boardId);

            this.boardDetail = data.boardDetail;
        },

        async fetchCommentList(boardId) {
            const {data} = await this.$_boardService.fetchCommentList(boardId);

            this.boardDetail.commentList = data.commentList;
        },

        async deleteComment(deleteCommentRequest) {
            try {
                const {status} = await this.$_boardService.deleteComment(deleteCommentRequest);
                this.commentDeleteResponseStatus = status;

                alert('삭제되었습니다.');

                await this.fetchCommentList(this.boardDetail.boardId);

            } catch (error) {
                alert(error.response.data.errorMessage);
            }
        },

        async writeComment(commentSaveRequest) {
            try {
                commentSaveRequest.boardId = this.boardDetail.boardId;

                let commentSaveResponse;

                if (this.$store.getters.loggedIn) {
                    commentSaveResponse = await this.$_boardService.writeMemberComment(commentSaveRequest);
                } else {
                    commentSaveResponse = await this.$_boardService.writeGuestComment(commentSaveRequest)
                }

                this.commentSaveResponseStatus = commentSaveResponse.status;

                await this.fetchCommentList(this.boardDetail.boardId)

            } catch (error) {
                const errorCode = error.response.data.errorCode;
                const errorMessage = error.response.data.errorMessage;

                //TODO: enum 만들기
                if (errorCode === "M002") {
                    alert(errorMessage);
                }

                if (errorCode === "C002") {
                    const firstErrorField = error.response.data.fieldErrorDetails[0].field;
                    const fieldErrorMessage = error.response.data.fieldErrorDetails[0].fieldErrorMessage;

                    //TODO: alert 말고 출력으로 바꾸기
                    if (firstErrorField === "content") {
                        alert(`댓글은 ${fieldErrorMessage}`)
                    } else if (firstErrorField === "guestPassword") {
                        alert(`${fieldErrorMessage}`)
                    } else if (firstErrorField === "guestNickname") {
                        alert(`닉네임은 ${fieldErrorMessage}`)
                    }
                }

            }
        },
        initCommentDeleteResponseStatus() {
            this.commentDeleteResponseStatus = 0;
        },
        initCommentSaveResponseStatus() {
            this.commentSaveResponseStatus = 0;
        },

        async deleteMyBoard() {
            try {
                await this.$_boardService.removeBoard({
                    boardId: this.boardDetail.boardId
                });
                alert('삭제되었습니다.');

                this.moveToBoardList();
            } catch (error) {
                alert(error.response.data.errorMessage);
            }
        },

        async guestPasswordCheck(action) {
            this.guestPasswordCheckRequest.boardId = this.boardDetail.boardId;

            try {

                if (action === 'delete') {

                    await this.$_boardService.removeBoard(this.guestPasswordCheckRequest);
                    alert('삭제되었습니다.');

                    this.moveToBoardList();

                } else if (action === 'modify') {

                    await this.$_boardService.checkBoardPw(this.guestPasswordCheckRequest);

                    this.moveToBoardModify();
                }
            } catch (error) {
                alert(error.response.data.errorMessage);

                this.guestPasswordCheckRequest.guestPassword = "";
            }
        },

        moveToBoardList() {
            this.$router.push({
                name: "BoardListView",
                query: this.$route.query
            });
        },

        moveToBoardModify() {
            this.$router.push({
                name: "BoardModifyView",
                params: {
                    boardId: this.boardDetail.boardId
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