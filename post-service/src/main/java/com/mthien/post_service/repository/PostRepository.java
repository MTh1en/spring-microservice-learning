package com.mthien.post_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mthien.post_service.entity.Post;

public interface PostRepository extends MongoRepository<Post, Object> {
    List<Post> findAllByUserId(String userId);
}
