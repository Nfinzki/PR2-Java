public class UserAlreadyPresentException extends Exception{
    public UserAlreadyPresentException(){
        super("User already present");
    }
}
