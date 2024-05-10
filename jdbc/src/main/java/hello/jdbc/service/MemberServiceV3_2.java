package hello.jdbc.service;

import java.sql.SQLException;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;

/*트랜잭션 - 트랜잭션 템플릿*/
@Slf4j
public class MemberServiceV3_2 {

	private final MemberRepositoryV3 memberRepositoryV3;
	private final TransactionTemplate txTemplate;

	public MemberServiceV3_2(MemberRepositoryV3 memberRepositoryV3, PlatformTransactionManager transactionManager) {
		this.memberRepositoryV3 = memberRepositoryV3;
		this.txTemplate = new TransactionTemplate(transactionManager);
	}

	public void accountTrasnfer(String fromId, String toId, int money) throws SQLException {
		// 트랜잭션 시작
		txTemplate.executeWithoutResult((status) -> {
			// 비즈니스 로직
			try {
				bizLogic(fromId, toId, money);
				// commit or rollback
			} catch (Exception e) {
				
				//언체크예외 rollback
				throw new IllegalStateException(e);
			}
		});
		

	}

	private void bizLogic(String fromId, String toId, int money) throws SQLException {
		// 시작
		Member fromMember = memberRepositoryV3.findById(fromId);
		Member toMember = memberRepositoryV3.findById(toId);

		memberRepositoryV3.update(fromId, fromMember.getMoney() - money);
		validation(toMember);

		memberRepositoryV3.update(toId, toMember.getMoney() + money);
	}

	private void validation(Member toMember) {
		if (toMember.getMemberId().equals("ex")) {
			throw new IllegalStateException("이체중 예외 발생");
		}
	}

}
