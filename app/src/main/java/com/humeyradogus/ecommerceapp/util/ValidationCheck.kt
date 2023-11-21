package com.humeyradogus.ecommerceapp.util

import android.util.Patterns

fun validateEmail(email:String): RegisterValidation{
    if(email.isEmpty())
        return RegisterValidation.Failed("Make sure that you entered your e-mail!")

    if(Patterns.EMAIL_ADDRESS.equals(email))
        return RegisterValidation.Failed("Your e-mail format is incorrect!")

    return RegisterValidation.Success
}

fun validatePassword(password:String): RegisterValidation{
    if(password.isEmpty())
        return RegisterValidation.Failed("Make sure that you entered your password!")

    if(password.length < 6)
        return RegisterValidation.Failed("Your password should contain at least 6 character!")

    return RegisterValidation.Success
}