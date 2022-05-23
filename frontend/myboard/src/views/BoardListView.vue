<template>
  <div>

  </div>
</template>

<script>
export default {
  name: 'BoardListView',
  components: {
  },
  data() {
    return {

    }
  },
  async created() {
    const response = await this.$_BoardService.fetchBoardList();
  }
}
</script>
