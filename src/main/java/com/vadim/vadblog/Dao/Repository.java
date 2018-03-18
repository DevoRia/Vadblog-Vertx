package com.vadim.vadblog.Dao;


import com.google.common.collect.Lists;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

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
