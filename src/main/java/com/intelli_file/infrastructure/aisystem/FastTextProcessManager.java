package com.intelli_file.infrastructure.aisystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.intelli_file.domain.model.PredictionResult;

public class FastTextProcessManager {

    private final String fastTextBinary;
    private final String modelPath;
    private final String trainingDataPath;

    public FastTextProcessManager() {
        this.fastTextBinary = "./bin/fasttext.exe";
        this.modelPath = "./model/trained-model.bin";
        this.trainingDataPath = "./model/train.txt";
    }

    public PredictionResult classifyFile(Path filePath) throws IOException, InterruptedException {
        ensureModel();

        String content = Files.readString(filePath);

        return predict(content);
    }

    private void ensureModel() throws IOException, InterruptedException {
        File modelFile = new File(modelPath);

        if (!modelFile.exists() || isModelOutdated()) {
            System.out.println("Modelo inexistente ou desatualizado. Treinando...");
            trainModel();
        }
    }

    private boolean isModelOutdated() {
        File model = new File(modelPath);
        File dataset = new File(trainingDataPath);

        return dataset.lastModified() > model.lastModified();
    }

    private void trainModel() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
            fastTextBinary,
            "supervised",
            "-input", trainingDataPath,
            "-output", modelPath.replace(".bin", "")
        );

        pb.redirectErrorStream(true);
        pb.directory(Path.of("").toAbsolutePath().toFile()); // Define o working dir para root do projeto

        Process process = pb.start();
        printProcessOutput(process);

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Erro ao treinar modelo FastText");
        }
    }

    private PredictionResult predict(String text) throws IOException, InterruptedException {

        ProcessBuilder pb = new ProcessBuilder(
            fastTextBinary,
            "predict-prob",
            modelPath,
            "-",
            "1"
        );

        Process process = pb.start();

        try (BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(process.getOutputStream()))) {

            String[] lines = text.split("\n");

            for (String line : lines) {
                if (line.length() < 5 || line.trim().isEmpty()) continue;

                line = line.toLowerCase();

                writer.write(line);
                writer.newLine();
            }
        }

        BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream())
        );

        Map<String, Double> scores = new HashMap<>();
        double totalScore = 0.0;

        String line;
        while ((line = reader.readLine()) != null) {

            String[] parts = line.split(" ");

            if (parts.length < 2) continue;

            String label = parts[0].replace("__label__", "");
            double prob = Double.parseDouble(parts[1]);

            scores.put(label, scores.getOrDefault(label, 0.0) + prob);
            totalScore += prob;
        }

        process.waitFor();

        if (scores.isEmpty()) {
            return new PredictionResult("unknown", 0.0);
        }

        String bestLabel = null;
        double bestScore = -1;

        for (Map.Entry<String, Double> entry : scores.entrySet()) {
            if (entry.getValue() > bestScore) {
                bestScore = entry.getValue();
                bestLabel = entry.getKey();
            }
        }

        double finalProb = bestScore / totalScore;

        return new PredictionResult(bestLabel, finalProb);
    }

    private void printProcessOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream())
        );

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("[fasttext] " + line);
        }
    }
}