package com.example.school.service;

import com.example.school.model.Student;
import io.reactivex.Completable;
import io.reactivex.Single;

import java.util.List;

public interface StudentService {
    Single<String> newStudent(Student student);

    Completable updateStudent(Student student);

    Single<List<Student>> getAllStudents();

    Single<Student> getStudentDetail(String id);

    Completable deleteStudent(String id);
}
