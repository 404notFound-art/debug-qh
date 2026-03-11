package com.cl.task;

import com.cl.entity.JiuzhentongzhiEntity;
import com.cl.service.JiuzhentongzhiService;
import com.cl.service.NotificationService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class NotificationTask {

    @Autowired
    private JiuzhentongzhiService jiuzhentongzhiService;

    @Autowired
    private NotificationService notificationService;

    /**
     * 每分钟检查一次待发送的通知
     */
    @Scheduled(cron = "0 * * * * ?")
    public void checkAndSendNotifications() {
        // 查询所有待发送的通知
        EntityWrapper<JiuzhentongzhiEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("tongzhizhuangtai", "待发送");
        wrapper.or().eq("tongzhizhuangtai", "发送失败");
        List<JiuzhentongzhiEntity> notifications = jiuzhentongzhiService.selectList(wrapper);

        for (JiuzhentongzhiEntity notification : notifications) {
            // 检查是否应该发送
            if (notificationService.shouldSendNotification(notification)) {
                if ("待发送".equals(notification.getTongzhizhuangtai())) {
                    // 发送新通知
                    notificationService.sendNotification(notification);
                } else if ("发送失败".equals(notification.getTongzhizhuangtai())) {
                    // 重试发送
                    notificationService.retrySendNotification(notification);
                }
            }
        }
    }

    /**
     * 每天凌晨清理过期通知
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanExpiredNotifications() {
        // 查询所有30天前的通知
        EntityWrapper<JiuzhentongzhiEntity> wrapper = new EntityWrapper<>();
        Date thirtyDaysAgo = new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000);
        wrapper.lt("addtime", thirtyDaysAgo);
        jiuzhentongzhiService.delete(wrapper);
    }

}