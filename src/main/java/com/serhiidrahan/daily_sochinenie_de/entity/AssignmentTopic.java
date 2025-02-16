package com.serhiidrahan.daily_sochinenie_de.entity;

import com.serhiidrahan.daily_sochinenie_de.enums.Language;
import jakarta.persistence.*;

@Entity
@Table(name = "assignment_topic")
public class AssignmentTopic extends BaseEntity {

    @Column(name = "topic_de", nullable = false)
    private String topicDe;

    @Column(name = "topic_ru", nullable = false)
    private String topicRu;

    @Column(name = "topic_en", nullable = false)
    private String topicEn;

    @Column(name = "description_de", nullable = false, columnDefinition = "TEXT")
    private String descriptionDe;

    @Column(name = "description_ru", nullable = false, columnDefinition = "TEXT")
    private String descriptionRu;

    @Column(name = "description_en", nullable = false, columnDefinition = "TEXT")
    private String descriptionEn;

    @Column(name = "keywords_de", nullable = false, columnDefinition = "TEXT")
    private String keywordsDe;

    @Column(name = "keywords_ru", nullable = false, columnDefinition = "TEXT")
    private String keywordsRu;

    @Column(name = "keywords_en", nullable = false, columnDefinition = "TEXT")
    private String keywordsEn;

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

    public String getTopicEn() {
        return topicEn;
    }

    public void setTopicEn(String topicEn) {
        this.topicEn = topicEn;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getKeywordsDe() {
        return keywordsDe;
    }

    public void setKeywordsDe(String keywordsDe) {
        this.keywordsDe = keywordsDe;
    }

    public String getKeywordsRu() {
        return keywordsRu;
    }

    public void setKeywordsRu(String keywordsRu) {
        this.keywordsRu = keywordsRu;
    }

    public String getKeywordsEn() {
        return keywordsEn;
    }

    public void setKeywordsEn(String keywordsEn) {
        this.keywordsEn = keywordsEn;
    }

    public String getKeywords(Language language) {
        return switch (language) {
            case DE -> keywordsDe;
            case EN -> keywordsEn;
            case RU -> keywordsRu;
        };
    }

    public String getDescription(Language language) {
        return switch (language) {
            case DE -> descriptionDe;
            case EN -> descriptionEn;
            case RU -> descriptionRu;
        };
    }

    public String getTopic(Language language) {
        return switch (language) {
            case DE -> topicDe;
            case EN -> topicEn;
            case RU -> topicRu;
        };
    }
}
