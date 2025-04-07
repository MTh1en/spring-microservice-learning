package com.mthien.post_service.mapper;

import org.mapstruct.Mapper;

import com.mthien.post_service.entity.Post;
import com.mthien.post_service.payload.response.PostResponse;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(Post post);
}
