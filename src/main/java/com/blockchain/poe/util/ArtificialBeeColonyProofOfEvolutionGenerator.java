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
    private static final int MAX_GENERATIONS = 5000;
    private static Long previousProof;

    public ArtificialBeeColonyProofOfEvolutionGenerator(Long previousProof) {
        this.previousProof = previousProof;
    }

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
        private Long proof;
        private double fitness;

        public Bee() {
            proof = generateRandomProof();
            fitness = 0.0;
        }

        public void sendEmployedBee() {
            Long newProof = generateRandomProof();
            double newFitness = ArtificialBeeColonyProofOfEvolutionGenerator.calculateFitness(newProof);

            if (newFitness > fitness) {
                proof = newProof;
            }
        }

        public void sendOnlookerBee() {
            Long newProof = generateRandomProof();
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

        public Long getProof() {
            return proof;
        }
    }

    private static Long generateRandomProof() {
        long range = (long) Math.pow(10, PROOF_LENGTH);
        return (long) (Math.random() * range);
    }


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
