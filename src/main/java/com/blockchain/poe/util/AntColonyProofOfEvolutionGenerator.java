package com.blockchain.poe.util;

import java.nio.charset.StandardCharsets;
import com.google.common.hash.Hashing;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntColonyProofOfEvolutionGenerator {
    private static final int PROOF_LENGTH = 8;
    private static final int TARGET_ZEROS = 6; // Zorluk seviyesi
    private static final int ANT_COUNT = 20;
    private static final double EVAPORATION_RATE = 0.1;
    private static String previousProof;

    public AntColonyProofOfEvolutionGenerator(String previousProof) {
        this.previousProof = previousProof;
    }

    public static String proofOfEvolution(Long previousProof) {
        List<Ant> ants = new ArrayList<>();
        for (int i = 0; i < ANT_COUNT; i++) {
            ants.add(new Ant());
        }

        Ant bestAnt = null;
        int maxGenerations = 1000;
        int generation = 0;

        while (generation < maxGenerations) {
            for (Ant ant : ants) {
                ant.generateProof();
            }

            for (Ant ant : ants) {
                if (ant.isValidProof()) {
                    if (bestAnt == null || ant.getFitness() > bestAnt.getFitness()) {
                        bestAnt = ant;
                    }
                }
            }

            if (bestAnt != null) {
                return bestAnt.getProof();
            }

            for (Ant ant : ants) {
                ant.updatePheromone();
            }

            generation++;
        }

        return null;
    }

    private static class Ant {
        private String proof;
        private double pheromoneLevel;

        public Ant() {
            proof = generateRandomProof();
            pheromoneLevel = 1.0;
        }

        public void generateProof() {
            String newProof = generateRandomProof();
            double fitness = calculateFitness(newProof);

            if (fitness > getFitness() || Math.random() < pheromoneLevel) {
                proof = newProof;
            }
        }

        public boolean isValidProof() {
            return calculateFitness(proof) >= TARGET_ZEROS;
        }

        public double getFitness() {
            return calculateFitness(proof);
        }

        public void updatePheromone() {
            pheromoneLevel = (1.0 - EVAPORATION_RATE) * pheromoneLevel + getFitness();
        }

        public String getProof() {
            return proof;
        }
    }

    private static String generateRandomProof() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < PROOF_LENGTH; i++) {
            char randomChar = (char) (random.nextInt(26) + 'a');
            sb.append(randomChar);
        }
        return sb.toString();
    }

    private static double calculateFitness(String proof) {
        String combinedProof = previousProof + proof;
        String sha256 = Hashing.sha256().hashString(combinedProof, StandardCharsets.UTF_8).toString();
        int leadingZeroCount = 0;
        while (leadingZeroCount < sha256.length() && sha256.charAt(leadingZeroCount) == '0') {
            leadingZeroCount++;
        }
        return leadingZeroCount;
    }
}
