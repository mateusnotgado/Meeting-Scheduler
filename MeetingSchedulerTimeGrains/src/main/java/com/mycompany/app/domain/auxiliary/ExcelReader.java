package com.mycompany.app.domain.auxiliary;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

import com.mycompany.app.domain.*;

public class ExcelReader {

    private String path;
    private List<Topic> topicList;
    // private List<Person> personList;
    private List<AssingPersonToTopic> assignList;
    private List<Room> roomList;
    private List<Day> dayList;

    public ExcelReader(String path) {
        this.path = path;
        this.topicList = new ArrayList<>();
        // this.personList = new ArrayList<>();
        this.assignList = new ArrayList<>();
        this.roomList = new ArrayList<>();
        this.dayList = new ArrayList<>();
    }

    public void processData() {
        try (FileInputStream fis = new FileInputStream(path);
                Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            // --- PARTE 1: Ler os dias disponíveis (começa na linha 1 até linha vazia)
            int rowIndex = 3;
            int id = 0;
            while (true) {
                Row row = sheet.getRow(rowIndex++);
                if (row == null || row.getCell(1) == null || row.getCell(1).getCellType() == CellType.BLANK)
                    break;

                String dia = row.getCell(1).getStringCellValue().trim();
                dayList.add(new Day(id,dia));
                id++;
            }

            // --- PARTE 2: Ler as salas (nome e capacidade)
            while (true) {
                Row row = sheet.getRow(rowIndex);
                if (row == null)
                    continue;
                Cell cell0 = row.getCell(0);
                if (cell0 != null && cell0.getStringCellValue().trim().equalsIgnoreCase("Salas de Reunião")) {
                    break;
                } else {
                    rowIndex++;
                }

            }
            id = 0;
            rowIndex++; // pula cabeçalho "Nome", "Capacidade"
            while (true) {
                Row row = sheet.getRow(rowIndex++);
                if (row == null || row.getCell(1) == null || row.getCell(1).getCellType() == CellType.BLANK)
                    break;

                String name = row.getCell(1).getStringCellValue().trim();
                int capacity = (int) row.getCell(2).getNumericCellValue();
                Room room = new Room(id++,name, capacity);
                roomList.add(room);
            }

            // --- PARTE 4: Ler as reuniões
            while (true) {
                Row row = sheet.getRow(rowIndex);
                if (row == null)
                    continue;
                Cell cell0 = row.getCell(0);
                if (cell0 != null && cell0.getStringCellValue().trim().equalsIgnoreCase("Reuniões a Serem Agendadas")) {
                    break;
                } else {
                    rowIndex++;
                }

            }

            rowIndex++; // pula cabeçalho
            id = 0;
            int assignId=0;
            while (true) {
                Row row = sheet.getRow(rowIndex++);
                if (row == null || row.getCell(1) == null || row.getCell(1).getCellType() == CellType.BLANK)
                    break;

                String topicName = row.getCell(1).getStringCellValue().trim();
                int duration = (int) row.getCell(2).getNumericCellValue();

                // Cria tópico
                Topic topic = new Topic(id, topicName, duration);
                id++;
                topicList.add(topic);
                // Parse pessoas
                List<String> required = parsePeople(row.getCell(3));
                topic.setTopicPersonListSize(required.size());
              //  String reqList = topic.getName() + " req: ";

                for (String req : required) {
                  //  reqList += req + ", ";

                    AssingPersonToTopic assign = new AssingPersonToTopic(assignId++,topic, req, AssignType.REQUIRED);
                    assignList.add(assign);
                }
              //  System.out.println(reqList);
                List<String> preferred = parsePeople(row.getCell(4));
              //  String prefList = topic.getName() + " pref: ";
                for (String pref : preferred) {
                  //  prefList += pref + ", ";
                    AssingPersonToTopic assign = new AssingPersonToTopic(assignId++,topic, pref, AssignType.PREFERENCIAL);
                    assignList.add(assign);
                }
               // System.out.println(prefList);
               // System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Métodos auxiliares ---

    private List<String> parsePeople(Cell cell) {
        List<String> people = new ArrayList<>();
        if (cell == null || cell.getCellType() == CellType.BLANK)
            return people;
        String[] names = cell.getStringCellValue().split(",");
        List<String> list = new ArrayList<>();
        for (String i : names) {
            list.add(i.trim());
        }
        return list;
    }

    // --- Getters ---

    public List<Topic> getTopicList() {
        return topicList;
    }

    public List<Day> getDayList() {
        return this.dayList;
    }

    public List<AssingPersonToTopic> getAssignList() {
        return assignList;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

}
