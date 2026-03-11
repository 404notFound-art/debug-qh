package com.cl.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;
import java.util.Date;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;


import com.cl.dao.TongzhijiluDao;
import com.cl.entity.TongzhijiluEntity;
import com.cl.service.TongzhijiluService;
import com.cl.entity.view.TongzhijiluView;

@Service("tongzhijiluService")
public class TongzhijiluServiceImpl extends ServiceImpl<TongzhijiluDao, TongzhijiluEntity> implements TongzhijiluService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TongzhijiluEntity> page = this.selectPage(
                new Query<TongzhijiluEntity>(params).getPage(),
                new EntityWrapper<TongzhijiluEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<TongzhijiluEntity> wrapper) {
		  Page<TongzhijiluView> page =new Query<TongzhijiluView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<TongzhijiluView> selectListView(Wrapper<TongzhijiluEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public TongzhijiluView selectView(Wrapper<TongzhijiluEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	@Override
	public boolean retrySendNotification(Long id) {
		TongzhijiluEntity record = this.selectById(id);
		if (record == null) {
			return false;
		}
		
		// 模拟发送通知
		boolean sendSuccess = sendNotification(record);
		
		if (sendSuccess) {
			record.setFasongzhuangtai(1); // 发送成功
			record.setFasongshijian(new Date());
			record.setShibaiyuanyin(null);
		} else {
			record.setFasongzhuangtai(2); // 发送失败
			record.setChongshicishu(record.getChongshicishu() + 1);
			record.setShibaiyuanyin("发送失败，请检查用户联系方式");
		}
		
		this.updateById(record);
		return sendSuccess;
	}
	
	@Override
	public int retrySendNotifications(Long[] ids) {
		int successCount = 0;
		for (Long id : ids) {
			if (retrySendNotification(id)) {
				successCount++;
			}
		}
		return successCount;
	}
	
	/**
	 * 模拟发送通知的方法
	 * 实际项目中这里应该调用短信接口、推送服务等
	 */
	private boolean sendNotification(TongzhijiluEntity record) {
		// 模拟发送逻辑，实际项目中替换为真实的发送代码
		// 例如：调用短信API、推送服务等
		
		// 这里模拟90%的成功率
		return Math.random() > 0.1;
	}
}
