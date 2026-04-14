package mn.num.edu.user_service.adapter.in.web.controller;

import jakarta.validation.Valid;
import mn.num.edu.user_service.adapter.in.web.request.CreateDepartmentRequest;
import mn.num.edu.user_service.adapter.in.web.request.CreateStudentRequest;
import mn.num.edu.user_service.adapter.in.web.request.CreateTeacherRequest;
import mn.num.edu.user_service.adapter.in.web.request.CreateUserRequest;
import mn.num.edu.user_service.application.dto.CreateDepartmentCommand;
import mn.num.edu.user_service.application.dto.CreateStudentCommand;
import mn.num.edu.user_service.application.dto.CreateTeacherCommand;
import mn.num.edu.user_service.application.dto.CreateUserCommand;
import mn.num.edu.user_service.application.port.in.*;
import mn.num.edu.user_service.domain.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final CreateStudentUseCase createStudentUseCase;
    private final CreateTeacherUseCase createTeacherUseCase;
    private final CreateDepartmentUseCase createDepartmentUseCase;
    private final FindUserUseCase findUserUseCase;

    public UserController(
            CreateStudentUseCase createStudentUseCase,
            CreateTeacherUseCase createTeacherUseCase, CreateDepartmentUseCase createDepartmentUseCase,
            FindUserUseCase findUserUseCase
    ) {
        this.createStudentUseCase = createStudentUseCase;
        this.createTeacherUseCase = createTeacherUseCase;
        this.createDepartmentUseCase = createDepartmentUseCase;
        this.findUserUseCase = findUserUseCase;
    }

    @PostMapping("/students")
    public Mono<ResponseEntity<User>> createStudent(
            @Valid @RequestBody CreateStudentRequest request
    ) {
        log.info("Received create student request. email={}, studentId={}, departmentId={}",
                request.email(), request.studentId(), request.departmentId());

        CreateStudentCommand command = new CreateStudentCommand(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.studentId(),
                request.departmentId(),
                request.major()
        );

        return createStudentUseCase.execute(command)
                .map(savedUser -> {
                    log.info("Student created successfully. userId={}", savedUser.getId());
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
                });
    }

    @PostMapping("/teachers")
    public Mono<ResponseEntity<User>> createTeacher(
            @Valid @RequestBody CreateTeacherRequest request
    ) {
        log.info("Received create teacher request. email={}, teacherCode={}, departmentId={}",
                request.email(), request.departmentId());

        CreateTeacherCommand command = new CreateTeacherCommand(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.departmentId(),
                request.position()
        );

        return createTeacherUseCase.execute(command)
                .map(savedUser -> {
                    log.info("Teacher created successfully. userId={}", savedUser.getId());
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
                });
    }

    @PostMapping("/departments")
    public Mono<ResponseEntity<User>> createDepartment(
            @Valid @RequestBody CreateDepartmentRequest request
    ) {
        log.info("Received create Department request. email={}, departmentId={}");

        CreateDepartmentCommand command = new CreateDepartmentCommand(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.departmentId(),
                request.departmentName()
        );

        return createDepartmentUseCase.execute(command)
                .map(savedUser -> {
                    log.info("Department created successfully. userId={}", savedUser.getId());
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
                });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> findUserById(@PathVariable String id) {
        log.info("Received find user request. userId={}", id);

        return findUserUseCase.findById(id)
                .map(user -> {
                    log.info("User found. userId={}", id);
                    return ResponseEntity.ok(user);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/all")
    public Flux<User> findAll() {
        log.info("Received find user request. All user");

        return findUserUseCase.findAll();
    }
}