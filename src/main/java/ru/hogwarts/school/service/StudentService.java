package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.model.entity.Faculty;
import ru.hogwarts.school.model.entity.Student;
import ru.hogwarts.school.model.response.FacultyResponse;
import ru.hogwarts.school.model.response.StudentResponse;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class
StudentService {
    Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public StudentResponse createStudent(String name, int age, Long facultyId) {
        logger.info("Was invoked method for create student");
        Faculty faculty = facultyRepository.findById(facultyId).orElseThrow(() -> {
            logger.error("There is not faculty with id = " + facultyId);
            return new FacultyNotFoundException();
        });
        Student student = new Student(name, age, faculty);
        Student studentCreate = studentRepository.save(student);
        return StudentMapper.toResponse(studentCreate);
    }

    public StudentResponse getStudentById(Long id) {
        logger.info("Was invoked method for get student by id");
        Student student = studentRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not student with id = " + id);
            return new StudentNotFoundException();
        });
        return StudentMapper.toResponse(student);
    }

    public StudentResponse updateStudent(Long id, String name, int age) {
        logger.info("Was invoked method for update student");
        Student student = studentRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not student with id = " + id);
            return new StudentNotFoundException();
        });
        student.setName(name);
        student.setAge(age);
        studentRepository.save(student);
        return StudentMapper.toResponse(student);
    }

    public StudentResponse deleteStudent(Long id) {
        logger.info("Was invoked method for delete student");
        Student student = studentRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not student with id = " + id);
            return new StudentNotFoundException();
        });
        studentRepository.deleteById(id);
        return StudentMapper.toResponse(student);
    }

    public List<StudentResponse> getStudentByAge(int age) {
        logger.info("Was invoked method for get student by age");
        List<Student> students = studentRepository.findAllByAge(age);
        return students.stream()
                .map(StudentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<StudentResponse> getStudentByAgeBetween(int min, int max) {
        logger.info("Was invoked method for get student by age between");
        List<Student> students = studentRepository.findByAgeBetween(min, max);
        return students.stream()
                .map(StudentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public FacultyResponse getStudentFaculty(Long id) {
        logger.info("Was invoked method for get student faculty");
        Student student = studentRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not student with id = " + id);
            return new StudentNotFoundException();
        });
        return FacultyMapper.toResponse(student.getFaculty());
    }

    public Long countStudents() {
        logger.info("Was invoked method for count students");
        return studentRepository.count();
    }

    public double getAverageAge() {
        logger.info("Was invoked method for get average age");
        return studentRepository.getAverageAge();
    }

    public List<StudentResponse> getLast5Students() {
        logger.info("Was invoked method for get 5 last students");
        List<Student> students = studentRepository.findAll(PageRequest.of(0, 5,
                Sort.by(Sort.Direction.DESC, "id"))).getContent();
        return students.stream()
                .map(StudentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<String> getAllNamesStartWithA() {
        logger.info("Was invoked method for get all names start with A");
        return studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(name -> name.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    public double getAverageAge2() {
        logger.info("Was invoked method for get average age 2");
        return studentRepository.findAll()
                .stream()
                .map(Student::getAge)
                .collect(Collectors.averagingDouble(Integer::doubleValue));
    }

    public void printNames() {
        logger.info("Was invoked method for print names");
        List<String> names = studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .sorted()
                .collect(Collectors.toList());

        System.out.println(names.get(0));
        System.out.println(names.get(1));

        Thread thread1 = new Thread() {
            @Override
            public synchronized void start() {
                System.out.println(names.get(2));
                System.out.println(names.get(3));
            }
        };
        Thread thread2 = new Thread(){
            @Override
            public synchronized void start() {
                System.out.println(names.get(4));
                System.out.println(names.get(5));
            }
        };

        thread1.start();
        thread2.start();
    }

    public void printNames2() {
        logger.info("Was invoked method for print names 2");
        List<String> names = studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .sorted()
                .collect(Collectors.toList());

        System.out.println(names.get(0));
        System.out.println(names.get(1));

        Thread thread1 = new Thread() {
            @Override
            public synchronized void start() {
                synchronized (names) {
                    System.out.println(names.get(2));
                    System.out.println(names.get(3));
                }
            }
        };
        Thread thread2 = new Thread(){
            @Override
            public synchronized void start() {
                synchronized (names) {
                    System.out.println(names.get(4));
                    System.out.println(names.get(5));
                }
            }
        };

        thread1.start();
        thread2.start();
    }
}
