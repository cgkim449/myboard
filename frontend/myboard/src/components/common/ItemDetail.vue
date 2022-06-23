<template>
  <v-card outlined min-height="400" class="pa-1" v-if="fetchedItemDetail !== undefined">
    <v-card-text class="mt-1">
      <v-row algin="center">
        <v-col
            cols="auto"
        >
<!--          TODO: undefiend? -->
          <template v-if="fetchedItemDetail.guestNickname !== null  && fetchedItemDetail.guestNickname !== undefined" >
            <span>{{ fetchedItemDetail.guestNickname }}</span>
          </template>
          <template v-if="fetchedItemDetail.memberNickname !== null && fetchedItemDetail.memberNickname !== undefined">
            <span>{{ fetchedItemDetail.memberNickname }}</span>
          </template>
          <template v-else-if="fetchedItemDetail.adminNickname !== null && fetchedItemDetail.adminNickname !== undefined">
            <span>{{ fetchedItemDetail.adminNickname }}</span>
          </template>
        </v-col>

        <v-spacer></v-spacer>

        <v-col
            cols="auto"
        >
          <span>등록일시 {{fetchedItemDetail.registerDate | formatDate}}</span>
          <v-divider
              class="mx-4"
              vertical
          ></v-divider>
          <span>수정일시 {{fetchedItemDetail.updateDate | formatDate}}</span>
        </v-col>
      </v-row>

      <v-row algin="center">
        <v-col
            cols="auto"
        >
          <strong v-if="itemType !== 'answer' && itemType !== 'notice'">[{{fetchedItemDetail.categoryName}}]</strong>
          <strong v-if="itemType === 'answer'" class="blue--text">처리완료</strong>
          <v-divider
              v-if="itemType !== 'notice'"
              class="mx-4"
              vertical
          ></v-divider>
          <strong>{{fetchedItemDetail.title}}</strong>
        </v-col>

        <v-spacer></v-spacer>

        <v-col
            cols="auto"
            v-if="itemType !== 'answer' && itemType !== 'notice'"
        >
          <span>조회수: {{fetchedItemDetail.viewCount}}</span>
        </v-col>
      </v-row>
    </v-card-text>

    <v-divider  class="mx-4"></v-divider>

    <v-card-text class="fill-height">
      <v-row>
        <v-col>
          <p>{{fetchedItemDetail.content}}</p>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  name: "ItemDetail",
  props: ["fetchedItemDetail", "itemType"],
}
</script>

<style scoped>

</style>