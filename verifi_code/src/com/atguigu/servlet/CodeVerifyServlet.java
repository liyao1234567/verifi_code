package com.atguigu.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atguigu.utils.VerifyCodeConfig;

import redis.clients.jedis.Jedis;

/**
 * Servlet implementation class CodeVerifyServlet
 */
public class CodeVerifyServlet extends HttpServlet {
	
 
    
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CodeVerifyServlet() {
        super();
    }

 
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取参数
		String phoneNo = request.getParameter("phone_no");
		String verifyCode = request.getParameter("verify_code");
		
		//判断不为空
		if(phoneNo==null||verifyCode==null)
			return;
		
		//获取校验码
		String phoneNoKey = VerifyCodeConfig.PHONE_PREFIX + phoneNo + VerifyCodeConfig.PHONE_SUFFIX;
		Jedis jedis = new Jedis(VerifyCodeConfig.HOST,VerifyCodeConfig.PORT);
		String code = jedis.get(phoneNoKey);
		
		//判断是否相等
		if(verifyCode.equals(code)) {
			response.getWriter().print(true);
		}
		
		//关闭连接
		jedis.close();
		
	}

}
