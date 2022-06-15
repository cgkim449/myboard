<template>
  <v-container >
    <v-row>
      <v-col
          cols="auto"

      >
        <h2>
          Q&A
        </h2>
      </v-col>
    </v-row>

<!--    검색창 코드 시작-->
      <v-row dense>
        <v-col
            cols="auto"
        >
          <div class="mt-2">
            등록일
          </div>
        </v-col>
        <v-col
          cols="2"
        >
          <v-menu
              v-model="menu1"
              :close-on-content-click="false"
              :nudge-right="40"
              transition="scale-transition"
              offset-y
              min-width="auto"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field

                  dense
                  outlined
                  v-model="searchCondition.fromDate"
                  label="From"
                  prepend-icon="mdi-calendar"
                  readonly
                  v-bind="attrs"
                  v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker
                v-model="searchCondition.fromDate"
                @input="menu1 = false"
            ></v-date-picker>
          </v-menu>
        </v-col>
        <v-col
            cols="auto"
        >
          <div class="mt-2">
            ~
          </div>
        </v-col>
        <v-col
          cols="2"
        >
          <v-menu
              v-model="menu2"
              :close-on-content-click="false"
              :nudge-right="40"
              transition="scale-transition"
              offset-y
              min-width="auto"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                  dense
                  outlined
                  v-model="searchCondition.toDate"
                  label="To"
                  prepend-icon="mdi-calendar"
                  readonly
                  v-bind="attrs"
                  v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker
                v-model="searchCondition.toDate"
                @input="menu2 = false"
            ></v-date-picker>
          </v-menu>
        </v-col>
        <v-col
            cols="2"
        >
          <v-select
              dense
              outlined
              v-model="searchCondition.categoryId"
              :items="items"
              item-text="categoryName"
              item-value="categoryId"
          ></v-select>
        </v-col>

        <v-col
        >
          <v-text-field
              dense
              outlined
              label="검색어를 입력해주세요. (제목 + 작성자 + 내용)"
              clearable
              v-model="searchCondition.keyword"
              v-on:keyup.enter="search"
          ></v-text-field>

        </v-col>
        <v-col
          cols="auto"
        >
          <v-btn
              normal
              class="mt-1"
              color="primary"
              v-on:click="search"
          >
            검색
          </v-btn>
        </v-col>
    </v-row>


<!--    여기까지 검색창 코드 자유게시판이랑 완전히 같음-->


    <v-row dense>
      <v-col
          cols="auto"
      >
          총 {{totalCounts}} 건
      </v-col>
    </v-row>


  <v-row justify="center" >
    <v-col
        cols="12"
    >
      <v-data-table
          dense
          :headers="headers"
          :items="itemList"
          hide-default-footer
          class="elevation-2"
      >
        <template v-slot:item.hasAttach="{item}">
          <v-icon
              v-if="item.hasAttach === 1"
          >
            mdi-attachment
          </v-icon>
          <v-icon
              v-else
          >
          </v-icon>
        </template>
        <template v-slot:item.title="{item}">

          <template v-if="item.isSecret === 0">
              <span
                  @click="moveToDetail(item.questionId)"
                  v-bind:style="{cursor: 'pointer'}"
                  class="d-flex start"
              >
                {{item.title | formatBoardTitle}}
              </span>
          </template>
          <template v-else-if="(item.isSecret === 1 && item.nickname === $store.state.nickname) || $cookies.get('role') === 'admin'">
              <span
                  @click="moveToDetail(item.questionId)"
                  v-bind:style="{cursor: 'pointer'}"
                  class="d-flex start"
              >
                {{item.title | formatBoardTitle}} <v-icon small>mdi-lock</v-icon>
              </span>
          </template>
          <template v-else>
            <span
                v-if="item.isSecret === 1 && item.nickname !== $cookies.get('nickname')"
                class="d-flex start"
            >
                비공개 글 입니다. <v-icon small>mdi-lock</v-icon>
              </span>
          </template>


        </template>
        <template v-slot:item.nickname="{item}">
            {{ item.nickname | formatQuestionNickname }}
        </template>
        <template v-slot:item.registerDate="{item}">
          {{item.registerDate | formatDate}}
        </template>
        <template v-slot:item.updateDate="{item}">
          {{formatUpdateDate(item)}}
        </template>
        <template v-slot:item.answerId="{item}">
          <span v-if="item.answerId === null">
            대기
          </span>
          <span v-if="item.answerId !== null">
            처리완료
          </span>
        </template>
      </v-data-table>
    </v-col>
  </v-row>

<!--    리스트 보기 방식 여기까지. questionId boardId만 통일하면 여기도 코드 똑같을듯.-->

    <v-row justify="center">
      <v-col
          cols="12"
      >
        <div class="text-center">
          <v-pagination
              v-model="searchCondition.page"
              :length="Math.ceil(totalCounts / 10)"
              :total-visible="10"
          ></v-pagination>
        </div>
      </v-col>
    </v-row>



    <v-row>
      <v-spacer></v-spacer>

      <v-col
          cols="auto"
      >
        <router-link v-bind:to="{
                    path: `/questions/new`, //TODO: 이거 router-link말고 메서드로 바꾸자, 비로그인이면 버튼 안보이게
                    query: this.searchCondition
                  }">
          <v-btn
              color="primary"
          >
            질문하기

          </v-btn>
        </router-link>
      </v-col>
    </v-row>
  </v-container>
</template>

<!--TODO: 자유게시판이랑 걍 똑같음. 근데 qna는 비밀글 등 더 추가할게 있는듯.-->
<script>
import {formatDate} from "@/utils/filters";
import {formatQuestionNickname} from "@/utils/filters";

export default {
  name: "QuestionListView",
  components: {},
  data() {
    return {
      searchCondition: {
        categoryId: "0",
        keyword: "",
        fromDate: "",
        toDate: "",
        page: 1,
      },
      date: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
      menu1: false,
      menu2: false,
      items: [
        { categoryName: '전체 카테고리', categoryId: "0"},
        { categoryName: 'Java', categoryId: "1"},
        { categoryName: 'JavaScript', categoryId: "2"},
        { categoryName: 'Database', categoryId: "3"},
      ],
      headers: [
        {
          text: '카테고리',
          align: 'center',
          sortable: false,
          value: 'categoryName',
          width: '10%',
        },
        {
          text: '',
          align: 'center',
          sortable: false,
          value: 'hasAttach',
          width: '1%',
        },
        {
          text: '제목',
          align: 'center',
          sortable: false,
          value: 'title',
        },
        {
          text: '작성자',
          align: 'center',
          sortable: false,
          value: 'nickname',
          width: '10%',
        },
        {
          text: '조회수',
          align: 'center',
          sortable: false,
          value: 'viewCount',
          width: '7%',
        },
        {
          text: '등록 일시',
          align: 'center',
          sortable: false,
          value: 'registerDate',
          width: '13%',
        },
        {
          text: '수정 일시',
          align: 'center',
          sortable: false,
          value: 'updateDate',
          width: '13%',
        },
        {
          text: '상태',
          align: 'center',
          sortable: false,
          value: 'answerId',
          width: '10%',
        },
      ],

      totalCounts: 0,
      itemList: [],
    };
  },
  async created() {
    this.searchCondition = {...this.$route.query};
    if(this.isEmpty(this.searchCondition)) {
      this.searchCondition = {
        categoryId: "0",
        keyword: "",
        fromDate: "",
        toDate: "",
        page: 1,
      };
    }
    if(!isNaN(this.searchCondition.page)) {
      this.searchCondition.page = Number(this.searchCondition.page);
    }
    try {
        const response = await this.$_QuestionService.fetchItemList(this.searchCondition);
        this.itemList = response.data.itemList;
        for (const item of this.itemList) {
          if(item.thumbnailUri !== "") {
            item.display = `http://localhost:8080/upload/${item.thumbnailUri}`
          }
        }
        this.totalCounts= response.data.totalCounts;
    } catch(error) {
      console.log(error.response.data.errorMessages)
    }
  },
  computed: {

  },
  watch: {
    "searchCondition.page"() {
      this.movePage();
    },
    async "$route.query"() {
      this.prepareSearchCondition();
      const response = await this.$_QuestionService.fetchItemList(this.searchCondition);
      this.itemList = response.data.itemList;
      for (const item of this.itemList) {
        if(item.thumbnailUri !== "") {
          item.display = `http://localhost:8080/upload/${item.thumbnailUri}`
        }
      }
      this.totalCounts= response.data.totalCounts;
    },
  },
  methods: {
    prepareSearchCondition() {
      this.searchCondition = {...this.$route.query};

      if(this.isEmpty(this.searchCondition)) {
        this.searchCondition = {
          categoryId: "0",
          keyword: "",
          fromDate: "",
          toDate: "",
          page: 1,
        };
      }

      if(!isNaN(this.searchCondition.page)) {
        this.searchCondition.page = Number(this.searchCondition.page);
      }
    },
    // 빈 객체인지 체크
    isEmpty(obj) {
      return Object.keys(obj).length === 0 && obj.constructor === Object;
    },
    //TODO: 게시글 리스트 보기 모드 유지해야
    moveToDetail(id) {
      this.$router.push({
        path:`/questions/${id}`,
        query: this.searchCondition
      }).catch(()=>{});
    },
    movePage() {
      this.$router.push({
        path:'/questions',
        query: this.searchCondition
      }).catch(()=>{});
    },
    search() {
      this.searchCondition.page = 1;

      this.$router.push({
        path:'/questions',
        query: this.searchCondition
      }).catch(()=>{});
    },
    formatUpdateDate(item) {
      return item.registerDate === item.updateDate ? '-' : formatDate(item.updateDate);
    }
  },
};
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

