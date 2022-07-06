<template>
    <v-card elevation="0">
        <v-card-text>
            <v-row justify="center">
                <v-col
                    cols="auto"
                >
                    <v-btn
                        @click="$_routerPush.goToBoardList($route.query)"
                        color="secondary"
                    >
                        목록
                    </v-btn>
                </v-col>

                <!--1.(로그인 사용자)본인 글이면 수정 삭제 버튼 보임.-->
                <template
                    v-if="fetchedBoardDetail.memberNickname && $store.state.username === fetchedBoardDetail.memberUsername">
                    <v-col
                        cols="auto"
                    >
                        <v-btn
                            @click="$_routerPush.goToBoardModify(fetchedBoardDetail.boardId, $route.query)"
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
                            @click="deleteMyBoardBtnClick"
                        >
                            삭제
                        </v-btn>
                    </v-col>
                </template>

                <!--2. 익명 글이면 누구나 수정 삭제 버튼 눌렀을시 모달 뜸-->
                <template v-else-if="fetchedBoardDetail.guestNickname">
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
                                                    v-model="guestPassword"
                                                    label="비밀번호를 입력해주세요."
                                                    v-on:keyup.enter="guestPasswordCheckBtnClick('modify')"
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
                                        @click="showModifyGuestBoardDialog = false"
                                    >
                                        취소
                                    </v-btn>

                                    <v-btn
                                        color="blue darken-1"
                                        text
                                        @click="guestPasswordCheckBtnClick('modify')"
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
                                                    v-model="guestPassword"
                                                    label="비밀번호를 입력해주세요."
                                                    v-on:keyup.enter="guestPasswordCheckBtnClick('delete')"
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
                                        @click="showDeleteGuestBoardDialog = false"
                                    >
                                        취소
                                    </v-btn>

                                    <v-btn
                                        color="blue darken-1"
                                        text
                                        @click="guestPasswordCheckBtnClick('delete')"
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
</template>

<script>
/**
 * 자유게시판 상세 조회 하단 네비게이션
 */
export default {
    name: "TheBoardDetailBottomNavigation",

    props: {
        fetchedBoardDetail: {
            type: Object,
        },
    },

    data() {
        return {
            guestPassword: "",

            showModifyGuestBoardDialog: false,
            showDeleteGuestBoardDialog: false,
        }
    },

    watch: {
        /**
         * 익명 게시물 수정 모달이 닫히면 guestPassword 초기화
         */
        showModifyGuestBoardDialog() {
            if (this.showModifyGuestBoardDialog === false) {
                this.guestPassword = "";
            }
        },

        /**
         * 익명 게시물 삭제 모달이 닫히면 guestPassword 초기화
         */
        showDeleteGuestBoardDialog() {
            if (this.showDeleteGuestBoardDialog === false) {
                this.guestPassword = "";
            }
        }
    },

    methods: {
        /**
         * 본인 게시물 삭제 버튼 클릭
         */
        deleteMyBoardBtnClick() {
            this.$emit("deleteMyBoardBtnClick");
        },

        /**
         * 익명 게시물 비밀번호 체크 버튼 클릭
         *
         * @param action
         */
        guestPasswordCheckBtnClick(action) {
            this.$emit("guestPasswordCheckBtnClick", action, this.guestPassword);
        }
    }
}
</script>
