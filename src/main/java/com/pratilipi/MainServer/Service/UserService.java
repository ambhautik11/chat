package com.pratilipi.MainServer.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.google.gson.Gson;
import com.pratilipi.MainServer.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@org.springframework.stereotype.Service
public class UserService {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;
    private String userTable = "user";
    private Gson gson = new Gson();
    public User signUp(User user) {

        if (getUser(user.getUser_id())!=null) {
            throw new IllegalArgumentException("User name is already taken. Please try again with different user name");
        }
        save(user);
        return user;
    }

    public boolean logIn(User user){
        User userFromDb = getUser(user.getUser_id());
        if(userFromDb==null)
            return false;
        return userFromDb.getPassword().equals(user.getPassword());
    }

    public List<String> getAllUserIds(){
        List<String> users = new ArrayList<>();
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        Table table = dynamoDB.getTable(userTable);

        ScanSpec scanSpec = new ScanSpec();
        try {
            ItemCollection<ScanOutcome> items = table.scan(scanSpec);

            Iterator<Item> iter = items.iterator();
            while (iter.hasNext()) {
                Item item = iter.next();
                System.out.println(item.toJSONPretty());
                User user = gson.fromJson(item.toJSON(),User.class);
                users.add(user.getUser_id());
            }

        }
        catch (Exception e) {
            System.err.println("Unable to scan the table:");
            System.err.println(e.getMessage());
        }
        return users;
    }

    public User getUser(String userId) {
        User user = null;
        HashMap<String, AttributeValue> key_to_get = new HashMap<String, AttributeValue>();
        key_to_get.put("user_id", new AttributeValue(userId));

        GetItemRequest request = new GetItemRequest().withKey(key_to_get).withTableName(userTable);
        try {
            Map<String, AttributeValue> returned_item =
                    amazonDynamoDB.getItem(request).getItem();
            if (returned_item != null) {
                user = new User();
                user.setUser_id(returned_item.get("user_id").getS());
                user.setPassword(returned_item.get("password").getS());
            }
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);

        }
        return user;
    }

    public User save(User user){
        HashMap<String, AttributeValue> item_values =
                new HashMap<String, AttributeValue>();
        item_values.put("user_id", new AttributeValue(user.getUser_id()));
        item_values.put("password", new AttributeValue(user.getPassword()));
        amazonDynamoDB.putItem(userTable, item_values);
        return user;
    }
}