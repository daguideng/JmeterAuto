package com.atguigu.gmall.common.web;

import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.common.page.QueryBase;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @ClassName: PerfHistoryReortQuery
 * @Description: 分页查询Perf报告的信息
 * @author
 * @date 2018年6月6日 下午5:58:29
 *
 */
public class PerfCurrentReortQuery extends QueryBase implements Serializable {

	private Integer id;

	private Integer uploadid ;

	private String lastruntime ;

	private String scriptname;
	
	private String threads;

	private String label;

	private String samples;

	private String ko;

	private String error;

	private String average;

	private String min;

	private String max;

	private String thpct90;

	private String thpct95;

	private String thpct99;

	private String throughput;

	private String received;

	private String sent;

	private String ip;

	private String starttime;

	private String endtime;

    private String jtlpath ;

    private String indexpath ;

    private Integer state ;

	public String getJtlpath() {
		return jtlpath;
	}

	public void setJtlpath(String jtlpath) {
		this.jtlpath = jtlpath;
	}

	public String getIndexpath() {
		return indexpath;
	}

	public void setIndexpath(String indexpath) {
		this.indexpath = indexpath;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public List<Criterion> getCriterion() {
		List<Criterion> criterion = new ArrayList<>();
		if (StringUtils.isNotEmpty(this.search)) {
			if (this.scriptname != null ) {
				Criterion c = new Criterion("scriptname like ", "%"+this.scriptname+"%");
				criterion.add(c);
			}
		}

		return criterion.isEmpty() ? null : criterion;
	}

	public List<Criterion> getCriterionLink() {
		List<Criterion> criterion = new ArrayList<>();
		if (StringUtils.isNotEmpty(this.search)) {
			if (this.scriptname != null ) {
				Criterion c = new Criterion("scriptname like ", "%"+this.scriptname+"%");
				criterion.add(c);
			}
		}

		return criterion.isEmpty() ? null : criterion;
	}

	@Override
	public PageBounds getPB() {
		this.setProperty("id");
		this.setDirection(Order.Direction.DESC);
		return super.getPB();
	}



	public PageBounds getPBAsc() {
		this.setProperty("id");
		this.setDirection(Order.Direction.ASC);
		return super.getPB();
	}


	public void setScriptname(String scriptname) {
		this.scriptname = scriptname;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUploadid() {
		return uploadid;
	}

	public void setUploadid(Integer uploadid) {
		this.uploadid = uploadid;
	}

	public String getLastruntime() {
		return lastruntime;
	}

	public void setLastruntime(String lastruntime) {
		this.lastruntime = lastruntime;
	}

	public String getScriptname() {
		return scriptname;
	}

	public String getThreads() {
		return threads;
	}

	public void setThreads(String threads) {
		this.threads = threads;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSamples() {
		return samples;
	}

	public void setSamples(String samples) {
		this.samples = samples;
	}

	public String getKo() {
		return ko;
	}

	public void setKo(String ko) {
		this.ko = ko;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getAverage() {
		return average;
	}

	public void setAverage(String average) {
		this.average = average;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getThpct90() {
		return thpct90;
	}

	public void setThpct90(String thpct90) {
		this.thpct90 = thpct90;
	}

	public String getThpct95() {
		return thpct95;
	}

	public void setThpct95(String thpct95) {
		this.thpct95 = thpct95;
	}

	public String getThpct99() {
		return thpct99;
	}

	public void setThpct99(String thpct99) {
		this.thpct99 = thpct99;
	}

	public String getThroughput() {
		return throughput;
	}

	public void setThroughput(String throughput) {
		this.throughput = throughput;
	}

	public String getReceived() {
		return received;
	}

	public void setReceived(String received) {
		this.received = received;
	}

	public String getSent() {
		return sent;
	}

	public void setSent(String sent) {
		this.sent = sent;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}


	public PerfCurrentReortQuery(){}


	public PerfCurrentReortQuery(Integer id, Integer uploadid, String lastruntime, String scriptname, String threads, String label, String samples, String ko, String error, String average, String min, String max, String thpct90, String thpct95, String thpct99, String throughput, String received, String sent, String ip, String starttime, String endtime,String jtlpath,String indexpath,Integer  state) {
		this.id = id;
		this.uploadid = uploadid;
		this.lastruntime = lastruntime;
		this.scriptname = scriptname;
		this.threads = threads;
		this.label = label;
		this.samples = samples;
		this.ko = ko;
		this.error = error;
		this.average = average;
		this.min = min;
		this.max = max;
		this.thpct90 = thpct90;
		this.thpct95 = thpct95;
		this.thpct99 = thpct99;
		this.throughput = throughput;
		this.received = received;
		this.sent = sent;
		this.ip = ip;
		this.starttime = starttime;
		this.endtime = endtime;
		this.jtlpath = jtlpath ;
		this.indexpath = indexpath ;
		this.state = state ;
	}
}
