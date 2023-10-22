package com.blockchain.poe.util;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArtificialBeeColonyProofOfEvolutionGenerator {
    private static final int PROOF_LENGTH = 8;
    private static final int TARGET_ZEROS = 6; // Zorluk seviyesi
    private static final int BEE_COUNT = 20;
    private static final int ONLOOKER_BEE_COUNT = 10;
    private static final int MAX_GENERATIONS = 1000;
    private static String previousProof;

    public ArtificialBeeColonyProofOfEvolutionGenerator(String previousProof) {
        this.previousProof = previousProof;
    }

    public static String proofOfEvolution(Long previousProof) {
        List<Bee> bees = new ArrayList<>();
        for (int i = 0; i < BEE_COUNT; i++) {
            bees.add(new Bee());
        }

        Bee bestBee = null;
        int generation = 0;

        while (generation < MAX_GENERATIONS) {
            // Employed Bee Phase
            for (Bee bee : bees) {
                bee.sendEmployedBee();
            }

            // Calculate fitness values
            for (Bee bee : bees) {
                bee.calculateFitness();
            }

            // Onlooker Bee Phase
            for (int i = 0; i < ONLOOKER_BEE_COUNT; i++) {
                Bee onlookerBee = bees.get(new Random().nextInt(BEE_COUNT));
                onlookerBee.sendOnlookerBee();
            }

            // Calculate fitness values
            for (Bee bee : bees) {
                bee.calculateFitness();
            }

            // Scout Bee Phase
            for (Bee bee : bees) {
                bee.sendScoutBee();
            }

            // Find the best bee
            for (Bee bee : bees) {
                if (bestBee == null || bee.getFitness() > bestBee.getFitness()) {
                    bestBee = bee;
                }
            }

            if (bestBee != null) {
                return bestBee.getProof();
            }

            generation++;
        }

        return null;
    }

    private static class Bee {
        private String proof;
        private double fitness;

        public Bee() {
            proof = generateRandomProof();
            fitness = 0.0;
        }

        public void sendEmployedBee() {
            String newProof = generateRandomProof();
            double newFitness = ArtificialBeeColonyProofOfEvolutionGenerator.calculateFitness(newProof);

            if (newFitness > fitness) {
                proof = newProof;
            }
        }

        public void sendOnlookerBee() {
            String newProof = generateRandomProof();
            double newFitness = ArtificialBeeColonyProofOfEvolutionGenerator.calculateFitness(newProof);

            if (newFitness > fitness) {
                proof = newProof;
            }
        }

        public void sendScoutBee() {
            if (fitness < TARGET_ZEROS) {
                proof = generateRandomProof();
            }
        }

        public void calculateFitness() {
            fitness = ArtificialBeeColonyProofOfEvolutionGenerator.calculateFitness(proof);
        }

        public double getFitness() {
            return fitness;
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
