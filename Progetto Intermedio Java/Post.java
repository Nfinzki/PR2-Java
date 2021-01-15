import java.sql.Timestamp;

public class Post implements IPost {
    //Overview: Oggetto immutabile contenente un id univoco, un autore, un testo non vuoto di massimo 140 caratteri
    //          e il timestamp che rappresenta data e ora della creazione del post
    //
    //AF: AF(post) -> <id, author, text, timestamp>
    //
    //IR: id >= 0 && author != null && text != null && 0 < text.length <= 140 && timestamp != null

    private int id;
    private String author;
    private String text;
    private Timestamp timestamp;

    //REQUIRES: id >= 0 and author != null and text != null
    //THROW: se id < 0 lancia una IllegalArgumentException
    //       se author = null lancia una NullPointerException
    //       se text = null lancia una NullPointerException
    //       se text.lenght > 140 oppure text.length = 0 lancia una IllegalArgumentException
    //EFFECTS: istanzia l'oggetto
    public Post(int id, String author, String text) throws NullPointerException, IllegalArgumentException {
        if (author == null || text == null) throw new NullPointerException();
        if (text.length() == 0 || text.length() > 140 || id < 0) throw new IllegalArgumentException();

        this.id = id;
        this.author = author;
        this.text = text;
        this.timestamp = new Timestamp(System.currentTimeMillis());

    }

    //Restituisce l'id del post
    //REQUIRES: True
    //EFFECTS: Restituisce l'id di this
    public int getId() {
        return this.id;
    }

    //Restituisce l'autore del post
    //REQUIRES: True
    //EFFECTS: Restituisce l'autore di this
    public String getAuthor() {
        return this.author;
    }

    //Restituisce il testo del post
    //REQUIRES: True
    //EFFECTS: Restituisce il testo di this
    public String getText() {
        return this.text;
    }

    //Restituisce il timestamp del post
    //REQUIRES: True
    //EFFECTS: Restituisce il timestamp di this
    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    //Restituisce sottoforma di stringa il post
    //REQUIRES: True
    //EFFECTS: Resituisce this sottoforma di stringa
    public String toString(){
        return "[" + id + "]" + " " + author + " Timestamp: "+ timestamp.toString() + " Text: " + text;
    }
}