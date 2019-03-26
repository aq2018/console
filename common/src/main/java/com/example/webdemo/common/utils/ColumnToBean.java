package com.example.webdemo.common.utils;

import java.io.*;

/**
 * @author tangaq
 * @date 2019/2/27
 */
public class ColumnToBean {
    public static void main(String[] args) {
        readFile();
    }

    private static void readFile() {
        String src = "column.txt";
        String des = "bean.txt";

        InputStream inputStream = ColumnToBean.class.getClassLoader().getResourceAsStream(src);
        Reader in = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(in);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("G:/aMytest_proj/demo/src/main/resources/bean.txt");
            Writer writer = new OutputStreamWriter(outputStream);
            String line = reader.readLine();
            while (line!= null) {
                String bean = setField(line);
                writer.write(bean+";\n");
                writer.flush();
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String setField(String field) {
        if (!field.contains("_")) {
            return field;
        }
        String[] split = field.split("_");
        StringBuilder sb = new StringBuilder();
        sb.append(split[0]);
        for (int i = 1; i < split.length; i++) {
            String s = split[i];
            sb.append(String.valueOf(s.charAt(0)).toUpperCase());
            for (int j = 1; j < s.length();j ++) {
                sb.append(String.valueOf(s.charAt(j)));
            }
        }
        return sb.toString();
    }
}
