package com.ctrip.implus;

import java.util.HashMap;

/**
 * Created by chengyq on 2016/10/26.
 */
public class HttpHeader {

    public HttpHeader(String text) {
        this.data = new HashMap<String, String>();
        String[] arr = text.split("\r\n");
        for(int i = 0; i<arr.length; i++){
            String line = arr[i];
            if(null == line) {
                continue;
            }
            line = line.trim();
            if("" == line){
                continue;
            }

            String[] arr1 = line.split(":");
            if(arr1.length != 2){
                continue;
            }

            this.data.put(arr1[0], arr1[1].trim());
        }
    }

    public String get(String key) {
        return this.data.get(key);
    }

    private HashMap<String,String> data;
}
