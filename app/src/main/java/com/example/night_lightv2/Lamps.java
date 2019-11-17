package com.example.night_lightv2;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Lamps {
    private HashMap<LampKey, HashSet<LampValue>> storage;
    private double difference = 0.0001;
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
    public HashSet<LampValue> getSurroundingLamps(double keyX, double keyY, int range) {

        HashMap<Integer, LampKey> removeDup = new HashMap<>();
        HashSet<LampValue> list = new HashSet<>();
        for (int i = 0 - range; i < 1 + range; i++) {
            for(int j = 0 - range; j < 1 + range; j++) {
                LampKey key = new LampKey(keyX + (difference * i), keyY + ( difference * j));
                if(storage.containsKey(key)) {
                    removeDup.put(key.getPrimaryKey(), key);
                    //           System.out.println(keyX + (difference * i) + " : " + keyY + (difference * j) + "\t\t" + storage.containsKey(key));

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

    public HashSet<LampValue> getSurroundingLamps(HashSet<LampKey> lampkeylist, int range) {
        HashSet<LampValue> removeDup = new HashSet<>();
        //for each  key in the hashsetlist
        for (LampKey key : lampkeylist){
            for(int i = 0 - range; i <  range; i++) {
                for (int j = 0 - range; j < range; j++) {

                    //create ranged Key
                    LampKey rangedKey = new LampKey(key.getX() + (difference * i), key.getY() + (difference * j));
                    //if the key exists within the storage aka it's in the data set
                    if (storage.containsKey(rangedKey)) {
                        //for each lampvalue within the storage key
                        for (LampValue value : storage.get(rangedKey)) {
                            removeDup.add(value);
                        }
                    }
                }

            }
        }
        return removeDup;

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
    public HashSet<LampKey> getLampsOnRoute(List<LatLng> path, int range) {
        HashSet<LampKey> list = new HashSet<>();
        System.out.println("129");

        for (int i = 0; i < path.size() -  1; i++){
            LampKey key = new LampKey(path.get(i).latitude, path.get(i).longitude);
            LampKey key1 = new LampKey(path.get(i + 1).latitude, path.get(i + 1).longitude);
            list.add(key);
            list.add(key1);
            if(notWithinRange(path.get(i), path.get(i + 1), range)){
                System.out.println("130");
                list = getInBetweenTwoPath(path.get(i), path.get(i + 1), range, list);
            }
        }

        return list;
    }
    public boolean notWithinRange(LatLng one, LatLng two, int range){
        System.out.println("139\t one X: "+ one.latitude + "Y:" + one.longitude);
        System.out.println("\t two X: "+ two.latitude + "Y:" + two.longitude);
        System.out.println("\t" + Math.abs(Math.abs(one.latitude) - Math.abs(two.latitude) + (difference * range)));
        System.out.println("\tisitevertrue" + ( (Math.abs(Math.abs(one.latitude) - Math.abs(two.latitude))) > (difference * range * 2)) );
        return ( ( (Math.abs(Math.abs(one.latitude) - Math.abs(two.latitude))) > (difference * range * 2)) ||
                ( (Math.abs(Math.abs(one.longitude) - Math.abs(two.longitude))) > (difference * range * 2)));
    }
    public HashSet<LampKey> getInBetweenTwoPath(LatLng one, LatLng two, int range, HashSet<LampKey> list){
        double diffX = two.latitude - one.latitude;
        double diffY = two.longitude - one.longitude;
        System.out.println("151: diffX" + diffX + "diffY " + diffY + " " +( Math.abs(diffX) > Math.abs(diffY)));
        double offset = .8;
        if (Math.abs(diffX) > Math.abs(diffY)){
            System.out.println("");
            double multiple =Math.abs( (diffX / ( range * difference)));
            diffX = diffX / multiple;
            diffY = diffY / multiple;
            System.out.println("153: diffX" + diffX + "diffY " + diffY + "multiple" + multiple);

            for(int i = 1; i <= multiple; i++ ){
                LampKey key = new LampKey(one.latitude + (diffX * range  * i ), one.longitude + (diffY * i  * range * offset));
                System.out.println("key 153");
                list.add(key);
            }
        } else{
            double multiple = Math.abs( (diffY / ( range * difference)));
            diffX = diffX / multiple;
            diffY = diffY / multiple;
            System.out.println("163: diffX" + diffX + "diffY " + diffY + "multiple" + multiple);

            for(int i = 1; i <= (int) Math.abs(multiple); i++ ){
                LampKey key = new LampKey(one.latitude + (diffX * range * i), one.longitude + (diffY *  i *  range * offset));
                list.add(key);
            }

        }

        return list;
    }
    public HashMap<LampKey, HashSet<LampValue>> getStorage(){
        return storage;
    }


}
