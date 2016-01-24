/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Daniel
 */
import java.util.ArrayList;
import java.util.LinkedList;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;

public class Streamer extends Thread implements StatusListener {

    ArrayList<String> users = new ArrayList<>();
    TwitterStream twitterStream;
    FilterQuery filter;
    LinkedList<Status> data = new LinkedList<> ();
    Trending trending;

    public Streamer(Configuration config, Trending trending) {
        this.trending = trending;

        TwitterStreamFactory tsf = new TwitterStreamFactory(config);
        twitterStream = tsf.getInstance();

        twitterStream.addListener(this);
        filter = new FilterQuery();
        filter.track (trending.getKeyWords ());
    }

    @Override
    public void run() {
        twitterStream.filter(filter);
    }

    public int total () {
        return data.size();
    }
    public LinkedList<Status> getData () {
        return data;
    }

    public void resetData () {
        data.clear ();
    }

    @Override
    public void onStatus(Status status) {
        if ((status.getPlace () != null) && !users.contains ((status.getUser ().getName()))
                && trending.validate (status)) {
            data.add (status);
            users.add (status.getUser().getName());
        }
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {

    }

    @Override
    public void onStallWarning(StallWarning warning) {

    }

    @Override
    public void onException(Exception ex) {
        ex.printStackTrace();
    }
}