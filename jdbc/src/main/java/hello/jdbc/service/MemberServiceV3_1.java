package hello.jdbc.service;


import java.sql.SQLException;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*트랜잭션 - 트랜잭션 매니저*/
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {
	
	private final MemberRepositoryV3 memberRepositoryV3;
	private final PlatformTransactionManager transactionManager;
	
	public void accountTrasnfer(String fromId, String toId, int money) throws SQLException {
		//트랜잭션 시작
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			
			// 비즈니스 로직
			bizLogic(fromId, toId, money);
			transactionManager.commit(status);
			
			//commit or rollback
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw new IllegalStateException(e);
		}
		
	}

	private void bizLogic(String fromId, String toId, int money) throws SQLException {
		//시작
		Member fromMember = memberRepositoryV3.findById(fromId);
		Member toMember = memberRepositoryV3.findById(toId);
		
		memberRepositoryV3.update(fromId, fromMember.getMoney() - money);
		validation(toMember);
		
		memberRepositoryV3.update(toId, toMember.getMoney() + money);
	}

	private void validation(Member toMember) {
		if(toMember.getMemberId().equals("ex")) {
			throw new IllegalStateException("이체중 예외 발생");
		}
	}

}
