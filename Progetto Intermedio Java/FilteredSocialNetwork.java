import java.util.*;

public class FilteredSocialNetwork extends SocialNetwork{
    //RI: super && bad_words != null && reported_post != null && !bad_words.isEmpty &&
    //    (for all s in bad_words . s != null) &&
    //    (for all p in reported_post . p != null && (exist i in like.keys . p = i)) &&
    //    (for all p in reported_post . (exist word in bad_words . p.text.contains(word))
    //
    //AF: AF(user) = <f(user), g(user), h(post), Users, Posts, Reported_post>
    //Dove:
    //    f, g, h, Users e Posts sono definiti come in super
    //
    //    Reported_post = {p | p appartiene a Posts && p.text.contains(bad_word)}
    //    Reported_post è l'insieme dell'utente che contengono parole offensive

    private String[] basic_words = {"mannaggia", "perbacco", "cacchio", "scemo", "scema", "scemi", "sceme", "stupido", "stupida", "stupidi", "stupide", "ignorante", "cretino"};
    private Set<String> bad_words;
    private Set<Post> reported_post;

    //REQUIRES: true
    //EFFECTS: istanzia l'oggetto
    public FilteredSocialNetwork(){
        super();
        bad_words = new HashSet<String>(Arrays.asList(basic_words));
        reported_post = new HashSet<Post>();
    }

    //Aggiunge una parola all'insieme delle parole offensive
    //REQUIRES: word != null
    //THROW: se word = null lancia una NullPointerException
    //MODIFY: this
    //EFFECTS: {bad_word}_post = {bad_word}_pre U {word}
    //         aggiunge all'insieme delle parole offensive word
    public void addBadWord (String word) throws NullPointerException{
        if (word == null) throw new NullPointerException("Null value in addBadWord");

        bad_words.add(word.toLowerCase()); //Aggiunge all'insieme delle bad_words il parametro word
    }

    //Aggiorna l'insieme dei post segnalati
    //REQUIRES: true
    //MODIFY: this
    //EFFECTS: {reported_posts}_post = {reported_posts}_pre U {posts_with_offensive_words}
    //         aggiunge all'insieme dei post segnalati i post che contengono parole offensive
    public void scanPost(){
        for(Post p : like.keySet())
            for(String s : bad_words)
                if(p.getText().toLowerCase().contains(s)) { //Se il testo del post contiene una parola offensiva viene aggiunto all'insieme dei post segnalati
                    reported_post.add(p);
                    break;
                }
    }

    //Restituisce la lista dei post segnalati
    //REQUIRES: true
    //EFFECTS: forall i in post_list => post_list.isReported
    //         restituisce la lista di tutti i post segnalati
    public List<Post> getReportedPost(){
        List<Post> rp = new ArrayList<Post>(reported_post);
        return rp;
    }

    //Aggiunge il post all'elenco dei post segnalati se contiene una parola offensiva
    //REQUIRES: p != null
    //THROW: se p = null lancia una NullPointerException
    //MODIFY: this
    //EFFECTS: {reported_posts}_post = {reported_posts}_pre U {post}
    //         aggiunge il post all'elenco dei post segnalati se contiene una parola offensiva
    public void reportPost(Post p) throws NullPointerException, IllegalArgumentException{
        if(p == null) throw new NullPointerException("Null value in reportPost");
        if(!like.containsKey(p)) throw new IllegalArgumentException("Post not present in the Social Network");

        reported_post.add(p);
    }

}
