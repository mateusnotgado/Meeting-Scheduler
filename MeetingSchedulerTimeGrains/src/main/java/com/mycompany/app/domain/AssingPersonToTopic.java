package com.mycompany.app.domain;

import org.optaplanner.core.api.domain.lookup.PlanningId;

import com.mycompany.app.domain.auxiliary.AssignType;

public class AssingPersonToTopic {
    @PlanningId
    private int id;
    private Topic topic;
    private String person;
    private AssignType assignType;

    public AssingPersonToTopic(int id, Topic topic, String person, AssignType assignType) {
        this.id = id;
        this.topic = topic;
        this.person = person;
        this.assignType = assignType;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public AssignType getAssignType() {
        return assignType;
    }

    public void setAssignType(AssignType assignType) {
        this.assignType = assignType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PersonTopicAssig{" +
                "topic=" + topic +
                ", person=" + person +
                ", assignType=" + assignType +
                '}';
    }

}
