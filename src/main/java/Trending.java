/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Arrays;
import java.util.logging.Level;
import twitter4j.*;

/**
 *
 * @author Daniel
 */
public class Trending {

    Twitter twitter;
    ResponseList<Location> locations;
    String[] countries = {"Canada", "United States", "United Kingdom"};
    final int size = countries.length;
    int[] codes = new int[size];
    Trends[] trends = new Trends[size];
    String[][] bank;
    int limit;

    public Trending(Twitter twitter, int limit) {
        this.twitter = twitter;
        bank = new String[size][limit];
        this.limit = limit;
        generateCodes();
        update();
        findTop ();
        //printAvailableTrends();
    }

    private void printAvailableTrends() {
        for (int i = 0; i < size; i++) {
            System.out.println("\nFor " + countries[i] + "\n");
            for (Trend trend : trends[i].getTrends()) {
                System.out.println(trend.getName());
            }
        }
    }

    private void generateCodes() {
        try {
            locations = twitter.getAvailableTrends();
            for (Location location : locations) {
                for (int i = 0; i < size; i++) {
                    if (location.getName().equals(countries[i])) {
                        codes[i] = location.getWoeid();
                    }
                }
            }
        } catch (TwitterException ex) {
            java.util.logging.Logger.getLogger(Trending.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }


    public String[] getCountries () {
        return countries;
    }

    public void update() {
        for (int i = 0; i < size; i++) {
            try {
                trends[i] = twitter.getPlaceTrends(codes[i]);
            } catch (TwitterException ex) {
                java.util.logging.Logger.getLogger(Trending.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    private void findTop () {
        for (int i = 0; i < size; i++) {
            Trend[] trend = trends[i].getTrends();
            int x = 0;
            for (int n = 0; n < limit; n++) {
                String temp = trend[x].getName();
                if (temp.indexOf ("#") == -1)
                    n --;
                else
                    bank [i][n] = temp;
                x++;
            }
        }
    }


    public String[] getTopTrends (String country){
        for (int i = 0; i < countries.length; i ++) {
            if (country.equalsIgnoreCase (countries [i]))
                return Arrays.copyOf (bank[i], 5);
        }
        return new String[] {""};
    }


    public String[] getKeyWords() {
        String[] results = new String[limit* size];
        for (int i = 0; i < size; i++) {
            for (int n = 0; n < limit; n++) {
                results[(i * limit) + n] = bank[i][n];
            }
        }
        return results;
    }

    public boolean validate(Status stat) {
        for (int i = 0; i < size; i++) {
            if (stat.getPlace().getCountry().equals(countries[i])) {
                for (int n = 0; n < limit; n++) {
                    if ((stat.getText().indexOf((bank[i][n]))) != -1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}