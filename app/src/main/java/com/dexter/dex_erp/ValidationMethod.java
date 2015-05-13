package com.dexter.dex_erp;

import android.widget.EditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationMethod {

    // method for email validation
    public static boolean checkEmail(String email) {
        Pattern email_id = Pattern.compile(".+@.+\\.[a-z]+");
        return email_id.matcher(email).matches();
    }

    // method to check edittext is empty or not

    public static boolean checkEmpty(EditText etText) {
        if (etText.getText().toString().trim().trim().length() > 0)
            return true;
        else
            return false;
    }

    // method to convert password in md5 format

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    // method to validate date

    public static boolean validate(final String date) {
        final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";

        Pattern pattern = Pattern.compile(DATE_PATTERN);

        Matcher matcher = pattern.matcher(date);

        if (matcher.matches()) {

            matcher.reset();

            if (matcher.find()) {

                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31")
                        && (month.equals("4") || month.equals("6")
                        || month.equals("9") || month.equals("11")
                        || month.equals("04") || month.equals("06") || month
                        .equals("09"))) {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                } else if (month.equals("2") || month.equals("02")) {
                    // leap year
                    if (year % 4 == 0) {
                        if (day.equals("30") || day.equals("31")) {
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        if (day.equals("29") || day.equals("30")
                                || day.equals("31")) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
