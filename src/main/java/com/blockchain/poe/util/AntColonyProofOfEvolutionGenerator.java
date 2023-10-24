package com.blockchain.poe.util;

import java.nio.charset.StandardCharsets;
import com.google.common.hash.Hashing;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/*Ant Colony Optimization (ACO) is a meta-heuristic optimization algorithm
inspired by the foraging behavior of ants. In ACO, a population of artificial
ants iteratively explores possible solutions to a problem. These ants communicate
with each other by depositing and sensing chemical pheromones on the paths they traverse.
 Over time, the algorithm converges towards an optimal solution by emphasizing paths with
 higher pheromone levels. ACO is commonly used to solve combinatorial optimization problems
 and is known for its ability to find high-quality solutions in complex search spaces.*/

/**
 * This class uses the Ant Colony Optimization algorithm to solve the nonce value on the blockchain.
 * @author Ahmet Can EROL
 * */

public class AntColonyProofOfEvolutionGenerator {
    private static final int PROOF_LENGTH = 8;
    private static final int TARGET_ZEROS = 4; // Difficulty level
    private static final int ANT_COUNT = 50;
    private static final double EVAPORATION_RATE = 0.2;
    private static Long previousProof;

    // Constructor that initializes the previous proof.
    public AntColonyProofOfEvolutionGenerator(Long previousProof) {
        this.previousProof = previousProof;
    }
    // Method to generate a proof of evolution using an ant colony optimization algorithm.
    public static Long proofOfEvolution(Long previousProof) {
        List<Ant> ants = new ArrayList<>();
        for (int i = 0; i < ANT_COUNT; i++) {
            ants.add(new Ant());
        }

        Ant bestAnt = null;
        int maxGenerations = 5000;
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
    // A private class representing an ant in the ant colony optimization.
    private static class Ant {
        @Getter
        private Long proof;
        private double pheromoneLevel;

        // Constructor for initializing an ant with a random proof and a pheromone level.
        public Ant() {
            proof = generateRandomProof();
            pheromoneLevel = 10.0;
        }
        // Method for the ant to generate a new proof and update it based on fitness.
        public void generateProof() {
            Long newProof = generateRandomProof();
            double fitness = calculateFitness(newProof);

            if (fitness > getFitness() || Math.random() < pheromoneLevel) {
                proof = newProof;
            }
        }
        // Check if the current proof is valid based on the target number of leading zeros.
        public boolean isValidProof() {
            return calculateFitness(proof) >= TARGET_ZEROS;
        }
        // Calculate and return the fitness of the current proof.
        public double getFitness() {
            return calculateFitness(proof);
        }
        // Update the pheromone level of the ant based on fitness and evaporation rate.

        public void updatePheromone() {
            pheromoneLevel = (1.0 - EVAPORATION_RATE) * pheromoneLevel + getFitness();
        }

    }
    // Generate a random proof of a specified length.
    private static Long generateRandomProof() {
        long range = (long) Math.pow(10, PROOF_LENGTH);
        return (long) (Math.random() * range);
    }
    // Calculate the fitness of a given proof by hashing and counting leading zeros.
    private static double calculateFitness(Long proof) {
        String combinedProof = String.valueOf(previousProof) + String.valueOf(proof);
        String sha256 = Hashing.sha256().hashString(combinedProof, StandardCharsets.UTF_8).toString();
        int leadingZeroCount = 0;
        while (leadingZeroCount < sha256.length() && sha256.charAt(leadingZeroCount) == '0') {
            leadingZeroCount++;
        }
        return leadingZeroCount;
    }
}
