package com.amazing.company.nodes.exceptions;

public class NodeNotFoundException extends RuntimeException {

    private final Long id;

    public NodeNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
