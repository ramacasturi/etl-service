package com.myswiftly.etl;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class IngestionQueueScannerService {

	private static final Logger LOGGER = Logger.getLogger(IngestionQueueScannerService.class.getSimpleName());

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private ApplicationContext applicationContext;

	@EventListener
	public void handleContextRefreshEvent(ContextRefreshedEvent ctxRefreshedEvent) {
		LOGGER.info("Context Refreshed Event received.");

		// This starts background threads that scan the ingestion queue. We could
		// potentially create at least as many scanners as there are CPU cores on the
		// machine sku we run the service on. If multiple stores are all pushing updates
		// at high scale, the queue gets longer. More scanner tasks will help reduce
		// latency with respect to when each update gets picked up for processing
		for (int i = 0; i < 2; i++) {
			taskExecutor.execute(applicationContext.getBean(IngestionQueueScanner.class));
		}

		// Starts another background thread that monitors the ingestion info table for
		// potentially failed scanner threads
		taskExecutor.execute(applicationContext.getBean(IngestionRetryScanner.class));
	}
}
