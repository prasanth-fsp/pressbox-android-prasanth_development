package com.usepressbox.pressbox.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by kruno on 12.04.16..
 * This class handles the conversion of url
 */
public class Signature {


    public Signature() {
    }

    public static String getUrlConversion(HashMap<String, String> namevaluepair) {
        String result = null;
        try {
            String urlConversion = urlEncodeParams(namevaluepair);

            if (urlConversion.contains("+") || urlConversion.contains("*")) {
                if (urlConversion.contains("+")) {
                    System.out.println("condition ok");
                    urlConversion = urlConversion.replace("+", "%20");
                }

                if (urlConversion.contains("*")) {
                    urlConversion = urlConversion.replace("*", "%2A");
                }
            }
            result = hashMac(urlConversion.toLowerCase(), Constants.SECERET_KEY);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return sb.toString();
    }

    public static String hashMac(String text, String secretKey)
            throws SignatureException, UnsupportedEncodingException {
        try {
            Key sk = new SecretKeySpec(secretKey.getBytes("UTF-8"), Constants.HASH_ALGORITHM);
            Mac mac = Mac.getInstance(sk.getAlgorithm());
            mac.init(sk);
            final byte[] hmac = mac.doFinal(text.getBytes("UTF-8"));

            return toHexString(hmac);

        } catch (NoSuchAlgorithmException e1) {
            throw new SignatureException(
                    "error building signature, no such algorithm in device "
                            + Constants.HASH_ALGORITHM);
        } catch (InvalidKeyException e) {
            throw new SignatureException(
                    "error building signature, invalid key " + Constants.HASH_ALGORITHM);
        }
    }

    public static String urlEncodeParams(HashMap<String, String> namevaluepair) {

        StringBuilder result = new StringBuilder();
        boolean first = true;
        Set<Map.Entry<String, String>> entries = namevaluepair.entrySet();

        Comparator<Map.Entry<String, String>> valueComparator = new Comparator<Map.Entry<String, String>>() {

            public int compare(Map.Entry<String, String> entry1,
                               Map.Entry<String, String> entry2) {
                // TODO Auto-generated method stub
                return entry1.getKey().compareTo(entry2.getKey());
            }
        };

        List<Map.Entry<String, String>> listOfEntries = new ArrayList<Map.Entry<String, String>>(entries);

        Collections.sort(listOfEntries, valueComparator);

        for (Map.Entry<String, String> entry : listOfEntries) {
            if (first)
                first = false;
            else
                result.append("%26");

            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("%3D");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return result.toString();
    }
}
