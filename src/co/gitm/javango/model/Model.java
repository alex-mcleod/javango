package co.gitm.javango.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import co.gitm.javango.data.DataObject;
import co.gitm.javango.data.DataObjectArray;
import co.gitm.javango.data.Query;
import co.gitm.javango.data.DataSource;
import co.gitm.javango.data.exceptions.CreateException;

/**
 * @author Alex McLeod
 * 
 *         Models are responsible for defining and interacting with application
 *         resources/data. Each model in the application must inherit from this
 *         class and implement three abstract methods: getDataSource, getName
 *         and getFields. These methods are then used by this class (i.e. the
 *         parent class) to getAll, getWithFilter and createNew child-defined
 *         model objects from the child-designated datasource.
 *         
 *         Models recieve and return DataObjectArray's of DataObjects.
 * 
 */
public abstract class Model {
	/**
	 * @return the datasource which the child-class uses to store the objects it
	 *         defines (e.g. MySQL database).
	 */
	protected abstract DataSource getDataSource();

	/**
	 * @return the name which can be used to identify the set of objects defined
	 *         by the child-class (e.g. the database table name)
	 */
	public abstract String getName();

	/**
	 * @return the list of fields which make up each child-class object.
	 */
	public abstract List<String> getFields();

	/**
	 * @return all objects defined by the child-class from the datasource.
	 */
	public DataObjectArray getAll() {
		return this.getDataSource().retrieve(new Query(this.getName()));
	}

	/**
	 * @param filter
	 *            is a Map which holds key-value pairs which should be used to
	 *            filter the child-class object set (e.g. {"author" : "George
	 *            Orwell} will return all Books whose author is George Orwell).
	 * @return the filter child-class object set.
	 * @throws NoSuchFieldException
	 *             if one or more of the keys in the map does not exist in the
	 *             model's fields. Will pass these invalid keys back to the
	 *             caller (as a string).
	 */
	public DataObjectArray getWithFilter(Map<String, String> filter)
			throws NoSuchFieldException {
		// Check if any fields in filter are invalid.
		// NOTE: Need to create new HashMap from filter, or else filter will be
		// modified by findInvalidFields method.
		Set invalidFields = this.findFieldsNotInModel(new HashMap(filter));
		if (!invalidFields.isEmpty()) {
			throw new NoSuchFieldException(invalidFields.toString());
		}
		// If there are no invalid fields, create a new Query object with this
		// filter, and use it to Query the datasource.
		Query query = new Query();
		query.setObjectName(this.getName());
		query.setFilter(filter);
		return this.getDataSource().retrieve(query);
	}

	/**
	 * @param inputData
	 *            to attempt to add to the datasource as a new child-class model
	 *            object.
	 * @throws CreateException
	 *             if the inputData object is not a valid object as defined by
	 *             the child-class model.
	 */
	public void createNew(DataObject inputData) throws CreateException {
		// Set the name of this object to that of the child-class model.
		inputData.setObjectName(this.getName());
		this.getDataSource().create(inputData);
	}

	/**
	 * @param mapToCompare
	 *            is a Map whose fields (i.e. keys) we want to compare against
	 *            the child-class model fields.
	 * @return all keys in the mapToCompare which do not exist in the
	 *         child-class model field set.
	 */
	private Set<String> findFieldsNotInModel(HashMap mapToCompare) {
		Set<String> modelFields = new HashSet<String>(this.getFields());
		Set<String> mapFields = mapToCompare.keySet();
		mapFields.removeAll(modelFields);
		return mapFields;
	}
}
