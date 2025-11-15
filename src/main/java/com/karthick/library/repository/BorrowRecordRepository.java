package com.karthick.library.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.karthick.library.config.DBConnectionManager;
import com.karthick.library.model.Book;
import com.karthick.library.model.BorrowRecord;
import com.karthick.library.model.Student;

public class BorrowRecordRepository {

    public void addBorrowRecord(BorrowRecord borrowRecord) {
        String sql="INSERT INTO borrow_records (student_id, book_id, borrow_date, due_date) VALUES (?, ?, ?, ?)";
        try(Connection conn=DBConnectionManager.getDataSource().getConnection();
            PreparedStatement pstmt=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, borrowRecord.getStudent().getId());
            pstmt.setInt(2, borrowRecord.getBook().getId());
            pstmt.setDate(3,java.sql.Date.valueOf(borrowRecord.getBorrowDate()));
            pstmt.setDate(4,java.sql.Date.valueOf(borrowRecord.getDueDate()));
            pstmt.executeUpdate();
            try(ResultSet rs=pstmt.getGeneratedKeys()) {
                if(rs.next()) {
                    borrowRecord.setId(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BorrowRecord getBorrowRecordById(int id){
        BorrowRecord borrowRecord = new BorrowRecord();   
        String sql = """
            SELECT 
                br.id AS borrow_id,
                br.borrow_date,
                br.due_date,
                br.return_date,
                s.id AS student_id,
                s.name AS student_name,
                s.email AS student_email,
                b.id AS book_id,
                b.title AS book_title,
                b.author AS book_author,
                b.quantity AS book_quantity
            FROM borrow_records br
            JOIN students s ON br.student_id = s.id
            JOIN books b ON br.book_id = b.id
            WHERE br.id = ?
            """;

        try(Connection conn=DBConnectionManager.getDataSource().getConnection();
            PreparedStatement pstmt=conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try(ResultSet rs=pstmt.executeQuery()) {
                if(rs.next()) {
                    return new BorrowRecord(
                        rs.getInt("borrow_id"),
                        new Student(
                            rs.getInt("student_id"),
                            rs.getString("student_name"),
                            rs.getString("student_email")
                        ),
                        new Book(
                            rs.getInt("book_id"),
                            rs.getString("book_title"),
                            rs.getString("book_author"),
                            rs.getInt("book_quantity")
                        ),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null
                    );
                }

            }
            return borrowRecord;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Database error occurred while retrieving borrow record.");
        }
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        List<BorrowRecord> list = new ArrayList<>();

        String sql = """
            SELECT 
                br.id AS borrow_id,
                br.borrow_date,
                br.due_date,
                br.return_date,
            
                s.id AS student_id,
                s.name AS student_name,
                s.email AS student_email,
            
                b.id AS book_id,
                b.title AS book_title,
                b.author AS book_author,
                b.quantity AS book_quantity
            FROM borrow_records br
            JOIN students s ON br.student_id = s.id
            JOIN books b ON br.book_id = b.id
        """;

        try (Connection conn = DBConnectionManager.getDataSource().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                Student student = new Student(
                    rs.getInt("student_id"),
                    rs.getString("student_name"),
                    rs.getString("student_email")
                );

                Book book = new Book(
                    rs.getInt("book_id"),
                    rs.getString("book_title"),
                    rs.getString("book_author"),
                    rs.getInt("book_quantity")
                );

                BorrowRecord record = new BorrowRecord(
                    rs.getInt("borrow_id"),
                    student,
                    book,
                    rs.getDate("borrow_date").toLocalDate(),
                    rs.getDate("due_date").toLocalDate(),
                    rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null
                );

                list.add(record);
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void updateReturnDate(int borrowId, LocalDate returnDate){
        String sql="UPDATE borrow_records SET return_date = ? WHERE id = ?";
        try(Connection conn=DBConnectionManager.getDataSource().getConnection();
            PreparedStatement pstmt=conn.prepareStatement(sql)) {
            pstmt.setDate(1,java.sql.Date.valueOf(returnDate));
            pstmt.setInt(2, borrowId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<BorrowRecord> getAllBorrowRecordsOfStudent(int studentId) {
        String sql="""
            SELECT
            br.id AS borrow_id,
            br.borrow_date,
            br.due_date,
            br.return_date,
            s.id AS student_id,
            s.name AS student_name,
            s.email AS student_email,
            b.id AS book_id,
            b.title AS book_title,
            b.author AS book_author,
            b.quantity AS book_quantity
            FROM borrow_records br
            JOIN students s 
            ON br.student_id = s.id
            JOIN books b 
            ON br.book_id = b.id
            WHERE br.student_id=?
            """;
        List<BorrowRecord> list = new ArrayList<>();   
        try(Connection conn=DBConnectionManager.getDataSource().getConnection();
            PreparedStatement pstmt=conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            try(ResultSet rs=pstmt.executeQuery()) {
                while(rs.next()) {
                    BorrowRecord record = new BorrowRecord(
                        rs.getInt("borrow_id"),
                        new Student(
                            rs.getInt("student_id"),
                            rs.getString("student_name"),
                            rs.getString("student_email")
                        ),
                        new Book(
                            rs.getInt("book_id"),
                            rs.getString("book_title"),
                            rs.getString("book_author"),
                            rs.getInt("book_quantity")
                        ),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null
                    );
                    list.add(record);
                }
                return list;
            }
            }catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
    }

    public List<BorrowRecord> getActiveBorrowRecords(){
        List<BorrowRecord> record=new ArrayList<>();
        String sql="""
                 SELECT
                    br.id AS borrow_id,
                    br.borrow_date,
                    br.due_date,
                    br.return_date,
                    s.id AS student_id,
                    s.name AS student_name,
                    s.email AS student_email,
                    b.id AS book_id,
                    b.title AS book_title,
                    b.author AS book_author,
                    b.quantity AS book_quantity
                FROM borrow_records br
                JOIN students s 
                ON br.student_id = s.id
                JOIN books b 
                ON br.book_id = b.id
                WHERE br.return_date IS NULL 

                """;
                try(Connection conn=DBConnectionManager.getDataSource().getConnection();
                    PreparedStatement pstmt=conn.prepareStatement(sql);
                    ResultSet rs=pstmt.executeQuery()) {    
                        while(rs.next()) {
                            BorrowRecord record1 = new BorrowRecord(
                                rs.getInt("borrow_id"),
                                new Student(
                                    rs.getInt("student_id"),
                                    rs.getString("student_name"),
                                    rs.getString("student_email")
                                ),
                                new Book(
                                    rs.getInt("book_id"),
                                    rs.getString("book_title"),
                                    rs.getString("book_author"),
                                    rs.getInt("book_quantity")
                                ),
                                rs.getDate("borrow_date").toLocalDate(),
                                rs.getDate("due_date").toLocalDate(),
                                rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null);
                            record.add(record1);
                        }
                        return record;
                }catch (Exception e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
    }

    public void deleteBorrowRecord(int id) {
    String sql = "DELETE FROM borrow_records WHERE id = ?";

    try (Connection conn = DBConnectionManager.getDataSource().getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, id);
        pstmt.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    public List<BorrowRecord> getOverdueBorrowRecords(){
        List<BorrowRecord> record=new ArrayList<>();
        String sql="""
                 SELECT
                    br.id AS borrow_id,
                    br.borrow_date,
                    br.due_date,
                    br.return_date,
                    s.id AS student_id,
                    s.name AS student_name,
                    s.email AS student_email,
                    b.id AS book_id,
                    b.title AS book_title,
                    b.author AS book_author,
                    b.quantity AS book_quantity
                FROM borrow_records br
                JOIN students s 
                ON br.student_id = s.id
                JOIN books b 
                ON br.book_id = b.id
                WHERE br.due_date < ? AND br.return_date IS NULL 

                """;
                try(Connection conn=DBConnectionManager.getDataSource().getConnection();
                    PreparedStatement pstmt=conn.prepareStatement(sql)) {
                        pstmt.setDate(1,java.sql.Date.valueOf(LocalDate.now()));
                        try(ResultSet rs=pstmt.executeQuery()) {    
                            while(rs.next()) {
                                BorrowRecord record1 = new BorrowRecord(
                                    rs.getInt("borrow_id"),
                                    new Student(
                                        rs.getInt("student_id"),
                                        rs.getString("student_name"),
                                        rs.getString("student_email")
                                    ),
                                    new Book(
                                        rs.getInt("book_id"),
                                        rs.getString("book_title"),
                                        rs.getString("book_author"),
                                        rs.getInt("book_quantity")
                                    ),
                                    rs.getDate("borrow_date").toLocalDate(),
                                    rs.getDate("due_date").toLocalDate(),
                                    rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null);
                                record.add(record1);
                            }
                            return record;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
    } 
}





