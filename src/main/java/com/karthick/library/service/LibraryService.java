package com.karthick.library.service;

import java.time.LocalDate;
import java.util.List;

import com.karthick.library.model.Book;
import com.karthick.library.model.BorrowRecord;
import com.karthick.library.model.Student;
import com.karthick.library.repository.BookRepository;
import com.karthick.library.repository.BorrowRecordRepository;
import com.karthick.library.repository.StudentRepository;

public class LibraryService {
    private StudentRepository studentRepository= new StudentRepository();
    private BookRepository bookRepository = new BookRepository();
    private BorrowRecordRepository borrowRecordRepository = new BorrowRecordRepository();

    //student operations
    public void addStudent(Student student) {
        studentRepository.addStudent(student);
    }
    public Student getStudentById(int id) throws Exception {
        return studentRepository.getStudentById(id);
    }
    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }
    public void updateStudent(Student updatedStudent) throws Exception {
        studentRepository.updateStudent(updatedStudent);
    }
    public void deleteStudent(int id) throws Exception {
        studentRepository.deleteStudent(id);
    }

    //book operations
    public void addBook(Book book) {
        bookRepository.addBook(book);
    }
    public Book getBookById(int id) throws Exception {
        return bookRepository.getBookById(id);
    }
    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }
    public void updateBook(Book updatedBook) throws Exception {
        bookRepository.updateBook(updatedBook);
    }
    public void deleteBook(int id) throws Exception {
        bookRepository.deleteBook(id);
    }

    //borrowRecord operations
    public void borrowBook(int studentId, int bookId) {
        try {
            //Check student exists
            Student student = studentRepository.getStudentById(studentId);
            if (student == null) {
                System.out.println("Student not found!");
                return;
            }

            //Check book exists
            Book book = bookRepository.getBookById(bookId);
            if (book == null) {
                System.out.println("Book not found!");
                return;
            }

            //Check availability
            if (book.getQuantity() <= 0) {
                System.out.println("Book is not available!");
                return;
            }

            // Create borrow record
            BorrowRecord record = new BorrowRecord(student, book);
            borrowRecordRepository.addBorrowRecord(record);

            //Decrease quantity
            book.setQuantity(book.getQuantity() - 1);
            bookRepository.updateBook(book);

            System.out.println("Book borrowed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   public void returnBook(int borrowId) {
    try {
        BorrowRecord record = borrowRecordRepository.getBorrowRecordById(borrowId);
        if (record == null) {
            System.out.println("Borrow record not found!");
            return;
        }

        if (record.isReturned()) {
            System.out.println("Book has already been returned!");
            return;
        }

        // 1. Mark as returned
        LocalDate today = LocalDate.now();
        record.markAsReturned(today);
        borrowRecordRepository.updateReturnDate(borrowId, today);

        // 2. Increment book quantity
        Book book = record.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.updateBook(book);

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordRepository.getAllBorrowRecords();
    }

    public List<BorrowRecord> getBorrowRecordsOfStudent(int id) {
        return borrowRecordRepository.getAllBorrowRecordsOfStudent(id);
    }

    public List<BorrowRecord> getActiveBorrowRecords() {
        return borrowRecordRepository.getActiveBorrowRecords();
    }

    public List<BorrowRecord> getOverdueBorrowRecords() {
        return borrowRecordRepository.getOverdueBorrowRecords();
    }

}
