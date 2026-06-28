package com.intelli_file.domain.model;

public class PredictionResult {
    private final String label;
    private final double probability;

    public PredictionResult(String label, double probability) {
        this.label = label;
        this.probability = probability;
    }

    public String getLabel() { return label; }
    public double getProbability() { return probability; }

    @Override
    public String toString() {
        return label + " (" + probability + ")";
    }
}