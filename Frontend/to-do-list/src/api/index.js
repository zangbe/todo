import Vue from 'vue'

export const board = {
    fetch(payload) {
        return Vue.prototype.$axios.get('/api/todos', {
            params: {
                page: payload.page - 1,
                pageSize: payload.itemsPerPage,
                sortBy: payload.sortBy.length === 0 ? 'id' : payload.sortBy[0],
                sortDesc: payload.sortDesc.length === 0 ? true : payload.sortDesc[0]
            }
        });
    },
    fetchBySearchValue(payload) {
        return Vue.prototype.$axios.get('/api/todos', {
            params: {
                searchValue: payload.searchValue,
                searchCondition: payload.searchConditionValue,
                page: payload.page - 1,
                pageSize: payload.pageSize,
                sortBy: payload.sortBy.length === 0 ? 'id' : payload.sortBy,
                sortDesc: payload.sortDesc.length === 0 ? true : payload.sortDesc
            }
        });
    },
    create(payload) {
        return Vue.prototype.$axios.post('/api/todo', payload);
    },
    update(payload) {
        return Vue.prototype.$axios.patch('/api/todo', payload);
    },
    delete(payload) {
        return Vue.prototype.$axios.delete('/api/todo', {
            params: {
                id: payload.id
              }
        });
    }
}