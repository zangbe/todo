const state = {
    createFail: '데이터 생성 실패',
    createSuccess: '데이터 생성 성공',
    readFail: '데이터 조회 실패',
    updateFail: '데이터 수정 실패',
    deleteFail: '데이터 삭제 실패',
    updateSuccess: '데이터 수정 성공',
    deleteSuccess: '데이터 삭제 완료',
    snack: false,
    snackColor: '',
    snackText: '',
    allTodoList: [],
    todoList: [],
    referenceTodoValue: {},
    referenceTodoList: [],
    totalItems: 0,
    itemsPerPage: 5,
    latestSearchConditionValue: '',
    latestSearchValue: '',
    isFiltered: false,
    sortBy: '',
    sortDesc: '',
    page: 0
}

export default state
