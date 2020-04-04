package com.zangbe;

import com.zangbe.todo.TodoApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoApplication.class)
class TodoApplicationTests {

//	@Autowired
//	private BoardRepository repo;

	@Test
	void contextLoads() {
	}

//	데이터 저장 test
//	@Test
//	public void testInsert() {
//		for(int i = 1; i <=100; i++) {
//			Board board = new Board();
//			board.setContent("Todo " + i);
//			board.setIsDone(false);
//			board.setReferenceTodo("");
//
//			repo.save(board);
//		}
//	}
}
