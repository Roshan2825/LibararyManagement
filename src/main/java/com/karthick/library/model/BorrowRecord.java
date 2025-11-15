package com.karthick.library.model;

import java.time.LocalDate;

public class BorrowRecord {
    private int id;
    private Student student;
    private Book book;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public BorrowRecord() {
    }
    public BorrowRecord(Student student,Book book) {
        this.student = student;
        this.book = book;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(14); 
        this.returnDate = null;
    }
    public BorrowRecord(int id, Student student, Book book, LocalDate borrowDate,LocalDate dueDate ,LocalDate returnDate) {
        this.id = id;
        this.student = student;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public Student getStudent() {return student;}
    public void setStudent(Student student) {this.student = student;}

    public Book getBook() {return book;}
    public void setBook(Book book) {this.book = book;}

    public LocalDate getBorrowDate() {return borrowDate;}
    public void setBorrowDate(LocalDate borrowDate) {this.borrowDate = borrowDate;}

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() {return returnDate;}
    public void setReturnDate(LocalDate returnDate) {this.returnDate = returnDate;}

    @Override
    public String toString() {
        return "BorrowRecord [id=" + id + ", studentId=" + student.getId() + ", bookId=" + book.getId() + ", borrowDate=" + borrowDate
                + ", returnDate=" + returnDate + "]";
    }

    public boolean isReturned() {
        return returnDate != null;
    }
    public void markAsReturned(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

}
