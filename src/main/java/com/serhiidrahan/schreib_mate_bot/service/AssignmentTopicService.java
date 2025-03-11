package com.serhiidrahan.schreib_mate_bot.service;

import com.serhiidrahan.schreib_mate_bot.entity.AssignmentTopic;
import com.serhiidrahan.schreib_mate_bot.repository.AssignmentTopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentTopicService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentTopicService.class);

    private final AssignmentTopicRepository topicRepository;

    public AssignmentTopicService(AssignmentTopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<AssignmentTopic> getUnassignedActiveTopics(List<Long> assignedTopicIds) {
        return topicRepository.findUnassignedActiveTopics(assignedTopicIds);
    }
}
