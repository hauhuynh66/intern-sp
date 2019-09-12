package com.intern.utils;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Converter {
    public Date stringToDate(@Nullable String sDate, String format){
        if(sDate==null){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        try{
            date = simpleDateFormat.parse(sDate);
        }catch (ParseException p1){
            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                date = simpleDateFormat.parse(sDate);
            }catch(ParseException p2){
                simpleDateFormat = new SimpleDateFormat("dd-EEE-yyyy");
                try {
                    date = simpleDateFormat.parse(sDate);
                }catch (ParseException p3){
                    date = null;
                }
            }

        }
        return date;
    }
    public String dateToString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = "";
        try{
            sDate = simpleDateFormat.format(date);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return sDate;
    }
    public boolean stringToBoolean(String str){
        if(str.equals("1")||str.equals("x")){
            return true;
        }
        return false;
    }
    public int stringToInt(String str){
        if (str.isEmpty()){
            return 0;
        }
        else if(!str.contains(".")){
            return Integer.parseInt(str);
        }else{
            return Integer.parseInt(str.split("\\.")[0]);
        }
    }
    public String removeQuote(String str){
        if(!str.contains("(")){
            return str;
        }else{
            str = str.substring(0,str.indexOf("(")-1);
            return str;
        }
    }
    public Double stringToDouble(String str){
        if(str.contains("%")){
            return Double.parseDouble(str.substring(0,str.indexOf("%")))/100;
        }
        else if(str.isEmpty()){
            return Double.valueOf("0");
        }
        else{
            return Double.parseDouble(str);
        }
    }
}
