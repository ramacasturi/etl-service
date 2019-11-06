package com.myswiftly.etl.ingestors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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

		try (BufferedReader reader = new BufferedReader(new FileReader(ingestionInfo.getCatalogFileUri()))) {

			int totalLinesReadAlready = ingestionInfo.getLinesIngested();

			int r, totalLinesRead = 0;

			// Ideally we would want to read character by character (buffered), so we avoid
			// the conversion from chars->String->char[]. For that, need to find a reliable
			// way to read line endings on different platforms. In the interest of time,
			// using readLine() for now
			// char[] buf = new char[FixedWidthProductParser.LINE_WIDTH];
			// while ((r = reader.read(buf, 0, FixedWidthProductParser.LINE_WIDTH)) != -1) {
			String line;
			while ((line = reader.readLine()) != null) {

				totalLinesRead++;

				// skip lines that were already read from a previous run that may have
				// terminated prematurely
				if (totalLinesRead <= totalLinesReadAlready) {
					continue;
				}

				// Product product = FixedWidthProductParser.parse(buf);
				Product product = FixedWidthProductParser.parse(line.toCharArray());
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
				// reader.read();
				// reader.read();
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
