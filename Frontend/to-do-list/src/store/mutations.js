const mutations = {
    SET_RESULT_DIALOG(state, payload) {
        state.snack = true
        state.snackColor = payload.color;
        state.snackText = payload.msg;
    },

    SET_SNACK(state, payload) {
        state.snack = payload;
    },

    SET_TODOS(state, payload) {
        
        if(payload.status === 200) {
            payload.data.pagingList.content.forEach((e) => {
                if (e.referenceTodo) {
                    state.referenceTodoValue[e.id] = e.referenceTodo.split(',');
                }
            });

            state.todoList = payload.data.pagingList.content;
            state.totalItems = payload.data.pagingList.totalElements;
        
            payload.data.allBoards.forEach((e) => {
                if (e.referenceTodo) {
                    e.referenceTodo = e.referenceTodo.split(',');
                }
            });
        
            state.allTodoList = payload.data.allBoards;
        
            state.referenceTodoList = [];
        
            payload.data.allBoards.forEach((e) => {
                let obj = {};
                obj.text = `ID: ${e.id}`;
                obj.value = e.id.toString();
                state.referenceTodoList.push(obj);
            });
        }
    },

    SET_FILTER(state, payload) {
        if(payload) {
            state.isFiltered = payload.isFiltered;
            state.latestSearchConditionValue = payload.searchConditionValue;
            state.latestSearchValue = payload.searchValue;
            state.page = 1;
        } else {
            state.isFiltered = false;
        }
    },

    SET_PAGING_VALUE(state, payload) {
        state.page = payload.page;
        state.sortBy = payload.sortBy;
        state.sortDesc = payload.sortDesc;
    },
}

export default mutations 