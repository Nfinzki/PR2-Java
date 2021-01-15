import java.sql.Timestamp;

public interface IPost {
    //Overview: Oggetto immutabile contenente un id, un autore, un testo
    //          e il timestamp che rappresenta data e ora della creazione del post
    //
    //Typical element: <id, author, text, timestamp>

    //Restituisce l'id del post
    //REQUIRES: True
    //EFFECTS: Restituisce l'id di this
    int getId();

    //Restituisce l'autore del post
    //REQUIRES: True
    //EFFECTS: Restituisce l'autore di this
    String getAuthor();

    //Restituisce il testo del post
    //REQUIRES: True
    //EFFECTS: Restituisce il testo di this
    String getText();

    //Restituisce il timestamp del post
    //REQUIRES: True
    //EFFECTS: Restituisce il timestamp di this
    Timestamp getTimestamp();
}