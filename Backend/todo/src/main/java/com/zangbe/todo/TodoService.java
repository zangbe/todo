package com.zangbe.todo;

import org.springframework.data.domain.Page;

import java.util.List;

public interface TodoService {

    List<BoardSummary> getAllBoardsOnlyIdAndIsDone();

    Page<Board> getBoards(int page, int pageSize, String SortBy, Boolean isDesc);

    Page<Board> getBoardsBySearchValue(int page, int pageSize, String SortBy, Boolean isDesc, String searchCondition, String searchValue);

    Boolean createBoard(Board board);

    Boolean deleteBoard(Long id);

    Boolean updateBoard(Board board);
}
