package com.serhiidrahan.daily_sochinenie_de.repository;

import com.serhiidrahan.daily_sochinenie_de.entity.Assignment;
import com.serhiidrahan.daily_sochinenie_de.enums.AssignmentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    /**
     * Finds the user's active assignment.
     */
    @Query("SELECT a FROM Assignment a WHERE a.user.id = :userId AND a.state IN :states")
    List<Assignment> findAssignmentsByUserIdAndStates(@Param("userId") Long userId,
                                                     @Param("states") List<AssignmentState> states);

    /**
     * Finds topic IDs that have already been assigned to the user.
     */
    @Query("SELECT a.topic.id FROM Assignment a WHERE a.user.id = :userId")
    List<Long> findAssignedTopicIdsByUserId(@Param("userId") Long userId);
}
