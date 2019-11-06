package com.myswiftly.etl.ingestors;

import com.myswiftly.etl.models.IngestionInfo;

public interface Ingestor {
	public void process(IngestionInfo ingestionInfo);
}
