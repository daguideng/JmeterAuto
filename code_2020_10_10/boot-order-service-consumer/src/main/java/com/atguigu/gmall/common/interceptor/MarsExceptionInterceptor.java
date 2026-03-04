package com.atguigu.gmall.common.interceptor;

import com.atguigu.gmall.common.exception.ServiceException;
import com.atguigu.gmall.common.log.LoggerUtil;
import com.atguigu.gmall.common.utils.json.JSONHelper;
import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.constant.RES_STATUS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.atguigu.gmall.common.log.LoggerUtil.SEPARATOR;


/**
 * 拦截http请求响应异常 UcExceptionInterceptor
 *
 * @date 2016年1月11日 上午10:15:12
 *
 */
@ControllerAdvice
public class MarsExceptionInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(MarsExceptionInterceptor.class);
	private static final Logger loggerError = LoggerFactory.getLogger(LoggerUtil.ERROR_LOG);
	/**
	 * 
	 * @param e
	 * @param request
	 * @param response
	 * @return
	 */
	@ExceptionHandler(ServiceException.class)
	@ResponseBody
	public Result<Void> handlerUcreditException(ServiceException e, HttpServletRequest request,
												HttpServletResponse response) {
		Result<Void> result = new Result<Void>();
		result.setCode(e.getErrorCode());
		result.setMsg(e.getErrorMsg());
		logger.error(request.getRequestURI() + SEPARATOR + RequestUtil.getIp(request) + SEPARATOR
				+ JSONHelper.toJSONStringAdvance(result) + SEPARATOR + result.getCode() + SEPARATOR
                + JSONHelper.toJSONStringAdvance(RequestUtil.getHttpParamter(request)) + SEPARATOR
                + request.getMethod(), e);
		loggerError.error(RequestUtil.getIp(request) + SEPARATOR
				+ JSONHelper.toJSONStringAdvance(result) + SEPARATOR + result.getCode() + SEPARATOR
                + JSONHelper.toJSONStringAdvance(RequestUtil.getHttpParamter(request)) + SEPARATOR
                + request.getRequestURI(),e);
		return result;
	}

	/**
	 * spring 参数缺失，框架默认异常
	 * @param e
	 * @param request
	 * @param response
	 * @return
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody
	public Result<Void> handlerMissParamException(MissingServletRequestParameterException e,
                                                  HttpServletRequest request, HttpServletResponse response) {
		Result<Void> result = new Result<Void>();
		result.setCode(RES_STATUS.INVALID_DATA.code);
		result.setMsg(RES_STATUS.INVALID_DATA.msg);
		logger.error(
				request.getRequestURI() + SEPARATOR + RequestUtil.getIp(request) + SEPARATOR
						+ JSONHelper.toJSONStringAdvance(result) + SEPARATOR + result.getCode()
						+ SEPARATOR + JSONHelper.toJSONStringAdvance(RequestUtil.getHttpParamter(request))
						+ SEPARATOR + request.getMethod()+ SEPARATOR + "miss paramName:" + e.getParameterName() + ",parameterType:"
						+ e.getParameterType(), e);
		loggerError.error(RequestUtil.getIp(request) + SEPARATOR
				+ JSONHelper.toJSONStringAdvance(result) + SEPARATOR + result.getCode() + SEPARATOR
                + JSONHelper.toJSONStringAdvance(RequestUtil.getHttpParamter(request)) + SEPARATOR
                + request.getRequestURI(),e);
		return result;
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseBody
	public Result<Void> maxUploadSizeExceededException(MissingServletRequestParameterException e,
                                                       HttpServletRequest request, HttpServletResponse response) {
		Result<Void> result = new Result<Void>();
		result.setCode(RES_STATUS.SERVER_ERROR.code);
		result.setMsg("上传文件大小请不要超过50MB");
		logger.error(request.getRequestURI() + SEPARATOR + RequestUtil.getIp(request) + SEPARATOR
				+ JSONHelper.toJSONStringAdvance(result) + SEPARATOR + result.getCode() + SEPARATOR
				+ JSONHelper.toJSONStringAdvance(RequestUtil.getHttpParamter(request))+SEPARATOR + request.getMethod(), e);
		loggerError.error(RequestUtil.getIp(request) + SEPARATOR
				+ JSONHelper.toJSONStringAdvance(result) + SEPARATOR + result.getCode() + SEPARATOR
                + JSONHelper.toJSONStringAdvance(RequestUtil.getHttpParamter(request)) + SEPARATOR
                + request.getRequestURI(),e);
		return result;
	}


	/**
	 * 拦截服务器未知异常
	 * 
	 * @param e
	 * @param request
	 * @param response
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Result<Void> handlerException(Exception e, HttpServletRequest request,
			HttpServletResponse response) {
		Result<Void> result = new Result<Void>(RES_STATUS.SERVER_ERROR);
		logger.error(request.getRequestURI() + SEPARATOR + RequestUtil.getIp(request) + SEPARATOR
				+ JSONHelper.toJSONStringAdvance(result) + SEPARATOR + result.getCode() + SEPARATOR
				+ JSONHelper.toJSONStringAdvance(RequestUtil.getHttpParamter(request))+SEPARATOR + request.getMethod(), e);
		loggerError.error(RequestUtil.getIp(request) + SEPARATOR
				+ JSONHelper.toJSONStringAdvance(result) + SEPARATOR + result.getCode() + SEPARATOR
                + JSONHelper.toJSONStringAdvance(RequestUtil.getHttpParamter(request)) + SEPARATOR
                + request.getRequestURI(),e);
		return result;
	}

}
