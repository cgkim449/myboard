<template>
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
          v-model="showFromDatePicker"
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
            @input="showFromDatePicker = false"
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
          v-model="showToDatePicker"
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
            @input="showToDatePicker = false"
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
          :items="categories"
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
          v-on:keyup.enter="searchBtnClick"
      ></v-text-field>
    </v-col>

    <v-col
        cols="auto"
    >
      <v-btn
          height="40"
          color="secondary"
          v-on:click="searchBtnClick"
      >
        검색
      </v-btn>
    </v-col>

  </v-row>
</template>

<script>
export default {
  name: "SearchForm",
  data() {
    return {
      showFromDatePicker: false,
      showToDatePicker: false,

      categories: [
        { categoryName: '전체 카테고리', categoryId: "0"},
        { categoryName: 'Java', categoryId: "1"},
        { categoryName: 'JavaScript', categoryId: "2"},
        { categoryName: 'Database', categoryId: "3"},
      ],

      searchCondition: {
        categoryId: "0",
        keyword: "",
        fromDate: "",
        toDate: "",
        page: 1,
      },
    }
  },
  props: ["updatedSearchCondition"],
  created() {
    this.searchCondition = this.updatedSearchCondition;
  },
  //TODO: computed 로 바꾸기
  watch: {
    updatedSearchCondition: {
      handler() {
        this.searchCondition = this.updatedSearchCondition;
      },
      deep: true
    },
  },
  methods: {
    searchBtnClick() {
      this.searchCondition.page = 1;
      this.$emit("searchBtnClick", this.searchCondition);
    },
  }
}
</script>

<style scoped>

</style>