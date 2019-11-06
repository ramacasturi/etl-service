package com.myswiftly.etl.models;

public class IngestionInfo {
	private String _id;
	private int _storeId;
	private String _catalogFileUri;
	private long _createdTimestamp;
	private long _lastUpdatedTimestamp;
	private int _linesIngested;
	private IngestionStatus _ingestionStatus;

	public IngestionInfo(String _id, int _storeId, String _catalogFileUri, long _createdTimestamp,
			long _lastUpdatedTimestamp, int _linesIngested, IngestionStatus _ingestionStatus) {
		super();
		this._id = _id;
		this._storeId = _storeId;
		this._catalogFileUri = _catalogFileUri;
		this._createdTimestamp = _createdTimestamp;
		this._lastUpdatedTimestamp = _lastUpdatedTimestamp;
		this._linesIngested = _linesIngested;
		this._ingestionStatus = _ingestionStatus;
	}

	public String getId() {
		return _id;
	}

	public void setId(String _id) {
		this._id = _id;
	}

	public int getStoreId() {
		return _storeId;
	}

	public void setStoreId(int _storeId) {
		this._storeId = _storeId;
	}

	public String getCatalogFileUri() {
		return _catalogFileUri;
	}

	public void setCatalogFileUri(String _catalogFileUri) {
		this._catalogFileUri = _catalogFileUri;
	}

	public long getCreatedTimestamp() {
		return _createdTimestamp;
	}

	public void setCreatedTimestamp(long _createdTimestamp) {
		this._createdTimestamp = _createdTimestamp;
	}

	public long getLastUpdatedTimestamp() {
		return _lastUpdatedTimestamp;
	}

	public void setLastUpdatedTimestamp(long _lastUpdatedTimestamp) {
		this._lastUpdatedTimestamp = _lastUpdatedTimestamp;
	}

	public int getLinesIngested() {
		return _linesIngested;
	}

	public void setLinesIngested(int _linesIngested) {
		this._linesIngested = _linesIngested;
	}

	public IngestionStatus getIngestionStatus() {
		return _ingestionStatus;
	}

	public void setIngestionStatus(IngestionStatus _ingestionStatus) {
		this._ingestionStatus = _ingestionStatus;
	}

}
