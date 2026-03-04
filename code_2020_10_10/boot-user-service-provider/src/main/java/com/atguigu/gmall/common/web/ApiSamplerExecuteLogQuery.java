package com.atguigu.gmall.common.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.common.page.QueryBase;

/**
 * 
 * @ClassName: ApiSamplerExecuteLogQuery
 * @Description: 分页查询Api报告的信息
 * @author shirui
 * @date 2018年6月6日 下午5:58:29
 *
 */
public class ApiSamplerExecuteLogQuery extends QueryBase {

	private String api_id;
	
	private List<String> api_service_type;
	
	private String api_service_url;

	private String api_run_user;

	private String api_report_receiver;

	private double api_sampler_passrate;

	private String api_htmlPath;

	private Date api_start_time;

	private String api_execute_duration;


	@Override
	public List<Criterion> getCriterion() {
		List<Criterion> criterion = new ArrayList<>();
		if (StringUtils.isNotEmpty(this.search)) {
			if (this.api_service_type != null && this.api_service_type.size() > 0) {
				Criterion c = new Criterion("api_service_type in ", this.api_service_type);
				criterion.add(c);
			}
		}

		return criterion.isEmpty() ? null : criterion;
	}

	@Override
	public PageBounds getPB() {
		this.setProperty("api_id");
		this.setDirection(Order.Direction.DESC);
		return super.getPB();
	}

	public List<String> getApi_service_type() {
		return api_service_type;
	}

	public void setApi_service_type(List<String> api_service_type) {
		this.api_service_type = api_service_type;
	}

	public String getApi_id() {
		return api_id;
	}

	public void setApi_id(String api_id) {
		this.api_id = api_id;
	}

	public String getApi_service_url() {
		return api_service_url;
	}

	public void setApi_service_url(String api_service_url) {
		this.api_service_url = api_service_url;
	}

	public String getApi_run_user() {
		return api_run_user;
	}

	public void setApi_run_user(String api_run_user) {
		this.api_run_user = api_run_user;
	}

	public String getApi_report_receiver() {
		return api_report_receiver;
	}

	public void setApi_report_receiver(String api_report_receiver) {
		this.api_report_receiver = api_report_receiver;
	}

	public double getApi_sampler_passrate() {
		return api_sampler_passrate;
	}

	public void setApi_sampler_passrate(double api_sampler_passrate) {
		this.api_sampler_passrate = api_sampler_passrate;
	}

	public String getApi_htmlPath() {
		return api_htmlPath;
	}

	public void setApi_htmlPath(String htmlPath) {
		this.api_htmlPath = htmlPath;
	}

	public Date getApi_start_time() {
		return api_start_time;
	}

	public void setApi_start_time(Date api_start_time) {
		this.api_start_time = api_start_time;
	}

	public String getApi_execute_duration() {
		return api_execute_duration;
	}

	public void setApi_execute_duration(String api_execute_duration) {
		this.api_execute_duration = api_execute_duration;
	}

   

}
