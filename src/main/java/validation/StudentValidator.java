package validation;

import domain.Student;

public class StudentValidator implements Validator<Student> {

    /**
     * Valideaza un student
     * @param entity - studentul pe care il valideaza
     * @throws ValidationException - daca studentul nu e valid
     */
    @Override
    public void validate(Student entity) throws ValidationException {
        //put null validations first + changed validation message
        if(entity.getID() == null){
            throw new ValidationException("Id-ul nu poate fi null!");
        }
        if(entity.getEmail() == null){
            throw new ValidationException("Email-ul nu poate fi null!");
        }
        if(entity.getNume() == null){
            throw new ValidationException("Numele nu poate fi null!");
        }
        if(entity.getID().equals("")){
            throw new ValidationException("Id incorect!");
        }
        if(entity.getNume() == ""){
            throw new ValidationException("Nume incorect!");
        }
        if(entity.getGrupa() < 0) {
            throw new ValidationException("Grupa incorecta!");
        }
        if(entity.getEmail().equals("")){
            throw new ValidationException("Email incorect!");
        }
        //validation for invalid email
        if(!entity.getEmail().contains("@") && !entity.getEmail().contains(".")){
            throw new ValidationException("Incorrect email format!");
        }
        //validation for ids with letters
        if(entity.getID().matches(".*[a-zA-Z].*")){
            throw new ValidationException("ID can't containt any letters!");
        }
        //validation for names that contain digits
        if(entity.getNume().matches(".*\\d+.*")){
            throw new ValidationException("Names can't contain digits!");
        }
    }
}