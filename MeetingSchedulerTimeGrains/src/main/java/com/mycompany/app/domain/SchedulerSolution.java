package com.mycompany.app.domain;

import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.solution.ProblemFactProperty;
import org.optaplanner.core.api.domain.valuerange.CountableValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;

@PlanningSolution
public class SchedulerSolution {
    @PlanningEntityCollectionProperty
    @ValueRangeProvider
    List<Topic> topicList;
    @ProblemFactCollectionProperty
    @ValueRangeProvider
    List<Room> roomList;
    @ProblemFactCollectionProperty
    @ValueRangeProvider
    List<Day> dayList;
    @ProblemFactCollectionProperty
    List<AssingPersonToTopic> assignList;
    @ProblemFactProperty
    int timeSlot;
    @PlanningScore
    HardMediumSoftLongScore score;

    public SchedulerSolution(List<Topic> topicList, List<Room> roomList, List<Day> dayList,
            List<AssingPersonToTopic> assignList, int timeSlot) {
        this.topicList = topicList;
        this.roomList = roomList;
        this.dayList = dayList;
        this.assignList = assignList;
        this.timeSlot = timeSlot;
    }

    public SchedulerSolution() {

    }

    public List<Topic> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<Topic> topicList) {
        this.topicList = topicList;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public List<Day> getDayList() {
        return dayList;
    }

    public void setDayList(List<Day> dayList) {
        this.dayList = dayList;
    }

    public List<AssingPersonToTopic> getAssignList() {
        return assignList;
    }

    public void setAssignList(List<AssingPersonToTopic> assignList) {
        this.assignList = assignList;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlots(int timeSlot) {
        this.timeSlot = timeSlot;
    }

    @ValueRangeProvider(id = "timeSlotRange")
    public CountableValueRange<Integer> getTimeSlotRange() {
        return ValueRangeFactory.createIntValueRange(480, 1020, this.timeSlot);
    }

    public void setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
    }

    public HardMediumSoftLongScore getScore() {
        return score;
    }

    public void setScore(HardMediumSoftLongScore score) {
        this.score = score;
    }

}
