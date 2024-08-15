package org.otsvgenerator.parser;

public class TimestampParser {
    private static final int MILLIS_PER_SECOND = 1000;
    private static final int MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    public int convertToMs(String clientTimestamp) {
        String[] timestampArr = clientTimestamp.split(":");
        int minute = Integer.parseInt(timestampArr[0]);
        int second = Integer.parseInt(timestampArr[1]);
        int millisecond = Integer.parseInt(timestampArr[2]);
        return minute * MILLIS_PER_MINUTE + second * MILLIS_PER_SECOND + millisecond;
    }

    public static void main(String[] args) {
        TimestampParser timestampParser = new TimestampParser();
        System.out.println(timestampParser.convertToMs("01:35:733"));
    }
}
