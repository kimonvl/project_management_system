package com.kimon.project_management_system.repo;

import com.kimon.project_management_system.model.Project;
import com.kimon.project_management_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Long> {

    @Query("SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN p.team t " +
            "LEFT JOIN p.tags tag " +
            "WHERE (p.owner = :user OR t = :user) " +
            "AND p.category = :category " +
            "AND tag IN :tags " +
            "GROUP BY p.id " +
            "HAVING COUNT(DISTINCT tag) = :tagCount"
    )
    public List<Project> findDistinctByOwnerOrTeamWithTagsAndCategory(@Param("user") User user,
                                                                      @Param("tags") List<String> tags,
                                                                      @Param("tagCount") long tagCount,
                                                                      @Param("category") String category);

    @Query("SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN p.team t " +
            "LEFT JOIN p.tags tag " +
            "WHERE (p.owner = :user OR t = :user) " +
            "AND tag IN :tags " +
            "GROUP BY p.id " +
            "HAVING COUNT(DISTINCT tag) = :tagCount"
    )
    public List<Project> findDistinctByOwnerOrTeamWithTags(@Param("user") User user,
                                                                      @Param("tags") List<String> tags,
                                                                      @Param("tagCount") long tagCount);

    @Query("SELECT DISTINCT p FROM Project p " +
                "LEFT JOIN p.team t " +
                "WHERE (p.owner = :user OR t = :user) " +
                "AND p.category = :category " +
                "GROUP BY p.id"
    )
    public List<Project> findDistinctByOwnerOrTeamWithCategory(@Param("user") User user,
                                                                          @Param("category") String category);

    public List<Project> findDistinctByOwnerOrTeamContains(User owner, User teamMember);

    public List<Project> findByNameContainingIgnoreCaseAndTeamContaining(String name, User member);
}
