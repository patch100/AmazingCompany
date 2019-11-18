package com.amazing.company.nodes.controllers;

import com.amazing.company.nodes.entities.Node;
import com.amazing.company.nodes.exceptions.NodeNotFoundException;
import com.amazing.company.nodes.repositories.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class NodeController {
    private final NodeRepository NodeRepository;

    @Autowired
    public NodeController(NodeRepository NodeRepository) {
        this.NodeRepository = NodeRepository;
    }

    @GetMapping("/nodes")
    public Page<Node> findAllNodes(Pageable page) {
        return NodeRepository.findAll(page);
    }

    @GetMapping("/nodes/{id}")
    public Node findNode(@PathVariable Long id) {
        return getNode(id);
    }

    @PostMapping("/nodes/{id}/child")
    public Node create(@PathVariable Long id) {
        Node parent = getNode(id);
        Node node = new Node();
        node.setParent(parent);
        node.setRoot(parent.getRoot());
        node.setHeight(parent.getHeight() + 1);
        return NodeRepository.save(node);
    }

    private Node getNode(Long id) {
        Optional<Node> result = NodeRepository.findById(id);
        if(result.isEmpty()) {
            throw new NodeNotFoundException(id);
        }
        return result.get();
    }
}

