package de.daver.unihub.util;

public class Useful {

    private Useful(){}

    public static String trimString(StringBuilder builder, int count){
        builder.setLength(builder.length() - count);
        return builder.toString();
    }
}
