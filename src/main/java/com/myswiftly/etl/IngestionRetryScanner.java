package com.myswiftly.etl;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.myswiftly.etl.dao.IngestionInfoDAO;
import com.myswiftly.etl.ingestors.Ingestor;
import com.myswiftly.etl.models.IngestionInfo;
import com.myswiftly.etl.models.IngestionStatus;

@Component
@Scope("prototype")
public class IngestionRetryScanner implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(IngestionRetryScanner.class.getSimpleName());

	public static final int RETRY_DURATION_IN_SECS = 600;

	@Autowired
	private IngestionInfoDAO _ingestionInfoDAO;

	@Autowired
	private Ingestor _catalogIngestor;

	/**
	 * Ingestion queue scanner threads might fail unexpectedly. In that case, the
	 * catalog update file might only be partially processed. This thread scans the
	 * ingestion table for any records whose last update timestamp (the last
	 * checkpoint) was more than a certain amount of time. If there are such
	 * records, it assumes the threads processing those records have failed. So it
	 * kicks off another processing of those records, which will start processing
	 * the file from the last checkpoint
	 */
	@Override
	public void run() {
		while (true) {
			LOGGER.info("Checking for failed ingestion messages [" + this.toString() + "]");

			List<IngestionInfo> activeInfos = _ingestionInfoDAO.findActiveInfosOlderThan(RETRY_DURATION_IN_SECS);

			if (activeInfos != null) {
				for (IngestionInfo info : activeInfos) {
					LOGGER.info("Processing [" + info.getStoreId() + "]  [" + this.toString() + "]");
					processMessage(info);
				}
			}

			LOGGER.info("Sleeping... [" + this.toString() + "]");

			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// ignore
			}
		}
	}

	private void processMessage(IngestionInfo info) {

		info.setIngestionStatus(IngestionStatus.Processing);
		info.setLastUpdatedTimestamp(Instant.now().toEpochMilli());

		_ingestionInfoDAO.saveOrUpdate(info);

		_catalogIngestor.process(info);

	}
}
