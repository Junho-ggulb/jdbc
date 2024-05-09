package hello.jdbc.connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DBConnectionUtilTest {

	@Test
	void connection() {
		Connection connection1 = DBConnectionUtil.getConnection();
		assertThat(connection1).isNotNull();
	}

}
