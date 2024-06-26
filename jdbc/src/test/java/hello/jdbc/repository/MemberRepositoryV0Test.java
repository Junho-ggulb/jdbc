package hello.jdbc.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class MemberRepositoryV0Test {
	
	MemberRepositoryV0 repository = new MemberRepositoryV0();

	@Test
	void crud() throws SQLException {
		
		//save
		Member member = new Member("memberV2", 10000);
		repository.save(member);
		
		//findById
		Member findMember = repository.findById(member.getMemberId());
		log.info("findMember={}", findMember);
		assertThat(findMember).isEqualTo(member);
		
		//update: money: 10000 -> 20000
		repository.update(member.getMemberId(),20000);
		Member updatedMember = repository.findById(member.getMemberId());
		assertThat(updatedMember.getMoney()).isEqualTo(20000);
		
		//delete
		repository.delete(member.getMemberId());
		//exception 검증
		assertThatThrownBy(() -> repository.findById(member.getMemberId())).isInstanceOf(NoSuchElementException.class);
	}
	

}
