package com.blue.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ResourceBundle;

public class AlipayConfig {
		
	/**
	 *属性都配置在merchantInfo文件中 
	 * */
	
	
	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
		public static String app_id = ResourceBundle.getBundle("merchantInfo").getString("app_id");
		
		// 商户私钥，您的PKCS8格式RSA2私钥
	    public static String merchant_private_key = ResourceBundle.getBundle("merchantInfo").getString("merchant_private_key");
		
		// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
	    public static String alipay_public_key = ResourceBundle.getBundle("merchantInfo").getString("alipay_public_key");

		// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
//		public static String notify_url = "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";
		public static String notify_url = ResourceBundle.getBundle("merchantInfo").getString("notify_url");

		// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
//		public static String return_url = "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";
		public static String return_url = ResourceBundle.getBundle("merchantInfo").getString("return_url");

		// 签名方式
		public static String sign_type = ResourceBundle.getBundle("merchantInfo").getString("sign_type");
		
		// 字符编码格式
		public static String charset = ResourceBundle.getBundle("merchantInfo").getString("charset");
		
		// 支付宝网关
		//生产环境
//		public static String gatewayUrl = "https://openapi.alipay.com/gateway.do";
		//沙箱测试环境
		public static String gatewayUrl = ResourceBundle.getBundle("merchantInfo").getString("gatewayUrl");
		
		
		// 支付宝网关
		public static String log_path = ResourceBundle.getBundle("merchantInfo").getString("log_path");
		
		/** 
	     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
	     * @param sWord 要写入日志里的文本内容
	     */
	    public static void logResult(String sWord) {
	        FileWriter writer = null;
	        try {
	            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
	            writer.write(sWord);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (writer != null) {
	                try {
	                    writer.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
	
}
