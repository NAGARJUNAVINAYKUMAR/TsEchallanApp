package com.tspolice.echallan.utils;

import java.util.regex.Pattern;

/**
 * Created by Srinivas on 10/5/2018.
 */

public class ValidationUtils {

    public static boolean isValidEmaillId(String email) {

        return Pattern
                .compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
                .matcher(email).matches();
    }

    public static boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 10 || phone.length() > 10) {
                // if(phone.length() != 10) {
                check = false;
            } else if (phone.length()==10){
                if (((phone.charAt(0)=='6')) || ((phone.charAt(0)=='7'))|| ((phone.charAt(0)=='8'))
                        || ((phone.charAt(0)=='9'))) {
                    check = true;
                }else {
                    check = false;
                }
            }
        } else {
            check = false;
        }
        return check;
    }

    private boolean isValidMobileNumber(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
