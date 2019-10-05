package com.test.recordparser;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class LogItemParser {

    public static List<LogItem> convert(String itemValue) {
        String[] split = itemValue.split("(?<=})");

        List<LogItem> listToReturn = new ArrayList<>(split.length);

        for (int i = 0; i < split.length; i++) {
            Gson gson = new Gson();
            if(split[i].trim().isEmpty()){
                continue;
            }
            listToReturn.add(gson.fromJson(split[i], LogItem.class));
        }

        return listToReturn;
    }
}
