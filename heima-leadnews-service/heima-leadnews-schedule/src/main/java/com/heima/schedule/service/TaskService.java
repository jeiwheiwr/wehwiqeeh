package com.heima.schedule.service;

import com.heima.model.schedule.dtos.Task;

public interface TaskService  {

    /**
     * 添加任务
     * @param task 任务对象
     * @return
     */
    public Long addTask(Task task);

    /**
     * 取消任务
     * @param taskId 任务ID
     * @return
     */
    public boolean cancelTask(Long taskId);

    /**
     * 消费任务
     * @param type
     * @param priority
     */
    public Task poll(int type,int priority);

}
