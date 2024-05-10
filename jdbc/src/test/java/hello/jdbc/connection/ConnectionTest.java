package hello.jdbc.connection;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ConnectionTest {
	
	

//	@Test
//	void driverManager() throws SQLException {
//		Connection con1 = DriverManager.getConnection(ConnectionConst.URL,ConnectionConst.USERNAME, ConnectionConst.PASSWROD);
//		Connection con2 = DriverManager.getConnection(ConnectionConst.URL,ConnectionConst.USERNAME, ConnectionConst.PASSWROD);
//		
//		log.info("connection={}, class={}", con1, con1.getClass());
//		log.info("connection={}, class={}", con2, con2.getClass());
//	}
//	
//	@Test
//	void dataSourceDriverManager() throws SQLException {
//		//DriverManagerDataSource - 항상 새로운 커넥션을 획득;
//		DriverManagerDataSource dataSource = new DriverManagerDataSource(ConnectionConst.URL,ConnectionConst.USERNAME, ConnectionConst.PASSWROD);
//		useDataSource(dataSource);
//	}
	
	@Test
	void dataSourceConnectionPool() throws SQLException, InterruptedException {
		//커넥션 풀링
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl(ConnectionConst.URL);
		dataSource.setUsername(ConnectionConst.USERNAME);
		dataSource.setPassword(ConnectionConst.PASSWROD);
		dataSource.setMaximumPoolSize(10);
		dataSource.setPoolName("MyPool");
		useDataSource(dataSource);
		Thread.sleep(1000);
		
	}
	
	private void useDataSource(DataSource dataSource) throws SQLException {
		Connection con1 = dataSource.getConnection();
		Connection con2 = dataSource.getConnection();
		
		log.info("connection={}, class={}", con1, con1.getClass());
		log.info("connection={}, class={}", con2, con2.getClass());
	}

}
