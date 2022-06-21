<template>
  <v-card outlined min-height="400" class="pa-1">
    <v-card-text class="mt-1">
      <v-row algin="center">
        <v-col
            cols="auto"
        >
          <template v-if="fetchedBoardDetail.guestNickname !== null">
            <span>{{ fetchedBoardDetail.guestNickname }}</span>
          </template>
          <template v-else-if="fetchedBoardDetail.memberNickname !== null">
            <span>{{ fetchedBoardDetail.memberNickname }}</span>
          </template>
          <template v-else-if="fetchedBoardDetail.adminNickname !== null">
            <span>{{ fetchedBoardDetail.adminNickname }}</span>
          </template>
        </v-col>

        <v-spacer></v-spacer>

        <v-col
            cols="auto"
        >
          <span>등록일시 {{fetchedBoardDetail.registerDate | formatDate}}</span>
          <v-divider
              class="mx-4"
              vertical
          ></v-divider>
          <span>수정일시 {{fetchedBoardDetail.updateDate | formatDate}}</span>
        </v-col>
      </v-row>

      <v-row algin="center">
        <v-col
            cols="auto"
        >
          <strong>[{{fetchedBoardDetail.categoryName}}]</strong>
          <v-divider
              class="mx-4"
              vertical
          ></v-divider>
          <strong>{{fetchedBoardDetail.title}}</strong>
        </v-col>

        <v-spacer></v-spacer>

        <v-col
            cols="auto"
        >
          <span>조회수: {{fetchedBoardDetail.viewCount}}</span>
        </v-col>
      </v-row>
    </v-card-text>

    <v-divider  class="mx-4"></v-divider>

    <v-card-text class="fill-height">
      <v-row>
        <v-col>
          <p>{{fetchedBoardDetail.content}}</p>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  name: "BoardDetail",
  props: ["fetchedBoardDetail"]
}
</script>

<style scoped>

</style>