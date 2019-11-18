package com.amazing.company.nodes.repositories;

import com.amazing.company.nodes.entities.Node;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface NodeRepository extends PagingAndSortingRepository<Node, Long>, QuerydslPredicateExecutor<Node>, CrudRepository<Node, Long> {
    Page<Node> findAll(Pageable pageable);
}
