package com.igihecyubuntu.app.repository;

import com.igihecyubuntu.app.entity.Category;
import com.igihecyubuntu.app.dto.projection.CategoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<CategoryProjection> findAllProjectedBy();

    Optional<CategoryProjection> findProjectedById(Long id);

    List<CategoryProjection> findByDoneByOrderByNameAsc(Long doneBy);

    Optional<CategoryProjection> findByName(String name);

    // Dashboard queries using List<Object[]>
    @Query("SELECT c.name, COUNT(pc) " +
           "FROM Category c LEFT JOIN PostCategory pc ON c.id = pc.categoryId " +
           "GROUP BY c.id, c.name " +
           "ORDER BY COUNT(pc) DESC")
    List<Object[]> findCategoryUsageStatistics();

    @Query("SELECT COUNT(c), c.doneBy FROM Category c GROUP BY c.doneBy")
    List<Object[]> findCategoryCountsByUser();
}