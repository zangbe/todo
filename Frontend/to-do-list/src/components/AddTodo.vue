<template>
    <v-row>
        <v-col cols="6">
            <v-text-field class="mr-5" label="Todo Item" single-line v-model="newTodoValue"></v-text-field>
        </v-col>
        <v-col cols="2">
            <v-btn class="mt-4" color="primary" @click="onClickAddTodoButton">Add Todo</v-btn> 
        </v-col>
    </v-row>
</template>

<script>
import {mapMutations, mapActions, mapState, mapGetters} from 'vuex';

export default {
    name: 'AddTodo',

    data() {
        return {
            newTodoValue: '',
        }
    },

    computed: {
        ...mapState({
            createFail: 'createFail',
            createSuccess: 'createSuccess'
        }),
        ...mapGetters({
            getResultMsgObj: 'getResultMsgObj'
        })
    },

    methods: {

        ...mapMutations([
            'SET_RESULT_DIALOG',
        ]),

        ...mapActions([
            'CREATE_TODO'
        ]),

        async onClickAddTodoButton() {
            try {
                if(!this.newTodoValue) {
                    await this.SET_RESULT_DIALOG(this.getResultMsgObj({
                        msg: '내용을 입력하세요.',
                        color: 'warning'
                    }));
                    return;
                }

                let result = await this.CREATE_TODO({
                    content: this.newTodoValue,
                    referenceTodo: '',
                    isDone: 0
                });

                this.newTodoValue = '';

                await this.SET_RESULT_DIALOG(this.getResultMsgObj({
                    msg: result ? this.createSuccess : this.createFail,
                    color: result ? 'success' : 'error'
                }));
            } catch (error) {
                await this.SET_RESULT_DIALOG(this.getResultMsgObj({
                    msg: this.createFail,
                    color: 'error'
                }));
            }
        },
    },
}
</script>
