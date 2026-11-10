package com.portfolio.devprofile.ingestion;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class Parser {
    public List<Document> parseDocuments(InputStream inputStream){
        List<Document> documents = new ArrayList<>();

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            CSVParser csvParser = CSVFormat.DEFAULT.builder().setIgnoreSurroundingSpaces(true).build().parse(reader);

            for(CSVRecord csvRecord : csvParser){
                System.out.println(csvRecord);
            }
            return documents;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file", e);
        }
    }
}
