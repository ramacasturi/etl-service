package com.myswiftly.etl;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Component;

import com.myswiftly.etl.models.IngestionQueueMessage;

@Component
public class IngestionQueueImpl implements IngestionQueue {

	private Queue<IngestionQueueMessage> _queue = new ConcurrentLinkedQueue<IngestionQueueMessage>();

	@Override
	public void add(IngestionQueueMessage message) {
		_queue.add(message);
	}

	@Override
	public IngestionQueueMessage remove() {
		return _queue.poll();
	}
}
