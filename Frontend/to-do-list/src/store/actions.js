import * as api from '../api'

const actions = {
    FETCH_TODOS({ commit }, { page, itemsPerPage, sortBy, sortDesc }) {
        return api.board.fetch({ page, itemsPerPage, sortBy, sortDesc }).then(data => {
            commit('SET_TODOS', data);
            return data;
        });
    },

    FETCH_TODOS_BY_SEARCH({ commit }, { searchValue, searchConditionValue, page, pageSize, sortBy, sortDesc }) {
        return api.board.fetchBySearchValue({ searchValue, searchConditionValue, page, pageSize, sortBy, sortDesc }).then(data => {
            commit('SET_TODOS', data);
            return data;
        });
    },

    CREATE_TODO({state, dispatch}, {content, referenceTodo, isDone} ) {
        return api.board.create({ content, referenceTodo, isDone }).then(data => {
            dispatch('FETCH_TODOS', {
                page: 1,
                itemsPerPage: state.itemsPerPage,
                sortBy: ['id'],
                sortDesc: [true],
            });

            return data;
        });
    },

    UPDATE_TODO({state, dispatch}, {id, content, isDone, createAt, updateAt}) {
        return api.board.update({ id, content, isDone, createAt, updateAt }).then(data => {
            dispatch('FETCH_TODOS', {
                page: 1,
                itemsPerPage: state.itemsPerPage,
                sortBy: ['id'],
                sortDesc: [true],
            });

            return data;
        });
    },

    DELETE_TODO({state, dispatch}, {id}) {
        return api.board.delete({id}).then(data => {
            dispatch('FETCH_TODOS', {
                page: 1,
                itemsPerPage: state.itemsPerPage,
                sortBy: ['id'],
                sortDesc: [true],
            });

            return data;
        });
    }
}

export default actions 