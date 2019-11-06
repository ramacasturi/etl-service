package com.myswiftly.etl.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.myswiftly.etl.models.IngestionInfo;

@Component
public class IngestionInfoDAOImpl implements IngestionInfoDAO {

	private Map<String, IngestionInfo> _ingestionInfoTable = new ConcurrentHashMap<String, IngestionInfo>();

	@Override
	public void saveOrUpdate(IngestionInfo ingestionInfo) {
		_ingestionInfoTable.put(ingestionInfo.getId(), ingestionInfo);
	}

	@Override
	public IngestionInfo get(String messageId) {
		return _ingestionInfoTable.get(messageId);
	}

	@Override
	public List<IngestionInfo> findActiveInfosOlderThan(long durationInSeconds) {
		// TODO Looks through the table to find any records that have not been updated 
		// since durationInSeconds ago
		return new ArrayList<IngestionInfo>();
	}
}
