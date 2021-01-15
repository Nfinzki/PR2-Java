import java.util.*;

public class SocialNetwork implements ISocialNetwork {
    //RI: followed != null && post != null && like != null &&
    //    && (for all pi, pj in like.keys . pi != pj => pi.id != pj.id)
    //    && (for all user in followed.keys . user != null && followed.get(user) != null && (for all i in followed.get(user) . i != null && (exist j in followed.keys . i = j) && user != i))
    //    && (for all user in followed.keys . (exist j in post.keys . user = j))
    //    && (for all user in post.keys . user != null && post.get(user) != null && (exist j in followed.keys . user = j))
    //    && (for all user in post.keys . (for all p in post.get(user) . p != null && (exist pi in like.keys . p = pi)))
    //    (for all p in like.keys . p != null && like.get(p) != null && (exits user in post.keys . (exist pi in post.get(user) . p = pi)) && (for all u in like.get(p) . u != null && (exist ui in followed.keys . ui = u)))

    //AF: AF(user) = <f(user), g(user), h(post), Users, Posts>
    //    Dove:
    //    f: user -> uSet(user) con uSet(user) sottoinsieme di Users
    //    t.c. (for all u in uSet(user) . (exist i in Users . i = u) && u != user)
    //
    //    g: user -> postSet(user) con postSet(user) sottoinsieme di Posts
    //    t.c. (for all p in postSet(user) . (exist u in Users . p.author = u))
    //
    //    h: post -> uSet(user) con uSet(user) sottoinsieme di Users
    //    t.c. post appartiene a g(user) && (for all u in uSet(user) . (exist i in Users . u = i) && u != user)
    //
    //    Users è l'insieme degli utenti
    //    Posts è l'insieme dei post
    //
    //    La funzione f associa ad un utente l'insieme delle persone che segue
    //    La funzione g associa ad un utente l'insieme dei post che ha creato
    //    La funzione h associa ad un post l'insieme dei suoi like

    private Map<String, Set<String>> followed;
    private Map<String, Set<Post>> post;
    protected Map<Post, Set<String>> like;
    private int next_id = 1;

    //REQUIRES: true
    //EFFECTS: istanzia l'oggetto
    public SocialNetwork(){
        followed = new HashMap<String, Set<String>>();
        post = new HashMap<String, Set<Post>>();
        like = new HashMap<Post, Set<String>>();
    }


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
    public void addLike(Post p, String username) throws NullPointerException, PostNotPresentException, UserAlreadyPresentException, SelfLikeException {
        if (p == null || username == null) throw new NullPointerException("Null value in addLike");
        if (p.getAuthor().equals(username)) throw new SelfLikeException();
        if (!like.containsKey(p)) throw new PostNotPresentException();

        if(!followed.containsKey(username)) addUser(username); //Se l'utente non è presente nella rete sociale lo aggiungo

        followed.get(username).add(p.getAuthor()); //Aggiungo l'autore del post all'insieme delle persone seguite da username
        like.get(p).add(username); //Aggiungo lo username tra i like
    }

    //Aggiunge utenti all'insieme dei followed di un utente.
    //Se un potenziale follower è l'autore del post, non viene aggiunto
    //REQUIRES: p != null
    //THROW: se p = null lancia una NullPointerException
    //MODIFY: this
    //EFFECTS: if(p.author != p.text.mentioned) {p.author.followed}_post = {p.author.followed}_pre U {p.text.mentioned}
    //          aggiunge all'insieme di persone seguite dal proprietario del post
    //          l'insieme delle persone menzionate nel post
    //          Se un potenziale follower è l'autore del post, non viene aggiunto
    private void addFollow(Post p) throws NullPointerException{
        if(p == null) throw new NullPointerException();

        int index_last_user = 0;
        while(p.getText().indexOf("@", index_last_user) != -1 && index_last_user != -1){ //Fino a quando non ci sono più menzioni
            int start = p.getText().indexOf("@", index_last_user); //Cerca l'inizio della menzione
            int end = p.getText().indexOf(" ", start); //Prova a cercare la fine della menzione
            String user = end != -1 ? p.getText().substring(start+1, end) : p.getText().substring(start+1); //Valuta se la menzione è alla fine della stringa o no
            index_last_user = end;
            if(!p.getAuthor().equals(user)) followed.get(p.getAuthor()).add(user); //Aggiunge l'utente menzionato all'insieme delle persone seguite dall'autore del post solo se l'utente menzionato non è l'autore del post
            try{
                if(!followed.containsKey(user)) addUser(user); //Se l'utente non è presente all'interno della rete sociale, lo aggiunge
            } catch (UserAlreadyPresentException e){}
        }
    }

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
    public Post addPost(String author, String text) throws NullPointerException, IllegalArgumentException, UserAlreadyPresentException {
        if(author == null || text == null) throw new NullPointerException("Null value in addPost");
        if (text.length() == 0 || text.length() > 140) throw new IllegalArgumentException("Il post non rispetta i limiti dei caratteri");

        Post p = new Post(next_id++, author, text);
        if(!post.containsKey(p.getAuthor())) //Se l'utente non è presente nella rete sociale viene aggiunto
            addUser(p.getAuthor());

        post.get(p.getAuthor()).add(p); //Aggiunge il post all'insieme dei post dell'utente
        like.put(p, new HashSet<String>()); //Aggiunge la possibilità di mettere like
        addFollow(p); //Controlla se l'utente ha menzionato, e quindi sta seguendo, qualcuno e li aggiunge alle persone che l'utente segue

        return p;
    }

    //Aggiunge un utente alla rete sociale
    //REQUIRES: username != null && !users.contains(username)
    //THROW: se username = null lancia una NullPointerException
    //       se users.contains(username) lancia una UserAlreadyPresentException
    //MODIFY: this
    //EFFECTS: {users}_post = {users}_pre U {username}
    public void addUser(String username) throws UserAlreadyPresentException{
        if(username == null) throw new NullPointerException("Null value in addUser");

        if(followed.containsKey(username)) throw new UserAlreadyPresentException();

        followed.put(username, new HashSet<String>()); //Aggiunge l'utente alla rete sociale
        post.put(username, new HashSet<Post>()); //Aggiunge la possibilità all'utente di creare dei post
    }

    //Restituisce sottoforma di stringa la rete sociale
    //REQUIRES: True
    //EFFECTS: Resituisce this sottoforma di stringa
    public String toString(){
        return "Followed:\n" + followed.toString() + "\nPost:\n" + post.toString() + "\nLike:\n" + like.toString();
    }

    //Restituisce la rete sociale derivata dalla lista post
    //REQUIRES: ps != null
    //THROW: se ps = null lancia una NullPointerException
    //EFFECTS: for all i in users,(for all j in users.followed, i.follows(j) = true)
    //         restituisce l'oggetto che associa agli utenti le persone seguite dall'utente
    public static Map<String, Set<String>> guessFollowers(List<Post> ps) throws NullPointerException{
        if (ps == null) throw new NullPointerException("Null value in guessFollowers");
        Map<String, Set<String>> reteSociale = new HashMap<String, Set<String>>();

        for(Post p : ps){
            if(!reteSociale.containsKey(p.getAuthor())) reteSociale.put(p.getAuthor(), new HashSet<String>()); //Se l'autore del post non fa parte della rete sociale lo aggiungo

            Set<String> mentioned = new HashSet<String>();
            int index_last_user = 0;
            while(p.getText().indexOf("@", index_last_user) != -1 && index_last_user != -1){ //Fino a quando non ci sono più menzioni
                int start = p.getText().indexOf("@", index_last_user); //Cerca l'inizio di una menzione
                int end = p.getText().indexOf(" ", start); //Cerca di trovare la fine di una menzione
                String user = end != -1 ? p.getText().substring(start+1, end) : p.getText().substring(start+1); //Valuta se la menzione è alla fine della stringa o no
                index_last_user = end;
                reteSociale.get(p.getAuthor()).add(user); //Aggiunge l'utente all'insieme delle persone che l'autore del post segue
            }
        }
        return reteSociale;
    }

    //Restituisce gli utenti più influenti della rete sociale
    //REQUIRES: true
    //EFFECTS: for all i, j in users.size(), i < j => users.get(i).getFollowers.compareTo(users.get(j).getFollowers) > 0
    //         restituisce la lista delle persone che hanno più follower in ordine decrescente
    public List<String> influencers(){
        Map<String, Integer> users = new HashMap<String, Integer>();
        for(Set<String> set : followed.values()) { //Crea una Map dove ad ogni utente è associato il numero dei propri followers
            for(String s : set){
                if(users.containsKey(s))
                    users.put(s, users.get(s) + 1);
                else
                    users.put(s, 1);
            }
        }

        List<Map.Entry<String, Integer>> sorted_influencers = new ArrayList<Map.Entry<String, Integer>>(users.entrySet());
        sorted_influencers.sort(Map.Entry.comparingByValue()); //Ordina in maniera crescente

        List<String> result = new ArrayList<String>();

        for(int i = sorted_influencers.size()-1; i >= 0; i--){ //Inverte la lista
            result.add(sorted_influencers.get(i).getKey());
        }

        return result;
    }

    //Restituisce l'insieme degli utenti menzionati (inclusi) nei post presenti nella rete sociale
    //REQUIRES: true
    //EFFECTS: for all i in users, i.hasPosted() = true
    //         restituisce l'insieme degli utenti che hanno creato almeno un post all'interno della rete sociale
    public Set<String> getMentionedUsers(){
        List<Post> ps = new ArrayList<Post>();
        for(Post p : like.keySet()){ //Crea una lista contenente tutti i post della rete sociale
            ps.add(p);
        }
        return SocialNetwork.getMentionedUsers(ps); //Invoca il metodo statico getMentionedUsers sulla lista creata
    }

    //Restituisce l'insieme degli utenti menzionati (inclusi) nella lista di post
    //REQUIRES: ps != null
    //THROW: se ps = null lancia una NullPointerException
    //EFFECTS: for all i in users, i.hasPosted() = true
    //         restituisce l'insieme degli utenti che hanno creato un post tra la lista di post
    public static Set<String> getMentionedUsers(List<Post> ps) throws NullPointerException{
        if(ps == null) throw new NullPointerException("Null value in getMentionedUsers");

        Set<String> string_list = new HashSet<String>();
        for(Post p : ps) {
            string_list.add(p.getAuthor()); //Aggiunge al set degli utenti l'autore del post
        }
        return string_list;
    }

    //Resituisce la lista dei post effettuati dall'utente nella rete sociale il cui nome è dato dal parametro username
    //Restituisce la lista vuota se l'utente non ha mai postato
    //REQUIRES: username != null && !users.contains(username)
    //THROW: se username = null lancia una NullPointerException
    //       se users.contains(username) lancia una UserNotPresentException
    //EFFECTS: for all i in post, i.author = username
    //         restituisce la lista dei post che sono stati scritti da username all'interno della rete sociale
    public List<Post> writtenBy(String username) throws NullPointerException, UserNotPresentException{
        if(username == null) throw new NullPointerException("Null value in writtenBy");
        if(!post.containsKey(username)) throw new UserNotPresentException();

        List<Post> ps = new ArrayList<Post>();
        for(Post p : like.keySet()){ //Crea una lista contenente tutti i post della rete sociale
            ps.add(p);
        }
        return SocialNetwork.writtenBy(ps, username); //Invoco il metodo statico writtenBy a cui passo la lista creata e il parametro username
    }

    //Restituisce la lista dei post effettuati dall'utente, il cui nome è
    // dato dal parametro username, presenti nella lista ps
    //REQUIRES: ps != null && username != null
    //THROW: se ps = null lancia una NullPointerException
    //       se username = null lancia una NullPointerException
    //EFFECTS: for all i in post, i.author = username
    //         restituisce la lista dei post che sono stati scritti da username all'interno della lista in ingresso
    public static List<Post> writtenBy(List<Post> ps, String username) throws NullPointerException{
        if(ps == null || username == null) throw new NullPointerException("Null value in writtenBy");
        List<Post> post_list = new ArrayList<Post>();
        for(Post p : ps){
            if(p.getAuthor().equals(username)) //Se il post è stato scritto da username lo aggiungo alla lista
                post_list.add(p);
        }
        return post_list;
    }

    //Restituisce la lista dei post presenti nella rete sociale che includono almeno
    // una delle parole presenti nella lista passata come parametro
    //REQUIRES: words != null
    //THROW: se words = null lancia una NullPointerException
    //EFFECTS: for all i in post, (exist j in words, i.text.contains(j))
    //         restituisce la lista dei post che appartengono alla rete sociale che contengono almeno
    //         una parola che appartiene alla lista delle parole passate in input
    public List<Post> containing(List<String> words) throws NullPointerException{
        if(words == null) throw new NullPointerException("Null value in containing");

        List<Post> post_list = new ArrayList<Post>();
        for(Post p : like.keySet()){
            for(String s : words) {
                if(p.getText().toLowerCase().contains(s.toLowerCase()) && !post_list.contains(p)) //Se il post contiene almeno una parola lo aggiungo alla lista
                    post_list.add(p);
            }
        }
        return post_list;
    }
}
