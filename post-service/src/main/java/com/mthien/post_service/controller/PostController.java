package com.mthien.post_service.controller;

import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mthien.post_service.payload.ApiResponse;
import com.mthien.post_service.payload.request.PostRequest;
import com.mthien.post_service.payload.response.PostResponse;
import com.mthien.post_service.service.PostService;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping("/")
    ApiResponse<PostResponse> createPost(@RequestBody PostRequest request) {
        return ApiResponse.<PostResponse>builder()
                .message("Create post successfully")
                .data(postService.createPost(request))
                .build();
    }

    @GetMapping("/my-post")
    ApiResponse<List<PostResponse>> getMyPost() {
        return ApiResponse.<List<PostResponse>>builder()
                .message("Get my post successfully")
                .data(postService.getMyPost())
                .build();
    }
}
