package com.serhiidrahan.schreib_mate_bot.repository;

import com.serhiidrahan.schreib_mate_bot.entity.AssignmentTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssignmentTopicRepository extends JpaRepository<AssignmentTopic, Long> {

    @Query("SELECT t FROM AssignmentTopic t WHERE t.active = true AND t.id NOT IN :assignedTopicIds")
    List<AssignmentTopic> findUnassignedActiveTopics(@Param("assignedTopicIds") List<Long> assignedTopicIds);
}
