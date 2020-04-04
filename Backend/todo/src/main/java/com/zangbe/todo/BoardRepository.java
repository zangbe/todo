package com.zangbe.todo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface BoardRepository extends CrudRepository<Board, Long> {

    Page<Board> findAll(Pageable pageable);

    List<BoardSummary> findAllByIdGreaterThan(Long id);

    Page<Board> findBoardByContentContaining(Pageable pageable, String content);

    Page<Board> findBoardByIsDoneEquals(Pageable pageable, Boolean isDone);

    Page<Board> findBoardByCreateAtBetween(Pageable pageable, Timestamp startDate, Timestamp endDate);

    Page<Board> findBoardByUpdateAtBetween(Pageable pageable, Timestamp startDate, Timestamp endDate);
}
