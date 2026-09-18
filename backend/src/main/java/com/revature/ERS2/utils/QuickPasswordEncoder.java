package com.revature.ERS2.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class QuickPasswordEncoder {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "passwordm";
        String hash = passwordEncoder.encode(password);
        System.out.println(hash);
    }
}