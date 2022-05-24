<template>
  <v-container fluid>
    <v-card>
      <v-simple-table>
        <template v-slot:default>
          <thead>
          <tr>
            <th class="text-left">
              번호
            </th>
            <th class="text-left">
              카테고리
            </th>
            <th class="text-left">
              첨부파일
            </th>
            <th class="text-left">
              제목
            </th>
            <th class="text-left">
              작성자
            </th>
            <th class="text-left">
              조회수
            </th>
            <th class="text-left">
              등록일시
            </th>
            <th class="text-left">
              수정일시
            </th>
          </tr>
          </thead>
          <tbody>
          <tr
              v-for="board in boardList"
              :key="board.boardId"
          >
            <td>{{ board.boardId }}</td>
            <td>{{ board.categoryName }}</td>
            <td>{{  }}</td>
            <td>{{ board.boardTitle }}</td>
            <td>{{ board.boardWriter }}</td>
            <td>{{ board.boardViewCount }}</td>
            <td>{{ board.boardRegisterDate }}</td>
            <td>{{ board.boardUpdateDate }}</td>
          </tr>
          </tbody>
        </template>
      </v-simple-table>
    </v-card>
  </v-container>
</template>

<script>
export default {
  name: "BoardListView",
  components: {},

  data() {
    return {
      boardList: [],
    };
  },
  async created() {
    const response = await this.$_BoardService.fetchBoardList();
    console.log(response.data);
    this.boardList = response.data.boardList;
  },
};
</script>

