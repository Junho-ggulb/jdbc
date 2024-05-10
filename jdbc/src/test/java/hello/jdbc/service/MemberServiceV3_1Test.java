package hello.jdbc.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import hello.jdbc.connection.ConnectionConst;
import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import hello.jdbc.repository.MemberRepositoryV3;



/*
 * 
 * 트랜잭션 - 커넥션 파라미터 전달 방식 동기화
 * */


@SpringBootTest
class MemberServiceV3_1Test {
	
	public static final String MEMBER_A = "memberA";
	public static final String MEMBER_B = "memberB";
	public static final String MEMBER_EX = "ex";
	
	private MemberRepositoryV3 repository;
	private MemberServiceV3_1 service;
	
	@BeforeEach
	void before() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWROD);
		
		repository = new MemberRepositoryV3(dataSource);
		service = new MemberServiceV3_1(repository, new  DataSourceTransactionManager(dataSource));
	}
	
	@AfterEach
	void after() throws SQLException {
		repository.delete(MEMBER_A);
		repository.delete(MEMBER_B);
		repository.delete(MEMBER_EX);
	}
	
	
	@Test
	@DisplayName("정상 이체")
	void accountTrasnferTest() throws SQLException {
		//given
		Member memberA = new Member(MEMBER_A, 10000);
		Member memberB = new Member(MEMBER_B, 10000);
		repository.save(memberA);
		repository.save(memberB);
		
		//when
		service.accountTrasnfer(memberA.getMemberId(), memberB.getMemberId(), 2000);
		//then
		Member findMemberA = repository.findById(memberA.getMemberId());
		Member findMemberB = repository.findById(memberB.getMemberId());
		assertThat(findMemberA.getMoney()).isEqualTo(8000);
		assertThat(findMemberB.getMoney()).isEqualTo(12000);
	}
	
	@Test
	@DisplayName("이체중 예외 발생")
	void accountTrasnferTest2() throws SQLException {
		//given
		Member memberA = new Member(MEMBER_A, 10000);
		Member memberB = new Member(MEMBER_EX, 10000);
		repository.save(memberA);
		repository.save(memberB);
		
		//when
		assertThatThrownBy(() -> service.accountTrasnfer(memberA.getMemberId(), memberB.getMemberId(), 2000)).isInstanceOf(IllegalStateException.class);
		
		//then
		Member findMemberA = repository.findById(memberA.getMemberId());
		Member findMemberB = repository.findById(memberB.getMemberId());
		assertThat(findMemberA.getMoney()).isEqualTo(10000);
		assertThat(findMemberB.getMoney()).isEqualTo(10000);
		
	}

}
