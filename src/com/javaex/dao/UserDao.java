package com.javaex.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import com.javaex.vo.UserVo;

public class UserDao {

	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";

	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);
			// System.out.println("접속성공");

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	public void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}
	
	public int insert(UserVo userVo) {
		int count = 0;
		
		getConnection();
		
		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query +=" insert into users ";
			query +=" value(seq_users_no.nextval, ";
			query +="             ?, ";
			query +="             ?, ";
			query +="             ?, ";
			query +="             ?, ";
			query +="   )  ";
			
			//쿼리로 만들기
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userVo.getId());
			pstmt.setString(2, userVo.getPassword());
			pstmt.setString(3, userVo.getName());
			pstmt.setString(4, userVo.getGender());
			
			count = pstmt.executeUpdate(); //쿼리문 실행
			
			
			// 4.결과처리
			System.out.println("insert" + count + "건 회원정보 저장");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}
	
	public UserVo getUser(String id, String pw) {
		UserVo userVo = null;
		getConnection();
		
		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query +=" select  no, ";
			query +="         name ";
			query +=" from users ";
			query +=" where id = ? ";
			query +=" and password = ? ";
			
			//쿼리로 만들기(번역)
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);

			
			rs = pstmt.executeQuery(); //쿼리문 실행
			
			
			// 4.결과처리
			while(rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");
				
				userVo = new UserVo(no, name);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		
		return userVo;
	}
	
	//사용자 정보 가져오기 (회원정보 수정시)
	public UserVo getUser(int no) {
		UserVo vo = null;
		
		getConnection();
		
		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query +=" select no, id, password, name, gender   ";
			query +=" from users ";
			query +=" where no = ? ";
			
			//쿼리로 만들기(번역)
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			
			//쿼리문 실행
			rs = pstmt.executeQuery();
		
			// 4.결과처리
			while (rs.next()) {
				int rNo = rs.getInt("no");
				String id = rs.getString("id");
				String password = rs.getString("password");
				String name = rs.getString("name");
				String gender = rs.getString("gender");
				
				vo = new UserVo(rNo, id, password, name, gender);
			}
			System.out.println("userVo.getUser(): " + vo.toString());
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return vo;
	}
	
	//회원정보 수정 업데이트
		public int update(UserVo vo) {
			int count = 0;
			getConnection();
			
			try {

				// 3. SQL문 준비 / 바인딩 / 실행
				String query = "";
				query +=" update users   ";
				query +=" set name = ? ";
				query +="     password = ? ";
				query +="     gender = ? ";
				query +=" where no = ? ";
				
				//쿼리로 만들기(번역)
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, vo.getName());
				pstmt.setString(2, vo.getPassword());
				pstmt.setString(3, vo.getGender());
				pstmt.setInt(4, vo.getNo());
				
				
				//쿼리문 실행
				count = pstmt.executeUpdate();
			
				// 4.결과처리
				System.out.println("userDao.update(): " + count + "건 회원정보 수정");
				
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
			close();
			return count;
		}
}