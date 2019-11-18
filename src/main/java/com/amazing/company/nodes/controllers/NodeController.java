package com.amazing.company.nodes.controllers;

import com.amazing.company.nodes.entities.Node;
import com.amazing.company.nodes.exceptions.BadRequestException;
import com.amazing.company.nodes.exceptions.NodeNotFoundException;
import com.amazing.company.nodes.repositories.NodeRepository;
import com.amazing.company.nodes.resources.UpdateNodeResource;
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

  @PatchMapping("/nodes/{id}")
  public Node updateNode(@PathVariable Long id, @RequestBody UpdateNodeResource resource) {
    Node node = getNode(id);

    if (node.getParent() == null || id == resource.parent) {
      throw new BadRequestException();
    }

    Node newParent = getNode(resource.parent);
    node.setParent(newParent);
    node.setRoot(newParent.getRoot());
    node.setHeight(newParent.getHeight() + 1);
    return NodeRepository.save(node);
  }

  @GetMapping("/nodes/{id}/parent")
  public Node findParentNode(@PathVariable Long id) {
    Node node = getNode(id);
    return getNode(node.getParentId());
  }

  @GetMapping("/nodes/{id}/root")
  public Node findRootNode(@PathVariable Long id) {
    Node node = getNode(id);
    return getNode(node.getRootId());
  }

  @GetMapping("/nodes/{id}/children")
  public Page<Node> findChildren(@PathVariable Long id, Pageable page) {
    return NodeRepository.findChildren(id, page);
  }

  @GetMapping("/nodes/{id}/descendants")
  public Page<Node> findDescendants(@PathVariable Long id, Pageable page) {
    return NodeRepository.findDescendantNodes(id, page);
  }

  @PostMapping("/nodes/{id}/children")
  public Node createChildNode(@PathVariable Long id) {
    return createNode(id);
  }

  private Node createNode(Long id) {
    Node parent = getNode(id);
    Node node = new Node();
    node.setParent(parent);
    node.setRoot(parent.getRoot());
    node.setHeight(parent.getHeight() + 1);
    return NodeRepository.save(node);
  }

  private Node getNode(Long id) {

    if (id == null) {
      throw new NodeNotFoundException(id);
    }

    Optional<Node> result = NodeRepository.findById(id);
    if (result.isEmpty()) {
      throw new NodeNotFoundException(id);
    }
    return result.get();
  }
}
