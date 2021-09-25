package com.navi.file.helper

import java.util.regex.Pattern

object FormValidator {
    private val emailPattern: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    private val passwordPattern: Pattern = Pattern.compile(
        "^" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{8,}" +
                "$"
    )

    /**
     * Validate Email Address
     *
     * @param email email address to validate
     * @return true if email matches our regex pattern, false when it does not matches our regex pattern.
     */
    fun validateEmail(email: String): Boolean = emailPattern.matcher(email).matches()

    /**
     * Validate Password
     *
     * @param password password input to validate
     * @return true if password matches our regex pattern, false when it does not matches our regex pattern.
     */
    fun validatePassword(password: String): Boolean = passwordPattern.matcher(password).matches()

    /**
     * validate whole model, whether input model passes our regex check.
     * Internally, it just calls validateEmail and validatePassword.
     *
     * @param email email to validate.
     * @param password password to validate
     * @return true only when both email and password passes our regex.
     */
    fun validateModel(email: String, password: String): Boolean  {
        return validateEmail(email) && validatePassword(password)
    }
}