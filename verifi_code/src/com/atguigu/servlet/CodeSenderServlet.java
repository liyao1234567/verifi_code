package com.atguigu.servlet;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atguigu.utils.SmsUtil;
import com.atguigu.utils.VerifyCodeConfig;
import com.sun.corba.se.spi.activation.Repository;

import redis.clients.jedis.Jedis;

/**
 * Servlet implementation class VerifiCodeServlet
 */
public class CodeSenderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CodeSenderServlet() {
        super();
    }


    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
     
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取值
		String phoneNo = request.getParameter("phone_no");
		
		//判断非空
		if(phoneNo==null)
			return;
		
		//获取连接
		Jedis jedis = new Jedis(VerifyCodeConfig.HOST,VerifyCodeConfig.PORT);
		
		//计数
		String phoneNoCount = VerifyCodeConfig.PHONE_PREFIX + phoneNo + VerifyCodeConfig.COUNT_SUFFIX;
		
		String countStr = jedis.get(phoneNoCount);
		if(countStr==null) {
			jedis.setex(phoneNoCount, VerifyCodeConfig.SECONDS_PER_DAY, "0");
			countStr = "0";
		}else {
			jedis.incr(phoneNoCount);
		}
		
		//判断次数
		int count = Integer.parseInt(countStr);
		if(count>=VerifyCodeConfig.COUNT_TIMES_1DAY) {
			System.out.println("flsakflsa");
			response.getWriter().print("limit");
			return;
		}
		
		
		//生成校验码
		String verifyCode = genCode(VerifyCodeConfig.CODE_LEN);
		
		//保存校验码
		String phoneNoKey = VerifyCodeConfig.PHONE_PREFIX + phoneNo + VerifyCodeConfig.PHONE_SUFFIX;
		jedis.setex(phoneNoKey, 120, verifyCode);
		
		//发送
		System.out.println(verifyCode);
		
		//返回
		response.getWriter().print(true);
		
		//关闭连接
		jedis.close(); 
		  
		 
		
	} 
	
	
	private   String genCode(int len){
		 String code="";
		 for (int i = 0; i < len; i++) {
		     int rand=  new Random().nextInt(10);
		     code+=rand;
		 }
		 
		return code;
	}
	
	
 
}
