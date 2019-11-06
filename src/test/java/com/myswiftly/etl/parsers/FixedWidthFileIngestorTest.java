package com.myswiftly.etl.parsers;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.myswiftly.etl.dao.IngestionInfoDAO;
import com.myswiftly.etl.dao.ProductDAO;
import com.myswiftly.etl.ingestors.FixedWidthFileIngestor;
import com.myswiftly.etl.models.IngestionInfo;
import com.myswiftly.etl.models.IngestionStatus;
import com.myswiftly.etl.models.Product;

@ExtendWith(MockitoExtension.class)
public class FixedWidthFileIngestorTest {

	@Mock
	private IngestionInfoDAO _ingestionInfoDAO;

	@Mock
	private ProductDAO _productDAO;

	@InjectMocks
	private FixedWidthFileIngestor _ingestor;

	@Test
	public void testProcess() {

		String catalogFileUri = getClass().getClassLoader().getResource("testCatalog").getFile();
		IngestionInfo info = new IngestionInfo(UUID.randomUUID().toString(), 1, catalogFileUri,
				Instant.now().toEpochMilli(), 0, 0, IngestionStatus.Waiting);

		_ingestor.process(info);

		Mockito.verify(_productDAO, Mockito.times(4)).saveOrUpdate(ArgumentMatchers.any());
		Mockito.verify(_ingestionInfoDAO, Mockito.times(1)).saveOrUpdate(ArgumentMatchers.any());
	}
}
