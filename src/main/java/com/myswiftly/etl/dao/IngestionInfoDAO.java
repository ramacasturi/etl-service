package com.myswiftly.etl.dao;

import java.util.List;

import com.myswiftly.etl.models.IngestionInfo;

public interface IngestionInfoDAO {
	public void saveOrUpdate(IngestionInfo ingestionInfo);

	public IngestionInfo get(String messageId);

	public List<IngestionInfo> findActiveInfosOlderThan(long durationInSeconds);
}
