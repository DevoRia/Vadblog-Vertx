package com.vadim.vadblog.dao;


import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Repository extends DataBaseBehavior{

    private List list;
    private MongoCollection<Document> collection;

    public Repository(String nameDatabase, String nameCollection) {
        super(nameDatabase);
        this.list = getListOfDocuments(getElementsByCollection(nameCollection));
    }

    @Override
    protected FindIterable<Document> getElementsByCollection(String nameOfCollection) {
        collection = getDatabase().getCollection(nameOfCollection);
        return collection.find();
    }

    @Override
    protected List getListOfDocuments(FindIterable<Document> documents) {
        List<String> list = new ArrayList<String>();
        for (Document document : documents) {
            Date date = document.getDate("date");
            long time = date.getTime();
            for (Map.Entry<String, Object> entry : document.entrySet()) {
                if (entry.getKey().equals("date"))  entry.setValue(time);
            }
            String json = document.toJson();
            list.add(json);
        }
        return list;
    }

    @Override
    protected void savePost() {
    }

    @Override
    protected void removePost() {

    }

    @Override
    protected void editPost() {

    }

    public List getList() {
        return list;
    }
}
