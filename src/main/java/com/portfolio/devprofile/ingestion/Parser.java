package com.portfolio.devprofile.ingestion;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class Parser {
    public List<Document> parseDocuments(InputStream inputStream){
        List<Document> documents = new ArrayList<>();

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            CSVParser csvParser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).setIgnoreSurroundingSpaces(true).build().parse(reader);

            String[] columns = {"MORNING", "AFTERNOON", "EVENING"};

            for (CSVRecord csvRecord : csvParser) {
                StringBuilder dayLog = new StringBuilder();
                for (String col : columns) {
                    if (csvRecord.isMapped(col)) {
                        String cell = csvRecord.get(col).trim();
                        if (!cell.isEmpty()) {
                            String[] tasks = cell.split("\\|");
                            for (String task : tasks) {
                                String trimmed = task.trim();
                                if (!trimmed.isEmpty()) {
                                    dayLog.append(trimmed).append("\n");
                                }
                            }
                        }
                    }
                }
                if (dayLog.length() > 20) {
                    String id = UUID.randomUUID().toString();
                    String content = "Engineering Activities:\n" + dayLog.toString().trim();
                    documents.add(new Document(id, content, "WorkDone Log"));
                }
            }
            return documents;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file", e);
        }
    }
}
