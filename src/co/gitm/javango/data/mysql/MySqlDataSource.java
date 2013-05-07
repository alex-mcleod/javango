package co.gitm.javango.data.mysql;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.Configuration;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import co.gitm.javango.config.Config;
import co.gitm.javango.data.DataObject;
import co.gitm.javango.data.DataObjectArray;
import co.gitm.javango.data.Query;
import co.gitm.javango.data.DataSource;
import co.gitm.javango.data.exceptions.CreateException;

/**
 * @author Alex McLeod
 * 
 *         This class enables the creation, retrieval, insertion and deletion of
 *         data in MySQL databases. It is essentially a wrapper for the JDBC
 *         MysqlDataSource class, but it enables querying of a database using
 *         Query objects, retrieval of data in the form of DataObjectArrays and
 *         insertion of data in the form of DataObjects.
 * 
 *         The configuration details for the MySQL database used by any given
 *         application are specified in the application's config/database.xml
 *         file. NOTE This is currently not the case, check notes in sample_database.xml
 * 
 */
public class MySqlDataSource implements DataSource {

	Connection dbConnection;
	PreparedStatement statement;
	ResultSet resultSet;
	DataObjectArray resultsArray;

	public MySqlDataSource() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.gitm.javango.data.DataSource#create(co.gitm.javango.data.DataObject)
	 */
	@Override
	public void create(DataObject newData) throws CreateException {
		// Connect to the database.
		this.connect();
		try {
			// Create an INSERT statement using the input data (newData).
			statement = dbConnection.prepareStatement(this
					.dataObjectToInsertStatement(newData));
			System.out.println(statement.toString());
			// Attempt to execute insertion.
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			// Throw an exception if insertion fails, include MySQL error
			// message.
			throw new CreateException(e.getMessage());
		} finally {
			// Ensure database connection is closed, even if exception was
			// thrown.
			this.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see co.gitm.javango.data.DataSource#retrieve(co.gitm.javango.data.Query)
	 */
	@Override
	public DataObjectArray retrieve(Query query) {
		this.connect();
		try {
			// Create a SELECT statement using the input query.
			statement = dbConnection.prepareStatement(this
					.queryToSelectStatement(query));
			System.out.println(statement.toString());
			// resultSet holds the result of executing the query on the
			// database.
			resultSet = statement.executeQuery();
			// Convert resultSet to a DataObjectArray (the type used by
			// Javango).
			resultsArray = ResultSetToDataObjectArray.convert(resultSet);
		} catch (SQLException e) {
			// Ensure that if a resultsArray is returned, it has nothing in it
			// (and will thus throw an exception when accessed).
			// TODO Throw a RetrieveException and pass MySQL error message to
			// caller.
			resultsArray = null;
			e.printStackTrace();
		} finally {
			// Ensure database connection is closed.
			this.close();
		}
		return resultsArray;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	/**
	 * Connects to a MySQL database using the configuration details specified in
	 * database.xml. database.xml just has to be a file on the current CLASSPATH
	 * (it can really be located anyway, though it should be in the
	 * application's config folder). To ensure database.xml is on CLASSPATH in
	 * Eclipse, right-click on it's parent folder and go to
	 * "Build path">"Use as source folder". Otherwise, you'll probably get
	 * database connection errors.
	 */
	private void connect() {
		Configuration dbConfig = Config.getConfig("database.xml");
		// Use the JDBC MysqlDataSource class to connect to the database
		// specified in database.xml.
		// System.getProperty is used by Elastic Beanstalk environment to connect to db. 
		String dbName = System.getProperty("RDS_DB_NAME"); //dbConfig.getString("database.name");
		String userName = System.getProperty("RDS_USERNAME"); //dbConfig.getString("database.user");
		String password = System.getProperty("RDS_PASSWORD"); //dbConfig.getString("database.password");
		String serverName = System.getProperty("RDS_HOSTNAME"); //dbConfig.getString("database.servername");
		String port = System.getProperty("RDS_PORT"); //dbConfig.getString("database.port");
		MysqlDataSource ds = new MysqlDataSource();
		ds.setUser(userName);
		ds.setPassword(password);
		ds.setServerName(serverName);
		ds.setPort(Integer.parseInt(port));
		ds.setDatabaseName(dbName);
		// Attempt to get a database connection, throw exception if this fails.
		try {
			dbConnection = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Close connection to the database.
	 */
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		} catch (Exception e) {
			// TODO Handle exception?
		}
	}

	/**
	 * @param query
	 *            is query for the database.
	 * @return a valid SQL SELECT statement.
	 */
	private String queryToSelectStatement(Query query) {
		// Generate first part of SELECT statement (not including WHERE and
		// ORDER clauses).
		String select = String
				.format("SELECT * FROM %s", query.getObjectName());
		String where = "";
		// Build WHERE clause if query object has an associated filter.
		if (query.getFilter() != null) {
			where = String.format("WHERE %s", mapToString(query.getFilter()));
		}
		// Build ORDER clause if query object has an associated order.
		// TODO Finish ORDER clause support...it probably won't work currently.
		String order = "";
		if (query.getOrder() != null) {
			order = String.format("ORDER %s", query.getOrder());
		}
		// Return the full SELECT statement.
		return String.format("%s %s %s", select, where, order);
	}

	/**
	 * @param data
	 *            object from which to build INSERT statement
	 * @return a valid SQL INSERT statement.
	 */
	private String dataObjectToInsertStatement(DataObject data) {
		// Build statement from dataObject keys and their associated values.
		String statement = String.format("INSERT INTO %s (%s) VALUES (%s)",
				data.getObjectName(), this.collectionToString(data.keySet())
						.replace("\"", ""), this.collectionToString(data
						.values()));
		return statement;
	}

	/**
	 * @param map
	 *            object to convert into a string
	 * @return a string representation of the map object.
	 * 
	 *         This conversion is very specific to the format required by MySQL,
	 *         which is why it's done here as opposed to being performed using
	 *         an override on the Map toString method.
	 */
	private String mapToString(Map map) {
		String mapStr = "";
		// Get iterator.
		Iterator<Map.Entry> itr = map.entrySet().iterator();
		// Use iterator to iterate over map entries and build a string
		// representation of it.
		while (itr.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) itr
					.next();
			String keyAndValue = String.format("%s=\"%s\" ", entry.getKey(),
					entry.getValue());
			if (itr.hasNext()) {
				keyAndValue = keyAndValue + "AND ";
			}
			mapStr = mapStr + keyAndValue;
		}
		return mapStr;
	}

	/**
	 * @param collection
	 *            to be converted into a string.
	 * @return a string representation of the collection.
	 * 
	 *         Again, the returned string is in a SQL friendly format.
	 */
	private String collectionToString(Collection collection) {
		String collectionStr = "";
		// Get iterator.
		Iterator itr = collection.iterator();
		collectionStr = String.format("\"%s\"", itr.next());
		// Use iterator to iterate over collection and build a string
		// representation of it.
		while (itr.hasNext()) {
			collectionStr = String.format("%s, \"%s\"", collectionStr,
					itr.next());
		}
		return collectionStr;
	}
}
