package com.cl.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.cl.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.annotation.SysLog;

import com.cl.entity.YishengyuyueEntity;
import com.cl.entity.view.YishengyuyueView;
import com.cl.entity.TongzhijiluEntity;
import com.cl.entity.JiuzhentongzhiEntity;

import com.cl.service.YishengyuyueService;
import com.cl.service.TongzhijiluService;
import com.cl.service.JiuzhentongzhiService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

/**
 * 医生预约
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-27 15:44:15
 */
@RestController
@RequestMapping("/yishengyuyue")
public class YishengyuyueController {
    @Autowired
    private YishengyuyueService yishengyuyueService;
    
    @Autowired
    private TongzhijiluService tongzhijiluService;
    
    @Autowired
    private JiuzhentongzhiService jiuzhentongzhiService;









    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,YishengyuyueEntity yishengyuyue,
                                                                                                                                            HttpServletRequest request){
                    String tableName = request.getSession().getAttribute("tableName").toString();
                                                                        if(tableName.equals("yisheng")) {
                    yishengyuyue.setYishengzhanghao((String)request.getSession().getAttribute("username"));
                                    }
                                                                                                                                        if(tableName.equals("yonghu")) {
                    yishengyuyue.setZhanghao((String)request.getSession().getAttribute("username"));
                                    }
                                                                                                                                                                                EntityWrapper<YishengyuyueEntity> ew = new EntityWrapper<YishengyuyueEntity>();
                                                                                                                                                                                                                        
        
        
        PageUtils page = yishengyuyueService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yishengyuyue), params), params));
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,YishengyuyueEntity yishengyuyue,
		HttpServletRequest request){
        EntityWrapper<YishengyuyueEntity> ew = new EntityWrapper<YishengyuyueEntity>();

		PageUtils page = yishengyuyueService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yishengyuyue), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( YishengyuyueEntity yishengyuyue){
       	EntityWrapper<YishengyuyueEntity> ew = new EntityWrapper<YishengyuyueEntity>();
      	ew.allEq(MPUtil.allEQMapPre( yishengyuyue, "yishengyuyue")); 
        return R.ok().put("data", yishengyuyueService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(YishengyuyueEntity yishengyuyue){
        EntityWrapper< YishengyuyueEntity> ew = new EntityWrapper< YishengyuyueEntity>();
 		ew.allEq(MPUtil.allEQMapPre( yishengyuyue, "yishengyuyue")); 
		YishengyuyueView yishengyuyueView =  yishengyuyueService.selectView(ew);
		return R.ok("查询医生预约成功").put("data", yishengyuyueView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        YishengyuyueEntity yishengyuyue = yishengyuyueService.selectById(id);
		yishengyuyue = yishengyuyueService.selectView(new EntityWrapper<YishengyuyueEntity>().eq("id", id));
        return R.ok().put("data", yishengyuyue);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        YishengyuyueEntity yishengyuyue = yishengyuyueService.selectById(id);
		yishengyuyue = yishengyuyueService.selectView(new EntityWrapper<YishengyuyueEntity>().eq("id", id));
        return R.ok().put("data", yishengyuyue);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    @SysLog("新增医生预约")
    public R save(@RequestBody YishengyuyueEntity yishengyuyue, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(yishengyuyue);
        yishengyuyueService.insert(yishengyuyue);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @SysLog("新增医生预约")
    @RequestMapping("/add")
    public R add(@RequestBody YishengyuyueEntity yishengyuyue, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(yishengyuyue);
        yishengyuyueService.insert(yishengyuyue);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改医生预约")
    public R update(@RequestBody YishengyuyueEntity yishengyuyue, HttpServletRequest request){
        //ValidatorUtils.validateEntity(yishengyuyue);
        yishengyuyueService.updateById(yishengyuyue);//全部更新
        return R.ok();
    }

    /**
     * 审核
     */
    @RequestMapping("/shBatch")
    @Transactional
    @SysLog("审核医生预约")
    public R update(@RequestBody Long[] ids, @RequestParam String sfsh, @RequestParam String shhf){
        List<YishengyuyueEntity> list = new ArrayList<YishengyuyueEntity>();
        for(Long id : ids) {
            YishengyuyueEntity yishengyuyue = yishengyuyueService.selectById(id);
            yishengyuyue.setSfsh(sfsh);
            yishengyuyue.setShhf(shhf);
            list.add(yishengyuyue);
            
            // 审核通过时，自动创建所有就诊通知
            if("是".equals(sfsh)) {
                createNotifications(yishengyuyue);
            }
        }
        yishengyuyueService.updateBatchById(list);
        return R.ok();
    }
    
    /**
     * 创建所有就诊通知
     * @param yishengyuyue 预约信息
     */
    private void createNotifications(YishengyuyueEntity yishengyuyue) {
        Date yuyueTime = yishengyuyue.getYuyueshijian();
        if(yuyueTime == null) {
            return;
        }
        
        Calendar cal = Calendar.getInstance();
        
        // 1. 创建预约成功通知（立即发送）
        TongzhijiluEntity tongzhi1 = new TongzhijiluEntity();
        tongzhi1.setYuyuebianhao(yishengyuyue.getYuyuebianhao());
        tongzhi1.setYishengzhanghao(yishengyuyue.getYishengzhanghao());
        tongzhi1.setZhanghao(yishengyuyue.getZhanghao());
        tongzhi1.setTongzhileixing(1);
        tongzhi1.setTongzhineirong("您的预约已审核通过，预约编号：" + yishengyuyue.getYuyuebianhao() + 
            "，就诊时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(yuyueTime));
        tongzhi1.setFasongzhuangtai(0);
        tongzhi1.setChongshicishu(0);
        tongzhi1.setJiuzhenshijian(yuyueTime);
        tongzhi1.setJihuafasongshijian(new Date());
        tongzhijiluService.insert(tongzhi1);
        
        // 立即尝试发送
        sendNotification(tongzhi1);
        
        // 2. 创建就诊前一天提醒
        cal.setTime(yuyueTime);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date oneDayBefore = cal.getTime();
        
        TongzhijiluEntity tongzhi2 = new TongzhijiluEntity();
        tongzhi2.setYuyuebianhao(yishengyuyue.getYuyuebianhao());
        tongzhi2.setYishengzhanghao(yishengyuyue.getYishengzhanghao());
        tongzhi2.setZhanghao(yishengyuyue.getZhanghao());
        tongzhi2.setTongzhileixing(2);
        tongzhi2.setTongzhineirong("温馨提醒：您明天有就诊预约，预约编号：" + yishengyuyue.getYuyuebianhao() + 
            "，就诊时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(yuyueTime));
        tongzhi2.setFasongzhuangtai(0);
        tongzhi2.setChongshicishu(0);
        tongzhi2.setJiuzhenshijian(yuyueTime);
        tongzhi2.setJihuafasongshijian(oneDayBefore);
        tongzhijiluService.insert(tongzhi2);
        
        // 立即尝试发送
        sendNotification(tongzhi2);
        
        // 3. 创建就诊当天提醒
        cal.setTime(yuyueTime);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date sameDay = cal.getTime();
        
        TongzhijiluEntity tongzhi3 = new TongzhijiluEntity();
        tongzhi3.setYuyuebianhao(yishengyuyue.getYuyuebianhao());
        tongzhi3.setYishengzhanghao(yishengyuyue.getYishengzhanghao());
        tongzhi3.setZhanghao(yishengyuyue.getZhanghao());
        tongzhi3.setTongzhileixing(3);
        tongzhi3.setTongzhineirong("温馨提醒：您今天有就诊预约，预约编号：" + yishengyuyue.getYuyuebianhao() + 
            "，就诊时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(yuyueTime));
        tongzhi3.setFasongzhuangtai(0);
        tongzhi3.setChongshicishu(0);
        tongzhi3.setJiuzhenshijian(yuyueTime);
        tongzhi3.setJihuafasongshijian(sameDay);
        tongzhijiluService.insert(tongzhi3);
        
        // 立即尝试发送
        sendNotification(tongzhi3);
        
        // 同时创建就诊通知记录（jiuzhentongzhi表）
        JiuzhentongzhiEntity jiuzhentongzhi = new JiuzhentongzhiEntity();
        jiuzhentongzhi.setTongzhibianhao("TZ" + System.currentTimeMillis());
        jiuzhentongzhi.setYishengzhanghao(yishengyuyue.getYishengzhanghao());
        jiuzhentongzhi.setDianhua(yishengyuyue.getDianhua());
        jiuzhentongzhi.setJiuzhenshijian(yuyueTime);
        jiuzhentongzhi.setTongzhishijian(new Date());
        jiuzhentongzhi.setZhanghao(yishengyuyue.getZhanghao());
        jiuzhentongzhi.setShouji(yishengyuyue.getShouji());
        jiuzhentongzhi.setTongzhibeizhu("预约审核通过自动创建");
        jiuzhentongzhiService.insert(jiuzhentongzhi);
    }
    
    /**
     * 发送通知并更新状态
     * @param tongzhi 通知记录
     */
    private void sendNotification(TongzhijiluEntity tongzhi) {
        // 模拟发送通知，实际项目中这里调用短信API或推送服务
        boolean sendSuccess = doSendNotification(tongzhi);
        
        if(sendSuccess) {
            tongzhi.setFasongzhuangtai(1); // 发送成功
            tongzhi.setFasongshijian(new Date());
            tongzhi.setShibaiyuanyin(null);
        } else {
            tongzhi.setFasongzhuangtai(2); // 发送失败
            tongzhi.setChongshicishu(tongzhi.getChongshicishu() + 1);
            tongzhi.setShibaiyuanyin("发送失败，请检查用户联系方式");
        }
        
        tongzhijiluService.updateById(tongzhi);
    }
    
    /**
     * 实际发送通知的方法
     * 实际项目中这里应该调用短信接口、推送服务等
     */
    private boolean doSendNotification(TongzhijiluEntity tongzhi) {
        // 模拟发送逻辑，实际项目中替换为真实的发送代码
        // 这里模拟90%的成功率
        return Math.random() > 0.1;
    }


    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除医生预约")
    public R delete(@RequestBody Long[] ids){
        yishengyuyueService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	





    /**
     * （按值统计）
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}")
    public R value(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName,HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", MPUtil.camelToSnake(xColumnName));
        params.put("yColumn", MPUtil.camelToSnake(yColumnName));
        EntityWrapper<YishengyuyueEntity> ew = new EntityWrapper<YishengyuyueEntity>();
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yisheng")) {
            ew.eq("yishengzhanghao", (String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("yonghu")) {
            ew.eq("zhanghao", (String)request.getSession().getAttribute("username"));
		}
        List<Map<String, Object>> result = MPUtil.snakeListToCamel(yishengyuyueService.selectValue(params, ew));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }

        Collections.sort(result, (map1, map2) -> {
            // 假设 total 总是存在并且是数值类型
            Number total1 = (Number) map1.get("total");
            Number total2 = (Number) map2.get("total");
            if(total1==null)
            {
                total1 = 0;
            }
            if(total2==null)
            {
                total2 = 0;
            }
            String order = request.getParameter("order");
            if(StringUtils.isNotBlank(order)&&order.equals("desc")){
                return Double.compare(total2.doubleValue(), total1.doubleValue());
            }
            return Double.compare(total1.doubleValue(), total2.doubleValue());
        });

        return R.ok().put("data", result);
    }

    /**
     * （按值统计(多)）
     */
    @RequestMapping("/valueMul/{xColumnName}")
    public R valueMul(@PathVariable("xColumnName") String xColumnName,@RequestParam String yColumnNameMul, HttpServletRequest request) {
        String[] yColumnNames = MPUtil.camelToSnake(yColumnNameMul).split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", MPUtil.camelToSnake(xColumnName));
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EntityWrapper<YishengyuyueEntity> ew = new EntityWrapper<YishengyuyueEntity>();
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yisheng")) {
            ew.eq("yishengzhanghao", (String)request.getSession().getAttribute("username"));
        }
        if(tableName.equals("yonghu")) {
            ew.eq("zhanghao", (String)request.getSession().getAttribute("username"));
        }
        for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = MPUtil.snakeListToCamel(yishengyuyueService.selectValue(params, ew));
            for(Map<String, Object> m : result) {
                for(String k : m.keySet()) {
                    if(m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date)m.get(k)));
                    }
                }
            }
            result2.add(result);
        }
        return R.ok().put("data", result2);
    }

    /**
     * （按值统计）时间统计类型
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}/{timeStatType}")
    public R valueDay(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType,HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", MPUtil.camelToSnake(xColumnName));
        params.put("yColumn", MPUtil.camelToSnake(yColumnName));
        params.put("timeStatType", timeStatType);
        EntityWrapper<YishengyuyueEntity> ew = new EntityWrapper<YishengyuyueEntity>();
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yisheng")) {
            ew.eq("yishengzhanghao", (String)request.getSession().getAttribute("username"));
        }
        if(tableName.equals("yonghu")) {
            ew.eq("zhanghao", (String)request.getSession().getAttribute("username"));
        }
        List<Map<String, Object>> result = MPUtil.snakeListToCamel(yishengyuyueService.selectTimeStatValue(params, ew));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }

    /**
     * （按值统计）时间统计类型(多)
     */
    @RequestMapping("/valueMul/{xColumnName}/{timeStatType}")
    public R valueMulDay(@PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType,@RequestParam String yColumnNameMul,HttpServletRequest request) {
        String[] yColumnNames = MPUtil.camelToSnake(yColumnNameMul).split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("timeStatType", timeStatType);
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EntityWrapper<YishengyuyueEntity> ew = new EntityWrapper<YishengyuyueEntity>();
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yisheng")) {
            ew.eq("yishengzhanghao", (String)request.getSession().getAttribute("username"));
        }
        if(tableName.equals("yonghu")) {
            ew.eq("zhanghao", (String)request.getSession().getAttribute("username"));
        }
        for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = MPUtil.snakeListToCamel(yishengyuyueService.selectTimeStatValue(params, ew));
            for(Map<String, Object> m : result) {
                for(String k : m.keySet()) {
                    if(m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date)m.get(k)));
                    }
                }
            }
            result2.add(result);
        }
        return R.ok().put("data", result2);
    }

    /**
     * 分组统计
     */
    @RequestMapping("/group/{columnName}")
    public R group(@PathVariable("columnName") String columnName,HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("column", MPUtil.camelToSnake(columnName));
        EntityWrapper<YishengyuyueEntity> ew = new EntityWrapper<YishengyuyueEntity>();
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yisheng")) {
            ew.eq("yishengzhanghao", (String)request.getSession().getAttribute("username"));
        }
        if(tableName.equals("yonghu")) {
            ew.eq("zhanghao", (String)request.getSession().getAttribute("username"));
        }
        List<Map<String, Object>> result = MPUtil.snakeListToCamel(yishengyuyueService.selectGroup(params, ew));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }




    /**
     * 总数量
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params,YishengyuyueEntity yishengyuyue, HttpServletRequest request){
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yisheng")) {
            yishengyuyue.setYishengzhanghao((String)request.getSession().getAttribute("username"));
        }
        if(tableName.equals("yonghu")) {
            yishengyuyue.setZhanghao((String)request.getSession().getAttribute("username"));
        }
        EntityWrapper<YishengyuyueEntity> ew = new EntityWrapper<YishengyuyueEntity>();
        int count = yishengyuyueService.selectCount(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yishengyuyue), params), params));
        return R.ok().put("data", count);
    }



}
