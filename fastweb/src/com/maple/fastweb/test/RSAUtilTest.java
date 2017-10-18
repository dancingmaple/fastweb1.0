package com.maple.fastweb.test;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * RSA算法，实现数据的加密解密。
 */
public class RSAUtilTest {
    private static Cipher cipher;

    static{
        try {
            cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成密钥对
     * @param filePath 生成密钥的路径
     * @return
     */
    public static Map<String,String> generateKeyPair(String filePath){
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            // 密钥位数
            keyPairGen.initialize(2048);
            // 密钥对
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 公钥
            PublicKey publicKey = keyPair.getPublic();
            // 私钥
            PrivateKey privateKey = keyPair.getPrivate();
            //得到公钥字符串
            String publicKeyString = getKeyString(publicKey);
            //得到私钥字符串
            String privateKeyString = getKeyString(privateKey);

            //将密钥对写入到文件
            FileWriter pubfw = new FileWriter(filePath + "/publicKey.keystore");
            FileWriter prifw = new FileWriter(filePath + "/privateKey.keystore");
            BufferedWriter pubbw = new BufferedWriter(pubfw);
            BufferedWriter pribw = new BufferedWriter(prifw);
            pubbw.write(publicKeyString);
            pribw.write(privateKeyString);
            pubbw.flush();
            pubbw.close();
            pubfw.close();
            pribw.flush();
            pribw.close();
            prifw.close();
            //将生成的密钥对返回
            Map<String,String> map = new HashMap<String,String>();
            map.put("publicKey", publicKeyString);
            map.put("privateKey", privateKeyString);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到公钥
     *
     * @param key
     *            密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 得到私钥
     *
     * @param key
     *            密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 得到密钥字符串（经过base64编码）
     *
     * @return
     */
    public static String getKeyString(Key key) throws Exception {
        byte[] keyBytes = key.getEncoded();
        String s = (new BASE64Encoder()).encode(keyBytes);
        return s;
    }

    /**
     * 使用公钥对明文进行加密，返回BASE64编码的字符串
     * @param publicKey
     * @param plainText
     * @return
     */
    public static String encrypt(PublicKey publicKey, String plainText){
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] enBytes = cipher.doFinal(plainText.getBytes());
            return (new BASE64Encoder()).encode(enBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用keystore对明文进行加密
     * @param publicKeystore 公钥文件路径
     * @param plainText      明文
     * @return
     */
    public static String fileEncrypt(String publicKeystore, String plainText){
        try {
            FileReader fr = new FileReader(publicKeystore);
            BufferedReader br = new BufferedReader(fr);
            String publicKeyString="";
            String str;
            while((str=br.readLine())!=null){
                publicKeyString+=str;
            }
            br.close();
            fr.close();
            cipher.init(Cipher.ENCRYPT_MODE,getPublicKey(publicKeyString));
            byte[] enBytes = cipher.doFinal(plainText.getBytes());
            return (new BASE64Encoder()).encode(enBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用公钥对明文进行加密
     * @param publicKey      公钥
     * @param plainText      明文
     * @return
     */
    public static String encrypt(String publicKey, String plainText){
        try {
            cipher.init(Cipher.ENCRYPT_MODE,getPublicKey(publicKey));
            byte[] enBytes = cipher.doFinal(plainText.getBytes());
            return (new BASE64Encoder()).encode(enBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用私钥对明文密文进行解密
     * @param privateKey
     * @param enStr
     * @return
     */
    public static String decrypt(PrivateKey privateKey, String enStr){
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] deBytes = cipher.doFinal((new BASE64Decoder()).decodeBuffer(enStr));
            return new String(deBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用私钥对密文进行解密
     * @param privateKey       私钥
     * @param enStr            密文
     * @return
     */
    public static String decrypt(String privateKey, String enStr){
        try {
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
            byte[] deBytes = cipher.doFinal((new BASE64Decoder()).decodeBuffer(enStr));
            return new String(deBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用keystore对密文进行解密
     * @param privateKeystore  私钥路径
     * @param enStr            密文
     * @return
     */
    public static String fileDecrypt(String privateKeystore, String enStr){
        try {
            FileReader fr = new FileReader(privateKeystore);
            BufferedReader br = new BufferedReader(fr);
            String privateKeyString="";
            String str;
            while((str=br.readLine())!=null){
                privateKeyString+=str;
            }
            br.close();
            fr.close();
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKeyString));
            byte[] deBytes = cipher.doFinal((new BASE64Decoder()).decodeBuffer(enStr));
            return new String(deBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
    //	generateKeyPair("D:/RSA");

        String publicKey;
        String privateKey;

    //    publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAviSuCu4Yg/WAyjp06qiaE/ioI2M/ACT9UTUVxWtM7IZlXMQZPjLn0H1x0zmJ/VLIhnBliyb06QLvtrrBFRt4jnOJR5LjoTg/g8XYdVXN6a+XFjqFvOUPgzZ7OdywOoXxiO+M7WrvT0XgqyBqCnDADpY1eucDqfIDYYOBHKbtMkh0N4ZVBcfULb1Sm+Q7ed+jUa8eXPQPhMrWvhQkIeZJh+hCIrNjXUxyfZPh1tSvqoJYArbyHZs8LnbUtjIQCx9OlR9+xJTx3L9h89I4D+hqA4CZqxUzfibsu5XgYKnoSri2OCR2FefSfYlCd8Fysp0wET/r1L141qnhoMQtrUs8jwIDAQAB";
    //    privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC+JK4K7hiD9YDKOnTqqJoT+KgjYz8AJP1RNRXFa0zshmVcxBk+MufQfXHTOYn9UsiGcGWLJvTpAu+2usEVG3iOc4lHkuOhOD+Dxdh1Vc3pr5cWOoW85Q+DNns53LA6hfGI74ztau9PReCrIGoKcMAOljV65wOp8gNhg4Ecpu0ySHQ3hlUFx9QtvVKb5Dt536NRrx5c9A+Eyta+FCQh5kmH6EIis2NdTHJ9k+HW1K+qglgCtvIdmzwudtS2MhALH06VH37ElPHcv2Hz0jgP6GoDgJmrFTN+Juy7leBgqehKuLY4JHYV59J9iUJ3wXKynTARP+vUvXjWqeGgxC2tSzyPAgMBAAECggEAMhFkhtpFOFIoFJgp+zRkRgf+9jqG91nGHmEVF4P2oH2PKUs1vmwXII43r8AB9uOai9QC2Q5sBQNR7dLlTtKJ/zCrIF6sc+JkzyUEp3jtnLAw35iPaLsER6/L6OOUwARPIpi5ijbTRxOGYmlJovAnkm+5K2CzVUe13jKLh+joool/ReZk0Rsr4tVLSLmvzDA/sRwYun0x0+jl5EZSQfwsVyN9bD5rY/In/EuvH9yj5R4lPe+mimF4Os6IgTsP5LzqDTAiFx5NNioFRJ2SkcTmM0CZQeMIBuvvF2HCtJlDEfCytD7wYup3GBvar2ccOe9T3YhJdsj5bfAJHVJtamxQwQKBgQDnYReMMzqAh2HOFL8QymzOjImsrOz6NCZatq38TU1hSe9PK+C0sFGhkd788y4AuURS1Btu4i7F+hOYcj1z3L+NSPGE3yLHVjakMrrNbA9rwG/t7oU0cG7d0WWM9bcTQiCSNcUyt69BGH3dZdqee1tITzqghE7+gh9RYiVcI6/8LwKBgQDSYEuWFLMUsR/s7unSHCucuEXjwbYrvknv8Y81sjvrWktNXrJoYlbGy/7HYA6lxzchtSxhPuUSjopwQ5scgMhqf8Gxz7jsDN9ak2dErF7cWRFYfh6aKhkbEw9oG01jIX15MK0TbMafoJslDhPQF1cP9i0+ZGg+gPbASdeUVRTNoQKBgQCOjwDOLgYeiMtXCOtL8hymCmsNDCKaaiUzgRijuhEyHzamJhe13Gj/TnwAh+hRI9UX333jjNJawqDuLXz1dQ5Eg6vjPQQVo2XZNzRnOuwpbJDKHUrPK3Lzkn+qIP6ii/y7eQu+GvSM/AUYsxfGy6RLYh1yJvLw1sVrBDiWk5prmwKBgFvgrmI3XBa3XKgPl5KptupVGEDmAveLvaLLLq5WzxB0eNqrduNbv2ZHBVhxvTPtk0hnZaB65XR7SD7LZ9zE6cKJVUCg5bRB0vIt2jYFydAWHhs1yYuuwxQt+NaQxfV7VN8uwQfww7ZHYDqIsWJ6Lw3Lh+rt0xEpJZrJJRulJNbBAoGBAK1OEnfBpSB99N8gdhp+ZGLsDfwFCQ2Cd4Jpsd4hxdwbXevNuA1OiE20sHPuKqEqfOKocgTMobCwbSfnymatRydVoeUumkEc4Ja+XDgH+P1eXQLdIuRCwh0AXl+vkuOCBDMw367Zp/j6vwPlNKh9ZmOBPwhV0Syv2Z8uGkTZ6g+f";
        publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAop1m+Lnf9SnycnbJMuSvRcc0P8vIX9UI" +
                "1GRs9l3+GNAAObrdHL+M22tPv9x4496vGBovacjMhWw5c7vUhQslNNeqCZhG5KgRCA1OS0/olyxY" +
                "HsYVWKUhOvX8y4+TD3oavaWCIx/DwdklTQhV4drou4iiaJgmsrwetZUrXJL13c2A2hF+u81zP4XJ" +
                "I1K65AqfPzVT2bZiHqbmVAqVT1497bPqplZOwnBNSjm/0S8EztLsufSdqcaKxrP3MF0kJPwRgv4X" +
                "jDDzTJP3/EET3QZewptv9Z8jOvkhiLd2ALRDV3wxkN6+Op1757oNZ93yvjhGslLTscvO7+NQuywL" +
                "pPlSfQIDAQAB";
        privateKey ="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCinWb4ud/1KfJydsky5K9FxzQ/" +
                "y8hf1QjUZGz2Xf4Y0AA5ut0cv4zba0+/3Hjj3q8YGi9pyMyFbDlzu9SFCyU016oJmEbkqBEIDU5L" +
                "T+iXLFgexhVYpSE69fzLj5MPehq9pYIjH8PB2SVNCFXh2ui7iKJomCayvB61lStckvXdzYDaEX67" +
                "zXM/hckjUrrkCp8/NVPZtmIepuZUCpVPXj3ts+qmVk7CcE1KOb/RLwTO0uy59J2pxorGs/cwXSQk" +
                "/BGC/heMMPNMk/f8QRPdBl7Cm2/1nyM6+SGIt3YAtENXfDGQ3r46nXvnug1n3fK+OEayUtOxy87v" +
                "41C7LAuk+VJ9AgMBAAECggEAJELhMVmRfUPrUmb2mqiwBOlU110Dw4rnuyThv1ZaUQIg6r6mi8CT" +
                "EmKtl+T7+CQx+zfrlrU2FhJ6BTq/OyOvGkd1HUqdlGN92MPOz7Bz9zHmPIaDQA8ih9lOk73iVzMt" +
                "P1jm2ho9Nr7LOJBBRmngN+FSBezXxEd6uZcX4j7LILhVePseXbJKACNEm49x8PMNw4cPrCqSj5Av" +
                "qZgQQP28m1xXtfTqxeY9/UQMuBYuFkGxD8elMjzr4/NmiM8HtV+rPsNalXBfOg8loUA6tg3RQCHQ" +
                "mErtGTfQkUU0O8JMpEQqVov4Fztbq5NYqS2EAJxI6j7rjbjZuHOvpFwCTCcYKQKBgQDYlKl0iCOi" +
                "fKmJ1VLRz+uF/Qgg5SXjZrGS70Qk1O0IlG0Mk6jKzezy43TKE//vMwIswsgpleHMEgs8HB/VuO/D" +
                "U3OZysuwqS4DHf7wefKQwaN+KcDkpvPhxqRvVNcsiFS0uyLXRJd7PSm+5Q3gmVY1M6q/oaSvXY6y" +
                "sYkYtgen8wKBgQDANkLPJ1rINIa/w12aPkBDo/rDRxE8SkA+clalSt1wMCoMzeSuwOOPl9FK3xT6" +
                "+Xez/isR6XbWlY9/RVS8UchlndICR1E1bC09jwMKgd0gnfD1t+gZlC3/lxcrOxyroKYX2hKsJsRC" +
                "5qF4OnZuUZUj1/+ZXBD9tH/9KBaEgCOnzwKBgQCILwpSnRwTGuIcKkgWZ/AH43BOBuiJEdTMuKiX" +
                "pE+bmoDrbJmxVEUPAVk5i9Pdm6xdaukjxPwjLqFj3adi4xPYNUw2Qx9LaUXmbeEVYwjUXv4n4adJ" +
                "33Tw3dAdtTJL7TmeHyGsTJQXyQDL6QmN1b0wXNmb/88m+eQ1lo7HPAaaYwKBgQC7TKSxpBHMgZLF" +
                "DqUJhpuszf+vhnL/IwOTVmukSl2Z0vVG4ON0frXA2RmzMcsJ/Upt9MEXhT6uP7NDO9YkZDU5AB8C" +
                "5Wf2/ODg2ZkHBiDqhb2mU30yX7irFRoYoFOB0tvG5lPHuUcKQh8aPy3ktk70TKjfUl2kO910XToE" +
                "B7G29QKBgDpgFQySBBmitdU4pc+7vNKisgb9xjJoDpBFbjuzKjEXUCNRXcH8mT8Bt8Ogyjt3R1xV" +
                "YMtGcJ+keeWsECPw0WSHKAG0tpMKAlK+4F/dLgqeK6DKUXzeMVp3Edm4Gkz72h/zPa0vCDtMjm/g" +
                "RstcbMgGuvITMpqrxoVlBYNWYtXd";

                System.err.println("公钥加密——私钥解密");
        String source = "高可用架构对于互联网服务基本是标配zsj999-00.%$2(。";
        System.out.println("\r加密前文字：\r\n" + source);
        String aData = RSAUtilTest.encrypt(publicKey, source);
        System.out.println("加密后文字：\r\n" + aData);
        String dData = RSAUtilTest.decrypt(privateKey, aData);
        System.out.println("解密后文字: \r\n" + dData);
    }
}