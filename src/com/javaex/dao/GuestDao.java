package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.javaex.vo.GuestVo;


public class GuestDao {
	
	// 0. import java.sql.*;
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

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
	//방명록 등록
	public int addGuest(GuestVo guestVo) {
		int count = 0;
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " insert into guestbook ";
			query += " vlaues (seq_no.nextval, ?, ?, ?, TO_DATE(SYSDATE)) ";
			// System.out.println(query);

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setString(1, guestVo.getName()); 
			pstmt.setString(2, guestVo.getPassword());
			pstmt.setString(3, guestVo.getContent());

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}
	
	//방명록 리스트
	public List<GuestVo> getGuestList(){
		List<GuestVo> guestList = new ArrayList<GuestVo>();
		
		getConnection();
		
		try {
				// 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
				String query = "";
				query += " select  no, ";
				query += "         name, ";
				query += "         content, ";
				query += "         reg_date ";
				query += " from guestbook";
				
				pstmt = conn.prepareStatement(query);
				
				
				rs = pstmt.executeQuery();

				// 4.결과처리
				while (rs.next()) {
					int no = rs.getInt("no");
					String name = rs.getString("name");
					String password = rs.getString("password");
					String content = rs.getString("content");
					String date = rs.getString("reg_date");

					GuestVo GuestVo = new GuestVo(no, name, password, content, date);
					guestList.add(GuestVo);
				}

			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

			close();

			return guestList;

	}
	
	// 사람 삭제
	public int guestDelete(int no, String password) {
		int count = 0;
		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " delete gusetbook ";
			query += " where no = ? ";
			query += " and password = ? ";
			
			pstmt = conn.prepareStatement(query); // 쿼리로 만들기
			pstmt.setInt(1, no);
			pstmt.setString(2, password);

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}
}

