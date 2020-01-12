package com.pratilipi.MainServer;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainServerApplication.class, args);

//        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
//                .withRegion(Regions.AP_SOUTH_1)
//                .build();
	}

}
