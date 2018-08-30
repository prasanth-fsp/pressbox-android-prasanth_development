package com.usepressbox.pressbox.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kruno on 12.04.16..
 * This class handles validation process for password and email
 */
public class ValidateCheckingClass {
    private Pattern pattern;
    private Pattern emailPattern;
    private Matcher matcher;


    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[A-Z]).{8,})";
    private static final String EMAIL_PATTERN = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

    public ValidateCheckingClass() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
        emailPattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
    }

    public boolean passwordValidate(final String password) {
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean emailValidate(final String emailID) {
        matcher = emailPattern.matcher(emailID);
        return matcher.matches();
    }
}
