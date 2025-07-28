package com.mycompany.app.score;

import org.optaplanner.core.api.score.stream.Constraint; // ✅ Correct
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import com.mycompany.app.domain.*;
import com.mycompany.app.domain.auxiliary.*;;

public class SchedulerConstraintProvider implements ConstraintProvider {

        public Constraint[] defineConstraints(ConstraintFactory factory) {
                return new Constraint[] {
                                timeLimitConstraint(factory),
                                roomCapacityConstraint(factory),
                                roomconflictConstraint(factory),
                                simultaniousrequiredAssignmentConstraint(factory),
                                simultaniousprefreqAssignmeConstraint(factory),
                                earlyInitConstraint(factory),
                                intervalConstraint(factory),
                                paralelMettingsConstraint(factory),
                                biggerRoomsConstraint(factory),
                                roomStabilityConstraint(factory)

                };
        }

        // HARD
        // CONSTRAINTS--------------------------------------------------------------------------------------------------------
        // ------------------------------------------------------------------------------------------------------------------------
        // ------------------------------------------------------------------------------------------------------------------------
        public Constraint timeLimitConstraint(ConstraintFactory factory) {
                return factory.forEach(Topic.class)
                                .filter(t -> t.getInitTime() + t.getDuration() > 1020)
                                .penalizeLong(HardMediumSoftLongScore.ONE_HARD,
                                                t -> t.getInitTime() + t.getDuration() - 1020)
                                .asConstraint("Time exceeded constraint");

        }

        public Constraint roomCapacityConstraint(ConstraintFactory factory) {
                return factory.forEach(Topic.class)
                                .filter(t -> t.getRoom().getCapacity() < t.getTopicPersonListSize())
                                .penalizeLong(HardMediumSoftLongScore.ONE_HARD,
                                                t -> t.getTopicPersonListSize() - t.getRoom().getCapacity())
                                .asConstraint("Room capaticy constraint");
        }

        public Constraint roomconflictConstraint(ConstraintFactory factory) {
                return factory.forEachUniquePair(Topic.class,
                                Joiners.equal((t) -> t.getDay().getId(), t -> t.getDay().getId()),
                                Joiners.equal(t -> t.getRoom(), t -> t.getRoom()))
                                .filter((t1, t2) -> isOverlapping(t1, t2))
                                .penalizeLong(HardMediumSoftLongScore.ONE_HARD, (t1, t2) -> overlap(t1, t2))
                                .asConstraint("Room conflict constraint");

        }

        public Constraint simultaniousrequiredAssignmentConstraint(ConstraintFactory factory) {
                return factory.forEachUniquePair(Topic.class,
                                Joiners.equal((t) -> t.getDay().getId(), t -> t.getDay().getId()))
                                .filter((t1, t2) -> isOverlapping(t1, t2) && !t1.getRoom().equals(t2.getRoom()))
                                .join(AssingPersonToTopic.class,
                                                Joiners.equal((t1, t2) -> t1.getId(),
                                                                (person) -> person.getTopic().getId()))
                                .filter((t1, t2, p) -> p.getAssignType() == AssignType.REQUIRED)
                                .join(AssingPersonToTopic.class,
                                                Joiners.equal((t1, t2, person) -> t2.getId(),
                                                                (person) -> person.getTopic().getId()),
                                                Joiners.equal((t1, t2, p) -> p.getPerson(), p -> p.getPerson()))
                                .filter((t1, t2, p1, p2) -> p2.getAssignType() == AssignType.REQUIRED)
                                .penalizeLong(HardMediumSoftLongScore.ONE_HARD, (t1, t2, p, p2) -> overlap(t1, t2))
                                .asConstraint("Required Assignment conflict constraint");
        }

        // MEDIUM
        // CONSTRAINTS--------------------------------------------------------------------------------------------------------
        // ------------------------------------------------------------------------------------------------------------------------
        // ------------------------------------------------------------------------------------------------------------------------
        public Constraint simultaniousprefreqAssignmeConstraint(ConstraintFactory factory) {
                return factory.forEachUniquePair(Topic.class,
                                Joiners.equal((t) -> t.getDay().getId(), t -> t.getDay().getId()))
                                .filter((t1, t2) -> isOverlapping(t1, t2)
                                                && t1.getRoom().getId() != t2.getRoom().getId())
                                .join(AssingPersonToTopic.class,
                                                Joiners.equal((t1, t2) -> t1.getId(),
                                                                (person) -> person.getTopic().getId()))
                                .join(AssingPersonToTopic.class,
                                                Joiners.equal((t1, t2, person) -> t2.getId(),
                                                                (person) -> person.getTopic().getId()),
                                                Joiners.equal((t1, t2, p) -> p.getPerson(), p -> p.getPerson()))
                                .filter((t1, t2, p1,
                                                p2) -> !(p1.getAssignType() == AssignType.REQUIRED
                                                                && p2.getAssignType() == AssignType.REQUIRED))
                                .penalizeLong(HardMediumSoftLongScore.ONE_MEDIUM, (t1, t2, p, p2) -> overlap(t1, t2))
                                .asConstraint("Required/preferencial Assignment conflict constraint");
        }

        // SOFT
        // CONSTRAINTS--------------------------------------------------------------------------------------------------------
        // ------------------------------------------------------------------------------------------------------------------------
        // ------------------------------------------------------------------------------------------------------------------------
        public Constraint earlyInitConstraint(ConstraintFactory factory) {
                return factory.forEach(Topic.class)
                                .penalizeLong(HardMediumSoftLongScore.ONE_SOFT, t -> t.getInitTime())
                                .asConstraint("Early init constraint");
        }

        public Constraint intervalConstraint(ConstraintFactory factory) {
                return factory.forEachUniquePair(Topic.class,
                                Joiners.equal((t) -> t.getDay().getId(), t -> t.getDay().getId()))
                                .filter((t1, t2) -> !isOverlapping(t1, t2))
                                .filter((t1, t2) -> getIntervalDistance(t1, t2) < 15)
                                .penalizeLong(HardMediumSoftLongScore.ONE_SOFT,
                                                (t1, t2) -> Math.max(0, 15 - getIntervalDistance(t1, t2)))
                                .asConstraint("interval constraint");
        }

        public Constraint paralelMettingsConstraint(ConstraintFactory factory) {
                return factory.forEachUniquePair(Topic.class,
                                Joiners.equal((t) -> t.getDay().getId(), t -> t.getDay().getId()))
                                .penalizeLong(HardMediumSoftLongScore.ONE_SOFT, (t1, t2) -> overlap(t1, t2))
                                .asConstraint("Paralel meetings constraint");

        }

        public Constraint biggerRoomsConstraint(ConstraintFactory factory) {
                return factory.forEach(Topic.class)
                                .rewardLong(HardMediumSoftLongScore.ONE_SOFT, t -> t.getRoom().getCapacity())
                                .asConstraint("Total assigned capcity constraint ");
        }

        public Constraint roomStabilityConstraint(ConstraintFactory factory) {
                return factory.forEachUniquePair(AssingPersonToTopic.class)
                                .filter((a1, a2) -> !a1.getTopic().equals(a2.getTopic()) && sameDay(a1, a2)
                                                && !isOverlapping(a1.getTopic(), a2.getTopic()) &&
                                                getIntervalDistance(a1.getTopic(), a2.getTopic()) < 30
                                                && a1.getPerson().equals(a2.getPerson()))
                                .penalizeLong(HardMediumSoftLongScore.ONE_SOFT,
                                                (a1, a2) -> Math.max(0,
                                                                30 - getIntervalDistance(a1.getTopic(), a2.getTopic())))
                                .asConstraint("Room stability constraint");

        }

        // AUXILIARY
        // METHODS--------------------------------------------------------------------------------------------------------
        // ------------------------------------------------------------------------------------------------------------------------
        // ------------------------------------------------------------------------------------------------------------------------
        private int overlap(Topic topic1, Topic topic2) {
                int start1 = topic1.getInitTime();
                int start2 = topic2.getInitTime();
                int end1 = start1 + topic1.getDuration();
                int end2 = start2 + topic2.getDuration();

                return Math.max(0, Math.min(end1, end2) - Math.max(start1, start2));
        }

        private boolean isOverlapping(Topic topic1, Topic topic2) {
                return overlap(topic1, topic2) > 0;
        }

        public int getIntervalDistance(Topic t1, Topic t2) {
                int start1 = t1.getInitTime();
                int end1 = start1 + t1.getDuration();
                int start2 = t2.getInitTime();
                int end2 = start2 + t2.getDuration();

                // Se não houver sobreposição, calcula a distância
                return Math.min(Math.abs(start2 - end1), Math.abs(start1 - end2));
        }

        public boolean sameDay(AssingPersonToTopic a1, AssingPersonToTopic a2) {
                return a1.getTopic().getDay().getId() == a2.getTopic().getDay().getId();
        }
}
