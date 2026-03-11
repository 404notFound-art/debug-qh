package com.cl.dao;

import com.cl.entity.TongzhijiluEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.view.TongzhijiluView;

import org.apache.ibatis.annotations.Param;


/**
 * 通知记录
 * 
 * @author 
 * @email 
 * @date 2025-03-27 15:44:15
 */
public interface TongzhijiluDao extends BaseMapper<TongzhijiluEntity> {
	
	List<TongzhijiluEntity> selectListVO(@Param("ew") Wrapper<TongzhijiluEntity> wrapper);
	
	TongzhijiluEntity selectVO(@Param("ew") Wrapper<TongzhijiluEntity> wrapper);
	
	List<TongzhijiluView> selectListView(@Param("ew") Wrapper<TongzhijiluEntity> wrapper);

	List<TongzhijiluView> selectListView(Pagination page,@Param("ew") Wrapper<TongzhijiluEntity> wrapper);
	
	TongzhijiluView selectView(@Param("ew") Wrapper<TongzhijiluEntity> wrapper);
	
}
