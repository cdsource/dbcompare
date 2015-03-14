/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2013年12月30日 下午3:49:31 
* 类说明 
*/ 

package org.jpf.utils;

import java.math.BigDecimal;  
import java.text.DecimalFormat;  
 
public class DoubleUtil {  
    private static final int DEF_DIV_SCALE = 10;  
  
    public static double scale2(double v) {  
        BigDecimal bd = new BigDecimal(v);  
        bd = bd.setScale(2, 4);  
        return bd.doubleValue();  
    }  
  
    public static int getIntNum(Double value) {  
        DecimalFormat df = new DecimalFormat("#");  
        String str = df.format(value).replaceAll("-", "");  
        return str.length();  
    }  
  
    public static void main(String[] args) {  
        Double double1 = Double.valueOf(-3.126D);  
        System.out.println(scale2(double1.doubleValue()));  
        System.out.println(changeDecimal(double1.doubleValue(), 2));  
        System.out.println("0.1*0.1=" + mul(Double.valueOf(0.1D), Double.valueOf(0.1D)));  
        System.out.println("1.0-0.1=" + sub(Double.valueOf(1.0D), Double.valueOf(0.1D)));  
        System.out.println("0.1+0.1=" + add(Double.valueOf(0.1D), Double.valueOf(0.1D)));  
        System.out.println(getIntNum(double1));  
        System.out.println(toDecimalString("4444564321.2135", 2));  
    }  
  
    public static double changeDecimal(double value, int num) {  
        BigDecimal b = new BigDecimal(value);  
        double v = b.setScale(num, 4).doubleValue();  
        return v;  
    }  
  
    public static String toDecimalString(String str, int decimal) {  
        if (null==str) {  
            return "";  
        }  
        return toDecimalString(Double.valueOf(str.trim()), decimal);  
    }  
  
    public static String toDecimalString(Double num, int decimal) {  
        if (num == null) {  
            return "";  
        }  
        String str = num.toString();  
        String formatStr = "#.";  
        for (int i = 0; i < decimal; ++i) {  
            formatStr = formatStr + "0";  
        }  
        DecimalFormat df = new DecimalFormat(formatStr);  
        try {  
            str = df.format(num);  
        } catch (Exception localException) {  
        }  
        return str;  
    }  
  
    public static String toSplitDecimalString(Double num, int decimal) {  
        if (num == null) {  
            return "";  
        }  
        String str = num.toString();  
        String formatStr = "###,###.";  
        for (int i = 0; i < decimal; ++i) {  
            formatStr = formatStr + "0";  
        }  
        DecimalFormat df = new DecimalFormat(formatStr);  
        try {  
            str = df.format(num);  
        } catch (Exception localException) {  
        }  
        return str;  
    }  
  
    public static String toSplitDecimalString(String str, int decimal) {  
        if (null==str) {  
            return "";  
        }  
        return toSplitDecimalString(Double.valueOf(str.trim()), decimal);  
    }  
  
    public static Double add(Double v1, Double v2) {  
        BigDecimal b1 = new BigDecimal(v1.toString());  
        BigDecimal b2 = new BigDecimal(v2.toString());  
        return Double.valueOf(b1.add(b2).doubleValue());  
    }  
  
    public static Double add(Double v1, Double v2, int num) {  
        return Double.valueOf(changeDecimal(add(v1, v2).doubleValue(), num));  
    }  
  
    public static Double sub(Double v1, Double v2) {  
        BigDecimal b1 = new BigDecimal(v1.toString());  
        BigDecimal b2 = new BigDecimal(v2.toString());  
        return Double.valueOf(b1.subtract(b2).doubleValue());  
    }  
  
    public static Double sub(Double v1, Double v2, int num) {  
        return Double.valueOf(changeDecimal(sub(v1, v2).doubleValue(), num));  
    }  
  
    public static Double mul(Double v1, Double v2) {  
        BigDecimal b1 = new BigDecimal(v1.toString());  
        BigDecimal b2 = new BigDecimal(v2.toString());  
        return Double.valueOf(b1.multiply(b2).doubleValue());  
    }  
  
    public static Double mul(Double v1, Double v2, int num) {  
        return Double.valueOf(changeDecimal(mul(v1, v2).doubleValue(), num));  
    }  
  
    public static Double div(Double v1, Double v2) {  
        BigDecimal b1 = new BigDecimal(v1.toString());  
        BigDecimal b2 = new BigDecimal(v2.toString());  
        return Double.valueOf(b1.divide(b2, 10, 4).doubleValue());  
    }  
  
    public static Double div(Double v1, Double v2, int scale) {  
        if (scale < 0) {  
            throw new IllegalArgumentException("The scale must be a positive integer or zero");  
        }  
        BigDecimal b1 = new BigDecimal(v1.toString());  
        BigDecimal b2 = new BigDecimal(v2.toString());  
        return Double.valueOf(b1.divide(b2, scale, 4).doubleValue());  
    }  
}  
