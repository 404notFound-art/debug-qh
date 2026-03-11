package com.cl.service;

import com.cl.entity.JiuzhentongzhiEntity;
import com.cl.service.impl.JiuzhentongzhiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class NoticeService {

    @Autowired
    private JiuzhentongzhiServiceImpl jiuzhentongzhiService;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    /**
     * 发送通知
     * @param notice 通知实体
     */
    public void sendNotice(JiuzhentongzhiEntity notice) {
        executorService.submit(() -> {
            try {
                // 模拟发送通知
                System.out.println("发送通知: " + notice.getTongzhibianhao());
                // 这里可以添加实际的通知发送逻辑，比如短信、邮件等
                
                // 模拟发送成功
                Thread.sleep(1000);
                
                // 更新通知状态为已发送
                notice.setFasongzhuangtai("1");
                notice.setTongzhishijian(new Date());
                jiuzhentongzhiService.updateById(notice);
            } catch (Exception e) {
                // 更新通知状态为发送失败
                notice.setFasongzhuangtai("2");
                notice.setShibaoyuanyin(e.getMessage());
                notice.setChongshicishu(notice.getChongshicishu() != null ? notice.getChongshicishu() + 1 : 1);
                jiuzhentongzhiService.updateById(notice);
                
                // 尝试重试
                retrySendNotice(notice);
            }
        });
    }

    /**
     * 重试发送通知
     * @param notice 通知实体
     */
    private void retrySendNotice(JiuzhentongzhiEntity notice) {
        if (notice.getChongshicishu() < 3) { // 最多重试3次
            executorService.submit(() -> {
                try {
                    // 等待一段时间后重试
                    Thread.sleep(5000);
                    
                    // 模拟发送通知
                    System.out.println("重试发送通知: " + notice.getTongzhibianhao() + "，重试次数: " + notice.getChongshicishu());
                    
                    // 模拟发送成功
                    Thread.sleep(1000);
                    
                    // 更新通知状态为已发送
                    notice.setFasongzhuangtai("1");
                    notice.setTongzhishijian(new Date());
                    jiuzhentongzhiService.updateById(notice);
                } catch (Exception e) {
                    // 更新通知状态为发送失败
                    notice.setFasongzhuangtai("2");
                    notice.setShibaoyuanyin(e.getMessage());
                    notice.setChongshicishu(notice.getChongshicishu() + 1);
                    jiuzhentongzhiService.updateById(notice);
                }
            });
        }
    }

    /**
     * 处理所有未发送的通知
     */
    public void processUnsentNotices() {
        List<JiuzhentongzhiEntity> unsentNotices = jiuzhentongzhiService.selectList(
                new com.baomidou.mybatisplus.mapper.EntityWrapper<JiuzhentongzhiEntity>()
                        .eq("fasongzhuangtai", "0")
                        .or().eq("fasongzhuangtai", "2")
                        .lt("chongshicishu", 3)
        );

        for (JiuzhentongzhiEntity notice : unsentNotices) {
            sendNotice(notice);
        }
    }
}
