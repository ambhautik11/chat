package com.pratilipi.MainServer.repository;

import com.pratilipi.MainServer.model.User;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

//@EnableScan
public interface UserInfoRepository1234 extends
        CrudRepository<User, String> {
//
//    List<User> findById(String id);
}
