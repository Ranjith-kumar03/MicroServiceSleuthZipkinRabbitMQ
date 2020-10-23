package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.currencyConversionBean.CurrencyConversionBean;
import com.example.demo.feignproxy.CurrencyExchangeServiceProxy;

@RestController
public class CurrencyConversionController {
	private Logger logger= org.slf4j.LoggerFactory.getLogger(this.getClass());
	@Autowired
	private RestTemplate template;
	@Autowired
	private CurrencyExchangeServiceProxy proxy;
			
//With Rest Template
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from,
			@PathVariable String to,@PathVariable BigDecimal quantity)
	{
		Map<String,String>config=new HashMap<>();
		config.put("from", from);
		config.put("to",to);
		ResponseEntity<CurrencyConversionBean> responseEntity = template.getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class,config);
		CurrencyConversionBean conversionBean = responseEntity.getBody();
		return new CurrencyConversionBean(conversionBean.getId(), conversionBean.getFrom(),conversionBean.getTo(), conversionBean.getConversionMutiple(), quantity, 
				quantity.multiply(conversionBean.getConversionMutiple()), conversionBean.getPort());
	}
	
	//using Feign
	
	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from,
			@PathVariable String to,@PathVariable BigDecimal quantity)
	{
		
		CurrencyConversionBean conversionBean = proxy.rteriveExchangeValue(from, to);
		logger.info("{}",conversionBean);
		return new CurrencyConversionBean(conversionBean.getId(), conversionBean.getFrom(),conversionBean.getTo(), conversionBean.getConversionMutiple(), quantity, 
				quantity.multiply(conversionBean.getConversionMutiple()), conversionBean.getPort());
	}
}
