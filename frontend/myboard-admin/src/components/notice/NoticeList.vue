<template>
  <div>
    <v-row dense>
      <v-col
          cols="auto"
      >
        총 {{updatedNoticeTotalCount}} 건
      </v-col>

      <v-spacer></v-spacer>

      <v-col
          cols="auto"
      >
        <v-btn icon v-bind:color="updatedListView ? 'secondary' : 'primary'" @click="clickGalleryView">
          <v-icon >mdi-view-grid-outline</v-icon>
        </v-btn>
      </v-col>
      <v-col
          cols="auto"
      >
        <v-btn icon v-bind:color="updatedListView ? 'primary' : 'secondary'" @click="clickListView">
          <v-icon>mdi-format-list-bulleted</v-icon>
        </v-btn>
      </v-col>
    </v-row>

    <template v-if="updatedListView">
      <v-row>
        <v-col
            cols="12"
        >
          <v-data-table
              dense
              :headers="tableHeaders"
              :items="fetchedNoticeList"
              hide-default-footer
              class="elevation-2"
          >
            <!--            TODO: 리팩토링-->
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
                    <span
                        @click="moveToNoticeDetail(item.noticeId)"
                        v-bind:style="{cursor: 'pointer'}"
                        class="d-flex start"
                    >
                      {{item.title | formatBoardTitle}}
                    </span>
            </template>
            <template v-slot:item.registerDate="{item}">
              {{item.registerDate | formatDate}}
            </template>
            <template v-slot:item.updateDate="{item}">
              {{formatUpdateDate(item)}}
            </template>
          </v-data-table>
        </v-col>
      </v-row>
    </template>

    <template v-else>
      <v-row>
        <v-col>
          <v-card outlined>
            <v-container fluid>
              <v-row dense>
                <v-col
                    v-for="notice in fetchedNoticeList"
                    :key="notice.noticeId"
                    :cols="3"
                >
                  <v-card outlined>
                    <v-img
                        @click="moveToNoticeDetail(notice.noticeId)"
                        v-bind:style="{cursor: 'pointer'}"
                        :src="notice.thumbnailUri"
                        class="white--text align-end"
                        gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                        height="200px"
                    >
                    </v-img>

                    <v-card-actions>
                      <v-col cols="auto">
                        <small class="font-weight-bold">{{notice.adminNickname}}</small><br>
                        <small>{{notice.registerDate | formatDate}}</small>
                      </v-col>
                    </v-card-actions>
                  </v-card>
                </v-col>
              </v-row>
            </v-container>
          </v-card>
        </v-col>
      </v-row>
    </template>
  </div>
</template>

<script>
import {formatDate} from "@/utils/filters";

export default {
  name: "NoticeList",
  props: ["updatedNoticeTotalCount", "updatedListView", "fetchedNoticeList"],
  data() {
    return {
      tableHeaders: [
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
          value: 'adminNickname',
          width: '10%',
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
      ],
    }
  },
  methods: {
    clickGalleryView() {
      this.$emit("GalleryViewBtnClick");
    },
    clickListView() {
      this.$emit("ListViewBtnClick");
    },
    moveToNoticeDetail(noticeId) {
      this.$router.push({
        name: "NoticeDetailView",
        params: {noticeId: noticeId},
        query: this.$route.query
      }).catch(()=>{});
    },
    formatUpdateDate(notice) {
      return notice.registerDate === notice.updateDate ? '-' : formatDate(notice.updateDate);
    }
  }
}
</script>

<style scoped>

</style>