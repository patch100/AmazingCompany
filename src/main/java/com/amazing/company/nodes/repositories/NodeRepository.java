package com.amazing.company.nodes.repositories;

import com.amazing.company.nodes.entities.Node;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface NodeRepository
    extends PagingAndSortingRepository<Node, Long>,
        QuerydslPredicateExecutor<Node>,
        CrudRepository<Node, Long> {
  Page<Node> findAll(Pageable pageable);

  @Query(
      value =
          "with descendants (parent_id, descendant, level) as\n"
              + "  (select parent_id, id, 1\n"
              + "    from nodes where id = ?1\n"
              + "  union all\n"
              + "    select d.parent_id, n.id, d.level + 1\n"
              + "    from descendants as d\n"
              + "      join nodes n\n"
              + "        on d.descendant = n.parent_id\n"
              + "  ) \n"
              + "select n.*\n"
              + "from nodes n, descendants d where n.id = d.descendant\n"
              + "order by parent_id, height",
      countQuery = "select count(*) from nodes",
      nativeQuery = true)
  Page<Node> findDescendantNodes(Long id, Pageable pageable);

  @Query(
      value = "select * from nodes where parent_id = ?1\n" + "order by id",
      countQuery = "select count(*) from nodes",
      nativeQuery = true)
  Page<Node> findChildren(Long id, Pageable pageable);
}
