package com.pratilipi.MainServer.Service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.gson.Gson;
import com.pratilipi.MainServer.model.Message;
import com.pratilipi.MainServer.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@org.springframework.stereotype.Service
public class MessageService {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;
    private String messageTable = "message";
    private Gson gson = new Gson();

    public List<Message> getAllMessages(User user) {
        List<Message> messages = getMessagesFrom(user.getUser_id());
        messages.addAll(getMessagesTo(user.getUser_id()));
        Collections.sort(messages, (a, b) -> a.getTimestamp().isBefore(b.getTimestamp()) ? -11 : 1);
        return messages;
    }

    public List<Message> getMessagesFrom(String fromUserId) {
        List<Message> messages = new ArrayList<>();
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        Table table = dynamoDB.getTable(messageTable);
//        QuerySpec spec = new QuerySpec()
//                .withKeyConditionExpression("Id = :v_id and ReplyDateTime > :v_reply_dt_tm")
//                .withFilterExpression("from_user = :from")
//                .withValueMap(new ValueMap().withString(":from", fromUserId))
//                .withConsistentRead(true);
//
//        ItemCollection<QueryOutcome> items = table.query(spec);
//
//        Iterator<Item> iterator = items.iterator();
//        while (iterator.hasNext()) {
//            System.out.println(iterator.next().toJSONPretty());
//        }

        ScanSpec scanSpec = new ScanSpec()
//                .withProjectionExpression("#yr, title, info.rating")
                .withFilterExpression("from_user = :from")
                .withValueMap(new ValueMap().withString(":from", fromUserId));

        ItemCollection<ScanOutcome> items = table.scan(scanSpec);

        Iterator<Item> iter = items.iterator();
        while (iter.hasNext()) {
            Item item = iter.next();
            System.out.println(item.toString());
            Message message = gson.fromJson(item.toJSON(), Message.class);
            if (item.get("timestamp") != null)
                message.setTimestamp(LocalDateTime.parse((String) item.get("timestamp")));
            messages.add(message);
        }

        return messages;
    }

    public List<Message> getMessagesTo(String toUserId) {
        List<Message> messages = new ArrayList<>();
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        Table table = dynamoDB.getTable(messageTable);
        ScanSpec scanSpec = new ScanSpec()
//                .withProjectionExpression("#yr, title, info.rating")
                .withFilterExpression("to_user = :to")
                .withValueMap(new ValueMap().withString(":to", toUserId));

        ItemCollection<ScanOutcome> items = table.scan(scanSpec);

        Iterator<Item> iter = items.iterator();
        while (iter.hasNext()) {
            Item item = iter.next();
            System.out.println(item.toString());
            Message message = gson.fromJson(item.toJSON(), Message.class);
            if (item.get("timestamp") != null)
                message.setTimestamp(LocalDateTime.parse((String) item.get("timestamp")));
            messages.add(message);
        }

        return messages;
    }

    public Message save(Message message) {
        HashMap<String, AttributeValue> item_values =
                new HashMap<String, AttributeValue>();
        int random = (int) (Math.random()*1000);
        item_values.put("id", new AttributeValue(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) +"_" +random));
        item_values.put("to_user", new AttributeValue(message.getTo_user()));
        item_values.put("from_user", new AttributeValue(message.getFrom_user()));
        item_values.put("timestamp", new AttributeValue(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
        item_values.put("text", new AttributeValue(message.getText()));
        amazonDynamoDB.putItem(messageTable, item_values);
        return message;
    }
}