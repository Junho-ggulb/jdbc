package hello.jdbc.repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.batch.BatchProperties.Jdbc;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * JDBC - DataSource 사용, JdbcUtils 사용
 * 
 * */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepositoryV1 {
	
	private final DataSource dataSource;
	
	public Member save(Member member) throws SQLException {
		String sql = "insert into member(member_id,money) values(?, ?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			//? 파라미터 넣어줘야함
			pstmt.setString(1,member.getMemberId());
			pstmt.setInt(2,member.getMoney());
			pstmt.executeUpdate(); //db실행
			return member;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("db error", e.getMessage());
			throw e;
		} finally {
			//외부 리소스 사용중 닫아줘야함
//			pstmt.close(); //Exception 
//			con.close();
			close(con,pstmt,null);
		}
	}
	
	public Member findById(String memberId) throws SQLException {
		String sql = "select * from member where member_id = ?";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		 con = getConnection();
		 
		 try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberId);
			//조회
			rs = pstmt.executeQuery();
			//한번은 호출해줘야함 내부에 커서 있음 내려와서 있으면 트루 없으면 
			log.info("rs={}", rs);
			if(rs.next()) {
				Member member = new Member(rs.getString("member_id"),rs.getInt("money"));
				return member;
			} else {
				throw new NoSuchElementException("member not found memberId=" + memberId);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("db error", e.getMessage());
			throw e;
		} finally {
			close(con, pstmt, rs);
		}
		 
		
	}
	
	public void update(String memberId, int money) throws SQLException {
		String sql = "update member set money = ? where member_id=?";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		con = getConnection();
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			//? 파라미터 넣어줘야함
			pstmt.setInt(1,money);
			pstmt.setString(2,memberId);
			int resultSize = pstmt.executeUpdate(); //db실행
			log.info("resultSize={}", resultSize);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("db error", e.getMessage());
			throw e;
		} finally {
			//외부 리소스 사용중 닫아줘야함
//			pstmt.close(); //Exception 
//			con.close();
			close(con,pstmt,null);
		}
	}
	
	public void delete(String memberId) throws SQLException {
		String sql = "delete from member where member_id=?";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		con = getConnection();
		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			//? 파라미터 넣어줘야함
			pstmt.setString(1,memberId);
			int resultSize = pstmt.executeUpdate(); //db실행
			log.info("resultSize={}", resultSize);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("db error", e.getMessage());
			throw e;
		} finally {
			//외부 리소스 사용중 닫아줘야함
//			pstmt.close(); //Exception 
//			con.close();
			close(con,pstmt,null);
		}
	}
	
	
	//리소스 정리는 역순
	// stmt 그냥  sql 넣음
	// pstmt 파라미터 까지 넣어서 넣음
	private void close(Connection con,Statement stmt, ResultSet rs) {
		
		JdbcUtils.closeResultSet(rs);
		JdbcUtils.closeStatement(stmt);
		JdbcUtils.closeConnection(con);
		
	}

	private Connection getConnection() throws SQLException {
		Connection con = dataSource.getConnection();
		log.info("get connection={}, class={}", con, con.getClass());
		return con; 
	}
}
