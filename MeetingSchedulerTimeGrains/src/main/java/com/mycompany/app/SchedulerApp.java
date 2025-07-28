package com.mycompany.app;

import java.util.List;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.app.domain.*;
import com.mycompany.app.domain.auxiliary.AssignType;
import com.mycompany.app.domain.auxiliary.ExcelReader;
import com.mycompany.app.domain.auxiliary.ExcelWriter;

public class SchedulerApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerSolution.class);

    public static void main(String[] args) {
        // SolverFactory<SchedulerSolution> solverFactory;
        String sourcePath = "src\\main\\resources\\data\\unsolved\\sampledata2.xlsx";
        String destinationPath = "src\\main\\Resources\\data\\solved\\";
        // SchedulerSolution problem = getTestData(interval);
        SchedulerSolution problem = getSolutionTable(sourcePath);

        // Solve the problem
        Solver<SchedulerSolution> solver = getSolver();
        SchedulerSolution solution = solver.solve(problem);
        // Visualize the solution
        printSolution(solution);
        showResults(solution);
        checkResult(solution);
        List<Topic> topicList = solution.getTopicList();
        ExcelWriter.writeTopicsToExcel(topicList, destinationPath + "sample2solved.xlsx");

    }

    private static SchedulerSolution getSolutionTable(String path) {
        ExcelReader reader = new ExcelReader(path);
        reader.processData();

        List<Topic> topicList = reader.getTopicList();
        List<AssingPersonToTopic> assignList = reader.getAssignList();
        List<Room> roomList = reader.getRoomList();
        List<Day> dayList = reader.getDayList();
        int index = 0;
        for (Topic topic : topicList) {
            topic.setInitTime(480);
            topic.setRoom(roomList.get(index % roomList.size()));
            topic.setDay(dayList.get(index % dayList.size()));
            index++;
        }
        return new SchedulerSolution(topicList, roomList, dayList, assignList, 15);
    }

    private static Solver<SchedulerSolution> getSolver() {
        SolverConfig solverConfig = SolverConfig.createFromXmlResource("solverConfig.xml");

        SolverFactory<SchedulerSolution> solverFactory = SolverFactory.create(solverConfig);

        Solver<SchedulerSolution> solver = solverFactory.buildSolver();
        return solver;
    }

    private static void printSolution(SchedulerSolution solution) {
        LOGGER.info("");
    }

    private static void checkResult(SchedulerSolution solution) {
        List<Topic> topicList = solution.getTopicList();
        List<AssingPersonToTopic> assignList = solution.getAssignList();
        boolean checkTimeLimitConflict = false;
        boolean checkRoomConflict = false;
        boolean checkRoomCapacity = false;
        boolean checkrequiringAssignConflict = false;
        boolean checkPrefReqAssignConflict = false;
        for (Topic topic : topicList) {
            if (topic.getInitTime() + topic.getDuration() > 1020)
                checkTimeLimitConflict = true;
            if (topic.getTopicPersonListSize() > topic.getRoom().getCapacity())
                checkRoomCapacity = true;
            for (Topic topic2 : topicList) {
                if (!topic.equals(topic2) && isOverlapping(topic, topic2) && sameDay(topic2, topic)) {
                    if (sameRoom(topic2, topic))
                        checkRoomConflict = true;
                    else
                        for (AssingPersonToTopic a1 : assignList) {
                            if (a1.getTopic().getId() == topic.getId() && a1.getAssignType() == AssignType.REQUIRED)
                                for (AssingPersonToTopic a2 : assignList) {
                                    if (a2.getTopic().getId() == topic2.getId() && a1.getPerson().equals(a2.getPerson())
                                            && a2.getAssignType() == AssignType.REQUIRED)
                                        checkrequiringAssignConflict = true;
                                }
                            for (AssingPersonToTopic a2 : assignList) {
                                if (a1.getTopic().getId() == topic.getId() && a2.getTopic().getId() == topic2.getId()
                                        && a1.getPerson().equals(a2.getPerson())
                                        && !(a1.getAssignType() == AssignType.REQUIRED
                                                && a2.getAssignType() == AssignType.REQUIRED))
                                    checkPrefReqAssignConflict = true;

                            }
                        }

                }

            }

        }
        System.out.println();
        System.out.println("HARD CONSTRAINTS:");
        if (checkTimeLimitConflict)
            System.out.println("Restriçao de horario violada");
        else
            System.out.println("Restrição de horario atendida");
        if (checkRoomCapacity)
            System.out.println("Restrição de limite de sala violada");
        else
            System.out.println("Restrição de limite de sala atendida");
        if (checkRoomConflict)
            System.out.println("Restrição de conflito de reuniões violada");
        else
            System.out.println("Restrição de conflito de reuniões atendida");

        if (checkrequiringAssignConflict)
            System.out.println("Restrição de conflito de requiring assign violada");
        else
            System.out.println("Restrição de conflito de requiring assign atendida");
        System.out.println("\nMEDIUM CONSTRAINTS:");

        if (checkPrefReqAssignConflict)
            System.out.println("Restrição de conflito de pref/req assign violada");
        else
            System.out.println("Restrição de conflito de pref/req assign atendida");

    }

    public static void showResults(SchedulerSolution solution) {
        List<Topic> topicList = solution.getTopicList();
        topicList.sort(null);
        List<AssingPersonToTopic> assignList = solution.getAssignList();
        System.out.println("\n\n");
        for (Topic topic : topicList) {
            StringBuilder sb = new StringBuilder();
            int initTime = topic.getInitTime();
            int endTime = initTime + topic.getDuration();
            if (initTime >= 720) {
                initTime += 60;
                endTime += 60;
            } else if (endTime > 720)
                endTime += 60;
            String initTimeFormated = timeFormat(initTime);
            String endtimeFormated = timeFormat(endTime);
            sb.append("Topic: " + topic.getName() + "\n" + "Day: " + topic.getDay().getDay() + "\nRoom: "
                    + topic.getRoom().getName()
                    + "\nInit time: " + initTimeFormated + " End time: "
                    + endtimeFormated + "\nReqList: ");
            String reqList = new String();
            String prefList = new String();
            for (AssingPersonToTopic assign : assignList) {
                if (assign.getTopic().getId() == topic.getId())
                    if (assign.getAssignType() == AssignType.REQUIRED)
                        reqList += assign.getPerson() + ", ";
                    else
                        prefList += assign.getPerson() + ", ";
            }
            sb.append(reqList + "\n PrefList: " + prefList);

            System.out.println(sb.toString() + "\n");
        }
    }

    public static String timeFormat(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }

    private static int overlap(Topic topic1, Topic topic2) {
        int start1 = topic1.getInitTime();
        int start2 = topic2.getInitTime();
        int end1 = start1 + topic1.getDuration();
        int end2 = start2 + topic2.getDuration();

        return Math.max(0, Math.min(end1, end2) - Math.max(start1, start2));
    }

    private static boolean isOverlapping(Topic topic1, Topic topic2) {
        return overlap(topic1, topic2) > 0;
    }

    private static boolean sameDay(Topic t1, Topic t2) {
        return t1.getDay().getId() == t2.getDay().getId();
    }

    private static boolean sameRoom(Topic t1, Topic t2) {
        return t1.getRoom().getId() == t2.getRoom().getId();
    }

}
