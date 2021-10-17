package com.springbank.user.query.api.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.springbank.user.core.models.User;

public interface UserRepository extends MongoRepository<User, String> {

}
