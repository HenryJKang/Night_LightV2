package com.example.night_lightv2;

public class LampValue {
    private double x;
    private double y;
    LampValue(double x, double y){
        this.x = Math.round((double) x * 100000) / 100000.0;
        this.y = Math.round((double) y * 100000) / 100000.0;


    }
    public double[] getValue() {
        double[] a = {x, y};
        return a;
    }
    public double getX() {
        return x;

    }
    public double getY() {
        return y;
    }
    public void print() {
        System.out.println("x" + x + "\t" + y);
    }
    public String  getPrimaryKey() {


        double thisX = ((int)x % 100000) * -1.0;
        double thisY = ((int)y % 100000) * -1.0;

        int balanceX =(int)((x + thisX)*10000);
        int balanceY =(int)((y + thisY)*10000);
        if(balanceX < 0) {
            balanceX *= -1;
        }
        if(balanceY < 0) {
            balanceY *= -1;
        }
        String a = "" + balanceX + balanceY;
        return a;

    }

    @Override
    public int hashCode()
    {
        double thisX = ((int)x % 100000) * -1.0;
        double thisY = ((int)y % 100000) * -1.0;
        int balanceX =(int)((x + thisX)*10000);
        int balanceY =(int)((y + thisY)*10000);
        if(balanceX < 0) {
            balanceX *= -1;
        }
        if(balanceY < 0) {
            balanceY *= -1;
        }
        String a = "" + (balanceX / 10) + balanceY;
        return Integer.parseInt(a);
    }
    @Override
    public boolean equals(Object a)
    {
        return (this.x == ((LampValue)a).x && this.y == ((LampValue)a).y);
    }
}
