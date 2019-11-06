package com.myswiftly.etl.ingestors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.myswiftly.etl.dao.IngestionInfoDAO;
import com.myswiftly.etl.dao.ProductDAO;
import com.myswiftly.etl.models.IngestionInfo;
import com.myswiftly.etl.models.IngestionStatus;
import com.myswiftly.etl.models.Product;
import com.myswiftly.etl.parsers.FixedWidthProductParser;

@Component
public class FixedWidthFileIngestor implements Ingestor {

	private static final Logger LOGGER = Logger.getLogger(FixedWidthFileIngestor.class.getSimpleName());

	@Autowired
	private IngestionInfoDAO _ingestionInfoDAO;

	@Autowired
	private ProductDAO _productDAO;

	@Override
	public void process(IngestionInfo ingestionInfo) {

		LOGGER.info("Ingesting file [" + ingestionInfo.getCatalogFileUri() + "]");

		File file = new File(ingestionInfo.getCatalogFileUri());
		try (InputStream in = new FileInputStream(file);
				Reader reader = new InputStreamReader(in, StandardCharsets.US_ASCII);
				Reader buffer = new BufferedReader(reader)) {

			int totalLinesReadAlready = ingestionInfo.getLinesIngested();

			char[] buf = new char[FixedWidthProductParser.LINE_WIDTH];
			int r, totalLinesRead = 0;
			while ((r = reader.read(buf, 0, FixedWidthProductParser.LINE_WIDTH)) != -1) {

				totalLinesRead++;

				// skip lines that were already read from a previous run that may have
				// terminated prematurely
				if (totalLinesRead <= totalLinesReadAlready) {
					continue;
				}

				Product product = FixedWidthProductParser.parse(buf);
				product.setStoreId(ingestionInfo.getStoreId());

				_productDAO.saveOrUpdate(product);

				// After every 10 records save, create a checkpoint
				if (totalLinesRead % 10 == 0) {
					LOGGER.info("Ingested [" + totalLinesRead + "] records from file ["
							+ ingestionInfo.getCatalogFileUri() + "]");
					ingestionInfo.setLinesIngested(totalLinesRead);
					ingestionInfo.setLastUpdatedTimestamp(Instant.now().toEpochMilli());
					_ingestionInfoDAO.saveOrUpdate(ingestionInfo);
				}

				// skip the carriage return and new line chars
				reader.read();
				reader.read();
			}

			ingestionInfo.setLinesIngested(totalLinesRead);
			ingestionInfo.setLastUpdatedTimestamp(Instant.now().toEpochMilli());
			ingestionInfo.setIngestionStatus(IngestionStatus.Completed);
			_ingestionInfoDAO.saveOrUpdate(ingestionInfo);

			LOGGER.info("Finished ingesting [" + totalLinesRead + "] records from file ["
					+ ingestionInfo.getCatalogFileUri() + "]");

		} catch (Exception e) {
			LOGGER.warning(e.getLocalizedMessage());
			ingestionInfo.setLastUpdatedTimestamp(Instant.now().toEpochMilli());
			ingestionInfo.setIngestionStatus(IngestionStatus.Failed);
			_ingestionInfoDAO.saveOrUpdate(ingestionInfo);
		}
	}
}
