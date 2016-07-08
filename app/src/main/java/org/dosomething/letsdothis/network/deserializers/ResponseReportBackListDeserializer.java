package org.dosomething.letsdothis.network.deserializers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;


/**
 * Filters incompatible kudos out of the report back response.
 * @author Eric Ruck
 */
public class ResponseReportBackListDeserializer<ResponseReportBackList>
		implements JsonDeserializer<ResponseReportBackList> {
	@Override
	public ResponseReportBackList deserialize(JsonElement json, Type type,
											  JsonDeserializationContext context)
			throws JsonParseException {
		// Check if we need to filter this object
		// TODO: Kudos need to be updated so they work in the future, for now we're removing them
		JsonObject jsonObject = json.getAsJsonObject();
		JsonElement dataElt = jsonObject.get("data");
		if ((dataElt != null) && dataElt.isJsonArray()) {
			// Cycle through the array
			for (JsonElement current : dataElt.getAsJsonArray()) {
				// Check for kudos
				JsonElement kudosElt = current.getAsJsonObject().get("kudos");
				if ((kudosElt == null) || !kudosElt.isJsonObject()) {
					continue;
				}
				JsonObject kudosObj = kudosElt.getAsJsonObject();
				JsonElement kudosDataElt = kudosObj.get("data");
				if ((kudosDataElt != null) && (kudosDataElt.isJsonObject())) {
					// Here's the problem, the current version of Android wants an array
					kudosObj.remove("data");
					kudosObj.add("data", new JsonArray());
				}
			}
		}

		// Deserialize the adjusted JSON
		return new Gson().fromJson(jsonObject, type);
	}
}
