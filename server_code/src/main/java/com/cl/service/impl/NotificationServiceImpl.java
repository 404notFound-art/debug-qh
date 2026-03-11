package com.cl.service.impl;

import com.cl.entity.JiuzhentongzhiEntity;
import com.cl.service.NotificationService;
import com.cl.service.JiuzhentongzhiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private JiuzhentongzhiService jiuzhentongzhiService;

    @Override
    public boolean sendNotification(JiuzhentongzhiEntity notification) {
        // 检查用户接收状态
        if (!checkUserReceiveStatus(notification.getShouji())) {
            updateNotificationStatus(notification.getId(), "发送失败", "用户接收状态异常");
            return false;
        }

        try {
            // 模拟发送通知的逻辑
            // 实际项目中这里可以调用短信API、邮件API等
            System.out.println("发送通知：" + notification.getTongzhibeizhu() + " 到手机：" + notification.getShouji());

            // 模拟发送成功
            updateNotificationStatus(notification.getId(), "发送成功", null);
            return true;
        } catch (Exception e) {
            // 发送失败，更新状态
            updateNotificationStatus(notification.getId(), "发送失败", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean retrySendNotification(JiuzhentongzhiEntity notification) {
        // 检查重试次数
        if (notification.getChongshicishu() == null) {
            notification.setChongshicishu(0);
        }

        if (notification.getChongshicishu() >= 3) {
            // 超过最大重试次数
            updateNotificationStatus(notification.getId(), "发送失败", "超过最大重试次数");
            return false;
        }

        // 增加重试次数
        notification.setChongshicishu(notification.getChongshicishu() + 1);
        notification.setZuihouchongshishijian(new Date());
        jiuzhentongzhiService.updateById(notification);

        // 重新发送
        return sendNotification(notification);
    }

    @Override
    public void updateNotificationStatus(Long notificationId, String status, String errorMessage) {
        JiuzhentongzhiEntity notification = jiuzhentongzhiService.selectById(notificationId);
        if (notification != null) {
            notification.setTongzhizhuangtai(status);
            if (errorMessage != null) {
                notification.setShibaiyuanyin(errorMessage);
            }
            jiuzhentongzhiService.updateById(notification);
        }
    }

    @Override
    public boolean shouldSendNotification(JiuzhentongzhiEntity notification) {
        // 检查通知时间是否到达
        Date now = new Date();
        return now.after(notification.getTongzhishijian()) || now.equals(notification.getTongzhishijian());
    }

    @Override
    public boolean checkUserReceiveStatus(String phone) {
        // 模拟检查用户接收状态
        // 实际项目中这里可以检查用户是否屏蔽了通知、是否欠费等
        return true;
    }

}