package com.mthien.post_service.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.mthien.post_service.entity.Post;

public interface PostRepository extends MongoRepository<Post, Object> {
    Page<Post> findAllByUserId(String userId, Pageable pageable);
}
