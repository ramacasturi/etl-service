package com.myswiftly.etl;

import java.time.Instant;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.myswiftly.etl.dao.IngestionInfoDAO;
import com.myswiftly.etl.ingestors.Ingestor;
import com.myswiftly.etl.models.IngestionInfo;
import com.myswiftly.etl.models.IngestionQueueMessage;
import com.myswiftly.etl.models.IngestionStatus;

@Component
@Scope("prototype")
public class IngestionQueueScanner implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(IngestionQueueScanner.class.getSimpleName());

	@Autowired
	private IngestionQueue _ingestionQueue;

	@Autowired
	private IngestionInfoDAO _ingestionInfoDAO;

	@Autowired
	private Ingestor _catalogIngestor;

	@Override
	public void run() {
		while (true) {
			LOGGER.info("Checking queue [" + this.toString() + "]");

			IngestionQueueMessage message = _ingestionQueue.remove();
			if (message != null) {
				LOGGER.info("Processing message for store [" + message.getStoreId() + "]  [" + this.toString() + "]");
				processMessage(message);
			}

			LOGGER.info("Sleeping... [" + this.toString() + "]");

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// ignore
			}
		}
	}

	private void processMessage(IngestionQueueMessage message) {

		IngestionInfo info = _ingestionInfoDAO.get(message.getId());

		info.setIngestionStatus(IngestionStatus.Processing);
		info.setLastUpdatedTimestamp(Instant.now().toEpochMilli());

		_ingestionInfoDAO.saveOrUpdate(info);

		// NOTE on alternate design
		// There could be different types of ingestors, depending on the types of files
		// (or other mechanisms) customers might push data into our service.
		// Instead ofa single catalog ingestor (like the fixed width format file
		// ingestor in this implementation), we could have a list of ingestors here and
		// select the right one depending on the type of ingestion message

		_catalogIngestor.process(info);

	}
}