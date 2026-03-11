package com.cl.service;

import com.cl.entity.JiuzhentongzhiEntity;

public interface NotificationService {

    /**
     * 发送通知
     * @param notification 通知实体
     * @return 是否发送成功
     */
    boolean sendNotification(JiuzhentongzhiEntity notification);

    /**
     * 重试发送通知
     * @param notification 通知实体
     * @return 是否发送成功
     */
    boolean retrySendNotification(JiuzhentongzhiEntity notification);

    /**
     * 更新通知状态
     * @param notificationId 通知ID
     * @param status 状态
     * @param errorMessage 错误信息（如果有）
     */
    void updateNotificationStatus(Long notificationId, String status, String errorMessage);

    /**
     * 检查通知是否应该发送
     * @param notification 通知实体
     * @return 是否应该发送
     */
    boolean shouldSendNotification(JiuzhentongzhiEntity notification);

    /**
     * 检查用户接收状态
     * @param phone 手机号
     * @return 用户接收状态
     */
    boolean checkUserReceiveStatus(String phone);

}