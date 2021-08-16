package com.example.school.resource;

import com.example.school.model.Student;
import com.example.school.service.StudentService;
import com.example.school.util.DataResponse;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/students")
public class StudentRestController {

    @Autowired
    private StudentService studentService;

    @PostMapping()
    public Single<ResponseEntity> newStudent(
        @RequestBody Student newStudentRequest) {
        return studentService.newStudent(newStudentRequest).subscribeOn(Schedulers.io()).map(
            student -> ResponseEntity.created(URI.create("/students/" + student))
                    .body(DataResponse.successNoData()));
    }

    @PutMapping(value = "/{studentId}")
    public Single<ResponseEntity> updateStudent(@PathVariable(value = "studentId") String studentId,
                                                              @RequestBody Student updateStudentRequest) {
        return studentService.updateStudent(toUpdateStudent(studentId, updateStudentRequest))
                .subscribeOn(Schedulers.io())
                .toSingle(() -> ResponseEntity.ok(DataResponse.successNoData()));
    }

    private Student toUpdateStudent(String studentId, Student updateStudentRequest) {
        Student updateStudent = new Student();
        BeanUtils.copyProperties(updateStudentRequest, updateStudent);
        updateStudent.setId(studentId);
        return updateStudent;
    }

    @GetMapping()
    public  Single<ResponseEntity<DataResponse<List<Student>>>> getAllStudents() {
        return studentService.getAllStudents()
                .subscribeOn(Schedulers.io())
        .map(studentResponses -> ResponseEntity.ok(DataResponse.successWithData(toStudentResponseList(studentResponses))));
    }

    private List<Student> toStudentResponseList(List<Student> studentResponseList) {
        return studentResponseList
                .stream()
                .map(this::toStudentResponse)
                .collect(Collectors.toList());
    }

    private Student toStudentResponse(Student studentResponse) {
        Student student = new Student();
        BeanUtils.copyProperties(studentResponse, student);
        return student;
    }

    @GetMapping(value = "/{studentId}")
    public Single<ResponseEntity<DataResponse<Student>>> getStudentDetail(@PathVariable(value = "studentId") String studentId) {
        return studentService.getStudentDetail(studentId)
                .subscribeOn(Schedulers.io())
                .map(studentResponse -> ResponseEntity.ok(DataResponse.successWithData(toStudentResponse(studentResponse))));
    }

    @DeleteMapping(value = "/{studentId}")
    public Single<ResponseEntity> deleteStudent(@PathVariable(value = "studentId") String studentId) {
        return studentService.deleteStudent(studentId)
                .subscribeOn(Schedulers.io())
                .toSingle(() -> ResponseEntity.ok(DataResponse.successNoData()));
    }

}
