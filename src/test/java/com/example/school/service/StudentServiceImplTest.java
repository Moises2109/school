package com.example.school.service;

import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;
import com.example.school.service.impl.StudentServiceImpl;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentServiceImpl studentService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldNewStudent() {
        when(studentRepository.save(any(Student.class)))
                .thenReturn(new Student("1", "David", true));

        studentService.newStudent(new Student("1", "David", true))
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue("1");

        verify(studentRepository, times(1)).save(any(Student.class));
    }

     @Test
    public void shouldUpdateStudent() {
        when(studentRepository.findById(anyString()))
                .thenReturn(Optional.of(new Student("1", "David", true)));
        when(studentRepository.save(any(Student.class)))
                .thenReturn(new Student("1", "David", true));

        studentService.updateStudent(new Student("1", "David", true))
                .test()
                .assertComplete()
                .assertNoErrors();

        verify(studentRepository, times(1)).findById(anyString());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    public void shouldFailUpdateStudent() {
        when(studentRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        studentService.updateStudent(new Student("1", "David", true))
                .test()
                .assertNotComplete()
                .assertError(EntityNotFoundException.class);

        verify(studentRepository, times(1)).findById(anyString());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    public void shouldGetAllStudents() {
        Student student1 = new Student("1", "David", true);
        Student student2 = new Student("2", "Marcos", true);
        Student student3 = new Student("3", "Maria", true);
        Student student4 = new Student("4", "Claudia", false);
        Student student5 = new Student("5", "Carolina", true);

        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);

        when(studentRepository.findAll())
                .thenReturn(students);

        TestObserver<List<Student>> testObserver =
                studentService.getAllStudents().test().assertComplete().assertNoErrors();

        testObserver.assertValue(studentResponse -> studentResponse.get(0).getId().equals("1") &&
                studentResponse.get(1).getId().equals("2") &&
                studentResponse.get(2).getId().equals("3") &&
                studentResponse.get(3).getId().equals("5"));

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    public void shouldGetAStudent() {
        Student student1 = new Student("1", "David", true);

        when(studentRepository.findById(anyString()))
                .thenReturn(Optional.of(student1));

        TestObserver<Student> testObserver = studentService.getStudentDetail("1").test();

        testObserver.assertValue(studentResponse -> studentResponse.getId().equals("1"));

        verify(studentRepository, times(1)).findById(anyString());
    }

    @Test
    public void notShouldGetAStudent() {
        when(studentRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        studentService.getStudentDetail("1")
                .test()
                .assertNotComplete()
                .assertError(EntityNotFoundException.class);

        verify(studentRepository, times(1)).findById(anyString());
    }

    @Test
    public void shouldDeleteStudent() {
        when(studentRepository.findById(anyString()))
                .thenReturn(Optional.of(new Student("1", "David", true)));
        doNothing().when(studentRepository).delete(any(Student.class));

        studentService.deleteStudent("1")
                .test()
                .assertComplete()
                .assertNoErrors();

       verify(studentRepository, times(1)).findById(anyString());
       verify(studentRepository, times(1)).delete(any(Student.class));
    }

    @Test
    public void notShouldDeleteStudent() {
        when(studentRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        studentService.deleteStudent("1")
                .test()
                .assertNotComplete()
                .assertError(EntityNotFoundException.class);

        verify(studentRepository, times(1)).findById(anyString());
        verify(studentRepository, never()).delete(any(Student.class));
    }
}