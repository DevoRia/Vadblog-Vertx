package com.vadim.vadblog.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

abstract class DataBaseBehavior {

    private final String HOST = "localhost";
    private final int PORT = 27017;
    private MongoClient client;
    private MongoDatabase database;

    DataBaseBehavior(String nameDatabase) {
        this.client = new MongoClient(HOST, PORT);
        database = this.getDatabaseByName(nameDatabase);
    }

    private MongoDatabase getDatabaseByName (String name){
        return this.client.getDatabase(name);
    }

    protected MongoDatabase getDatabase() {
        return database;
    }

    public void closeSession () {
        client.close();
    }

    protected abstract FindIterable<Document> getElementsByCollection (String nameOfCollection);
    protected abstract List getListOfDocuments(FindIterable<Document> documents);
    protected abstract void savePost();
    protected abstract void removePost();
    protected abstract void editPost();

}
