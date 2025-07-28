package com.mycompany.app.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.mycompany.app.domain.auxiliary.TopicDificulty;

@PlanningEntity(difficultyComparatorClass = TopicDificulty.class)
public class Topic implements Comparable<Topic>{
    String name;
    @PlanningId
    int id;
    @PlanningVariable
    Room room;
    @PlanningVariable
    Day day;
    @PlanningVariable(valueRangeProviderRefs = "timeSlotRange")
    Integer initTime;
    int duration;
    int topicPersonListSize;

    public Topic() {

    }
 public Topic(int id ,int duration) {
        this.id = id;
        this.duration = duration;
    }
    public Topic(int id, String name, int duration) {
        this.id = id;
        this.duration = duration;
        this.name = name;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Integer getInitTime() {
        return initTime;
    }

    public void setInitTime(Integer initTime) {
        this.initTime = initTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTopicPersonListSize() {
        return topicPersonListSize;
    }

    public void setTopicPersonListSize(int topicPersonListSize) {
        this.topicPersonListSize = topicPersonListSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Topic other) {
        int dayCompare = Integer.compare(this.getDay().getDay(), other.getDay().getDay());
        if (dayCompare != 0) return dayCompare;
      int thisStart = this.getInitTime();
        int otherStart = other.getInitTime();
        int timeCompare= Integer.compare(thisStart, otherStart);
        if(timeCompare!=0) return timeCompare;
        String thisRoom = this.getRoom().getName();
        String otherRoom = other.getRoom().getName();
        int roomCompare = thisRoom.compareTo(otherRoom);
        return roomCompare;

  
    }

}
