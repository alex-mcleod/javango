package co.gitm.javango.data;

import java.util.Map;

/**
 * @author Alex McLeod
 * 
 *         Query objects are used to query DataSources. Each Query has an
 *         objectName (the name of the table being queried), a filter (which
 *         corresponds to WHERE clause arguments) and an order ( which
 *         determines the order of the results set returned by the DataSource
 *         being queried).
 * 
 */
public class Query {
	private String objectName = null;
	private Map<String, String> filter = null;
	private String order = null;

	public Query() {
	}

	public Query(String objectName) {
		this.setObjectName(objectName);
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public void setFilter(Map<String, String> filter) {
		// Ensure that if filter is empty, this.filter does not get set
		// so that any classes which use query can check whether a filter
		// has been set using one method (i.e. testing if query.getFilter ==
		// null)
		if (!filter.isEmpty()) {
			this.filter = filter;
		}
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getObjectName() {
		return this.objectName;
	}

	public Map<String, String> getFilter() {
		return this.filter;
	}

	public String getOrder() {
		return this.order;
	}
}
