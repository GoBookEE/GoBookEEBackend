package com.gobookee.study.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
public class StudyDao {
	
	private Properties sql = new Properties();
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private static final StudyDao studyDao = new StudyDao();
	
	private StudyDao() {
		
	}
	
	
	
	
}
