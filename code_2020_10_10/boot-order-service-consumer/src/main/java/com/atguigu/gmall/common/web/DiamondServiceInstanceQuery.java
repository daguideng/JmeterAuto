package com.atguigu.gmall.common.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.common.page.QueryBase;

/**
 * 
 * @ClassName: DiamondServiceInstanceQuery
 * @Description: 环境信息查询Query
 * @author shirui
 * @date 2018年6月6日 下午6:20:33
 *
 */
public class DiamondServiceInstanceQuery extends QueryBase {
	// 排序值.用于PageBounds参数
	private String sii_id;
	// 查询值: 服务分类id
	private List<Integer> sii_category_id;
	// 查询值: 微服务名
	private String sii_service_name;
	
	
	// 查询值&返回值：服务分支信息: branch
	private String sii_release_version;
	// 返回结果：diamond_id（无需显示）
	private String sii_diamond_service_id;
	// 返回结果：微服务名称
	private String sii_name;
	// 返回值：微服务上次部署时间
	private String sii_last_update_time;
	// 返回值：微服务实例运行状态
	private String sii_running_state;
	// 返回值：微服务实例CPU
	private String sii_cpushare;
	// 返回值：微服务实例Memory
	private String sii_memory;
	// 返回值：微服务实例个数replicas
	private String sii_replicas;
	// 返回值: 微服务所在Docker主机
	private String sii_docker_host;
	// 返回值：微服务最后一次部署人
	private String sii_last_operator;

	/** 构造查询条件,此处都是单值比较 */
	@Override
	public List<Criterion> getCriterion() {
		List<Criterion> criterion = new ArrayList<>();
		
		if (StringUtils.isNotEmpty(this.search)) {
			if (this.sii_category_id != null && this.sii_category_id.size() > 0) {
				Criterion c = new Criterion("sii_category_id in ", this.sii_category_id);
				criterion.add(c);
			}
		}

		return criterion.isEmpty() ? null : criterion;
	}

	/** 排序字段 */
	@Override
	public PageBounds getPB() {
		this.setProperty("sii_id");
		this.setDirection(Order.Direction.DESC);
		return super.getPB();
	}

	public String getSii_id() {
		return sii_id;
	}

	public void setSii_id(String sii_id) {
		this.sii_id = sii_id;
	}


	

	public List<Integer> getSii_category_id() {
		return sii_category_id;
	}

	public void setSii_category_id(List<Integer> sii_category_id) {
		this.sii_category_id = sii_category_id;
	}

	public String getSii_release_version() {
		return sii_release_version;
	}

	public void setSii_release_version(String sii_release_version) {
		this.sii_release_version = sii_release_version;
	}

	public String getSii_diamond_service_id() {
		return sii_diamond_service_id;
	}

	public void setSii_diamond_service_id(String sii_diamond_service_id) {
		this.sii_diamond_service_id = sii_diamond_service_id;
	}

	public String getSii_name() {
		return sii_name;
	}

	public void setSii_name(String si_name) {
		this.sii_name = si_name;
	}

	public String getSii_last_update_time() {
		return sii_last_update_time;
	}

	public void setSii_last_update_time(String sii_last_update_time) {
		this.sii_last_update_time = sii_last_update_time;
	}

	public String getSii_running_state() {
		return sii_running_state;
	}

	public void setSii_running_state(String sii_running_state) {
		this.sii_running_state = sii_running_state;
	}

	public String getSii_cpushare() {
		return sii_cpushare;
	}

	public void setSii_cpushare(String sii_cpushare) {
		this.sii_cpushare = sii_cpushare;
	}

	public String getSii_memory() {
		return sii_memory;
	}

	public void setSii_memory(String sii_memory) {
		this.sii_memory = sii_memory;
	}

	public String getSii_replicas() {
		return sii_replicas;
	}

	public void setSii_replicas(String sii_replicas) {
		this.sii_replicas = sii_replicas;
	}

	public String getSii_docker_host() {
		return sii_docker_host;
	}

	public void setSii_docker_host(String sii_docker_host) {
		this.sii_docker_host = sii_docker_host;
	}

	public String getSii_last_operator() {
		return sii_last_operator;
	}

	public void setSii_last_operator(String sii_last_operator) {
		this.sii_last_operator = sii_last_operator;
	}

	public String getSii_service_name() {
		return sii_service_name;
	}

	public void setSii_service_name(String sii_service_name) {
		this.sii_service_name = sii_service_name;
	}

	@Override
	public String toString() {
		return "DiamondServiceInstanceQuery [sii_id=" + sii_id + ", sii_category_id=" + sii_category_id
				+ ", sii_service_name=" + sii_service_name + ", sii_release_version=" + sii_release_version
				+ ", sii_diamond_service_id=" + sii_diamond_service_id + ", sii_name=" + sii_name
				+ ", sii_last_update_time=" + sii_last_update_time + ", sii_running_state=" + sii_running_state
				+ ", sii_cpushare=" + sii_cpushare + ", sii_memory=" + sii_memory + ", sii_replicas=" + sii_replicas
				+ ", sii_docker_host=" + sii_docker_host + ", sii_last_operator=" + sii_last_operator + "]";
	}

	

	

}
