<template>
  <v-row>
    <v-col cols="5">
        <v-select
            class="ml-5"
            placeholder="검색 조건 선택" 
            :items="searchConditionList" 
            v-model="searchConditionValue" 
            @change="onChangeSearchConditionSelect">
        </v-select>
    </v-col>
    <v-col cols="4">
        <v-text-field
            v-if="searchConditionValue === 'content'"
            class="mr-1"
            label="검색어 입력"
            single-line
            v-model="searchValue"
        ></v-text-field>
        <v-checkbox
            v-if="searchConditionValue === 'isDone'"
            class="mr-1"
            label="완료 여부"
            v-model="searchValue"
        ></v-checkbox>
        <v-menu
            v-if="searchConditionValue === 'createAt' || searchConditionValue === 'updateAt'"
            ref="menu"
            v-model="dateMenu"
            :close-on-content-click="false"
            :return-value.sync="searchValue"
            transition="scale-transition"
            offset-y
            min-width="290px"
        >
            <template v-slot:activator="{ on }">
            <v-text-field
                class="mr-1"
                v-model="searchValue"
                label="날짜 선택"
                prepend-icon="mdi-event"
                readonly
                v-on="on"
            ></v-text-field>
            </template>
            <v-date-picker v-model="searchValue" no-title scrollable>
            <v-spacer></v-spacer>
            <v-btn text color="primary" @click="dateMenu = false">Cancel</v-btn>
            <v-btn text color="primary" @click="$refs.menu.save(searchValue)">OK</v-btn>
            </v-date-picker>
        </v-menu>
    </v-col>
    <v-col cols="2">
        <v-btn
            class="mt-3" 
            v-if="searchConditionValue !== ''" 
            color="primary" 
            @click="onClickSearchButton(true)">
            Search
        </v-btn>
    </v-col>
  </v-row>
</template>

<script>

import {mapMutations, mapActions, mapState, mapGetters} from 'vuex';

export default {
    name: 'Search',

    computed: {
        ...mapState({
            isFiltered: 'isFiltered',
            readFail: 'readFail',
            page: 'page',
            itemsPerPage: 'itemsPerPage',
            sortBy: 'sortBy',
            sortDesc: 'sortDesc',
            latestSearchConditionValue: 'latestSearchConditionValue',
            latestSearchValue: 'latestSearchValue'
        }),
        ...mapGetters({
            getResultMsgObj: 'getResultMsgObj'
        })
    },

    data() {
        return {
            searchConditionList: [
                {text: '내용', value: 'content'},
                {text: '완료 여부', value: 'isDone'},
                {text: '생성일', value: 'createAt'},
                {text: '수정일', value: 'updateAt'},
            ],
            searchConditionValue: '',
            searchValue: '',
            dateMenu: false,
            dateModal: false,
        }
    },

    methods: {

        ...mapMutations([
            'SET_RESULT_DIALOG',
            'SET_FILTER',
            'SET_SNACK',
            'SET_CURRENT_PAGE'
        ]),

        ...mapActions([
            'FETCH_TODOS',
            'FETCH_TODOS_BY_SEARCH'
        ]),

        onChangeSearchConditionSelect() {
            this.searchValue = '';
        },

        closeSnack() {
            setTimeout(() => {
                this.SET_SNACK(false);
            }, 3000);
        },

        async onClickSearchButton(isCallBySearchButton) {

            try {
                if(!isCallBySearchButton) {
                    this.searchValue = this.latestSearchValue;
                    this.searchConditionValue = this.latestSearchConditionValue;
                }

                if(!this.searchValue) {

                    await this.SET_RESULT_DIALOG(this.getResultMsgObj({
                        msg: '검색어 입력하세요.',
                        color: 'warning'
                    }));

                    this.closeSnack();

                    return;
                }

                let result = await this.FETCH_TODOS_BY_SEARCH({
                    searchValue: this.searchValue, 
                    searchConditionValue: this.searchConditionValue,
                    page: this.page,
                    pageSize: this.itemsPerPage,
                    sortBy: this.sortBy.length === 0 ? 'id' : this.sortBy[0],
                    sortDesc: this.sortDesc.length === 0 ? true : this.sortDesc[0]   
                });
                
                if(result.status === 200) {
                    this.SET_FILTER({
                        isFiltered: true, 
                        searchValue: this.searchValue, 
                        searchConditionValue: this.searchConditionValue
                    });

                    if(isCallBySearchButton) {
                        this.SET_CURRENT_PAGE({page: 1})
                    }
                } else {
                    await this.SET_RESULT_DIALOG(this.getResultMsgObj({
                        msg: this.readFail,
                        color: 'error'
                    }));
                    this.closeSnack();
                }

            } catch (error) {
                await this.SET_RESULT_DIALOG(this.getResultMsgObj({
                    msg: this.readFail,
                    color: 'error'
                }));
                this.closeSnack();
            }
        },
    },
}
</script>
