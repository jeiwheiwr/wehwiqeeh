package com.heima.schedule.feign;

import com.heima.apis.schedule.IScheduleClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.schedule.dtos.Task;
import com.heima.schedule.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScheduleClient implements IScheduleClient {
    @Autowired
    private TaskService taskService;

    /**
     * 添加任务
     * @param task
     * @return
     */
    @PostMapping("/api/v1/schedule/add")
    @Override
    public ResponseResult addTask(Task task) {
        Long addTask = taskService.addTask(task);
        return ResponseResult.okResult(addTask);
    }

    /**
     * 取消任务
     * @param taskId 任务ID
     * @return 是否取消成功
     */
    @GetMapping("/api/v1/schedule/cancel/{taskId}")
    @Override
    public ResponseResult cancelTask(@PathVariable Long taskId) {
        boolean cancelTask = taskService.cancelTask(taskId);
        return ResponseResult.okResult(cancelTask);
    }

    /**
     * 按照类型和优先级来拉取任务
     * @param type 任务类型
     * @param priority 任务优先级
     * @return 任务对象
     */
    @GetMapping("/api/v1/schedule/poll/{type}/{priority}")
    @Override
    public ResponseResult poll(@PathVariable int type, @PathVariable int priority) {
        Task task = taskService.poll(type, priority);
        return ResponseResult.okResult(task);
    }


}
