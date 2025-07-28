package com.mycompany.app.domain.auxiliary;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mycompany.app.domain.Topic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelWriter {

    public static void writeTopicsToExcel(List<Topic> topicList, String outputPath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Agenda");

        // Cabeçalho
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Nome");
        header.createCell(1).setCellValue("Duração (min)");
        header.createCell(2).setCellValue("Dia");
        header.createCell(3).setCellValue("Sala");
        header.createCell(4).setCellValue("Início");

        int rowIdx = 1;
        for (Topic topic : topicList) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(topic.getName());
            row.createCell(1).setCellValue(topic.getDuration());
            row.createCell(2).setCellValue(topic.getDay().getDate());
            row.createCell(3).setCellValue(topic.getRoom().getName());
            row.createCell(4).setCellValue(timeFormat(topic.getInitTime()));
        }

        // Autosize colunas
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        // Salvar arquivo
        try (FileOutputStream fileOut = new FileOutputStream(outputPath)) {
            workbook.write(fileOut);
            System.out.println("Arquivo gerado com sucesso: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String timeFormat(int minutes) {
        if (minutes >= 720) {
            minutes += 60;
        }
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }
}
