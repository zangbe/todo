package com.zangbe.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private BoardRepository repo;

    @GetMapping("/todos")
    public Map<String, Object> todos(
            @RequestParam(value="page") int page,
            @RequestParam(value="pageSize") int pageSize,
            @RequestParam(value="sortBy") String sortBy,
            @RequestParam(value="sortDesc") Boolean isDesc,
            @RequestParam(value="searchCondition", required = false, defaultValue = "") String searchCondition,
            @RequestParam(value="searchValue", required = false) String searchValue) {

        Page<Board> pagingBoards = null;

        if(searchCondition.isEmpty()) {
            pagingBoards = todoService.getBoards(page, pageSize, sortBy, isDesc);
        } else {
            pagingBoards = todoService.getBoardsBySearchValue(page, pageSize, sortBy, isDesc, searchCondition, searchValue);
        }

        List<BoardSummary> allBoards = todoService.getAllBoardsOnlyIdAndIsDone();

        Map<String, Object> map = new HashMap<>();

        map.put("pagingList", pagingBoards);
        map.put("allBoards", allBoards);

        return map;
    }

    @PostMapping("/todo")
    public Boolean todoPost(@RequestBody Board board) {
        Boolean result = todoService.createBoard(board);

        return result;
    }

    @DeleteMapping("/todo")
    public Boolean todo(@RequestParam(value="id") Long id) {

        Boolean result = todoService.deleteBoard(id);

        return result;
    }

    @PatchMapping("/todo")
    public Boolean todoPatch(@RequestBody Board board) {

        Boolean isSuccess = todoService.updateBoard(board);

        return isSuccess;
    }

}
