package com.myswiftly.etl;

import com.myswiftly.etl.models.IngestionQueueMessage;

public interface IngestionQueue {
	public void add(IngestionQueueMessage message);

	public IngestionQueueMessage remove();
}
