package com.blockchain.poe.util;

import java.nio.charset.StandardCharsets;
import com.google.common.hash.Hashing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockProofOfEvolutionGenerator {
    private static final int PROOF_LENGTH = 8;
    private static final int TARGET_ZEROS = 6; // Zorluk seviyesi
    private static final int POPULATION_SIZE = 20;
    private static final double MUTATION_RATE = 0.2;
    private static String previousProof;

    public BlockProofOfEvolutionGenerator(String previousProof) {
        this.previousProof = previousProof;
    }

    public static String proofOfEvolution(Long previousProof) {
        List<String> population = generateInitialPopulation();
        int generation = 0;

        while (true) {
            List<String> nextGeneration = new ArrayList<>();
            for (String proof : population) {
                if (isProofValid(String.valueOf(previousProof), proof)) {
                    return proof;
                }
            }

            population = selectBestIndividuals(population);
            nextGeneration.add(population.get(0)); // Elitism

            while (nextGeneration.size() < POPULATION_SIZE) {
                String parent1 = selectRandomParent(population);
                String parent2 = selectRandomParent(population);
                String child = crossover(parent1, parent2);
                child = mutate(child);
                nextGeneration.add(child);
            }
            population = nextGeneration;
            generation++;
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

    private static boolean isProofValid(String previousProof, String currentProof) {
        String combinedProof = previousProof + currentProof;
        String sha256 = Hashing.sha256().hashString(combinedProof, StandardCharsets.UTF_8).toString();
        return sha256.startsWith("0".repeat(TARGET_ZEROS));
    }

    private static List<String> generateInitialPopulation() {
        List<String> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(generateRandomProof());
        }
        return population;
    }

    private static List<String> selectBestIndividuals(List<String> population) {
        // Fitness değerine göre sıralamaya gerek yok, en iyiyi seçelim.
        String bestIndividual = Collections.max(population, (p1, p2) -> Double.compare(calculateFitness(p1), calculateFitness(p2)));
        return List.of(bestIndividual);
    }


    private static String selectRandomParent(List<String> population) {
        return population.get(new Random().nextInt(population.size()));
    }

    private static String crossover(String parent1, String parent2) {
        int crossoverPoint = new Random().nextInt(PROOF_LENGTH);
        return parent1.substring(0, crossoverPoint) + parent2.substring(crossoverPoint);
    }

    private static String mutate(String child) {
        StringBuilder mutatedChild = new StringBuilder(child);
        for (int i = 0; i < PROOF_LENGTH; i++) {
            if (Math.random() < MUTATION_RATE) {
                char randomChar = (char) (new Random().nextInt(26) + 'a');
                mutatedChild.setCharAt(i, randomChar);
            }
        }
        return mutatedChild.toString();
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
