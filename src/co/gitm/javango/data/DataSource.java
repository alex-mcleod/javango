package co.gitm.javango.data;

import co.gitm.javango.data.exceptions.CreateException;

/**
 * @author Alex McLeod
 * 
 *         All DataSources which can be accessed by model objects must implement
 *         this interface. This ensures that they support creating, retrieving,
 *         updating and deleting model objects.
 * 
 */
public interface DataSource {
	/**
	 * @param newData
	 *            to insert into datasource
	 * @throws CreateException
	 *             if insertion failed.
	 */
	public void create(DataObject newData) throws CreateException;

	/**
	 * @param query
	 *            to query the datasource
	 * @return all objects in the datasource which matched that query. TODO
	 *         Should throw a RetrieveException.
	 */
	public DataObjectArray retrieve(Query query);

	/**
	 * TODO Implement update.
	 */
	public void update();

	/**
	 * TODO Implement delete.
	 */
	public void delete();
}
