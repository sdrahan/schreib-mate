package com.serhiidrahan.schreib_mate_bot.service;

import com.serhiidrahan.schreib_mate_bot.entity.Assignment;
import com.serhiidrahan.schreib_mate_bot.entity.AssignmentTopic;
import com.serhiidrahan.schreib_mate_bot.entity.User;
import com.serhiidrahan.schreib_mate_bot.enums.AssignmentState;
import com.serhiidrahan.schreib_mate_bot.repository.AssignmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class AssignmentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentService.class);

    private final AssignmentRepository assignmentRepository;
    private final AssignmentTopicService assignmentTopicService;
    private final Random random = new Random();

    public AssignmentService(AssignmentRepository assignmentRepository,
                             AssignmentTopicService assignmentTopicService) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentTopicService = assignmentTopicService;
    }

    /**
     * Changes the state of an assignment (e.g., mark as DONE or CANCELLED).
     */
    @Transactional
    public void changeAssignmentState(Assignment assignment, AssignmentState newState) {
        assignment.setState(newState);
        assignmentRepository.save(assignment);
    }

    /**
     * Returns the user's current active assignment.
     * Throws an exception if multiple active assignments exist.
     */
    @Transactional(readOnly = true)
    public Assignment getCurrentActiveAssignment(User user) {
        List<Assignment> activeAssignments = assignmentRepository.findAssignmentsByUserIdAndStates(user.getId(), List.of(AssignmentState.ACTIVE, AssignmentState.SUBMITTED));

        if (activeAssignments.isEmpty()) {
            LOGGER.info("No active assignment found.");
            return null;
        } else if (activeAssignments.size() > 1) {
            throw new IllegalStateException("Multiple active assignments found for user.");
        }

        return activeAssignments.get(0);
    }

    /**
     * Assigns a new topic to the user and marks it as NEW.
     * Ensures the user gets a topic they haven't had before.
     */
    @Transactional
    public Assignment assignNewTopic(User user) {
        List<Long> assignedTopicIds = assignmentRepository.findAssignedTopicIdsByUserId(user.getId());

        List<AssignmentTopic> availableTopics = assignmentTopicService.getUnassignedActiveTopics(assignedTopicIds);
        if (availableTopics.isEmpty()) {
            throw new IllegalStateException("No available new topics for the user " + user.getTelegramId());
        }

        // Pick a random topic
        AssignmentTopic randomTopic = availableTopics.get(random.nextInt(availableTopics.size()));

        // Create a new assignment
        Assignment newAssignment = new Assignment();
        newAssignment.setUser(user);
        newAssignment.setTopic(randomTopic);
        newAssignment.setState(AssignmentState.ACTIVE);

        return assignmentRepository.save(newAssignment);
    }

    @Transactional
    public void setTelegramMessageId(Assignment assignment, Integer telegramMessageId) {
        assignment.setTelegramMessageId(telegramMessageId);
        assignmentRepository.save(assignment);
    }

    @Transactional(readOnly = false)
    public boolean hasAvailableTopics(User user) {
        List<Long> assignedTopicIds = assignmentRepository.findAssignedTopicIdsByUserId(user.getId());
        List<AssignmentTopic> availableTopics = assignmentTopicService.getUnassignedActiveTopics(assignedTopicIds);

        // TODO: come up with a better way of dealing with no topics left
        if (availableTopics.isEmpty()) {
            LOGGER.warn("User {} has no more new topics. Checking for skipped topics...", user.getTelegramId());

            int deletedCount = assignmentRepository.deleteCancelledAssignmentsByUserId(user.getId());
            if (deletedCount > 0) {
                LOGGER.info("Reset {} skipped topics for user {}.", deletedCount, user.getTelegramId());
                return true;
            }
        }
        return !availableTopics.isEmpty();
    }
}
