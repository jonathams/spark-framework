package app.services;

import static app.jooq.Tables.BOOKS;

import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;
import org.junit.jupiter.api.Test;

class BookServiceTest {

	@Test
	void test() {
		MockDataProvider provider = new MyProvider();
		MockConnection conn = new MockConnection(provider);
		BookService bookService = new BookServiceImpl();
		bookService.setConn(conn);;
		
		bookService.getAll();
		
	}

	public class MyProvider implements MockDataProvider{

		@Override
		public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
			
			DSLContext dslContext = DSL.using(SQLDialect.MYSQL);
			MockResult[] mock = new MockResult[1];
			
			String  sql = ctx.sql();
			
			if(sql.toUpperCase().startsWith("SELECT")) {
				Result<Record3<Integer, Integer, String>>  result = dslContext.newResult(BOOKS.ISBN, BOOKS.AUTHOR_ID, BOOKS.TITLE);
				result.add(dslContext.newRecord(BOOKS.ISBN, BOOKS.AUTHOR_ID, BOOKS.TITLE).values(998392839, 1, "IT"));
				mock[0] = new MockResult(1,result);
			}
			
			return mock;
		}
		
	}
}
