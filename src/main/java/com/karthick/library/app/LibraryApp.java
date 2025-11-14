package com.karthick.library.app;

import com.karthick.library.model.Book;
import com.karthick.library.model.Student;
import com.karthick.library.repository.BookRepository;
import com.karthick.library.repository.StudentRepository;


public class LibraryApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Library Management System");
        StudentRepository studentRepo = new StudentRepository();

       try {
            Student student = studentRepo.getStudentById(2);
            student.setEmail("sreedhar.karthick2825@gmail.com");
            student.setName("Karthick Roshan");
            studentRepo.updateStudent(student);

            for(Student st:studentRepo.getAllStudents()){
                System.out.println(st);
            }
            
        } catch (Exception e) {
            e.printStackTrace();

         

        }
    }
}