package ru.shishkin.data.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.shishkin.data.api.StudentsApi;
import ru.shishkin.data.data.Student;
import ru.shishkin.data.data.StudentRepository;
import ru.shishkin.data.model.StudentDataCreateRequest;
import ru.shishkin.data.model.StudentDataResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudentController implements StudentsApi {

    private final StudentRepository studentRepository;

    @Override
    public ResponseEntity<StudentDataResponse> createStudentDataInData(StudentDataCreateRequest request) {
        Student student = new Student();
        student.setName(request.getFullName());
        student.setPassport(request.getPassport());
        studentRepository.save(student);

        StudentDataResponse response = new StudentDataResponse();
        response.setId(student.getId());
        response.setFullName(student.getName());
        response.setPassport(student.getPassport());

        return ResponseEntity.status(200).body(response);
    }
    @Override
    public ResponseEntity<StudentDataResponse> getStudentByIdFromData(Long id) {
        return studentRepository.findById(id)
                .map(student -> {
                    StudentDataResponse r = new StudentDataResponse();
                    r.setId(student.getId());
                    r.setFullName(student.getName());
                    r.setPassport(student.getPassport());
                    return ResponseEntity.ok(r);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<java.util.List<ru.shishkin.data.model.StudentDataResponse>> getStudentsFromData() {

        java.util.List<ru.shishkin.data.model.StudentDataResponse> list =
                studentRepository.findAll().stream()
                        .map(s -> {
                            ru.shishkin.data.model.StudentDataResponse r =
                                    new ru.shishkin.data.model.StudentDataResponse();
                            r.setId(s.getId());
                            r.setFullName(s.getName());   // у тебя name = fio
                            r.setPassport(s.getPassport());
                            return r;
                        })
                        .toList();

        return ResponseEntity.ok(list);
    }
}