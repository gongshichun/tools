package com.tongbaotu.smscenter.util;
/**
 * 检查类
 * @author Tim
 */
public class ParaChecker {
    /**
     * 检查参数有效性（单个）
     * @param para
     * @return 若该参数有效，返回TRUE；否则返回FALSE
     */
    public static boolean isValidPara(String para) {
        if (para == null || para.equals(""))
            return false;
        else
            return true;
    }

    public static boolean isValidPara(int para) {
        if (para == Integer.MIN_VALUE)
            return false;
        else
            return true;
    }

    public static boolean isValidPara(long para) {
        if (para == Long.MIN_VALUE)
            return false;
        else
            return true;
    }

    public static boolean isValidPara(double para) {
        if (para == Double.MIN_VALUE)
            return false;
        else
            return true;
    }

    public static boolean isValidDatePara(long para) {
        if (para == Long.MIN_VALUE || para == -1 || para == 0)
            return false;
        else
            return true;
    }

    public static boolean isNull(String para) {
        if (para == null || para.equals(""))
            return true;
        else
            return false;
    }
}
