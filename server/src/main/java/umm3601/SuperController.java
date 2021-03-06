package umm3601;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.Iterator;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

public abstract class SuperController {
    protected Gson gson;
    protected MongoDatabase database;
    protected MongoCollection<Document> collection;

    public SuperController(){
        gson = null;
        database = null;
        collection = null;
    }


    public String getItem(String id) {

        FindIterable<Document>  jsonObjects
            = collection
            .find(eq("_id", new ObjectId(id)));

        Iterator<Document> iterator = jsonObjects.iterator();
        if (iterator.hasNext()) {
            Document item = iterator.next();
            return item.toJson();
        } else {
            // We didn't find the desired item
            return null;
        }
    }

    public String getItems(Map<String, String[]> queryParams) {
        Document filterDoc = new Document();

        if (queryParams.containsKey("email")) {
            String targetEmail = (queryParams.get("email")[0]);
            filterDoc = filterDoc.append("email", targetEmail);
        }

        if (queryParams.containsKey("userId")) {
            String targetUserId = (queryParams.get("userId")[0]);
            filterDoc = filterDoc.append("userId", targetUserId);
        }
        else {
            System.out.println("It had no userID");
            String[] arr = {};
            return JSON.serialize(arr);
        }

        if (queryParams.containsKey("subject")) {
            String targetContent = (queryParams.get("subject")[0]);
            Document contentRegQuery = new Document();
            contentRegQuery.append("$regex", targetContent);
            contentRegQuery.append("$options", "i");
            filterDoc = filterDoc.append("subject", contentRegQuery);        }

        if (queryParams.containsKey("body")) {
            String targetContent = (queryParams.get("body")[0]);
            Document contentRegQuery = new Document();
            contentRegQuery.append("$regex", targetContent);
            contentRegQuery.append("$options", "i");
            filterDoc = filterDoc.append("body", contentRegQuery);
        }

//        if (queryParams.containsKey("date")) {
//            String targetContent = (queryParams.get("date")[0]);
//            String[] arg1 = targetContent.split(",");
//            String targetStartContent = arg1[0];
//            String targetEndContent = arg1[1];
//            Document contentRegQuery = new Document();
//            contentRegQuery.append("$gte", targetStartContent);
//            System.out.println(JSON.serialize(contentRegQuery));
//            contentRegQuery.append("$lte", targetEndContent);
//            System.out.println(JSON.serialize(contentRegQuery));
//            filterDoc = filterDoc.append("date", contentRegQuery);
//            System.out.println("here");
//            System.out.println(JSON.serialize(filterDoc));
//        }

       // db.emotions.find({ "date" : {$lte: "Wed Mar 3 2018 12:02:21 GMT-0500",$gte: "Wed Mar 3 2018 12:02:21 GMT-0500"}})

        FindIterable<Document> matchingItems = collection.find(filterDoc);
System.out.println(JSON.serialize(matchingItems));
        return JSON.serialize(matchingItems);
    }
}
