package com.blue.controller;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.blue.utils.AlipayConfig;
import com.blue.utils.PaymentUtil;

@Controller
@RequestMapping("/pay")
public class OnlinePayController {
	private static Log log= (Log) LogFactory.getLog(OnlinePayController.class);
	/**
	 * @param yh 银行
	 * @param orderid 订单id
	 * @param money 金额
	 * */
	@RequestMapping("/prepay")
	public String prePay(String orderid,String money, @RequestParam(value="subject",defaultValue="") String subject,@RequestParam(value="body",defaultValue="") String body,Model model){
		model.addAttribute("orderid", orderid);
		model.addAttribute("subject", subject);
		model.addAttribute("money", money);
		model.addAttribute("body", body);
		return "confirm";
	}
	
	@RequestMapping("/onlinepay")
	@ResponseBody
	public String onlinePay(String orderid,String money, @RequestParam(value="subject1",defaultValue="") String subject1,@RequestParam(value="body1",defaultValue="") String body1) throws AlipayApiException{
		
		//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
		
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = orderid;
		//付款金额，必填
		String total_amount = money;
		//订单名称，必填
		String subject = subject1;
		//商品描述，可空
		String body = body1;
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
				+ "\"total_amount\":\""+ total_amount +"\"," 
				+ "\"subject\":\""+ subject +"\"," 
				+ "\"body\":\""+ body +"\"," 
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		log.info(alipayRequest.getBizContent());
		
		//若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
		//alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
		//		+ "\"total_amount\":\""+ total_amount +"\"," 
		//		+ "\"subject\":\""+ subject +"\"," 
		//		+ "\"body\":\""+ body +"\"," 
		//		+ "\"timeout_express\":\"10m\"," 
		//		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		//请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节
		
		//请求
		String result = alipayClient.pageExecute(alipayRequest).getBody();
		
		return result;
	}
	
	
	
}
