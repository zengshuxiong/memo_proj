package com.eden.util.sign;

import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * 根据DB中保存的证书来签名、验证。
 * 使用方法：
 *   //import SignHelper;
 *
 *   //使用客户端的Cer/Pfx
 *   sig_result sigResult = Sender.doSign(app_id, json);
 *   boolean shouldOK = Receiver.validateSigData(app_id, sigResult);
 *
 *   //使用服务端的Cer/Pfx
 *   sig_result sigResult = Sender.doSign(app_id, json, CerSourceType.SERVER);
 *   boolean shouldOK = Receiver.validateSigData(app_id, sigResult, CerSourceType.SERVER);
 *
 */
public final class SignHelper {
    public enum CerSourceType {
        CLIENT, //APP
        SERVER  //UM
    }

    public static class Sender {
        /**
         * 使用client端的私钥pfx进行签名
         *
         * @param app_id : 
         * @param source : 要签名的字符串
         *
         * @return {@link sig_result}
         *
         * */
        public static sig_result doSign(String app_id, String source) throws Exception{
            return doSign(app_id, source, CerSourceType.CLIENT);
        }

        /**
         * @param csType 使用客户端或服务端的秘钥来签名。 {@link CerSourceType}
         * */
        public static sig_result doSign(String app_id, String source, CerSourceType csType) throws Exception{
            if(StringUtils.isEmpty(app_id) || StringUtils.isBlank(app_id)){
                throw new IllegalArgumentException("app_id不能为空");
            }
            if(source == null){
                throw new IllegalArgumentException("source不能为空");
            }
            if(CerSourceType.CLIENT != csType && CerSourceType.SERVER != csType){
                throw new IllegalArgumentException("不支持的枚举值CerSourceType:" + csType);
            }

//          client_info clientInfo = findClientInfoByAppId(app_id);
            um_client_cert cert = new um_client_cert(); //clientInfo.cert;
            //String cert = "xxxx";//base 64 cert(public key)
            try {
                String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String signSource = timestamp + source;

                PrivateKey privateKey = (CerSourceType.CLIENT == csType) ?
                                    getPrivateKeyFromBase64Pfx(cert.app_pfx, cert.app_pfx_password)
                                    :getPrivateKeyFromBase64Pfx(cert.um_pfx, cert.um_pfx_password);

                Signature signature = Signature.getInstance("SHA1withRSA");
                signature.initSign(privateKey);
                signature.update(signSource.getBytes("UTF-8"));
                byte[] sign_bytes = signature.sign();

                String source_b64 = Base64Helper.encode(source.getBytes("UTF-8"));
                String source_urlcode = URLEncoder.encode(source_b64, "UTF-8");
                String sign_b64 = Base64Helper.encode(sign_bytes);
                String sign_urlcode = URLEncoder.encode(sign_b64, "UTF-8");

                return new sig_result(timestamp, source_urlcode, sign_urlcode);
            }catch(Exception e){
                String msg = "签名失败," + e.getMessage();
                throw new Exception("[send_tool#doSign_ERROR]" + msg, e);
            }

        }

        public static PrivateKey getPrivateKeyFromBase64Pfx(String base64PfxText,
                                                            String password) throws Exception {
            KeyStore store = KeyStore.getInstance("PKCS12");

            InputStream stream = new ByteArrayInputStream(Base64Helper.decode(base64PfxText));
            store.load(stream, password.toCharArray());
            stream.close();

            @SuppressWarnings({ "rawtypes" })
            Enumeration aliases = store.aliases();

            String alias = (String) aliases.nextElement();

            return ((PrivateKey) store.getKey(alias, password.toCharArray()));
        }
    }

    public static class Receiver {

        public enum TransferType{
            HTTP,
            JVM;
        }

        /**
         * 使用client端的公钥cer验证签名。
         *  transferType == HTTP
         *
         * @param app_id : 
         * @param sigResult {@link sig_result}
         *
         * */
        public static boolean validateSigData(String app_id, sig_result sigResult) throws Exception{
            return validateSigData(app_id, sigResult, CerSourceType.CLIENT, TransferType.HTTP);
        }

        public static boolean validateSigData(String app_id, sig_result sigResult, TransferType transferType) throws Exception{
            return validateSigData(app_id, sigResult, CerSourceType.CLIENT, transferType);
        }

        /**
         * 使用client端的公钥cer验证签名。
         *
         * @param app_id : 
         * @param sigResult {@link sig_result}
         * @param csType  {@link CerSourceType}
         * @param transferType {@link TransferType}
         *
         * */
        public static boolean validateSigData(String app_id, sig_result sigResult, CerSourceType csType, TransferType transferType) throws Exception{
            if(StringUtils.isEmpty(app_id)){
                throw new IllegalArgumentException("app_id不能为空");
            }

            if(sigResult == null){
                throw new IllegalArgumentException("sig_params不能为null.");
            }

            String timestamp = sigResult.timestamp;
            String source_urlcode = sigResult.source;
            String sign_urlcode = sigResult.sig_data;
            if(StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(source_urlcode) || StringUtils.isEmpty(sign_urlcode)){
                return false;
            }

            //client_info clientInfo = UMHelper.findClientInfoByAppId(app_id);
            um_client_cert cert = new um_client_cert();

            String source_b64 = URLDecoder.decode(source_urlcode, "UTF-8");
            String source = new String(Base64Helper.decode(source_b64), "UTF-8");
            byte[] sign_bytes = null;
            if(transferType == TransferType.JVM) {
                String sign_b64 = URLDecoder.decode(sign_urlcode, "UTF-8");
                sign_bytes = Base64Helper.decode(sign_b64);
            }
            else if(transferType == TransferType.HTTP){
                sign_bytes = Base64Helper.decode(sign_urlcode);
            }

            String signSource = timestamp + source;
            return (CerSourceType.CLIENT == csType) ?
                    validateSigData(cert.app_cer, signSource, sign_bytes)
                    : validateSigData(cert.um_cer, signSource, sign_bytes);
        }

        public static boolean validateSigData(String base64CerText, String requestStr, byte[] sigData) throws Exception{

            if(sigData == null || sigData.length == 0) {
                return false;
            }
            byte[] updateData = requestStr.getBytes();
            CertificateFactory
                    certificatefactory = CertificateFactory.getInstance("X.509");

            InputStream stream = new ByteArrayInputStream(Base64Helper.decode(base64CerText));
            X509Certificate certificate
                    = (X509Certificate) certificatefactory.generateCertificate(stream);

            Signature rsa = Signature.getInstance("SHA1withRSA");
            PublicKey pub = certificate.getPublicKey();
            rsa.initVerify(pub);
            rsa.update(updateData);

            return rsa.verify(sigData);
        }
    }

    public static class sig_result {
        public final String timestamp;
        public final String source;
        public final String sig_data;

        public sig_result(String timestamp, String source, String sig_data){
            this.timestamp = timestamp;
            this.source = source;
            this.sig_data = sig_data;
        }

        public sig_result(sig_result other){
            this.timestamp = other.timestamp;
            this.source = other.source;
            this.sig_data = other.sig_data;
        }

        @Override
        public String toString() {
            return "sig_result{" +
                    "timestamp='" + timestamp + '\'' +
                    ", source='" + source + '\'' +
                    ", sig_data='" + sig_data + '\'' +
                    '}';
        }
    }

    //--------------------------------
    //run this method in jsp
    //--------------------------------
//    public static void main(String[] args) throws Exception{
//
//        String app_id = "1e76fdc99fe54de9ac6d5ac4d27da743"; // TEST_CLIENT_001
//        if(args.length > 0 && !StringUtils.isEmpty(args[0])){
//            app_id = args[0];
//        }
//        System.out.println("main_test_InJsp()#app_id=" + app_id);
//
//        String json = "{\"QueryType\":\"01:楼盘信息\",\"PageIndex\":\"0\",\"PageSize\":\"1000\"}";
//
//        sig_result sigResult = Sender.doSign(app_id, json);
//        boolean shouldOK = Receiver.validateSigData(app_id, sigResult, TransferType.JVM);
//        System.out.print("case1:签名验证-");
//        System.out.println( shouldOK ? "测试通过" : "测试失败！！！");
//
//        sig_result sigResult2 = Sender.doSign(app_id, json, CerSourceType.SERVER);
//        boolean shouldOK2 = Receiver.validateSigData(app_id, sigResult2, CerSourceType.SERVER, TransferType.JVM);
//        System.out.print("case2:签名验证-");
//        System.out.println( shouldOK2 ? "测试通过" : "测试失败！！！");
//    }

}

class um_client_cert {
    public String cer_id;
    public String client_id;
    public String app_cer;
    public String app_pfx;
    public String app_pfx_password;
    public String um_cer;
    public String um_pfx;
    public String um_pfx_password;

    public um_client_cert() {
    }
}

class Base64Helper{
    public static byte[] decode(String str){return null;}
    public static String decode(byte[] dt){return "";}

    public static byte[] encode(String str){return new byte[];}
    public static String encode(byte[] dt){return "";}

}