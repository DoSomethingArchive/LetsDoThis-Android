package org.dosomething.letsdothis.network.deserializers;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by juy on 9/9/15.
 *
 * Custom deserializer for responses of type ResponseCampaign.
 */
public class ResponseCampaignDeserializer<ResponseCampaign> implements JsonDeserializer<ResponseCampaign> {
    @Override
    public ResponseCampaign deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // solutions.support_copy has been seen to sometimes be an object and sometimes a string.
        // The expectation is for it to be an object.
        // See ResponseCampaign.ResponseSolution.ResponseSolutionObject
        JsonObject solutions = jsonObject.getAsJsonObject("solutions");
        JsonElement supportCopy = solutions.getAsJsonObject().get("support_copy");
        if (supportCopy.isJsonPrimitive()) {
            String str = supportCopy.getAsString();
            JsonObject obj = new JsonObject();
            obj.addProperty("formatted", str);
            obj.addProperty("raw", str);

            solutions.remove("support_copy");
            solutions.add("support_copy", obj);
        }

        return new Gson().fromJson(jsonObject, type);
    }
}
