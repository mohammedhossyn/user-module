package com.usermodule.utils;

import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 * User: Ali Reza Sahlolbey
 * Date: Oct 3, 2005
 * Time: 4:18:03 PM
 */
public class KeyGenerator {

  public KeyGenerator() {
  }

    public static byte[] getKey() {
    byte[] encryptionKey = null;
          String str = getObfuscateKey(3,"348887fd9920834abbe7e7e987bbac2");
          encryptionKey = new BigInteger(str, 16).toByteArray();
    return encryptionKey;
  }

  public static String getObfuscateKey(int index,String key){
    char[] charKey = key.toCharArray();
    StringBuilder str = new StringBuilder();
    for (int k = 1; k < index; k++) {
      str = new StringBuilder();
      for (int j = 0; j < k; j++) {
        for (int i = j; i < charKey.length; i = i + k) {
          str.append(Character.toString(charKey[i]));
        }
      }
      charKey = str.toString().toCharArray();
    }
    return str.toString();
  }
}
