package com.serhiidrahan.daily_sochinenie_de.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "assignment_topic")
public class AssignmentTopic extends BaseEntity {

    @Column(name = "topic_de", nullable = false)
    private String topicDe;

    @Column(name = "topic_ru", nullable = false)
    private String topicRu;

    @Column(name = "description_de", nullable = false, columnDefinition = "TEXT")
    private String descriptionDe;

    @Column(name = "description_ru", nullable = false, columnDefinition = "TEXT")
    private String descriptionRu;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    public String getTopicDe() {
        return topicDe;
    }

    public void setTopicDe(String topicDe) {
        this.topicDe = topicDe;
    }

    public String getTopicRu() {
        return topicRu;
    }

    public void setTopicRu(String topicRu) {
        this.topicRu = topicRu;
    }

    public String getDescriptionDe() {
        return descriptionDe;
    }

    public void setDescriptionDe(String descriptionDe) {
        this.descriptionDe = descriptionDe;
    }

    public String getDescriptionRu() {
        return descriptionRu;
    }

    public void setDescriptionRu(String descriptionRu) {
        this.descriptionRu = descriptionRu;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
