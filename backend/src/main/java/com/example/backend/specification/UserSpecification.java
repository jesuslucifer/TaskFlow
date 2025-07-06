package com.example.backend.specification;

import com.example.backend.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> byUsernameLike(String username) {
        return (root, query, cb) ->
                username == null ? null :
                        cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }
}
