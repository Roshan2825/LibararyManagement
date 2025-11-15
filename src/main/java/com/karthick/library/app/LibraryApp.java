package com.karthick.library.app;

import java.util.Scanner;

import com.karthick.library.model.Book;
import com.karthick.library.model.Student;
import com.karthick.library.service.LibraryService;
public class LibraryApp {
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        LibraryService libraryService = new LibraryService();
        System.out.println("Welcome to the Library Management System");
        while(true){
            System.out.println("""
                1. Add Student
                2. View All Students
                3. Update Student
                4. Delete Student
                5. Add Book
                6. View All Books
                7. Update Book
                8. Delete Book
                9. Borrow Book
                10. Return Book
                11. View All Borrow Records
                12. View Borrow Records of a Student
                13. View Overdue Borrow Records
                0. Exit
            """);
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());
            
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Student Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter Student Email: ");
                    String email = scanner.nextLine();
                    Student student = new Student();
                    student.setName(name);
                    student.setEmail(email);

                    libraryService.addStudent(student);
                    System.out.println("Student added successfully!" );
                }
                case 2 -> {
                    System.out.println("All Students:");
                    for (Student s : libraryService.getAllStudents()) {
                        System.out.println(s);
                    }
                }
                case 3 -> {
                    System.out.print("Enter Student ID to update: ");
                    int id = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter new Student Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter new Student Email: ");
                    String email = scanner.nextLine();

                    Student updatedStudent = new Student();
                    updatedStudent.setId(id);
                    updatedStudent.setName(name);
                    updatedStudent.setEmail(email);

                    try {
                        libraryService.updateStudent(updatedStudent);
                        System.out.println("Student updated successfully!" );
                    } catch (Exception e) {
                        System.out.println("Error updating student: " + e.getMessage());
                    }
                }
                case 4 -> {
                    System.out.print("Enter Student ID to delete: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    try {
                        libraryService.deleteStudent(id);
                        System.out.println("Student deleted successfully!" );
                    } catch (Exception e) {
                        System.out.println("Error deleting student: " + e.getMessage());
                    }
                }
                case 5 -> {
                    System.out.print("Enter Book Title: ");
                    String title = scanner.nextLine();

                    System.out.print("Enter Book Author: ");
                    String author = scanner.nextLine();

                    System.out.print("Enter Book Quantity: ");
                    int quantity = Integer.parseInt(scanner.nextLine());

                    // Create and add book
                    Book book = new Book();
                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setQuantity(quantity);

                    libraryService.addBook(book);
                    System.out.println("Book added successfully!" );
                }
                case 6 -> {
                    System.out.println("All Books:");
                    for (Book b : libraryService.getAllBooks()) {
                        System.out.println(b);
                    }
                }
                case 7 -> {
                    System.out.print("Enter Book ID to update: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter new Book Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter new Book Author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter new Book Quantity: ");
                    int quantity = Integer.parseInt(scanner.nextLine());
                    Book updatedBook = new Book();
                    updatedBook.setId(id);
                    updatedBook.setTitle(title);
                    updatedBook.setAuthor(author);
                    updatedBook.setQuantity(quantity);
                    try {
                        libraryService.updateBook(updatedBook);
                        System.out.println("Book updated successfully!" );
                    } catch (Exception e) {
                        System.out.println("Error updating book: " + e.getMessage());
                    }
                }
                case 8 -> {
                    System.out.print("Enter Book ID to delete: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    try {
                        libraryService.deleteBook(id);
                        System.out.println("Book deleted successfully!" );
                    } catch (Exception e) {
                        System.out.println("Error deleting book: " + e.getMessage());
                    }
                }
                case 9 -> {
                    System.out.print("Enter Student ID: ");
                    int studentId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Book ID: ");
                    int bookId = Integer.parseInt(scanner.nextLine());
                    libraryService.borrowBook(studentId, bookId);
                }
                case 10 -> {
                    System.out.print("Enter Borrow Record ID to return: ");
                    int recordId = Integer.parseInt(scanner.nextLine());
                    libraryService.returnBook(recordId);    
                }
                case 11 -> {
                    System.out.println("All Borrow Records:");
                    for (var record : libraryService.getAllBorrowRecords()) {
                        System.out.println(record);
                    }
                }
                case 12 -> {
                    System.out.print("Enter Student ID to view borrow records: ");
                    int studentId = Integer.parseInt(scanner.nextLine());
                    System.out.println("Borrow Records for Student ID " + studentId + ":");
                    for (var record : libraryService.getBorrowRecordsOfStudent(studentId)) {
                        System.out.println(record);
                    }
                }
                case 13 -> {
                    System.out.println("Overdue Borrow Records:");
                    for (var record : libraryService.getOverdueBorrowRecords()) {
                        System.out.println(record);
                    }
                }
                case 0 -> {
                    System.out.println("Exiting... Goodbye!");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}