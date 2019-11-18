package com.amazing.company.nodes.controllers;

import com.amazing.company.nodes.AmazingCompanyApplication;
import com.amazing.company.nodes.entities.Node;
import com.amazing.company.nodes.repositories.NodeRepository;
import com.amazing.company.nodes.resources.UpdateNodeResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = AmazingCompanyApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class NodeControllerIntegrationTest {
  @Autowired private NodeController nodeController;

  @Autowired private MockMvc mockMvc;

  @Autowired private NodeRepository nodeRepository;

  private static final int NUMBER_OF_CHILDREN = 5;

  @Test
  public void whenNodeControllerInjected_thenNotNull() throws Exception {
    assertThat(nodeController).isNotNull();
  }

  @Test
  public void findNode_withIdPathVariable_thenCorrectResponse() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/nodes/{id}", "1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.root").value("1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.parent").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.height").value("0"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.children").isArray());
  }

  @Test
  public void createChildNode_withIdPathVariable_thenCorrectResponse() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/nodes/{id}/children", "1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.root").value("1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.parent").value("1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.height").value("1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.children").isEmpty());
  }

  @Test
  public void updateNode_withNewParent_thenNodeHasCorrectResponse() throws Exception {
    Optional<Node> root = nodeRepository.findById(1L);
    Node parentNode = createNode(root.get());
    Node childNode = createNode(parentNode);
    Node newParentNode = createNode(root.get());
    String json = createUpdateNodeResource(newParentNode);

    mockMvc
        .perform(
            MockMvcRequestBuilders.patch("/nodes/{id}", String.valueOf(childNode.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(childNode.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.root").value("1"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.parent").value(String.valueOf(newParentNode.getId())))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.height")
                .value(String.valueOf(newParentNode.getHeight() + 1)));
  }

  @Test
  public void findDescendants_withRootNodeWithChildren_thenNodeHasCorrectResponse()
      throws Exception {
    createDescendantNodes();

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/nodes/{id}/descendants", "1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].children").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].children", hasSize(NUMBER_OF_CHILDREN)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(NUMBER_OF_CHILDREN * 2 + 1));
  }

  private String createUpdateNodeResource(Node newParentNode) throws JsonProcessingException {
    UpdateNodeResource childNodeUpdate = new UpdateNodeResource();
    childNodeUpdate.parent = newParentNode.getId();
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(childNodeUpdate);
  }

  private void createDescendantNodes() {
    Optional<Node> root = nodeRepository.findById(1L);

    for (int i = 0; i < NUMBER_OF_CHILDREN; i++) {
      Node parent = createNode(root.get());
      createNode(parent);
    }
  }

  private Node createNode(Node parent) {
    Node node = new Node();
    node.setParent(parent);
    node.setRoot(parent.getRoot());
    node.setHeight(parent.getHeight() + 1);
    return nodeRepository.save(node);
  }
}
