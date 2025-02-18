package com.hanyc.demo.jiami;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {

    //   RSA rsa = new RSA()
    public static final String CHARSET = "UTF-8";
    // ALGORITHM ['ælgərɪð(ə)m] 算法的意思
    public static final String RSA_ALGORITHM = "RSA";

    /**
     * 创建钥匙对
     *
     * @param keySize
     * @return
     */
    public static Map<String, String> createKeys(int keySize) {
        // 为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }

        // 初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        // 生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        // 得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        // 得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
        // map装载公钥和私钥
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);
        // 返回map
        return keyPairMap;
    }

    /**
     * 得到公钥
     *
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    /**
     * 得到私钥
     *
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * rsa切割解码  , ENCRYPT_MODE,加密数据   ,DECRYPT_MODE,解密数据
     */
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        //最大块
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    //可以调用以下的doFinal（）方法完成加密或解密数据：
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 创建密钥对
        Map<String, String> keys = RSAUtils.createKeys(1024);
        // 从Map中获取密钥对
        String publicKey = keys.get("publicKey");
        String privateKey = keys.get("privateKey");
        // 获取公钥
        System.out.println("publicKey:" + publicKey);
        String keyword = "istic5888";
        // 使用公钥加密
        String aaaaaaaaa = publicEncrypt(keyword, getPublicKey(publicKey));
        System.out.println("------ :   " + aaaaaaaaa);

        // 使用私钥解密
        String bbbbb = privateDecrypt("LtShYPz00LYSSKa6L0VLR2kKKeIFW9nUyeETJ3hwWVt4ptoVd1Gb0oTtQ13AvdC7BKfF2VbjkHsfPTDXoK9PwrJRn2rYvSxuSoxIleMHn20dMDGluPoQ/z2di5PhIimugPAlDyoL906TtxwBZ3majPsmLHodCZo5wVY4nQ/oKwc=", getPrivateKey("MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKjD06/Gdx/D9jRANeotUtFRJDmV+QL4ZtOMtwn/chXPTUl6q/HZHspeHivH3RjEYuK7HjvZICYTB2CENZzFzMSNdG0bmZzSICId1X2AHjZxC5hukfDu9Y/sbYI1bbZqQTy8C3VTwz3s6ucaZAdv6bXP27sQle5cQS0IPcFHoLMtAgMBAAECgYEAgXFqPWkxV3j7r4oMkGhhHFFrQowIacHTRLGqdw2qGJZfRlvD5IVPO5LC+3uZ1vRSkGtLPkZEhU+VGe9uG6gPbPe+0BpfpYbOBZQuFOh/qZhStMjptKRF9Ileeqf3rXnvIIDX8uarJ2LgzyLvIFanoY3XeHYL4SlAqUV18mqn+fUCQQDWLDoD8/siPdVuBOsbDzrAxEv7V5FlEHeD20M1h/p4qP38u+eZYLlDYg9BcQ37zP9LsZopsl0JHOxq6AKxvIuPAkEAyblknSVdXq/Fmq0dhFxwa7ugv4ailzMJXia8owX4JyCP3ZsFJWZP4/P+5TIrbiySJWmuGjUXnHViGWJlR7WngwJBAJNAVycOoXTadJqvEQJKx0dv7xnBDDsjRjjHEP+GHUTGU9JnUwXWRao5immhFE8yk7h0RJ00f9jbJQUfvXJ8QqsCQQCcoALb9PgDHcz65PEnJTLwoxUTwHCbnzflz4PlvCvKDYFGmieruz3Ki1ddE9Ae0iKDRAO3O4Piqk2J0hk7TXMLAkBWQlLMWHb/7LKAEtmK8slbTQKbp3oaoL3GSilQZOgtXO9XcIpbqa2xIaQtS/vmi3uBtarEzUM8EdRmoXgV/QQp"));
        System.out.println("------ :   " + bbbbb);

        // 公钥解密
//        String ccccc = publicDecrypt(aaaaaaaaa, getPublicKey(publicKey));
//        System.out.println("------ :   " + ccccc);
        // 获取私钥
        System.out.println("privateKey:" + privateKey);
    }

}
