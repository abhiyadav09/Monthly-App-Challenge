package com.theblackcat102.fivechess.Game;

/**
 * Created by theblackcat on 12/3/17.
 */

public class Coordinate {
    public int x;
    public int y;

    public Coordinate(){

    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(int x, int y){
        this.x = x;
        this.y = y;
    }

}
