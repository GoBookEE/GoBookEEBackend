package com.gobookee.book.model.dao;

import com.gobookee.book.model.dto.BookCategory;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.gobookee.common.JDBCTemplate.close;

public class BookCategoryDao {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Properties sqlProp = new Properties();

    private static BookCategoryDao dao;
    public static BookCategoryDao bookCategoryDao() {
        if (dao == null) { dao = new BookCategoryDao(); }
        return dao;
    }
    private BookCategoryDao() {
        String path = BookDao.class.getResource("/config/book-sql.properties").getPath();
        try(FileReader fr = new FileReader(path)){
            sqlProp.load(fr);
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public List<BookCategory>getCategoryList(Connection conn,int cid, int level) {
        List<BookCategory> categories = new ArrayList<BookCategory>();
        try{
            String sql = sqlProp.getProperty("getCategoryList");
            switch (level){
                case 1: sql = sql.replace("${whereColumn}","BC_DEPT = 1"); break;
                case 2: sql = sql.replace("${whereColumn}","BC_DEPT = 2 AND BC_DEPT1_CID ="+cid); break;
                case 3: sql = sql.replace("${whereColumn}","BC_DEPT = 3 AND BC_DEPT2_CID ="+cid); break;
                case 4: sql = sql.replace("${whereColumn}","BC_DEPT = 4 AND BC_DEPT3_CID ="+cid); break;
                case 5: sql = sql.replace("${whereColumn}","BC_DEPT = 5 AND BC_DEPT4_CID ="+cid); break;
            }
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                categories.add(getCategory(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(rs);
            close(pstmt);
        }
        return categories;
    }

    public BookCategory getCategory(ResultSet rs) throws SQLException {
        new BookCategory();
        return BookCategory.builder()
            .bcCid(rs.getInt("BC_CID"))
            .bcDept(rs.getInt("BC_DEPT"))
            .bcCidName(rs.getString("BC_CID_NAME"))
            .bcCountry(rs.getString("BC_COUNTRY"))
            .bcDept1Cid(rs.getInt("BC_DEPT1_CID"))
            .bcDept1Name(rs.getString("BC_DEPT1_NAME"))
            .bcDept2Cid(rs.getInt("BC_DEPT2_CID"))
            .bcDept2Name(rs.getString("BC_DEPT2_NAME"))
            .bcDept3Cid(rs.getInt("BC_DEPT3_CID"))
            .bcDept3Name(rs.getString("BC_DEPT3_NAME"))
            .bcDept4Cid(rs.getInt("BC_DEPT4_CID"))
            .bcDept4Name(rs.getString("BC_DEPT4_NAME"))
            .bcDept5Cid(rs.getInt("BC_DEPT5_CID"))
            .bcDept5Name(rs.getString("BC_DEPT5_NAME"))
            .build();

    }
}
