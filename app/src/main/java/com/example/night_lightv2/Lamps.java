package com.example.night_lightv2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class Lamps {
    private HashMap<LampKey, HashSet<LampValue>> storage;

    public Lamps(){
        storage = new HashMap();
    }
    public void addLamp(double x, double y) {

        LampKey key = new LampKey(x ,y);
        LampValue value = new LampValue(x, y);
        if (storage.containsKey(key)) {
            storage.get(key).add(value);
        } else {
            HashSet<LampValue> hs = new HashSet<>();
            hs.add(value);
            storage.put(key,  hs);
        }

    }
    public void add(double[] arr) {
        LampKey key = new LampKey(arr[0] , arr[1]);
        LampValue value = new LampValue(arr[0] , arr[1]);
        if (storage.containsKey(key)) {
            storage.get(key).add(value);
        } else {
            HashSet<LampValue> hs = new HashSet<>();
            hs.add(value);
            storage.put(key,  hs);
        }
    }
    public HashSet<LampValue> getHashSet(double keyX, double keyY){
        LampKey key = new LampKey(keyX ,keyY);
        return (storage.get(key));

    }
    public boolean keyExists(double keyX, double keyY) {
        LampKey key = new LampKey(keyX ,keyY);

        return (storage.containsKey(key));
    }
    public boolean keyExists(LampKey key) {

        return (storage.containsKey(key));
    }
    public double[] getLampValueAsDoubleArray(double keyX, double keyY){
        LampKey key = new LampKey(keyX ,keyY);
        double[] dubArr = new double[storage.get(key).size() * 2];
        int counter = 0;
        for(LampValue lamp : storage.get(key)) {
            dubArr[counter++] = lamp.getX();
            dubArr[counter++] = lamp.getY();

        }
        return dubArr;
    }

    public double[][] getLampValueAsDoubleDoubleArray(double keyX, double keyY){
        LampKey key = new LampKey(keyX ,keyY);
        double[][] dubArr = new double[storage.get(key).size()][2];
        int counter = 0;
        int count = 2;
        for (LampValue lamp : storage.get(key)) {
            dubArr[counter][count%2] = lamp.getX();
            count++;
            dubArr[counter][count%2] = lamp.getY();
            count++;
            counter++;
        }
        return dubArr;
    }
    public ArrayList<LampValue> getSurroundingLamps(double keyX, double keyY, int range) {

        double difference = 0.0001;
        HashMap<Integer, LampKey> removeDup = new HashMap<>();
        ArrayList<LampValue> list = new ArrayList<>();
        for (int i = 0 - range; i < 1 + range; i++) {
            for(int j = 0 - range; j < 1 + range; j++) {
                LampKey key = new LampKey(keyX + (difference * i), keyY + ( difference * j));
                if(storage.containsKey(key)) {
                    removeDup.put(key.getPrimaryKey(), key);
                    System.out.println(keyX + (difference * i) + " : " + keyY + (difference * j) + "\t\t" + storage.containsKey(key));

                }
            }

        }
        for (Map.Entry<Integer, LampKey> entry: removeDup.entrySet()){

            for( LampValue value : storage.get(entry.getValue())) {
                list.add(value);
            }


        }
        return list;
    }

    
    public String print() {
        String s = "";
    int counter = 0;
        for (Map.Entry<LampKey, HashSet<LampValue>> entry : storage.entrySet()) {
            s += "KEY \tX:" + entry.getKey().getX() + "\t Y: " + entry.getKey().getY()+ "\n";
            for(LampValue lampVal : entry.getValue()){
                s+="\t\t\t" + lampVal.getX() + " : " + lampVal.getY() + "\n";
            }
            counter ++;
            if (counter == 200) break;
        }
        return s;
    }

}
