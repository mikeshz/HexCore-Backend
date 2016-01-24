/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import static spark.Spark.*;

import java.util.ArrayList;
import java.util.logging.Level;

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


/**
 *
 * @author Daniel
 */
public class Main {

    Trending t;
    Streamer s;
    Parser p;

    ArrayList<String> countries = new ArrayList<String>();
    ArrayList<String[]> texts = new ArrayList<String[]>();
    ArrayList<String[]> trendingPhrases = new ArrayList<String[]>();

    private String outString = "Initialized outString";

    public Main() {
        OAuth auth = new OAuth();
        Configuration config = auth.getConfig();

        TwitterFactory tf = new TwitterFactory(config);
        Twitter twitter = tf.getInstance();

        t = new Trending(twitter, 20);
        s = new Streamer(config, t);
        p = new Parser(t);
        Timing timer = new Timing(this);

        s.run();
        timer.start(150);

        get("/", (req, res) -> outString);
        get("/hello", (req, res) -> {
            return "Returning some data";
        });

        get("/mood", (req, res) -> {
            return "ayy lmao";
        });
    }

    /*
     * "hub" for the program. This method will trigger ever x-seconds the
     * processing of the data is done throught getTextsFor(). you should have a
     * method that takes in at least a 1D array representing the total amount of
     * tweets tweeted in 1 country during the x-second interval
     */
    public void buzz() {
        p.setData(s.getData());

        update();
        s.resetData();
        p.resetData();
    }

    private void update() {
        countries.clear();
        texts.clear();
        trendingPhrases.clear();
        for (String s : t.getCountries()) {
            countries.add(s);
            texts.add(p.getTextsFor(s));
            trendingPhrases.add(t.getTopTrends(s));
        }
        doStuff();
    }

    private void doStuff() {
        for (int i = 0; i < countries.size(); i++) {
            System.out.print("\n" + countries.get(i) + " has " + texts.get(i).length + " tweet(s). ");
            System.out.println("The top 5 trending hashtags are: ");
            for (String s : trendingPhrases.get(i)) {
                System.out.print(s + " ");
            }
            System.out.println("\nUsers tweeted: ");
            for (String s : texts.get(i)) {
                System.out.println("[#]" + s);
            }
        }

        outString = "Tweets have been grabbed!";
    }

    public static void main(String[] args) {
        Main m = new Main();
    }
}