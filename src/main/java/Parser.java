/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.LinkedList;
import twitter4j.Status;

/**
 *
 * @author Daniel
 */
public class Parser extends Thread {

    Trending trending;
    String [] countries;
    ArrayList<ArrayList<String>> messages;

    public Parser (Trending trending) {
        this.trending = trending;

        countries = trending.getCountries();
        resetData();
    }

    public void resetData () {
        messages = new ArrayList<ArrayList<String>>();
        for (String s : countries) {
            messages.add(new ArrayList<String>());
        }
    }

    public void setData(LinkedList<Status> data) {
        for (Status s : data) {
            for (int i = 0; i < countries.length; i++) {
                if (s.getPlace().getCountry().equalsIgnoreCase(countries[i])) {
                    messages.get(i).add(s.getText());
                }
            }
        }
    }

    public void printAll (){
        for (int i = 0; i < messages.size (); i ++) {
            System.out.println ("For: " + countries [i] + ":");
            for (String s: messages.get(i)){
                System.out.println (s);
            }
        }
    }

    public String[] getTextsFor (String countryName) {
        for (int i = 0; i < countries.length; i ++) {
            if (countryName.equalsIgnoreCase (countries [i])){
                return messages.get(i).toArray (new String [messages.get(i).size()]);
            }
        }
        return new String [] {"invalid country"};
    }

    @Override
    public void run () {

    }
}