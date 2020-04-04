<template>
  <v-container>
    <v-row class="text-center">
      <v-col cols="12">
        <v-card>
          <v-card-title>
            Todo List
            <Search ref="searchForm" />
            <AddTodo />
            <v-btn v-if="isFiltered" class="mr-2" color="success" @click="onClickResetFilterButton">Reset Filter</v-btn>
            <v-btn color="warning" @click="onClickRefreshButton" >
              <v-icon> mdi-refresh </v-icon>
            </v-btn>
          </v-card-title>
          <v-data-table
            :headers="headers"
            :items="todoList"
            :options.sync="options"
            :server-items-length="totalItems"
            hide-default-footer
            :page.sync="page"
            :items-per-page="itemsPerPage"
            @page-count="pageCount = $event"
            class="elevation-1"
          >
            <template v-slot:item.content="props">
              <v-edit-dialog
                :return-value.sync="props.item.content"
                @save="updateContentEvent(props.item)"
              > {{ props.item.content }}
                <template v-slot:input>
                  <v-text-field
                    v-model="props.item.content"
                    label="Edit"
                    single-line
                    counter
                  ></v-text-field>
                </template>
              </v-edit-dialog>
            </template>
            <template v-slot:item.action="{ item }">
              <v-icon @click="onClickDeleteButton(item)">
                mdi-delete
              </v-icon>
            </template>
            <template v-slot:item.referenceTodo="{ item }">
              <v-select 
                multiple 
                chips 
                :items="referenceTodoList" 
                v-model="referenceTodoValue[item.id]" 
                @blur="onBlurSelectBox(item)">
              </v-select>
            </template>
            <template v-slot:item.isDone="{ item }">
              <v-checkbox
                v-model="item.isDone"
                @change="onChangeCheckBox(item)"
              ></v-checkbox>
            </template>
          </v-data-table>
          <div class="text-center pt-2">
            <v-pagination v-model="page" :length="pageCount"></v-pagination>
          </div>
        </v-card>

        <v-snackbar top v-model="snack" :timeout="0" :color="snackColor">
          {{ snackText }}
          <v-btn text @click="onClickSnackCloseButton">Close</v-btn>
        </v-snackbar>

      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Search from './Search';
import AddTodo from './AddTodo';
import {mapState, mapActions, mapMutations, mapGetters} from 'vuex'

export default {
  name: "Todo",

  components: {
    Search,
    AddTodo
  },

  computed: {
    ...mapState({
      snack: 'snack',
      snackColor: 'snackColor',
      snackText: 'snackText',
      allTodoList: 'allTodoList',
      todoList: 'todoList',
      referenceTodoValue: 'referenceTodoValue',
      referenceTodoList: 'referenceTodoList',
      totalItems: 'totalItems',
      itemsPerPage: 'itemsPerPage',
      latestSearchConditionValue: 'latestSearchConditionValue',
      latestSearchValue: 'latestSearchValue',
      isFiltered: 'isFiltered',
      updateFail: 'updateFail',
      deleteFail: 'deleteFail',
      updateSuccess: 'updateSuccess',
      deleteSuccess: 'deleteSuccess',
    }),
    ...mapGetters({
        getResultMsgObj: 'getResultMsgObj'
    })
  },

  data: () => ({
    headers: [
      { text: 'Actions', value: 'action', sortable: false, class: 'font-weight-black'},
      { text: 'Todo ID', value: 'id', class: 'font-weight-black'},
      { text: '내용(본문 클릭 시 수정 가능)', value: 'content', sortable: false, class: 'font-weight-black'},
      { text: '참조 Todo ID', value: 'referenceTodo', sortable: false, class: 'font-weight-black'},
      { text: '완료여부', value: 'isDone', class: 'font-weight-black'},
      { text: '생성일', value: 'createAt', class: 'font-weight-black'},
      { text: '수정일', value: 'updateAt', class: 'font-weight-black'},
    ],
    options: {},
    page: 1,
    pageCount: 0,
  }),

  watch: {
    options: {
      handler() {
        this.SET_PAGING_VALUE(this.options);
        this.getTodoListByCondition();
      },
      deep: true
    },
  },

  methods: {

    ...mapMutations([
        'SET_RESULT_DIALOG',
        'SET_SNACK',
        'SET_FILTER',
        'SET_PAGING_VALUE'
    ]),

    ...mapActions([
      'FETCH_TODOS',
      'UPDATE_TODO',
      'DELETE_TODO'
    ]),

    async getTodoListByCondition() {
      if(this.isFiltered) {
        await this.$refs.searchForm.onClickSearchButton(false);
      } else {
        await this.getTodoList();
      }
    },

    async onClickRefreshButton() {
      try {
        await this.getTodoListByCondition();  
      } catch (error) {
        await this.SET_RESULT_DIALOG(this.getResultMsgObj({
          msg: this.readFail,
          color: 'error'
        }));
        this.$refs.searchForm.closeSnack();
      }
    },

    onClickResetFilterButton() {
      this.getTodoList();  
    },
    
    async onClickSnackCloseButton() {
      await this.SET_SNACK(false);
    },

    async getTodoList() {

      try {
        const { sortBy, sortDesc, page, itemsPerPage } = this.options;

        let result = await this.FETCH_TODOS({page, itemsPerPage, sortBy, sortDesc});
        
        if(result.status === 200) {
          this.SET_FILTER(false);
        } else {
          await this.SET_RESULT_DIALOG(this.getResultMsgObj({
            msg: this.readFail,
            color: 'error'
          }));
          this.$refs.searchForm.closeSnack();
        }

      } catch (error) {
        await this.SET_RESULT_DIALOG(this.getResultMsgObj({
          msg: this.readFail,
          color: 'error'
        }));
        this.$refs.searchForm.closeSnack();
      }
    },

    validateTodoId(idArray) {
      let isValid = true;
      let msg = '';
      let isNotDoneIdArray = [];

      for(const id of idArray) {
        
        let found = this.allTodoList.find((e) => {
          return e.id === parseInt(id);
        });

        if(found) {
          if(!found.isDone) {
            isValid = false;
            isNotDoneIdArray.push(found.id);
            msg = '참조하는 Todo 중 완료 되지 않은 Todo가 있습니다.';
          }
        }
      }

      if(!isValid) {
        msg += ` 미완료 Todo ID - ${isNotDoneIdArray}`;
      }

      return {isValid, msg}
    },

    async updateResult(result) {
      if(result) {
        await this.SET_RESULT_DIALOG(this.getResultMsgObj({
            msg: this.updateSuccess,
            color: 'success'
        }));
      } else {
        await this.SET_RESULT_DIALOG(this.getResultMsgObj({
          msg: this.updateFail,
          color: 'error'
        }));
      }
      this.$refs.searchForm.closeSnack();
    },

    async onChangeCheckBox(item) {
      try {

        if(this.referenceTodoValue[item.id]) {
          const {isValid, msg} = await this.validateTodoId(this.referenceTodoValue[item.id]);
        
          if(isValid) {
            let result = await this.UPDATE_TODO({
              id: item.id,
              content: item.content,
              isDone: item.isDone,
              createAt: item.createAt,
              updateAt: item.updateAt
            });

            await this.updateResult(result);

          } else {
            item.isDone = !item.isDone;
            await this.SET_RESULT_DIALOG(this.getResultMsgObj({
              msg: msg,
              color: 'error'
            }));
            this.$refs.searchForm.closeSnack();
          }
        } else {
          let result = await this.UPDATE_TODO({
            id: item.id,
            content: item.content,
            isDone: item.isDone,
            createAt: item.createAt,
            updateAt: item.updateAt
          });
          
          await this.updateResult(result);
        }
        
      } catch (error) {
        await this.SET_RESULT_DIALOG(this.getResultMsgObj({
          msg: this.updateFail,
          color: 'error'
        }));
        this.$refs.searchForm.closeSnack();
      }
    },

    async updateContentEvent (item) {
      try {
        let result = await this.UPDATE_TODO({
          id: item.id,
          content: item.content,
          isDone: item.isDone,
          createAt: item.createAt,
          updateAt: item.updateAt
        });

        await this.updateResult(result);
      } catch (error) {
        await this.SET_RESULT_DIALOG(this.getResultMsgObj({
          msg: this.updateFail,
          color: 'error'
        }));
        this.$refs.searchForm.closeSnack();
      }
    },

    async onClickDeleteButton(item) {
      try {
        let result = await this.DELETE_TODO({id: item.id});

        await this.SET_RESULT_DIALOG(this.getResultMsgObj({
          msg: result ? this.deleteSuccess : this.deleteFail,
          color: result ? 'success' : 'error'
        }));
        this.$refs.searchForm.closeSnack();
      } catch (error) {
        await this.SET_RESULT_DIALOG(this.getResultMsgObj({
          msg: this.deleteFail,
          color: 'error'
        }));
        this.$refs.searchForm.closeSnack();
      }

    },

    isSelfId(idArray, selfId) {
      let isValid = true;
      let msg = '';
      let foundIndex = -1;

      for(let i = 0; i < idArray.length; i++) {
        if(selfId === parseInt(idArray[i])) {
          isValid = false;
          msg = `참조 할 수 없는 ID 입니다. 선택한 Todo ID: ${idArray[i]} - Self ID: ${selfId}`;
          foundIndex = i;
          break;
        }
      }

      return {isValid, msg, foundIndex}
    },

    async onBlurSelectBox(item) {

      try {

        if(!item.referenceTodo && !this.referenceTodoValue[item.id]) {
          return;
        } 

        const {isValid, msg, foundIndex} = await this.isSelfId(this.referenceTodoValue[item.id], item.id);

        if(isValid) {
          item.referenceTodo = this.referenceTodoValue[item.id].join(',');  
          
          let result = await this.UPDATE_TODO({
            id: item.id,
            content: item.content,
            isDone: item.isDone,
            createAt: item.createAt,
            updateAt: item.updateAt
          });

          await this.updateResult(result);
          
        } else {
          this.referenceTodoValue[item.id].splice(foundIndex, 1);
          await this.SET_RESULT_DIALOG(this.getResultMsgObj({
            msg: msg,
            color: 'error'
          }));
          this.$refs.searchForm.closeSnack();  
        }
      } catch (error) {
        await this.SET_RESULT_DIALOG(this.getResultMsgObj({
          msg: this.updateFail,
          color: 'error'
        }));
        this.$refs.searchForm.closeSnack();
      }

    },
  }
};
</script>
