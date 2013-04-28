package co.gitm.javango.view;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Parameter;
import org.restlet.resource.ServerResource;

import co.gitm.javango.data.DataObject;

/**
 * @author Alex McLeod
 * 
 *         Views are responsible for presenting data from models to the user.
 *         They receive requests (GET, POST, PUT or DELETE), access models and
 *         return a response.
 * 
 *         This class basically just inherits all its functionality from the
 *         Restlet ServerResource class.
 * 
 */
public class View extends ServerResource {
	/**
	 * The parent class has a getQuery method which returns the key-value pairs
	 * in the query string of the current request. However, it returns these
	 * using the restlet Form class (which I'm trying to avoid using in the
	 * interest of reducing Javango's Restlet dependency). So this function
	 * takes that Form and converts it into a standard HashMap, then returns
	 * that to the user.
	 * 
	 * @return a HashMap containing the key-value pairs from the current
	 *         request's query string.
	 */
	protected Map getQueryAsMap() {
		Map newMap = new HashMap();
		for (Parameter parameter : this.getQuery()) {
			newMap.put(parameter.getName(), parameter.getValue());
		}
		return newMap;
	}
}
