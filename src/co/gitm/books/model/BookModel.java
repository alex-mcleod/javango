package co.gitm.books.model;

import java.util.ArrayList;
import java.util.List;

import co.gitm.javango.data.DataSource;
import co.gitm.javango.data.mysql.MySqlDataSource;
import co.gitm.javango.model.Model;

/**
 * @author Alex McLeod
 * 
 *         The BookModel (like all models) has three roles: 1) To describe all
 *         the fields which each BookModel object (i.e. Book) will have. 2) To
 *         maintain and interface with a DataSource object (i.e. a database)
 *         which will contain all BookModel objects. 3) To hold the name of the
 *         DataSource collection which contains all BookModel objects (i.e. to
 *         hold the name of the Book object table in the database).
 * 
 */
public class BookModel extends Model {
	// This MySqlDataSource instance will hold all BookModel objects. Details for accessing
	// the database are specified in the co/gitm/books/config/database.xml file. NOTE This 
	// is currently not the case, check notes in sample_database.xml. 
	DataSource db = new MySqlDataSource();

	/**
	 * @return a DataSource object which holds all BookModel objects.
	 * This is use by functions in the parent class (i.e. Model) to access
	 * BookModel objects. 
	 * @see co.gitm.javango.model.Model#getDataSource()
	 */
	@Override
	protected DataSource getDataSource() {
		return db;
	}

	/**
	 * @return the name of the table in the DataSource (i.e. MySql database) which holds
	 * BookModel objects. Used by parent class.  
	 * @see co.gitm.javango.model.Model#getName()
	 */
	public String getName() {
		return "books_books";
	}

	/**
	 * @return a list of BookModel object fields: I.e. return
	 * a list of the column names in the "books_books" table in the MySQL database 
	 * asociated with this application.  
	 * This method is mostly used by the parent class. 
	 * @see co.gitm.javango.model.Model#getFields()
	 */
	@Override
	public List<String> getFields() {
		List<String> fields = new ArrayList<String>();
		fields.add("id");
		fields.add("isbn");
		fields.add("title");
		fields.add("authors");
		fields.add("image");
		fields.add("rrp");
		fields.add("description");
		fields.add("edition");
		fields.add("format");
		return fields;
	}
}
