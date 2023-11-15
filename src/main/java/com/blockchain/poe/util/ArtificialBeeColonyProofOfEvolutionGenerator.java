package com.blockchain.poe.util;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*Artificial Bee Colony (ABC) is an optimization algorithm inspired by the foraging behavior of honeybees.
In ABC, a population of artificial bees iteratively explores possible solutions to an optimization problem.
These bees communicate with each other by sharing information about the quality of solutions they've discovered.
Over time, the algorithm converges toward an optimal solution by emphasizing promising solutions and avoiding
poor ones. ABC is often used for solving complex optimization problems and is known for its ability to find
high-quality solutions in various domains.*/

/**
 * This class uses the Artificial Bee Optimization algorithm to solve the nonce value on the blockchain.
 * @author Ahmet Can EROL
 * */

public class ArtificialBeeColonyProofOfEvolutionGenerator {
    private static final int PROOF_LENGTH = 15;
    private static final int TARGET_ZEROS = 12; // Difficulty level
    private static final int BEE_COUNT = 5;
    private static final int ONLOOKER_BEE_COUNT = 1;
    private static final int MAX_GENERATIONS = 500;
    private static Long previousProof;

    // Constructor that initializes the previous proof.
    public ArtificialBeeColonyProofOfEvolutionGenerator(Long previousProof) {
        this.previousProof = previousProof;
    }
    // Method to perform the Artificial Bee Colony algorithm for generating a proof of evolution.
    public static Long proofOfEvolution(Long previousProof) {
        List<Bee> bees = new ArrayList<>();
        for (int i = 0; i < BEE_COUNT; i++) {
            bees.add(new Bee());
        }

        Bee bestBee = null;
        int generation = 0;

        while (generation < MAX_GENERATIONS) {
            // Employed Bee Phase
            for (Bee bee : bees) {
                bee.sendEmployedBee();}
            // Calculate fitness values
            for (Bee bee : bees) {
                bee.calculateFitness();}
            // Onlooker Bee Phase
            for (int i = 0; i < ONLOOKER_BEE_COUNT; i++) {
                Bee onlookerBee = bees.get(new Random().nextInt(BEE_COUNT));
                onlookerBee.sendOnlookerBee();}
            // Calculate fitness values
            for (Bee bee : bees) {
                bee.calculateFitness();}
            // Scout Bee Phase
            for (Bee bee : bees) {
                bee.sendScoutBee();}
            // Find the best bee
            for (Bee bee : bees) {
                if (bestBee == null || bee.getFitness() > bestBee.getFitness()) {
                    bestBee = bee;}}
            if (bestBee != null) {
                return bestBee.getProof();}
            generation++;}
        return null;}
    // A private class representing a bee in the Artificial Bee Colony algorithm.
    private static class Bee {
        private Long proof;
        private double fitness;

        public Bee() {
            proof = generateRandomProof();
            fitness = 0.0;
        }
        // Employed Bee sends foraging bees and updates its proof.
        public void sendEmployedBee() {
            Long newProof = generateRandomProof();
            double newFitness = ArtificialBeeColonyProofOfEvolutionGenerator.calculateFitness(newProof);

            if (newFitness > fitness) {
                proof = newProof;
            }
        }
        // Onlooker Bee sends foraging bees and updates its proof.
        public void sendOnlookerBee() {
            Long newProof = generateRandomProof();
            double newFitness = ArtificialBeeColonyProofOfEvolutionGenerator.calculateFitness(newProof);

            if (newFitness > fitness) {
                proof = newProof;
            }
        }
        // Scout Bee phase where a bee searches for a new proof if its fitness is low.
        public void sendScoutBee() {
            if (fitness < TARGET_ZEROS) {
                proof = generateRandomProof();
            }
        }
        // Calculate and update the fitness of the bee.
        public void calculateFitness() {
            fitness = ArtificialBeeColonyProofOfEvolutionGenerator.calculateFitness(proof);
        }
        // Get the fitness of the bee.
        public double getFitness() {
            return fitness;
        }
        // Get the current proof of the bee.
        public Long getProof() {
            return proof;
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
