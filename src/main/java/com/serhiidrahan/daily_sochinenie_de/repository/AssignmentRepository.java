package com.serhiidrahan.daily_sochinenie_de.repository;

import com.serhiidrahan.daily_sochinenie_de.entity.Assignment;
import com.serhiidrahan.daily_sochinenie_de.enums.AssignmentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query("SELECT a FROM Assignment a WHERE a.user.id = :userId AND a.state IN :states")
    List<Assignment> findAssignmentsByUserIdAndStates(@Param("userId") Long userId,
                                                      @Param("states") List<AssignmentState> states);

    @Query("SELECT a.topic.id FROM Assignment a WHERE a.user.id = :userId")
    List<Long> findAssignedTopicIdsByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM Assignment a WHERE a.user.id = :userId AND a.state = 'CANCELLED'")
    int deleteCancelledAssignmentsByUserId(@Param("userId") Long userId);

}
