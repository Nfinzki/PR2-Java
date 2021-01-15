public class UserNotPresentException extends Exception{
    public UserNotPresentException(){
        super("User not present in the social network");
    }
}
