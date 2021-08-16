package com.example.school.service.impl;

import com.example.school.exception.BadRequestException;
import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;
import com.example.school.service.StudentService;
import io.reactivex.Completable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Single<String> newStudent(Student student) {
        return Single.create(singleSubscriber -> {
            Optional<Student> optionalStudent = studentRepository.findById(student.getId());
            if (optionalStudent.isPresent())
                singleSubscriber.onError(new BadRequestException());
            else {
                String newStudent = studentRepository.save(student).getId();
                singleSubscriber.onSuccess(newStudent);
            }
        });
    }

    @Override
    public Completable updateStudent(Student studentData) {
        return Completable.create(completableSubscriber -> {
            Optional<Student> optionalStudent = studentRepository.findById(studentData.getId());
            if (!optionalStudent.isPresent())
                completableSubscriber.onError(new EntityNotFoundException());
            else {
                Student student = optionalStudent.get();
                student.setName(studentData.getName());
                student.setActive(studentData.isActive());
                studentRepository.save(student);
                completableSubscriber.onComplete();
            }
        });
    }

    @Override
    public Single<List<Student>> getAllStudents() {
        return Single.create(singleSubscriber -> {
            List<Student> students = studentRepository.findAll();
            List<Student> studentsListActives = students.stream().filter(studentResponse -> studentResponse.isActive()).collect(Collectors.toList());
            singleSubscriber.onSuccess(studentsListActives);
        });

    }

    @Override
    public Single<Student> getStudentDetail(String id) {
        return Single.create(singleSubscriber -> {
            Optional<Student> optionalStudent = studentRepository.findById(id);
            if (!optionalStudent.isPresent())
                singleSubscriber.onError(new EntityNotFoundException());
            else {
                singleSubscriber.onSuccess(optionalStudent.get());
            }
        });
    }

    @Override
    public Completable deleteStudent(String id) {
        return Completable.create(completableSubscriber -> {
            Optional<Student> optionalStudent = studentRepository.findById(id);
            if (!optionalStudent.isPresent())
                completableSubscriber.onError(new EntityNotFoundException());
            else {
                studentRepository.delete(optionalStudent.get());
                completableSubscriber.onComplete();
            }
        });
    }

}
