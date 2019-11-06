package com.myswiftly.etl.controllers;

import java.time.Instant;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myswiftly.etl.IngestionQueue;
import com.myswiftly.etl.dao.IngestionInfoDAO;
import com.myswiftly.etl.models.IngestionInfo;
import com.myswiftly.etl.models.IngestionQueueMessage;
import com.myswiftly.etl.models.IngestionStatus;

@Controller
public class IngestionController {

	private static final Logger LOGGER = Logger.getLogger(IngestionController.class.getSimpleName());

	@Autowired
	private IngestionQueue _ingestionQueue;

	@Autowired
	private IngestionInfoDAO _ingestionInfoDAO;

	@RequestMapping(method = RequestMethod.POST, path = "/stores/{storeId}/catalog")
	@ResponseBody
	public void handleCatalogUpdate(@PathVariable int storeId, @RequestParam String catalogFileUri) {

		LOGGER.info("Request to ingest to storeId[" + storeId + "] from catalog file [" + catalogFileUri + "]");

		String messageId = UUID.randomUUID().toString();
		long timestamp = Instant.now().toEpochMilli();

		IngestionInfo ingestionInfo = new IngestionInfo(messageId, storeId, catalogFileUri,
				timestamp, timestamp, 0, IngestionStatus.Waiting);

		_ingestionInfoDAO.saveOrUpdate(ingestionInfo);

		_ingestionQueue.add(new IngestionQueueMessage(messageId, storeId, catalogFileUri));
	}
}