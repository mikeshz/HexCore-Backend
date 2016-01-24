/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Daniel
 */
public class Timing extends TimerTask {
    Main m;
    Timer timer;
    public Timing (Main m) {
        this.m = m;
        timer = new Timer ();
    }

    public void start (int sec) {
        timer.scheduleAtFixedRate (this, sec*1000, sec * 1000);
    }

    @Override
    public void run () {
        m.buzz ();
    }
}