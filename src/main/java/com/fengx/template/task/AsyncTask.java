package com.fengx.template.task;

import com.fengx.template.utils.common.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 异步任务
 */
@Slf4j
@Component
public class AsyncTask {
	
	@Async
	public void doTask() throws InterruptedException{
		log.info("Task1 started.");
		DateUtils.startTime();
        Thread.sleep(5000);
		log.info("Task1 finished, time elapsed: {} ms.", DateUtils.endTime());
	}
}