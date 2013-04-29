package co.gitm.books.view;

import java.io.IOException;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.data.MediaType;
import org.restlet.data.Status;

import co.gitm.books.model.BookModel;
import co.gitm.javango.data.DataObject;
import co.gitm.javango.data.DataObjectArray;
import co.gitm.javango.data.exceptions.CreateException;
import co.gitm.javango.view.View;

import org.json.simple.parser.ParseException;

/**
 * @author Alex McLeod
 * 
 *         The BooksView exposes the simple Books API to the user.
 * 
 *         This view handles GET requests via the getBooks method. This methods
 *         returns all BookModel objects in the database as JSON, or a subset of
 *         these if filters are specified by the user in the GET request query
 *         string.
 * 
 *         Incoming POST requests are also handled by this view via the
 *         createBook method. The 'Content-Type' of the request must be
 *         'application/json' or it will simply be ignored. If the JSON is
 *         malformed, an error message will be sent back to the user explaining
 *         this. If the JSON is correctly formatted, then this view will attempt
 *         to create a new BookModel object and insert it into the database. If
 *         MySQL returns an error, then this error is passed back to the user.
 *         Otherwise, the server status is set to SUCCESS_CREATED (201) and a
 *         success method is returned to the user.
 * 
 */
public class BooksView extends View {
	/**
	 * Handles incoming GET requests.
	 * 
	 * @return either a JSON to the user or an error message if the GET request
	 *         query string contained fields which do not exist in the
	 *         BookModel.
	 */
	@Get("json")
	public Representation getBooks() {
		// Load the BookModel.
		BookModel books = new BookModel();
		// Load a DataObjectArray to hold any DataObjects which
		// may be returned by the BookModel.
		DataObjectArray doa = new DataObjectArray();
		// Try to get books from the database using
		// the get request query string as a filter. Throw an exception if the
		// query string contains
		// fields which do not exist in the BookModel (e.g. if
		// the query string contains an "authr=Alex" entry then an
		// exception will be thrown because there is no authr field
		// in the BookModel, just an 'authors' field).
		try {
			doa = books.getWithFilter(this.getQueryAsMap());
		} catch (NoSuchFieldException e) {
			// Return an exception to the user, indicating which fields
			// in their query string were invalid.
			throw new ResourceException(
					Status.CLIENT_ERROR_NOT_FOUND,
					String.format(
							"One of your GET parameters was invalid. The '%s' model does not have %s field(s).",
							books.getName(), e.getMessage()));
		}
		// Convert DataArrayObject returned by BookModel to a string
		// in the JSON format. DataArrayObject inherits from the JSONArray
		// class,
		// so this is as easy as calling its toString method.
		String jsonString = doa.toString();
		// Return the results of querying the BookModel to the user.
		// TODO Change MediaType to APPLICATION_JSON when in production.
		return new StringRepresentation(jsonString, MediaType.TEXT_PLAIN);
	}

	/**
	 * Handles incoming POST requests where the 'Content-Type' is
	 * 'application/json'.
	 * 
	 * @param postData
	 *            will hold the POST data sent by the user.
	 * @return a success or failure message to the user depending on whether
	 *         their POST request was successfully.
	 */
	@Post("json")
	public Representation createBook(Representation postData) {
		// Load the BookModel.
		BookModel books = new BookModel();
		// Instantiated a DataObject which will hold the postData and
		// which we will attempt to insert into the database as a new
		// BookModel object.
		DataObject newBook;
		// Try to initialize the DataObject using postData. If the postData
		// is not a properly formatted JSON (ParseException), or if the
		// postData.getText method
		// returns an IOException, then return an error message to the user.
		// NOTE: Had to use multiple catch statements so that application is
		// compatible with JDK 1.6. 
		try {
			newBook = DataObject.instanceFromJson(postData.getText());
		} catch (ParseException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE,
					"Your POST request is malformed. Ensure your JSON is correctly formated.");
		} catch (IOException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE,
					"Your POST request is malformed. Ensure your JSON is correctly formated.");
		}
	
		// Try and create a new BookModel object in the database using the
		// newBook DataObject.
		// If newBook is not a valid BookModel object, then return error message
		// to the user.
		// TODO Validation of BookModel objects is currently the responsibility
		// of MySQL, this will
		// be changed so that validation is performed by the BookModel object
		// itself.
		try {
			books.createNew(newBook);
		} catch (CreateException e) {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE,
					String.format("MySQL returned an error: \"%s\".",
							e.getMessage()));
		}
		// Set status of response to SUCCESS_CREATED (i.e. 201).
		this.setStatus(Status.SUCCESS_CREATED);
		// Return success message to user.
		return new StringRepresentation("Item created.", MediaType.TEXT_PLAIN);
	}
}
