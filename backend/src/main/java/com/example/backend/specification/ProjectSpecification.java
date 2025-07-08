package com.example.backend.specification;

import com.example.backend.model.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProjectSpecification {

    public static Specification<Project> statusEquals(Status status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("status"), status);
    }

    public static Specification<Project> nameLike(String name) {
        return (root, query, cb) ->
                name == null ? null :
                        cb.like(root.get("name"), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Project> priorityEquals(Priority priority) {
        return (root, query, cb) ->
                priority == null ? null :
                        cb.equal(root.get("priority"), priority);
    }

    public static Specification<Project> isCreator(Long userId) {
        return (root, query, cb) ->
                cb.equal(root.get("createUser").get("id"), userId);
    }

    public static Specification<Project> isExecutor(Long userId) {
        return (root, query, cb) -> {
            Join<Project, ProjectExecutor> join = root.join("executors");
            Predicate user = cb.equal(join.get("user").get("id"), userId);
            Predicate role = cb.notEqual(join.get("role"), ExecutorRole.ADMINISTRATOR);
            return cb.and(user, role);
        };
    }
}
