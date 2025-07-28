package com.mycompany.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.optaplanner.test.api.score.stream.ConstraintVerifier;

import com.mycompany.app.domain.*;
import com.mycompany.app.domain.auxiliary.AssignType;
import com.mycompany.app.score.SchedulerConstraintProvider;

public class ConstraintTest {
    private ConstraintVerifier<SchedulerConstraintProvider, SchedulerSolution> constraintVerifier;

    @BeforeEach
    public void setup() {
        constraintVerifier = ConstraintVerifier.build(
                new SchedulerConstraintProvider(),
                SchedulerSolution.class,
                Topic.class);
    }

    // HARD
    // CONSTRAINTS----------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    @Test
    public void test_finish_time_constraint_penalization() {
        Room room = new Room(1,"", 1);
        Day day = new Day(1);
        Topic topic = new Topic(0, 600);
        topic.setInitTime(480);
        topic.setDay(day);
        topic.setRoom(room);
        constraintVerifier.verifyThat(SchedulerConstraintProvider::timeLimitConstraint)
                .given(topic)
                .penalizesBy(60L);

    }

    @Test
    public void test_room_capacity_penalization() {
        Room room = new Room(1,"", 5);
        Day day = new Day(1);
        Topic topic = new Topic(0, 600);
        topic.setInitTime(480);
        topic.setDay(day);
        topic.setRoom(room);
        topic.setTopicPersonListSize(8);
        constraintVerifier.verifyThat(SchedulerConstraintProvider::roomCapacityConstraint)
                .given(topic)
                .penalizesBy(3);

    }

    @Test
    public void test_room_conflict_penalization() {
        Room room = new Room(1,"", 5);
        Day day = new Day(1);
        Topic topic = new Topic(0, 60);
        topic.setInitTime(480);
        topic.setDay(day);
        topic.setRoom(room);
        Topic topic2 = new Topic(1, 30);
        topic2.setInitTime(530);
        topic2.setDay(day);
        topic2.setRoom(room);
        constraintVerifier.verifyThat(SchedulerConstraintProvider::roomconflictConstraint)
                .given(topic, topic2)
                .penalizesBy(10);
    }

    @Test
    public void test_req_conflict_penalization() {
        Room room = new Room(1,"", 5);
        Room room2 = new Room(2,"sf", 5);
        Day day = new Day(1);
        Topic topic = new Topic(0, 60);
        topic.setInitTime(480);
        topic.setDay(day);
        topic.setRoom(room);
        Topic topic2 = new Topic(1, 70);
        topic2.setInitTime(530);
        topic2.setDay(day);
        topic2.setRoom(room2);
        AssingPersonToTopic assign = new AssingPersonToTopic(0,topic, "Joao", AssignType.REQUIRED);
        AssingPersonToTopic assign2 = new AssingPersonToTopic(1,topic2, "Joao", AssignType.REQUIRED);
        constraintVerifier.verifyThat(SchedulerConstraintProvider::simultaniousrequiredAssignmentConstraint)
                .given(topic, topic2, assign, assign2)
                .penalizesBy(10);

    }

    // MEDiUM
    // CONSTRAINTS----------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    @Test
    public void test_req_pref_conflict_penalization() {
        Room room = new Room(1,"", 5);
        Room room2 = new Room(2,"sf", 5);
        Day day = new Day(1);
        Topic topic = new Topic(0, 60);
        topic.setInitTime(480);
        topic.setDay(day);
        topic.setRoom(room);
        Topic topic2 = new Topic(1, 70);
        topic2.setInitTime(530);
        topic2.setDay(day);
        topic2.setRoom(room2);
        AssingPersonToTopic assign = new AssingPersonToTopic(1,topic, "Joao", AssignType.PREFERENCIAL);
        AssingPersonToTopic assign2 = new AssingPersonToTopic(2,topic2, "Joao", AssignType.REQUIRED);
        constraintVerifier.verifyThat(SchedulerConstraintProvider::simultaniousprefreqAssignmeConstraint)
                .given(topic, topic2, assign, assign2)
                .penalizesBy(10);

    }

    // SOFT
    // CONSTRAINTS----------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    @Test
    public void test_early_constraint() {
        Room room = new Room(1,"1", 2);
        Room room2 = new Room(2,"2", 2);
        Day day = new Day(1);
        Topic t1 = new Topic(0, 60);
        Topic t2 = new Topic(1, 30);
        Topic t3 = new Topic(2, 45);
        t1.setDay(day);
        t2.setDay(day);
        t3.setDay(day);
        t1.setRoom(room);
        t2.setRoom(room2);
        t3.setRoom(room2);
        t1.setInitTime(480);
        t2.setInitTime(480);
        t3.setInitTime(510);
        constraintVerifier.verifyThat(SchedulerConstraintProvider::earlyInitConstraint)
                .given(t1, t2, t3)
                .penalizesBy(1470);

    }

    @Test
    public void test_min_interval() {
        Room room = new Room(1,"1", 2);
        Room room2 = new Room(2,"2", 2);
        Day day = new Day(1);
        Topic t1 = new Topic(0, 60);
        Topic t2 = new Topic(1, 30);
        Topic t3 = new Topic(2, 45);
        t1.setDay(day);
        t2.setDay(day);
        t3.setDay(day);
        t1.setRoom(room);
        t2.setRoom(room2);
        t3.setRoom(room2);
        t1.setInitTime(480);
        t2.setInitTime(480);
        t3.setInitTime(515);
        constraintVerifier.verifyThat(SchedulerConstraintProvider::intervalConstraint)
                .given(t1, t2, t3)
                .penalizesBy(10);
    }

    @Test
    public void test_paralel_meetings() {
        Room room = new Room(1,"1", 2);
        Room room2 = new Room(2,"2", 2);
        Room room3 = new Room(3,"3", 2);
        Day day = new Day(1);
        Topic t1 = new Topic(0, 60);
        Topic t2 = new Topic(1, 30);
        Topic t3 = new Topic(2, 45);

        t1.setDay(day);
        t2.setDay(day);
        t3.setDay(day);
        t1.setRoom(room);
        t2.setRoom(room2);
        t3.setRoom(room3);
        t1.setInitTime(480);
        t2.setInitTime(480);
        t3.setInitTime(480);
        constraintVerifier.verifyThat(SchedulerConstraintProvider::paralelMettingsConstraint)
                .given(t1, t2, t3)
                .penalizesBy(105);
    }

    @Test
    public void test_capacity_amount() {
        Room room = new Room(1,"1", 5);
        Room room2 = new Room(2,"2", 3);
        Day day = new Day(1);
        Topic t1 = new Topic(0, 60);
        Topic t2 = new Topic(1, 30);
        Topic t3 = new Topic(2, 45);

        t1.setDay(day);
        t2.setDay(day);
        t3.setDay(day);
        t1.setRoom(room);
        t2.setRoom(room2);
        t3.setRoom(room2);
        t1.setInitTime(480);
        t2.setInitTime(480);
        t3.setInitTime(480);
        constraintVerifier.verifyThat(SchedulerConstraintProvider::biggerRoomsConstraint)
                .given(t1, t2, t3)
                .rewardsWith(11);
    }

    @Test
    public void test_room_stability() {
        Room room = new Room(1,"1", 2);
        Room room2 = new Room(2,"2", 2);
        Day day = new Day(1);
        Topic t1 = new Topic(0, 60);
        Topic t2 = new Topic(1, 30);
        Topic t3 = new Topic(2, 45);
        AssingPersonToTopic assign1 = new AssingPersonToTopic(1,t1, "jon", AssignType.REQUIRED);
        AssingPersonToTopic assign2 = new AssingPersonToTopic(2,t2, "jon", AssignType.PREFERENCIAL);
        AssingPersonToTopic assign3 = new AssingPersonToTopic(3,t3, "jon", AssignType.PREFERENCIAL);
        t1.setDay(day);
        t2.setDay(day);
        t3.setDay(day);
        t1.setRoom(room);
        t2.setRoom(room2);
        t3.setRoom(room);
        t1.setInitTime(480);
        t2.setInitTime(550);
        t3.setInitTime(510);
        constraintVerifier.verifyThat(SchedulerConstraintProvider::roomStabilityConstraint)
        .given(assign1,assign2,assign3)
        .penalizesBy(20);
    }
}
