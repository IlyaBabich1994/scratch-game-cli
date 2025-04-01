package com.github.babichil.scratchgame.service;

import com.github.babichil.scratchgame.model.BonusSymbolProbability;
import com.github.babichil.scratchgame.model.GameConfig;
import com.github.babichil.scratchgame.model.StandardSymbolProbability;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class MatrixGenerator implements IMatrixGenerator {

    private static final double MAX_BONUS_COVERAGE = 0.25;

    private final GameConfig config;
    private final String fallbackSymbol;

    public MatrixGenerator(GameConfig config) {
        this.config = config;
        this.fallbackSymbol = resolveFallbackSymbol();
        validateConfig();
    }

    @Override
    public String[][] generate() {
        int rows = config.rows();
        int cols = config.columns();
        String[][] matrix = new String[rows][cols];

        Set<String> bonusPositions = selectBonusPositions(rows, cols);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                String coord = r + ":" + c;
                if (bonusPositions.contains(coord)) {
                    matrix[r][c] = rollBonusSymbol();
                    log.debug("Placed bonus symbol at [{}][{}]", r, c);
                } else {
                    matrix[r][c] = rollStandardSymbol(r, c);
                    log.debug("Placed standard symbol at [{}][{}]: {}", r, c, matrix[r][c]);
                }
            }
        }

        return matrix;
    }

    private Set<String> selectBonusPositions(int rows, int cols) {
        Map<String, Integer> weightedBonuses = Optional.ofNullable(config.probabilities().getBonusSymbols())
                .map(BonusSymbolProbability::symbols)
                .orElse(Collections.emptyMap());

        if (weightedBonuses.isEmpty()) {
            log.debug("No bonus symbol weights provided. No bonus symbols will be placed.");
            return Collections.emptySet();
        }

        int totalCells = rows * cols;
        int maxBonuses = Math.max(1, (int) (totalCells * MAX_BONUS_COVERAGE));
        int numberOfBonuses = ThreadLocalRandom.current().nextInt(1, maxBonuses + 1);

        Set<String> selected = new HashSet<>();
        while (selected.size() < numberOfBonuses) {
            int r = ThreadLocalRandom.current().nextInt(rows);
            int c = ThreadLocalRandom.current().nextInt(cols);
            selected.add(r + ":" + c);
        }

        return selected;
    }

    private String rollStandardSymbol(int row, int col) {
        Optional<StandardSymbolProbability> match = config.probabilities().getStandardSymbols().stream()
                .filter(p -> p.getRow() == row && p.getColumn() == col)
                .findFirst();

        Map<String, Integer> distribution = match
                .map(StandardSymbolProbability::getSymbols)
                .filter(m -> !m.isEmpty())
                .orElseGet(() -> {
                    List<StandardSymbolProbability> defaults = config.probabilities().getStandardSymbols();
                    if (defaults.isEmpty() || defaults.getFirst().getSymbols() == null || defaults.getFirst().getSymbols().isEmpty()) {
                        log.debug("No default standard symbols, using fallback '{}'", fallbackSymbol);
                        return Map.of(fallbackSymbol, 1);
                    }
                    return defaults.getFirst().getSymbols();
                });

        return pickRandomByWeight(distribution);
    }

    private String rollBonusSymbol() {
        Map<String, Integer> bonuses = Optional.ofNullable(config.probabilities().getBonusSymbols())
                .map(BonusSymbolProbability::symbols)
                .orElse(Collections.emptyMap());

        return pickRandomByWeight(bonuses);
    }

    private String pickRandomByWeight(Map<String, Integer> weightedMap) {
        int totalWeight = weightedMap.values().stream().mapToInt(i -> i).sum();
        if (totalWeight <= 0) {
            log.debug("Total weight is zero. Returning fallback symbol '{}'", fallbackSymbol);
            return fallbackSymbol;
        }

        int roll = ThreadLocalRandom.current().nextInt(totalWeight);
        int cumulative = 0;
        for (Map.Entry<String, Integer> entry : weightedMap.entrySet()) {
            cumulative += entry.getValue();
            if (roll < cumulative) {
                return entry.getKey();
            }
        }

        log.debug("Weighted selection failed. Returning fallback '{}'", fallbackSymbol);
        return fallbackSymbol;
    }

    private void validateConfig() {
        if (config.rows() <= 0 || config.columns() <= 0) {
            throw new IllegalArgumentException("Rows and columns must be greater than zero.");
        }
    }

    private String resolveFallbackSymbol() {
        return config.symbols().entrySet().stream()
                .filter(e -> e.getValue().isStandard())
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("F");
    }
}
