/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import twitter4j.*;
import twitter4j.auth.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Daniel
 */
public class OAuth {

    private static final String C_KEY = "q3kkDmb2u4v59Ae5ODJlfhaSM";
    private static final String C_SECRET = "BthjCVnsPKjwyuTOWXoawMMJLsKPWaLcKcd0tVAe5ypg4IiJlF";
    private static final String A_TOKEN = "361466701-ksHhDyiIHnFpPEugsSMu4a7QXd602rxcA9i49uRR";
    private static final String A_SECRET = "xk8Dlmz2DPScK5px4B5V68hM5DrfxMoSuda3XaJvOJq0d";

    private Configuration config;

    public OAuth() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(C_KEY)
                .setOAuthConsumerSecret(C_SECRET)
                .setOAuthAccessToken(A_TOKEN)
                .setOAuthAccessTokenSecret(A_SECRET);
        config = cb.build();
    }

    public Configuration getConfig() {
        return config;
    }

    private void reauthorize() throws Exception {
        // In case we lose the A_SECRET
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(C_KEY, C_SECRET);
        RequestToken requestToken = twitter.getOAuthRequestToken();
        AccessToken accessToken = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (null == accessToken) {
            System.out.println("Open the following URL and grant access to your account:");
            System.out.println(requestToken.getAuthorizationURL());
            System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
            String pin = br.readLine();
            try {
                if (pin.length() > 0) {
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                } else {
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if (401 == te.getStatusCode()) {
                    System.out.println("Unable to get the access token.");
                } else {
                    te.printStackTrace();
                }
            }
        }
    }

    private static void show(int useId, AccessToken accessToken) {
        System.out.println("key: " + C_KEY);
        System.out.println("key secret:" + C_SECRET);
        System.out.println("token: " + accessToken.getToken());
        System.out.println("secret: " + accessToken.getTokenSecret());
    }
}