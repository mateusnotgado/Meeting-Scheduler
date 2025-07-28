package com.mycompany.app;

import java.util.List;
import com.mycompany.app.domain.*;
import com.mycompany.app.domain.auxiliary.AssignType;
import com.mycompany.app.domain.auxiliary.ExcelReader;

public class App {
    public static void main(String[] args) {

        String path = "src\\main\\Resources\\data\\unsolved\\sampledata1.xlsx";
        SchedulerSolution solution = getSolutionTable(path);
       // showResults(solution);
  

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

    public static void showResults(SchedulerSolution solution) {
        List<Topic> topicList = solution.getTopicList();
        List<AssingPersonToTopic> assignList = solution.getAssignList();
        System.out.println("\n\n");
        for (Topic topic : topicList) {
            StringBuilder sb = new StringBuilder();
            int initTime = topic.getInitTime();
            int endTime = initTime + topic.getDuration();
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
}
