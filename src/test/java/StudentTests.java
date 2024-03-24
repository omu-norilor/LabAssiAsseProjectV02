import domain.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.util.Random;

public class StudentTests {
    private final StudentValidator studentValidator;
    private final TemaValidator temaValidator;
    private final StudentXMLRepo studentXMLRepository;
    private final TemaXMLRepo temaXMLRepository;
    private final NotaValidator notaValidator;
    private final NotaXMLRepo notaXMLRepository;
    private final Service service;

    public StudentTests() {
        this.studentValidator = new StudentValidator();
        this.temaValidator = new TemaValidator();

        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";

        this.studentXMLRepository = new StudentXMLRepo(filenameStudent);
        this.temaXMLRepository = new TemaXMLRepo(filenameTema);
        this.notaValidator = new NotaValidator(this.studentXMLRepository, this.temaXMLRepository);
        this.notaXMLRepository = new NotaXMLRepo(filenameNota);
        this.service = new Service(this.studentXMLRepository, this.studentValidator, this.temaXMLRepository, this.temaValidator, this.notaXMLRepository, this.notaValidator);
    }

    @Test
    public void testAddStudent(){
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        Service service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
        Student student = new Student("1", "Mihai", 1, "Mihai@mihai.mihai");
        Student student1 = service.addStudent(student);
        Assertions.assertEquals(student, student1);
        Assertions.assertNotEquals(null, student1);
    }

    @Test
    public void TestSmallerId(){
        //id student < 1 => still works even if normally it shouldn't
        Student hwStudent1 = new Student("0", "Mihai", 1, "Mihai@mihai.com");
        service.addStudent(hwStudent1);
        Assertions.assertNotEquals(null, service.findStudent("0")); // it returns a student hence it's not an error
    }

    @Test
    public void TestExistingId(){
        //id student = 2 => it already exists => it shouldn't work, but it does => ERROR
        Student hwStudent2 = new Student("2", "Mihai", 1, "Mihai@mihai.com");
        Assertions.assertNotEquals(null, service.findStudent("2"));
        service.addStudent(hwStudent2);
        Assertions.assertNotEquals(hwStudent2, service.findStudent("2"));
    }

    @Test
    public void TestInvalidId(){
        //id student = a => it should throw an error, but it doesn't => ERROR
        Student hwStudent3 = new Student("a", "Michael", 1, "Michael@Jackson.mj");
        Assertions.assertThrows(ValidationException.class, () ->{ service.addStudent(hwStudent3);});
    }

    @Test
    public void TestRandomId(){
        //Choose an id that doesn't exist => it should work
        Student hwStudent4 = new Student("153", "Mike", 1, "MikeTyson@Mike.com");
        Assertions.assertEquals(hwStudent4, service.addStudent(hwStudent4));
    }

    @Test
    public void TestNullId(){
        //try to add a student with a null id => it shouldn't work
        Student hwStudent5 = new Student(null, "Mikey", 1, "MikeyHash@gmail.com");
        Assertions.assertThrows(ValidationException.class, () ->{ service.addStudent(hwStudent5);});
    }

    @Test
    public void TestNullName(){
        //try to add a student with a null name => it shouldn't work
        Student hwStudent6 = new Student("154", null, 1, "e@e.com");
        Assertions.assertThrows(ValidationException.class, () ->{ service.addStudent(hwStudent6);});
    }

    @Test
    public void TestInvalidName(){
        //try to add a student who has a name that is not valid(no letters)
        Student hwStudent7 = new Student("155", "910", 1, "e@e.com");
        Assertions.assertThrows(ValidationException.class, () ->{ service.addStudent(hwStudent7);});
    }

    @Test
    public void TestNegativeGroup(){
        //try to add a student from a group that is a negative number
        Student hwStudent8 = new Student("156", "Student", -1, "e@e.com");
        Assertions.assertThrows(ValidationException.class, () ->{ service.addStudent(hwStudent8);});
    }

    @Test
    public void TestNullEmail(){
        //try to add a student with a null email
        Student hwStudent9 = new Student("157", "Student", 1, null);
        Assertions.assertThrows(ValidationException.class, () ->{ service.addStudent(hwStudent9);});
    }

    @Test
    public void TestNumberEmail(){
        //try to add a student that has an email that contains only numbers
        Student hwStudent10 = new Student("158", "Michael", 1, "910");
        Assertions.assertThrows(ValidationException.class, () ->{ service.addStudent(hwStudent10);});
    }

    @Test
    public void TestMinimumGroup(){
        //try to add a student with the lowest group
        Student hwStudent11 = new Student("159", "Michael", 1, "910@email.com");
        Assertions.assertEquals(hwStudent11, service.addStudent(hwStudent11));
    }

    @Test
    public void TestMaximumGroup(){
        //try to add a student with a large number as group
        Student hwStudent12 = new Student("160", "Michael", 1000000000, "910@email.com");
        Assertions.assertEquals(hwStudent12, service.addStudent(hwStudent12));
    }

    @Test
    public void TestRandomGroup(){
        //choose a random group number(not generated)
        Student hwStudent13 = new Student("160", "Michael", 123, "910@email.com");
        Assertions.assertEquals(hwStudent13, service.addStudent(hwStudent13));
    }

    @Test
    public void TestValidInput(){
        //test valid input fields
        Student hwStudent14 = new Student("25", "Michael", 123, "Michael@email.com");
        Assertions.assertEquals(hwStudent14, service.addStudent(hwStudent14));
    }
}