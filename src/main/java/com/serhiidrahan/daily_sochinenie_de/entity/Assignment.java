package com.serhiidrahan.daily_sochinenie_de.entity;

import com.serhiidrahan.daily_sochinenie_de.enums.AssignmentState;
import jakarta.persistence.*;

@Entity
@Table(name = "assignment")
public class Assignment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_assignment_user_id"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false, foreignKey = @ForeignKey(name = "fk_assignment_assignment_topic_id"))
    private AssignmentTopic topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private AssignmentState state;

    @Column(name = "telegram_message_id")
    private Integer telegramMessageId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AssignmentTopic getTopic() {
        return topic;
    }

    public void setTopic(AssignmentTopic topic) {
        this.topic = topic;
    }

    public AssignmentState getState() {
        return state;
    }

    public void setState(AssignmentState state) {
        this.state = state;
    }

    public Integer getTelegramMessageId() {
        return telegramMessageId;
    }

    public void setTelegramMessageId(Integer telegramMessageId) {
        this.telegramMessageId = telegramMessageId;
    }
}
