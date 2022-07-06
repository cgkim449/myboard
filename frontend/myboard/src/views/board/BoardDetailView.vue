<template>
    <v-container>
        <CommonPageTitle>
            <h2 slot="title" @click="$_routerPush.goToBoardList($route.query)" v-bind:style="{ cursor: 'pointer' }">
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

                <TheBoardDetailBottomNavigation
                    v-bind:fetchedBoardDetail="boardDetail"
                    v-on:deleteMyBoardBtnClick="deleteMyBoard"
                    v-on:guestPasswordCheckBtnClick="guestPasswordCheck"
                ></TheBoardDetailBottomNavigation>
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
import TheBoardDetailBottomNavigation from "@/components/board/TheBoardDetailBottomNavigation";

/**
 * 게시물 상세 조회 페이지
 */
export default {
    name: "BoardDetailView",

    components: {
        TheBoardDetailBottomNavigation,
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
        }
    },

    /**
     * 게시물 상세 조회 api 호출
     *
     * @returns {Promise<void>}
     */
    async created() {
        const boardId = this.$route.params.boardId;

        await this.fetchBoardDetail(boardId);
    },

    methods: {
        /**
         * 게시물 상세 조회
         *
         * @param boardId
         * @returns {Promise<void>}
         */
        async fetchBoardDetail(boardId) {
            const {data} = await this.$_boardService.fetchBoard(boardId);

            this.boardDetail = data.boardDetail;
        },

        /**
         * 댓글 목록 조회
         *
         * @param boardId
         * @returns {Promise<void>}
         */
        async fetchCommentList(boardId) {
            const {data} = await this.$_boardService.fetchCommentList(boardId);

            this.boardDetail.commentList = data.commentList;
        },

        /**
         * 댓글 삭제
         *
         * @param deleteCommentRequest
         * @returns {Promise<void>}
         */
        async deleteComment(deleteCommentRequest) {
            const {status} = await this.$_boardService.deleteComment(deleteCommentRequest);

            this.commentDeleteResponseStatus = status;

            alert('삭제되었습니다.');

            await this.fetchCommentList(this.boardDetail.boardId);
        },

        /**
         * 댓글 작성
         *
         * @param commentSaveRequest
         * @returns {Promise<void>}
         */
        async writeComment(commentSaveRequest) {
            commentSaveRequest.boardId = this.boardDetail.boardId;

            let commentSaveResponse;

            if (this.$store.getters.loggedIn) {
                commentSaveResponse = await this.$_boardService.writeMemberComment(commentSaveRequest);
            } else {
                commentSaveResponse = await this.$_boardService.writeGuestComment(commentSaveRequest)
            }

            this.commentSaveResponseStatus = commentSaveResponse.status;

            await this.fetchCommentList(this.boardDetail.boardId)
        },

        /**
         * 본인의 게시물 삭제
         *
         * @returns {Promise<void>}
         */
        async deleteMyBoard() {
            await this.$_boardService.removeBoard({
                boardId: this.boardDetail.boardId
            });

            alert('삭제되었습니다.');

            await this.$_routerPush.goToBoardList(this.$route.query);
        },

        /**
         * 익명 게시물 비밀번호 체크
         *
         * @param action
         * @param guestPassword
         * @returns {Promise<void>}
         */
        async guestPasswordCheck(action, guestPassword) {

            if (action === 'delete') {

                await this.$_boardService.removeBoard({
                    guestPassword: guestPassword,
                    boardId: this.boardDetail.boardId
                });

                alert('삭제되었습니다.');

                await this.$_routerPush.goToBoardList(this.$route.query);

            } else if (action === 'modify') {

                await this.$_boardService.checkBoardPw({
                    guestPassword: guestPassword,
                    boardId: this.boardDetail.boardId
                });

                await this.$_routerPush.goToBoardModify(this.boardDetail.boardId, this.$route.query);
            }
        },

        initCommentDeleteResponseStatus() {
            this.commentDeleteResponseStatus = 0;
        },

        initCommentSaveResponseStatus() {
            this.commentSaveResponseStatus = 0;
        },
    },
}
</script>
