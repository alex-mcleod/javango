package co.gitm.javango.data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

/**
 * @author Alex McLeod
 * 
 *         DataObjects are used to hold and pass around data held in
 *         DataSources. They extend the JSONObject class, so they can easily be
 *         serialised as a JSON.
 * 
 */
public class DataObject extends JSONObject {
	/**
	 * Each DataObject has a name, which corresponds to the name of the Model it
	 * belongs to (or is null if it does not yet belong to a model). TODO Why
	 * not have a Model field instead?
	 */
	private String objectName = null;

	public DataObject() {
	}

	public DataObject(String objectName) {
		this.setObjectName(objectName);
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectName() {
		return this.objectName;
	}

	/**
	 * @param jsonStr
	 *            from which to construct new DataObject.
	 * @return new DataObject constructed from jsonStr
	 * @throws ParseException
	 *             if jsonStr is not a valid JSON string.
	 */
	public static DataObject instanceFromJson(String jsonStr)
			throws ParseException {
		JSONParser p = new JSONParser();
		JSONObject jsonData;
		DataObject dObj = new DataObject();
		jsonData = (JSONObject) p.parse(jsonStr);
		dObj.putAll(jsonData);
		return dObj;
	}
}
