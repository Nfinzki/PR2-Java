public class SelfLikeException extends Exception{
    public SelfLikeException(){
        super("Tryed to put a like to himself");
    }
}
