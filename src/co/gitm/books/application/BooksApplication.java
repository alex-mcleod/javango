package co.gitm.books.application;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import co.gitm.books.view.BooksView;

/**
 * @author Alex McLeod
 * 
 *         The Application class is responsible for receiving and routing all
 *         incoming requests from Tomcat. Tomcat knows to send all incoming
 *         requests to this class because it is specified as the main servlet in
 *         web.xml.
 */
public class BooksApplication extends Application {

	/**
	 * Handle all incoming requests. 
	 * @return a basic router which sends /books URL to the BooksView class.
	 * 
	 * @see org.restlet.Application#createInboundRoot()
	 */
	@Override
	public synchronized Restlet createInboundRoot() {
		System.out.println("Called Application ");
		Router router = new Router();
		router.attach("/books", BooksView.class);
		return router;
	}
}
