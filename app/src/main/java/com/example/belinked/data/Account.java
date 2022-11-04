package com.example.belinked.data;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

public class Account {

    public static String accountType;

    public static void setAccountType(String at) {
        accountType = at;
    }

    @Nullable
    @Contract(pure = true)
    public static String getAccountType() {
        String accountValidation;
        if (accountType != null) {
            accountValidation = accountType;
        }
        else {
            accountValidation = null;
        }
        return accountValidation;
    }

}
