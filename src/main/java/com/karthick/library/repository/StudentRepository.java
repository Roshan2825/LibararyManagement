package com.karthick.library.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.karthick.library.config.DBConnectionManager;
import com.karthick.library.model.Student;

public class StudentRepository {

    public void addStudent(Student student) {
       String sql = "INSERT INTO students (name, email) VALUES (?, ?)";
       try(Connection conn= DBConnectionManager.getDataSource().getConnection();
           PreparedStatement pstmt=conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
           
           pstmt.setString(1,student.getName());
           pstmt.setString(2,student.getEmail());
           pstmt.executeUpdate();
           try(ResultSet rs = pstmt.getGeneratedKeys()) {
               if(rs.next()) {
                   student.setId(rs.getInt(1));
               }
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
    }

    public Student getStudentById(int id) throws Exception {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection connection = DBConnectionManager.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new Exception("Database error occurred while retrieving student.");
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection connection = DBConnectionManager.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Student student = new Student(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email")
                );
                students.add(student);
            }
            return students;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(); 
    }

    public void updateStudent(Student student){
        String sql = "UPDATE students SET name = ?, email = ? WHERE id = ?";
        try (Connection connection = DBConnectionManager.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getEmail());
            preparedStatement.setInt(3, student.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection connection = DBConnectionManager.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
