package com.zangbe.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TodoServiceImpl implements TodoService {

    @Autowired
    private BoardRepository repo;

    @Override
    public List<BoardSummary> getAllBoardsOnlyIdAndIsDone() {

        List<BoardSummary> allBoards = repo.findAllByIdGreaterThan(0L);

        return allBoards;
    }

    @Override
    public Page<Board> getBoards(int page, int pageSize, String SortBy, Boolean isDesc) {

        Sort.Direction orderBy = isDesc == true ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, pageSize, orderBy, SortBy);

        Page<Board> results = repo.findAll(pageable);

        return results;
    }

    @Override
    public Page<Board> getBoardsBySearchValue(int page, int pageSize, String SortBy, Boolean isDesc, String searchCondition, String searchValue) {

        Sort.Direction orderBy = isDesc == true ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, pageSize, orderBy, SortBy);

        Page<Board> results = null;

        switch (SearchType.valueOf(searchCondition.toUpperCase())) {

            case CONTENT:
                results = repo.findBoardByContentContaining(pageable, searchValue);
                break;

            case ISDONE:
                results = repo.findBoardByIsDoneEquals(pageable, Boolean.parseBoolean(searchValue));
                break;

            case CREATEAT:
            case UPDATEAT:

                Timestamp startDate = Timestamp.valueOf(searchValue.concat(" 00:00:00"));
                Timestamp endDate = Timestamp.valueOf(searchValue.concat(" 23:59:59"));

                if(searchCondition.equals("createAt")) {
                    results = repo.findBoardByCreateAtBetween(pageable, startDate, endDate);
                } else {
                    results = repo.findBoardByUpdateAtBetween(pageable, startDate, endDate);
                }

                break;
        }

        return results;
    }

    @Override
    public Boolean createBoard(Board board) {

        Boolean isSuccess = false;

        long newId = repo.save(board).getId();

        Optional<Board> result = repo.findById(newId);

        if(result.isPresent()) {
            isSuccess = true;
        }

        return isSuccess;
    }

    @Override
    public Boolean deleteBoard(Long id) {

        repo.deleteById(id);

        Boolean result = repo.existsById(id);

        return result;
    }

    @Override
    public Boolean updateBoard(Board board) {

        Boolean isSuccess = false;

        Optional<Board> result = repo.findById(board.getId());

        if(result.isPresent()) {
            repo.save(board);
            isSuccess = true;
        }

        return isSuccess;
    }
}
