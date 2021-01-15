import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ISocialNetwork {
    //Overview: Un Social Network è un oggetto mutable che associa utenti ad un insieme di utenti e
    //          e ad un insieme di post. I post a loro volta sono associati ad un insieme di like.
    //          Un Social Network non può avere valori null.
    //
    //Typical element: user0::Ck, user1::C1 ..., userk::Ck
    //                 dove Ck è la coppia (Fk1, Pk2::Lk3)
    //                 dove Fki è l'insieme delle persone seguite
    //                 e Pki è l'insieme dei post
    //                 e Lki è l'insieme dei like



    //Aggiunge il like di un utente al post e il proprietario del post viene aggiunto all'insieme
    // delle persone seguite dall'utente che sta mettendo il like.
    //Se l'utente non è presente all'interno della rete sociale, l'utente viene aggiunto.
    //Se il post non è presente all'interno della rete sociale viene lanciata una PostNotPresentException
    //REQUIRES: p != null && username != null && postSet.contains(p) && p.author != username
    //MODIFY: this
    //THROW: se username = null lancia una NullPointerException
    //       se p = null lancia una NullPointerException
    //       se !postSet.contains(p) lancia una PostNotPresentException
    //       se p.author = username lancia una SelfLikeException
    //EFFECT: {p.like}_post = {p.like}_pre U {username}
    //        and if !users.contains(username) then {users}_post = {users}_pre U {username} endif
    //        and {username.follows}_post = {username.follows}_pre U {p.author}
    //        username viene aggiunto all'insieme dei like del post
    //        l'autore del post viene aggiunto all'insieme delle persone seguite da username
    //        se username non appartiene alla rete sociale viene aggiunto
    void addLike(Post p, String username) throws NullPointerException, PostNotPresentException, UserAlreadyPresentException, SelfLikeException;

    //Aggiunge un post alla rete sociale
    //Se l'utente è già all'interno della rete sociale, aggiungo il post.
    //Se l'utente non è presente nella rete sociale, viene aggiunto e viene aggiunto il suo post.
    //Restituisce il post creato
    //REQUIRES: author != null && text != null && 0 < text.length <= 140
    //THROW: se author = null lancia una NullPointerException
    //       se text = null lancia una NullPointerException
    //       se text.length == 0 or text.length > 140 lancia una IllegalArgumentException
    //EFFECTS: Aggiunge il post all'insieme dei post dell'utente author.
    //         Se author non appartiene alla rete sociale, viene aggiunto e successivamente viene aggiunto
    //         il suo post.
    //         Inoltre viene restituito il post creato.
    //         {author.posts}_post = {author.posts}_pre U {new post}
    //         {users}_post = {users}_pre U {author}
    Post addPost(String author, String text) throws NullPointerException, IllegalArgumentException, UserAlreadyPresentException;

    //Aggiunge un utente alla rete sociale
    //REQUIRES: username != null && !users.contains(username)
    //THROW: se username = null lancia una NullPointerException
    //       se users.contains(username) lancia una UserAlreadyPresentException
    //MODIFY: this
    //EFFECTS: {users}_post = {users}_pre U {username}
    void addUser(String username) throws UserAlreadyPresentException;

    //Restituisce gli utenti più influenti della rete sociale
    //REQUIRES: true
    //EFFECTS: for all i, j in users.size(), i < j => users.get(i).getFollowers.compareTo(users.get(j).getFollowers) > 0
    //         restituisce la lista delle persone che hanno più follower in ordine decrescente
    List<String> influencers();

    //Restituisce l'insieme degli utenti menzionati (inclusi) nei post presenti nella rete sociale
    //REQUIRES: true
    //EFFECTS: for all i in users, i.hasPosted() = true
    //         restituisce l'insieme degli utenti che hanno creato almeno un post all'interno della rete sociale
    Set<String> getMentionedUsers();

    //Resituisce la lista dei post effettuati dall'utente nella rete sociale il cui nome è dato dal parametro username
    //Restituisce la lista vuota se l'utente non ha mai postato
    //REQUIRES: username != null && users.contains(username)
    //THROW: se username = null lancia una NullPointerException
    //       se !users.contains(username) lancia una UserNotPresentException
    //EFFECTS: for all i in post, i.author = username
    //         restituisce la lista dei post che sono stati scritti da username all'interno della rete sociale
    List<Post> writtenBy(String username) throws NullPointerException, UserNotPresentException;

    //Restituisce la lista dei post presenti nella rete sociale che includono almeno
    // una delle parole presenti nella lista passata come parametro
    //REQUIRES: words != null
    //THROW: se words = null lancia una NullPointerException
    //EFFECTS: for all i in post, (exist j in words, i.text.contains(j))
    //         restituisce la lista dei post che appartengono alla rete sociale che contengono almeno
    //         una parola che appartiene alla lista delle parole passate in input
    List<Post> containing(List<String> words);
}
