package com.karthick.library.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;


import com.karthick.library.config.DBConnectionManager;
import com.karthick.library.model.Book;

public class BookRepository {
    
    public void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, quantity) VALUES (?, ?, ?)";

        try (Connection connection = DBConnectionManager.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getQuantity());

            preparedStatement.executeUpdate();
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    book.setId(rs.getInt(1));
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Book getBookById(int id) throws Exception {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection connection = DBConnectionManager.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getInt("quantity")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new Exception("Database error occurred while retrieving book.");
    }

    public List<Book> getAllBooks(){
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection connection = DBConnectionManager.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet=preparedStatement.executeQuery()) {
                while(resultSet.next()){
                    Book book=new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getInt("quantity")
                    );
                    books.add(book);
                }
                return books;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();   
    }

    public void updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, quantity = ? WHERE id = ?";

        try (Connection connection = DBConnectionManager.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getQuantity());
            preparedStatement.setInt(4, book.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection connection = DBConnectionManager.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
