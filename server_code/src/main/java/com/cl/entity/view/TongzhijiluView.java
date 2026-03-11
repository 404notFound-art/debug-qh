package com.cl.entity.view;

import com.cl.entity.TongzhijiluEntity;

import java.io.Serializable;

/**
 * 通知记录
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 */
public class TongzhijiluView extends TongzhijiluEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 通知类型的值
	 */
	private String tongzhileixingValue;

	/**
	 * 发送状态的值
	 */
	private String fasongzhuangtaiValue;

	public String getTongzhileixingValue() {
		if (tongzhileixing != null) {
			switch (tongzhileixing) {
				case 1: return "预约成功通知";
				case 2: return "就诊前一天提醒";
				case 3: return "就诊当天提醒";
				default: return "";
			}
		}
		return "";
	}

	public String getFasongzhuangtaiValue() {
		if (fasongzhuangtai != null) {
			switch (fasongzhuangtai) {
				case 0: return "待发送";
				case 1: return "发送成功";
				case 2: return "发送失败";
				default: return "";
			}
		}
		return "";
	}
}
