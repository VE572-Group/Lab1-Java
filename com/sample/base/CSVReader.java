package com.sample.base;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.DoubleStream;

public class CSVReader {

    public static class DataFrame {

        public ArrayList<String> name;
        public HashMap<String, String> quantity;
        public HashMap<String, String> unit;
        public HashMap<String, ArrayList<Double>> value;

        public DataFrame() {
            name = new ArrayList<String>();
            quantity = new HashMap<String, String>();
            unit = new HashMap<String, String>();
            value = new HashMap<String, ArrayList<Double>>();
        }

    }

    public static DataFrame read(String csvFile) {

        // String csvFile = "/Users/mkyong/csv/country.csv";
        String line = "";
        String cvsSplitBy = ",";
        DataFrame sDF = new DataFrame();
        int i = 0;
        try (BufferedReader input = new BufferedReader(new FileReader(csvFile))) {

            line = input.readLine();
            String[] arr_name = line.split(cvsSplitBy);
            for (String str : arr_name) {
                sDF.name.add(str);
                sDF.value.put(str, new ArrayList<Double>());
            }

            i = 0;
            line = input.readLine();
            String[] arr_unit = line.split(cvsSplitBy);
            for (String str : arr_name) {
                sDF.unit.put(str, arr_unit[i]);
                i++;
            }

            i = 0;
            line = input.readLine();
            String[] arr_quantity = line.split(cvsSplitBy);
            for (String str : arr_name) {
                sDF.quantity.put(str, arr_quantity[i]);
                i++;
            }

            while ((line = input.readLine()) != null) {
                i = 0;
                String[] arr_value = line.split(cvsSplitBy);
                for (String str : sDF.name) {
                    sDF.value.get(str).add(Double.parseDouble(arr_value[i]));
                    i++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sDF;
    }

}