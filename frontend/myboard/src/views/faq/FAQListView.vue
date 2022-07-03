<template>
    <v-container>
        <PageTitle>
            <h2 slot="title">
                FAQ
            </h2>
        </PageTitle>

        <v-row>
            <v-col>
                <v-data-table
                    :headers="faqHeaders"
                    :items="faqList"
                    :single-expand="singleExpand"
                    :expanded.sync="expanded"
                    item-key="faqId"
                    show-expand
                    hide-default-footer
                    class="elevation-0"
                >
                    <template v-slot:top>

                        <v-row dense>
                            <v-col cols="auto">
                                <v-sheet
                                    elevation="0"
                                >
                                    <v-chip-group
                                        mandatory
                                        active-class="primary--text"
                                    >

                                        <v-chip @click="search(0)">
                                            전체
                                        </v-chip>
                                        <v-chip @click="search(1)">
                                            Java
                                        </v-chip>
                                        <v-chip @click="search(2)">
                                            JavaScript
                                        </v-chip>
                                        <v-chip @click="search(3)">
                                            Database
                                        </v-chip>

                                    </v-chip-group>
                                </v-sheet>
                            </v-col>

                            <v-spacer></v-spacer>

                            <v-col cols="auto">
                                <v-switch
                                    v-model="singleExpand"
                                    label="한 개만 펼치기"
                                    class="mt-2"
                                ></v-switch>
                            </v-col>
                        </v-row>
                    </template>

                    <template v-slot:expanded-item="{ headers, item }">
                        <td :colspan="headers.length">
                            <strong>A.</strong>&nbsp;<span>{{ item.content }}</span>
                        </td>
                    </template>

                    <template v-slot:item.title="{item}">
            <span
                class="d-flex start"
            >
              <strong>Q.</strong>&nbsp;<span>{{ item.title | formatBoardTitle }}</span>
            </span>
                    </template>

                    <template v-slot:item.registerDate="{item}">
              <span
                  class="d-flex start"
              >
                {{ item.registerDate | formatDate }}
              </span>
                    </template>

                    <template v-slot:item.updateDate="{item}">
              <span
                  class="d-flex start"
              >
                {{ item.updateDate | formatDate }}
              </span>
                    </template>
                </v-data-table>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import {formatDate} from "@/utils/filters";
import PageTitle from "@/components/common/PageTitle";

export default {
    name: "FAQListView",
    components: {
        PageTitle
    },
    data() {
        return {
            expanded: [],

            singleExpand: true,

            faqList: [],

            faqHeaders: [
                {text: '', value: 'data-table-expand'},
                {text: '카테고리', align: 'center', sortable: false, width: '13%', value: 'categoryName'},
                {text: '제목', align: 'center', sortable: false, width: '50%', value: 'title'},
                {text: '등록일', align: 'center', sortable: false, width: '13%', value: 'registerDate'},
                {text: '수정일', align: 'center', sortable: false, width: '13%', value: 'updateDate'},
            ],

        }
    },
    methods: {
        async search(categoryId) {
            const {data} = await this.$_FAQService.fetchFAQList(categoryId);
            this.faqList = data.faqList;
        },
    },
    async created() {
        const {data} = await this.$_FAQService.fetchFAQList(0);
        this.faqList = data.faqList;
    },
}
</script>

<style scoped>

</style>