<template>
  <v-container >
    <v-row>
      <v-col
          cols="auto"

      >
        <h2>
          자유게시판
        </h2>
      </v-col>
    </v-row>


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

    <v-row dense>
      <v-col
          cols="auto"
      >
          총 {{boardTotalCounts}} 건
      </v-col>

      <v-spacer></v-spacer>

      <v-col
        cols="auto"
      >
        <v-btn icon v-bind:color="listView ? 'secondary' : 'primary'" @click="listView = false">
          <v-icon >mdi-view-grid-outline</v-icon>
        </v-btn>
      </v-col>
        <v-col
            cols="auto"
        >
        <v-btn icon v-bind:color="listView ? 'primary' : 'secondary'" @click="listView = true">
          <v-icon>mdi-format-list-bulleted</v-icon>
        </v-btn>
      </v-col>
    </v-row>


<template v-if="listView">
  <v-row justify="center" >
    <v-col
        cols="12"
    >
      <v-data-table
          dense
          :headers="headers"
          :items="boardList"
          hide-default-footer
          class="elevation-2"
      >
        <template v-slot:item.boardHasAttach="{item}">
          <v-icon
              v-if="item.boardHasAttach === 1"
          >
            mdi-attachment
          </v-icon>
          <v-icon
              v-else
          >
          </v-icon>
        </template>
        <template v-slot:item.boardTitle="{item}">
              <span
                  @click="moveToBoardDetail(item.boardId)"
                  v-bind:style="{cursor: 'pointer'}"
                  class="d-flex start"
              >
                {{item.boardTitle | formatBoardTitle}}
              </span>
        </template>
        <template v-slot:item.guestNickname="{item}">
              <span v-if="item.guestNickname === null">
                {{ item.nickname }}
              </span>
          <span v-if="item.guestNickname !== null">
                {{ item.guestNickname }}
              </span>
        </template>
        <template v-slot:item.boardRegisterDate="{item}">
          {{item.boardRegisterDate | formatDate}}
        </template>
        <template v-slot:item.boardUpdateDate="{item}">
          {{formatUpdateDate(item)}}
        </template>
      </v-data-table>
    </v-col>
  </v-row>
</template>
<template v-else>
  <v-card outlined>
    <v-container fluid>
      <v-row dense>
        <v-col
            v-for="board in boardList"
            :key="board.boardId"
            :cols="3"
        >
          <v-card outlined>
            <v-img
                @click="moveToBoardDetail(board.boardId)"
                v-bind:style="{cursor: 'pointer'}"
                :src="board.display"
                class="white--text align-end"
                gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                height="200px"
            >
            </v-img>

            <v-card-actions>
              <v-col cols="auto">
                <small class="font-weight-bold">[{{board.categoryName}}] {{ board.boardTitle }}</small><br>
                <small class="font-weight-bold" v-if="board.nickname !== null">{{board.nickname}}</small>
                <small class="font-weight-bold" v-if="board.nickname === null">{{board.guestNickname}}</small><br>
                <small>조회수: {{board.boardViewCount}}</small><br>
                <small>{{board.boardRegisterDate | formatDate}}</small>
              </v-col>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-card>


</template>







    <v-row justify="center">
      <v-col
          cols="12"
      >
        <div class="text-center">
          <v-pagination
              v-model="searchCondition.page"
              :length="Math.ceil(boardTotalCounts / 10)"
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
                    path: `/boards/new`,
                    query: this.searchCondition
                  }">
          <v-btn
              color="primary"
          >
            글쓰기
          </v-btn>
        </router-link>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import {formatDate} from "@/utils/filters";

export default {
  name: "BoardListView",
  components: {},
  data() {
    return {
      cards: [
        { title: 'Pre-fab homes', src: 'https://cdn.vuetifyjs.com/images/cards/house.jpg', flex: 4 },
        { title: 'Favorite road trips', src: 'https://cdn.vuetifyjs.com/images/cards/road.jpg', flex: 4 },
        { title: 'Best airlines', src: 'https://cdn.vuetifyjs.com/images/cards/plane.jpg', flex: 4 },
      ],
      listView: true,
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
          value: 'boardHasAttach',
          width: '1%',
        },
        {
          text: '제목',
          align: 'center',
          sortable: false,
          value: 'boardTitle',
        },
        {
          text: '작성자',
          align: 'center',
          sortable: false,
          value: 'guestNickname',
          width: '10%',
        },
        {
          text: '조회수',
          align: 'center',
          sortable: false,
          value: 'boardViewCount',
          width: '7%',
        },
        {
          text: '등록 일시',
          align: 'center',
          sortable: false,
          value: 'boardRegisterDate',
          width: '13%',
        },
        {
          text: '수정 일시',
          align: 'center',
          sortable: false,
          value: 'boardUpdateDate',
          width: '13%',
        },
      ],
      searchCondition: {
        categoryId: "0",
        keyword: "",
        fromDate: "",
        toDate: "",
        page: 1,
      },
      boardTotalCounts: 0,
      boardList: [],
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
        const response = await this.$_BoardService.fetchBoardList(this.searchCondition);
        this.boardList = response.data.boardList;
        for (const board of this.boardList) {
          if(board.thumbnailUri !== "") {
            board.display = `http://localhost:8080/upload/${board.boardThumbnailUri}`
          }
        }
        this.boardTotalCounts= response.data.boardTotalCounts;
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
      const response = await this.$_BoardService.fetchBoardList(this.searchCondition);
      this.boardList = response.data.boardList;
      for (const board of this.boardList) {
        if(board.hasThumbnail) {
          board.display = `http://localhost:8080/attaches/${board.thumbnail.attachId}/display`
        }
      }
      this.boardTotalCounts= response.data.boardTotalCounts;
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
    moveToBoardDetail(boardId) {
      this.$router.push({
        path:`/boards/${boardId}`,
        query: this.searchCondition
      }).catch(()=>{});
    },
    movePage() {
      this.$router.push({
        path:'/boards',
        query: this.searchCondition
      }).catch(()=>{});
    },
    search() {
      this.searchCondition.page = 1;

      this.$router.push({
        path:'/boards',
        query: this.searchCondition
      }).catch(()=>{});
    },
    formatUpdateDate(board) {
      return board.boardRegisterDate === board.boardUpdateDate ? '-' : formatDate(board.boardUpdateDate);
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

