package com.atguigu.gmall.common.page;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.common.exception.ServiceException;
import com.atguigu.gmall.common.utils.DateUtil;
import com.atguigu.gmall.common.web.QueryFilter;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.Order.Direction;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页基础类
 */
public abstract class QueryBase {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public int pageNo = 1;

	public int pageSize = 20;
	/**
	 * 查询条件
	 */
	public String search;
	/**
	 * 过滤器
	 */
	public String filter;
	/**
	 * 排序 列名
	 */
	public String property;
	/**
	 * ASE ，DESC 正序或者倒序
	 */
	public Direction direction;
	/**
	 * 空格
	 */
	private static final String SQL_SYMBOL_SPACE = " ";

	/**
	 * 获取排序和分页 方法
	 *
	 * @return
	 */
	public PageBounds getPB() {
		if (!StringUtils.isBlank(this.property) && this.direction != null) {
			return new PageBounds(this.pageNo, this.pageSize, Order.create(this.property, this.direction.name()));
		}
		return new PageBounds(this.pageNo, this.pageSize);

	}

	/**
	 * 获取分页条件方法
	 *
	 * @return
	 */
	public abstract List<Criterion> getCriterion();

	/**
	 * 查询对象方法
	 *
	 * @return
	 */
	public List<Criterion> criterion() {

		if (!StringUtils.isBlank(this.filter)) {
		//	logger.info("filter不为空,根据Criterion获取FilterList");
			return this.addFilterList(this.getCriterion());
		}

		return this.getCriterion();
	}

	/**
	 * filter过滤条件不为空,增加过滤条件
	 *
	 * @param list
	 * @return
	 */
	private List<Criterion> addFilterList(List<Criterion> list) {
		try {

			List<QueryFilter> qfList = JSON.parseArray(this.filter, QueryFilter.class);
			if (list == null) {
				list = new ArrayList<>();
			}

			for (QueryFilter qf : qfList) {
			//	logger.info("根据转化的{}构建Criterion", qf.toString());
				Criterion c;
				// 实现模糊匹配
				if ("like".equals(qf.getOperator())) {
					c = new Criterion(
							qf.getProperty() + QueryBase.SQL_SYMBOL_SPACE +QueryBase.getSqlSymbol(qf.getOperator()),
							"%"+qf.getValue()+"%");
				} else {


					if((qf.getProperty().equalsIgnoreCase("lastruntime") || qf.getProperty().equalsIgnoreCase("perfstarttime") || qf.getProperty().equalsIgnoreCase("uploadtime") || qf.getProperty().equalsIgnoreCase("create_time")) && !qf.getValue().toString().contains("-")){
						c = new Criterion(
								qf.getProperty() + QueryBase.SQL_SYMBOL_SPACE + QueryBase.getSqlSymbol(qf.getOperator()),
								DateUtil.timeStamp2Date(qf.getValue().toString(), "yyyy-MM-dd HH:mm:ss"));
					}else {

						c = new Criterion(
								qf.getProperty() + QueryBase.SQL_SYMBOL_SPACE + QueryBase.getSqlSymbol(qf.getOperator()),
								qf.getValue());
					}
				}

				// System.out.println("c===>"+c);
				list.add(c);
			//	logger.info("根据QueryFilter新增Criterion:{}", c.toString());

			}
		} catch (Exception e) {
		//	logger.warn("过滤条件解析失败，filter:" + this.filter);
		}

		return list;
	}

	/**
	 * 转变sql符号
	 *
	 * @param type
	 * @return
	 */
	private static String getSqlSymbol(String type) throws ServiceException {
		switch (type) {
		case QueryFilter.COMPARISION_EQ:
			return "=";
		case QueryFilter.COMPARISION_LT:
			return "<";
		case QueryFilter.COMPARISION_LE:
			return "<=";
		case QueryFilter.COMPARISION_GT:
			return ">";
		case QueryFilter.COMPARISION_GE:
		    return ">=";
		case QueryFilter.COMPARISION_LIKE:
			return "like";
		case QueryFilter.TYPE_BOOLEAN:
			return "=";
		case QueryFilter.TYPE_LIST:
			return "in";
		default:
			return null ;
			//throw new ServiceException(RES_STATUS.INVALID_COMPARISION_TYPE);
		}
	}

	public int getPageNo() {
		return this.pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getProperty() {
		return this.property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * @return the search
	 */
	public String getSearch() {
		return this.search;
	}

	/**
	 * @param search
	 *            the search to set
	 */
	public void setSearch(String search) {
		this.search = search;
	}

	public String getFilter() {
		return this.filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
}
