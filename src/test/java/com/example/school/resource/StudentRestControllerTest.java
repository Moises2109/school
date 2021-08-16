package com.example.school.resource;

import com.example.school.exception.BadRequestException;
import com.example.school.exception.ErrorCode;
import com.example.school.model.Student;
import com.example.school.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Completable;
import io.reactivex.Single;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentRestController.class)
public class StudentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    public void shouldNewStudent() throws Exception {
        when(studentService.newStudent(any(Student.class)))
                .thenReturn(Single.just("1"));

        MvcResult mvcResult = mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new Student())))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated());

        verify(studentService, times(1)).newStudent(any(Student.class));
    }

    @Test
    public void shouldFailToNewStudent() throws Exception {
        when(studentService.newStudent(any(Student.class)))
                .thenReturn(Single.error(new BadRequestException()));

        MvcResult mvcResult = mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new Student())))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", equalTo(ErrorCode.ENTITY_FOUND.toString())))
                .andExpect(jsonPath("$.message", equalTo(ErrorCode.ENTITY_FOUND.getMessage())));

        verify(studentService, times(1)).newStudent(any(Student.class));
    }

    @Test
    public void shouldUpdateStudent() throws Exception {
        when(studentService.updateStudent(any(Student.class)))
                .thenReturn(Completable.complete());

        MvcResult mvcResult = mockMvc.perform(put("/students/id", "1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new Student())))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());

        verify(studentService, times(1)).updateStudent(any(Student.class));
    }

    @Test
    public void shouldFailToUpdateStudent() throws Exception {
        when(studentService.updateStudent(any(Student.class)))
                .thenReturn(Completable.error(new EntityNotFoundException()));

        MvcResult mvcResult = mockMvc.perform(put("/students/id", "1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new Student())))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", equalTo(ErrorCode.ENTITY_NOT_FOUND.toString())))
                .andExpect(jsonPath("$.message", equalTo(ErrorCode.ENTITY_NOT_FOUND.getMessage())));

        verify(studentService, times(1)).updateStudent(any(Student.class));
    }

    @Test
    public void shouldGetAllStudentsActives() throws Exception {
        Student student1 = new Student("1", "David", true);
        Student student2 = new Student("2", "Marcos", true);
        Student student3 = new Student("3", "Maria", true);

        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);

        when(studentService.getAllStudents())
                .thenReturn(Single.just(students));

        MvcResult mvcResult = mockMvc.perform(get("/students")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id", equalTo("1")))
                .andExpect(jsonPath("$.data[0].name", equalTo("David")))
                .andExpect(jsonPath("$.data[1].id", equalTo("2")))
                .andExpect(jsonPath("$.data[1].name", equalTo("Marcos")))
                .andExpect(jsonPath("$.data[2].id", equalTo("3")))
                .andExpect(jsonPath("$.data[2].name", equalTo("Maria")));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    public void shouldGetAStudent() throws Exception {
        when(studentService.getStudentDetail(anyString()))
                .thenReturn(Single.just(new Student("1", "David", true)));

        MvcResult mvcResult = mockMvc.perform(get("/students/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", equalTo("1")));

        verify(studentService, times(1)).getStudentDetail(anyString());
    }

    @Test
    public void shouldFailGetStudent() throws Exception {
        when(studentService.getStudentDetail(anyString()))
                .thenReturn(Single.error(new EntityNotFoundException()));

        MvcResult mvcResult = mockMvc.perform(get("/students/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", equalTo(ErrorCode.ENTITY_NOT_FOUND.toString())))
                .andExpect(jsonPath("$.message", equalTo(ErrorCode.ENTITY_NOT_FOUND.getMessage())));

        verify(studentService, times(1)).getStudentDetail(anyString());
    }

    @Test
    public void shouldDeleteStudent() throws Exception {
        when(studentService.deleteStudent(anyString()))
                .thenReturn(Completable.complete());

        MvcResult mvcResult = mockMvc.perform(delete("/students/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(anyString());
    }

    @Test
    public void shouldFailDeleteStudent() throws Exception {
        when(studentService.deleteStudent(anyString()))
                .thenReturn(Completable.error(new EntityNotFoundException()));

        MvcResult mvcResult = mockMvc.perform(delete("/students/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", equalTo(ErrorCode.ENTITY_NOT_FOUND.toString())))
                .andExpect(jsonPath("$.message", equalTo(ErrorCode.ENTITY_NOT_FOUND.getMessage())));

        verify(studentService, times(1)).deleteStudent(anyString());
    }
}