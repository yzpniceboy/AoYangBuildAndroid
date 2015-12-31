package com.saint.aoyangbuulid.charge.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zzh on 15-12-30.
 */
public class BillUtils {
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA);

    public static String genBillNum() {
        return simpleDateFormat.format(new Date());
    }

}
