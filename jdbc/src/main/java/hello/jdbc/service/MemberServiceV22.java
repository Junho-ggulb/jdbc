package hello.jdbc.service;


import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*트랜잭션 - 파라미터 연동, 풀을 고려한 종료*/
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV22 {
	
	private final MemberRepositoryV2 memberRepositoryV2;
	private final DataSource dataSource;
	
	public void accountTrasnfer(String fromId, String toId, int money) throws SQLException {
		Connection con = dataSource.getConnection();
		try {
			log.info("con1={}", con);
			con.setAutoCommit(false);//트랜잭션 시작
			// 비즈니스 로직

			bizLogic(con, fromId, toId, money);
			
			con.commit(); //성공시 커밋
			//commit or rollback
		} catch (Exception e) {
			log.info("con3={}", con);
			log.info("rollback!");
			con.rollback(); //실패시 롤백
			throw new IllegalStateException(e);
		}finally {
			release(con);
		}
		
	}

	private void bizLogic(Connection con,String fromId, String toId, int money) throws SQLException {
		log.info("con2={}", con);
		//시작
		Member fromMember = memberRepositoryV2.findById(con,fromId);
		Member toMember = memberRepositoryV2.findById(con,toId);
		
		memberRepositoryV2.update(con,fromId, fromMember.getMoney() - money);
		validation(toMember);
		
		memberRepositoryV2.update(con,toId, toMember.getMoney() + money);
	}

	private void release(Connection con) {
		if(con != null) {
			try {
				con.setAutoCommit(true);
				con.close();
			} catch (Exception e2) {
				log.info("error",e2);
			}
		}
	}

	private void validation(Member toMember) {
		if(toMember.getMemberId().equals("ex")) {
			throw new IllegalStateException("이체중 예외 발생");
		}
	}

}
