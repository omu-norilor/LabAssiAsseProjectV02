import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import domain.Nota;
import domain.Student;
import domain.Tema;
import validation.ValidationException;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

public class BigBangTests {
    private final StudentValidator studentValidator;
    private final TemaValidator temaValidator;
    private final StudentXMLRepo studentXMLRepository;
    private final TemaXMLRepo temaXMLRepository;
    private final NotaValidator notaValidator;
    private final NotaXMLRepo notaXMLRepository;
    private final Service service;
    private boolean cleanup;

    public BigBangTests() {
        this.studentValidator = new StudentValidator();
        this.temaValidator = new TemaValidator();
        this.cleanup = true;

        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";

        this.studentXMLRepository = new StudentXMLRepo(filenameStudent);
        this.temaXMLRepository = new TemaXMLRepo(filenameTema);
        this.notaValidator = new NotaValidator(this.studentXMLRepository, this.temaXMLRepository);
        this.notaXMLRepository = new NotaXMLRepo(filenameNota);
        this.service = new Service(this.studentXMLRepository, this.studentValidator, this.temaXMLRepository,
                this.temaValidator, this.notaXMLRepository, this.notaValidator);
    }

    @Test
    public void testAddGrade() {
        long sizeBefore = service.getAllNote().spliterator().getExactSizeIfKnown();

        Nota nota = new Nota("1010", "100", "111", 9, LocalDate.of(2024, 5, 12));
        if (this.cleanup)
            try {
                service.addNota(nota, "Good job!");
            } catch (Exception e) {
                Assertions.assertEquals(e.getMessage(), "Studentul nu exista!");
            }
        else {
            service.addNota(nota, "Good job!");
            Assertions.assertEquals(sizeBefore+1, service.getAllNote().spliterator().getExactSizeIfKnown());
        }
    }

    @Test
    public void testAddStudent() {
        long sizeBefore = service.getAllStudenti().spliterator().getExactSizeIfKnown();

        Student student = new Student("100", "Pacatosu'", 932, "calcin@pacat.com");
        service.addStudent(student);
        if(this.cleanup){
            service.deleteStudent("100");
            Assertions.assertEquals(sizeBefore, service.getAllStudenti().spliterator().getExactSizeIfKnown());
        }
    }

    @Test
    public void testAddAssignment() {
        long sizeBefore = service.getAllTeme().spliterator().getExactSizeIfKnown();

        Tema assignment = new Tema("111", "Nice job", 10, 1);
        service.addTema(assignment);
        Assertions.assertEquals(sizeBefore + 1, service.getAllTeme().spliterator().getExactSizeIfKnown());
        if(this.cleanup){
            service.deleteTema("111");
            Assertions.assertEquals(sizeBefore, service.getAllTeme().spliterator().getExactSizeIfKnown());
        }
            
    }

    @Test
    public void testBigBang() {
        this.cleanup = false;
        testAddAssignment();
        testAddStudent();
        testAddGrade();

        service.deleteNota("1010");
        service.deleteStudent("100");
        service.deleteTema("111");
        this.cleanup = true;
    }

    @Test
    public void testIncrementalIntegration(){

        this.cleanup = false;

        // Phase 1
        testAddStudent();
        service.deleteStudent("100");

        // Phase 2
        testAddAssignment();
        testAddStudent();
        service.deleteTema("111");
        service.deleteStudent("100");

        // Phase 3
        testAddAssignment();
        testAddStudent();
        testAddGrade();

        service.deleteNota("1010");
        service.deleteStudent("100");
        service.deleteTema("111");
        this.cleanup = true;
    }
}
