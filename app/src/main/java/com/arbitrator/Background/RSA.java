package com.arbitrator.Background;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.math.BigInteger;


public class RSA {

    private final Context context;


    public RSA(Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public RSA(Context context, SharedPreferences sp) {
        this.context = context;
    }

    public String Encode(String s, int k, int n) throws IOException {
        String s1 = "";


        for (int i = 0; i < s.length(); i++) {

            s1 += Ec(s.charAt(i), k, n) + "-";
        }


        return s1.substring(0, s1.length() - 1);
    }

    public String Decode(String s, int k, int n) throws IOException {
        String s1 = "";

        String ss[] = s.split("-");
        for (int i = 0; i < ss.length; i++) {

            s1 += (char) De(Integer.parseInt(ss[i]), k, n);
        }

        return s1;
    }

    //ENCODING
    public long Ec(int a, int k, int n) {
        BigInteger aa = new BigInteger(String.valueOf(k));
        BigInteger bb = new BigInteger(String.valueOf(n));
        BigInteger cc = new BigInteger(String.valueOf(a));
        BigInteger dd = cc.modPow(aa, bb);
        return dd.intValue();
    }

    //DECODING
    public int De(int a, int d, int n) throws IOException {

        BigInteger aa = new BigInteger(String.valueOf(d));
        BigInteger bb = new BigInteger(String.valueOf(n));
        BigInteger cc = new BigInteger(String.valueOf(a));
        BigInteger dd = cc.modPow(aa, bb);
        return dd.intValue();
    }

}
