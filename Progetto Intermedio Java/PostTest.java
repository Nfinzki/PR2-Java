import java.util.*;

public class PostTest {
    public static void main (String[] args){
        ISocialNetwork MicroBlog = new SocialNetwork();

        //////////////Batteria di test per Post//////////////
        //text.length = 0
        try {
            Post troppo_prolisso = new Post(1,"Fabio", "");
        }catch (Exception E){
            System.out.println(E.toString() + ": Il post ha il campo text vuoto");
        }

        //text.length > 140
        try {
            Post troppo_prolisso = new Post(1,"Fabio", "Questo post intenzionalmente supererà il limite di 140 caratteri perché vuole testare le condizioni imposte nel testo del progetto. Questo post intenzionalmente supererà il limite di 140 caratteri perché vuole testare le condizioni imposte nel testo del progetto. ");
        }catch (Exception E){
            System.out.println(E.toString() + ": Il post supera il limite dei 140 caratteri");
        }

        //author null
        try{
            String author = null;
            Post unkown_author = new Post(1, author, "Che bella giornata oggi!");
        } catch (Exception E){
            System.out.println(E.toString() + ": Author null");
        }

        //text null
        try{
            String text = null;
            Post unkown_text = new Post(1,"Luca", text);
        } catch (Exception E){
            System.out.println(E.toString() + ": Text null");
        }

        //negative id
        try{
            Post negative_id = new Post(-4,"Luca", "Questo è un post di prova");
        } catch (Exception E){
            System.out.println(E.toString() + ": Negative id");
        }

        //post test
        String author1 = "Luca";
        String text1 = "Oggi è proprio una bella giornata!";
        Post post1 = new Post(1, author1, text1);
        assert post1.getId() == 1;
        assert post1.getAuthor().equals(author1);
        assert post1.getText().equals(text1);

        //////////////Batteria di test per SocialNetwork//////////////
        String author2 = "Fabio";
        String text2 = "@Luca hai proprio ragione!";
        String author3 = "Marco";
        String text3 = "Oggi andrò a pescare con @Luca e i miei amici";
        String author4 = "Fabio";
        String text4 = "Oggi ho proprio voglia di postare";
        Post post2 = null;
        Post post3 = null;
        Post post4 = null;

        ////////influencers test with empty SocialNetwork////////
        List<String> expected_empty = new ArrayList<String>();
        expected_empty = MicroBlog.influencers();
        assert expected_empty.isEmpty();

        ////////getMentionedUsers test with empty SocialNetwork////////
        Set<String> expected_empty_set = new HashSet<String>();
        expected_empty_set = MicroBlog.getMentionedUsers();
        assert expected_empty_set.isEmpty();

        ////////addPost test////////
        //lecit addPost
        try{
            post1 = MicroBlog.addPost(author1, text1);
            post2 = MicroBlog.addPost(author2, text2);
            post3 = MicroBlog.addPost(author3, text3);
            post4 = MicroBlog.addPost(author4, text4);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //null author
        String null_author = null;
        try{
            MicroBlog.addPost(null_author, "Questo è un post di prova");
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //null text
        String null_text = null;
        try{
            MicroBlog.addPost("Lenny", null_text);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //text.length > 140
        try{
            MicroBlog.addPost("Fabio", "Questo post intenzionalmente supererà il limite di 140 caratteri perché vuole testare le condizioni imposte nel testo del progetto. Questo post intenzionalmente supererà il limite di 140 caratteri perché vuole testare le condizioni imposte nel testo del progetto. ");
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //text.length = 0
        try{
            MicroBlog.addPost("Fabio", "");
        } catch (Exception e){
            System.out.println(e.toString());
        }

        ////////addUser test////////
        //User already present test
        String user_already_present = "Marco";
        try {
            MicroBlog.addUser(user_already_present);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //Null user test
        String null_user = null;
        try {
            MicroBlog.addUser(null_user);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        String user1 = "Mario";
        String user2 = "Stefano";
        try {
            MicroBlog.addUser(user1);
            MicroBlog.addUser(user2);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        ////////addLike test////////
        //null post
        Post null_post = null;
        try{
            MicroBlog.addLike(null_post, user1);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //null user
        try{
            MicroBlog.addLike(post1, null_user);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //self like
        String user3 = "Luca";
        try{
            MicroBlog.addLike(post1, user3);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //post not present
        Post post5 = new Post(5, "Lenny", "Oggi ho visto un bel film!");
        try{
            MicroBlog.addLike(post5, user1);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //addLike lecita
        try{
            MicroBlog.addLike(post1, user1);
            MicroBlog.addLike(post1, user2);
            MicroBlog.addLike(post3, user3);
            MicroBlog.addLike(post3, user1);
            MicroBlog.addLike(post4, user2);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //Like already present
        try{
            MicroBlog.addLike(post1, user1);
        } catch (Exception e){
            System.out.println(e.toString()); //An Exception is not expected
        }

        ////////writtenBy////////
        List<Post> returned_post = new ArrayList<Post>();

        //null username
        try{
            returned_post = MicroBlog.writtenBy(null_user);
        }catch (Exception e){
            System.out.println(e.toString());
        }

        //user not present in the social network
        try{
            returned_post = MicroBlog.writtenBy("Lenny");
        }catch (Exception e){
            System.out.println(e.toString());
        }

        //user has never posted
        try{
            returned_post = MicroBlog.writtenBy("Mario");
            assert returned_post.isEmpty();
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //writtenBy lecita
        try{
            List<Post> actually_posted = new ArrayList<Post>();
            actually_posted.add(post2);
            actually_posted.add(post4);
            returned_post = MicroBlog.writtenBy("Fabio");

            assert returned_post.containsAll(actually_posted);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        ////////static writtenBy////////
        List<Post> postlist_aux = null;
        List<Post> post_list = new ArrayList<Post>();
        List<Post> expected_result = new ArrayList<Post>();

        //empty post list
        try {
            postlist_aux = SocialNetwork.writtenBy(post_list, "Lenny");
            assert postlist_aux.isEmpty();
        } catch (Exception e){
            System.out.println(e.toString());
        }

        Post written_post = new Post(1, "Lenny", "Hello world!");
        post_list.add(written_post);
        expected_result.add(written_post);
        written_post = new Post(5, "Tommaso", "La quarantena è noiosa...");
        post_list.add(written_post);
        written_post = new Post(3,"Lenny", "Il mio messaggio è stato postato?");
        post_list.add(written_post);
        expected_result.add(written_post);
        written_post = new Post(6, "Lenny", "Prova prova");
        post_list.add(written_post);
        expected_result.add(written_post);
        written_post = new Post(8, "Luchino", "Ciao a tutti!");
        post_list.add(written_post);

        //null list
        try{
            postlist_aux = SocialNetwork.writtenBy(postlist_aux, "Someone");
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //null username
        try{
            postlist_aux = SocialNetwork.writtenBy(post_list, null_user);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //user never posted
        try{
            postlist_aux = SocialNetwork.writtenBy(post_list, "Mario");
            assert postlist_aux.isEmpty();
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //writtenBy lecita
        try{
            postlist_aux = SocialNetwork.writtenBy(post_list, "Lenny");
            assert postlist_aux.containsAll(expected_result);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        ////////influencers test////////
        List<String> influencers_results = MicroBlog.influencers();
        List<String> influncers_list = new ArrayList<String>();
        influncers_list.add("Luca");
        influncers_list.add("Marco");
        influncers_list.add("Fabio");
        assert influencers_results.containsAll(influncers_list);

        ////////getMentionedUsers test////////
        Set<String> users_returned = MicroBlog.getMentionedUsers();
        Set<String> users_expected = new HashSet<String>();
        users_expected.add("Fabio");
        users_expected.add("Marco");
        users_expected.add("Luca");

        assert users_returned.containsAll(users_expected);

        ////////static getMentionedUsers test////////
        //null post list
        List<Post> mentioned_post = null;
        Set<String> users_from_post;
        try{
            users_from_post = SocialNetwork.getMentionedUsers(mentioned_post);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //empty post list
        mentioned_post = new ArrayList<Post>();
        try{
            users_from_post = SocialNetwork.getMentionedUsers(mentioned_post);
            assert users_from_post.isEmpty();
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //static getMentionedUsers lecita
        Post aux_post = new Post(5, "Isabel", "Il mio gatto si chiama Ares");
        mentioned_post.add(aux_post);
        aux_post = new Post(10, "Francesca", "Oggi sono veramente stanca");
        mentioned_post.add(aux_post);
        aux_post = new Post(6, "Giulia", "Il tuo gatto ha veramente un nome fantastico @Isabel");
        mentioned_post.add(aux_post);
        aux_post = new Post(2, "Vanessa", "Chissà cosa c'è per pranzo oggi");
        mentioned_post.add(aux_post);
        aux_post = new Post(8,"Isabel", "Ho anche un cane che si chiama Teddy");
        mentioned_post.add(aux_post);

        users_from_post = SocialNetwork.getMentionedUsers(mentioned_post);
        List<String> expected_output = new ArrayList<String>();
        expected_output.add("Isabel");
        expected_output.add("Giulia");
        expected_output.add("Francesca");
        expected_output.add("Vanessa");

        assert users_from_post.containsAll(expected_output);


        ////////guessFollowers test////////
        List<Post> guess_from_post = null;
        Map<String, Set<String>> expected_followers = new HashMap<String, Set<String>>();
        Map<String, Set<String>> returned_followers;

        //null post list
        try{
            returned_followers = SocialNetwork.guessFollowers(guess_from_post);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //empty post list
        guess_from_post = new ArrayList<Post>();
        try{
            returned_followers = SocialNetwork.guessFollowers(guess_from_post);
            assert returned_followers.isEmpty();
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //static guessFollower lecita
        aux_post = new Post(5, "Chiara", "Ho litigato con Pasquale");
        guess_from_post.add(aux_post);
        aux_post = new Post(4, "Matteo", "Oggi sono andato ad allenarmi");
        guess_from_post.add(aux_post);
        aux_post = new Post(3, "Giulia", "@Chiara cosa è successo?");
        guess_from_post.add(aux_post);
        aux_post = new Post(6, "Matteo", "Sto uscendo di casa. @Giulia ti serve un passaggio?");
        guess_from_post.add(aux_post);
        aux_post = new Post(7, "Pasquale", "Per favore perdonami @Chiara");
        guess_from_post.add(aux_post);

        expected_followers.put("Chiara", new HashSet<String>());
        expected_followers.put("Matteo", new HashSet<String>());
        expected_followers.get("Matteo").add("Giulia");
        expected_followers.put("Giulia", new HashSet<String>());
        expected_followers.get("Giulia").add("Chiara");
        expected_followers.put("Pasquale", new HashSet<String>());
        expected_followers.get("Pasquale").add("Chiara");

        try{
            returned_followers = SocialNetwork.guessFollowers(guess_from_post);
            assert returned_followers.keySet().containsAll(expected_followers.keySet());
            for(String s : returned_followers.keySet())
                assert returned_followers.get(s).containsAll(expected_followers.get(s));
        } catch (Exception e){
            System.out.println(e.toString());
        }

        ////////containing test////////
        List<String> words = null;
        List<Post> post_from_word;

        //null words list
        try {
            post_from_word = MicroBlog.containing(words);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //empty words list
        words = new ArrayList<String>();
        try {
            post_from_word = MicroBlog.containing(words);
            assert post_from_word.isEmpty();
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //empty post_from_word expected
        words.add("Basket");
        words.add("tastiera");
        words.add("Foto");

        try {
            post_from_word = MicroBlog.containing(words);
            assert post_from_word.isEmpty();
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //containing lecita
        words.add("giornata");
        words.add("pescare");
        words.add("Luca");
        List<Post> aux_post_list = new ArrayList<Post>();
        aux_post_list.add(post3);
        aux_post_list.add(post1);
        aux_post_list.add(post2);
        try{
            post_from_word = MicroBlog.containing(words);
            assert post_from_word.containsAll(aux_post_list);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //////////////Batteria di test per FilteredSocialNetwork//////////////

        FilteredSocialNetwork FilteredMicroBlog = new FilteredSocialNetwork();
        List<Post> exp_rp = new ArrayList<Post>();
        List<Post> rp = new ArrayList<Post>();

        Post fp0 = null;
        Post fp1 = null;
        Post fp2 = null;
        Post fp3 = null;
        Post fp4 = null;
        Post fp5 = null;
        try{
            fp0 = FilteredMicroBlog.addPost("Lenny", "C140 a tt!");
            fp1 = FilteredMicroBlog.addPost("Davide", "Ciao!");
            FilteredMicroBlog.addLike(fp1, "Lenny");
        } catch (Exception e){
            System.out.println(e.toString());
        }

        ////////scanPost and getReportedPost with empty reported_post test////////
        FilteredMicroBlog.scanPost();
        rp = FilteredMicroBlog.getReportedPost();
        assert rp.isEmpty();


        try{
            fp2 = FilteredMicroBlog.addPost("Mauro", "@Lenny ma che cacchio hai scritto?");
            fp3 = FilteredMicroBlog.addPost("Lenny", "@Mauro sei scemo se non capisci quello che scrivo");
            fp4 = FilteredMicroBlog.addPost("Carlo", "@Mauro e @Lenny calmatevi, state facendo la figura dei deficienti");
            FilteredMicroBlog.addLike(fp4, "Stefano");
            FilteredMicroBlog.addLike(fp4, "Luca");
            FilteredMicroBlog.addLike(fp4, "Marco");
        } catch (Exception e){
            System.out.println(e.toString());
        }

        ////////scanPost and getReportedPost tests////////
        FilteredMicroBlog.scanPost();
        exp_rp.add(fp2);
        exp_rp.add(fp3);
        rp = FilteredMicroBlog.getReportedPost();
        assert rp.containsAll(exp_rp);

        ////////addBadWord test////////
        //bad word null
        String null_word = null;
        try {
            FilteredMicroBlog.addBadWord(null_word);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //addBadWord lecita
        FilteredMicroBlog.addBadWord("deficienti");
        FilteredMicroBlog.scanPost();
        rp = FilteredMicroBlog.getReportedPost();
        exp_rp.add(fp4);
        assert rp.containsAll(exp_rp);

        ////////reportPost test////////
        //post null
        try{
            FilteredMicroBlog.reportPost(null);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //post not present
        try{
            FilteredMicroBlog.reportPost(post1);
        } catch (Exception e){
            System.out.println(e.toString());
        }

        //reportPost lecita
        try{
            fp5 = FilteredMicroBlog.addPost("Tommaso", "Mi sembrate dei deficienti");
        } catch (Exception e){
            System.out.println(e.toString());
        }

        try{
            FilteredMicroBlog.reportPost(fp5);
        } catch (Exception e){
            System.out.println(e.toString());
        }
        exp_rp.add(fp5);
        rp = FilteredMicroBlog.getReportedPost();
        assert rp.containsAll(exp_rp);
    }
}
