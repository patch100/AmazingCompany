package com.amazing.company.nodes.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "nodes")
public class Node {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JoinColumn(name = "root_id")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonIgnore
    private Node root;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Node parent;

    @OneToMany
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Set<Node> children;

    private int height;

    @JsonProperty("children")
    public Set<Long> getChildrenIds() {
        return children == null ? null : children.stream().map(Node::getId).collect(Collectors.toSet());
    }

    @JsonProperty("root")
    public Long getRootId() {
        return getRoot() == null ? null : getRoot().getId();
    }

    @JsonProperty("parent")
    public Long getParentId() {
        return getParent() == null ? null : getParent().getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Set<Node> getChildren() {
        return children;
    }

    public void setChildren(Set<Node> children) {
        this.children = children;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
