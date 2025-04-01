package com.github.babichil.scratchgame.service;

import com.github.babichil.scratchgame.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class RewardCalculator implements IRewardCalculator {

    private final GameConfig config;

    @Override
    public GameResult calculate(String[][] matrix, int betAmount) {
        Map<String, Long> symbolCount = countStandardSymbols(matrix);
        Map<String, List<String>> appliedCombinations = new HashMap<>();
        double totalReward = 0;

        for (var entry : symbolCount.entrySet()) {
            String symbolName = entry.getKey();
            long count = entry.getValue();
            Symbol symbol = config.symbols().get(symbolName);

            if (symbol == null || !symbol.isStandard()) continue;

            double base = betAmount * Optional.ofNullable(symbol.getRewardMultiplier()).orElse(1.0);
            ResultWithApplied combResult = applyWinCombinations(matrix, symbolName, count);
            if (!combResult.combinations().isEmpty()) {
                appliedCombinations.put(symbolName, combResult.combinations());
                totalReward += base * combResult.multiplier();
            }
        }

        return new GameResult(matrix, totalReward, List.of(), appliedCombinations);
    }

    private ResultWithApplied applyWinCombinations(String[][] matrix, String symbolName, long count) {
        double multiplier = 1.0;
        List<String> applied = new ArrayList<>();
        Set<String> usedGroups = new HashSet<>();

        List<Map.Entry<String, WinCombination>> sorted = config.winCombinations().entrySet().stream()
                .sorted((a, b) -> Double.compare(
                        b.getValue().getRewardMultiplier(),
                        a.getValue().getRewardMultiplier()
                ))
                .toList();

        for (var entry : sorted) {
            String name = entry.getKey();
            WinCombination comb = entry.getValue();
            String group = comb.getGroup();

            if (group != null && usedGroups.contains(group)) continue;

            boolean matched = switch (CombinationType.from(comb.getWhen())) {
                case SAME_SYMBOLS -> count >= comb.getCount();
                case LINEAR_SYMBOLS -> comb.getCoveredAreas() != null &&
                        matchesCoveredArea(matrix, symbolName, comb.getCoveredAreas());
            };

            if (matched) {
                applied.add(name);
                multiplier *= comb.getRewardMultiplier();
                if (group != null) usedGroups.add(group);
                log.debug("Applied combination '{}' for symbol '{}'", name, symbolName);
            }
        }

        return new ResultWithApplied(multiplier, applied);
    }


    private boolean matchesCoveredArea(String[][] matrix, String symbol, List<List<String>> areas) {
        int rows = matrix.length, cols = matrix[0].length;

        for (List<String> area : areas) {
            if (area == null || area.isEmpty()) continue;

            boolean allMatch = true;
            for (String coord : area) {
                String[] parts = coord.split(":");
                if (parts.length != 2) continue;

                try {
                    int r = Integer.parseInt(parts[0]);
                    int c = Integer.parseInt(parts[1]);
                    if (r < 0 || c < 0 || r >= rows || c >= cols || !symbol.equals(matrix[r][c])) {
                        allMatch = false;
                        break;
                    }
                } catch (NumberFormatException e) {
                    log.warn("Invalid coordinate '{}', skipping", coord);
                    allMatch = false;
                    break;
                }
            }

            if (allMatch) return true;
        }

        return false;
    }

    private Map<String, Long> countStandardSymbols(String[][] matrix) {
        Map<String, Long> countMap = new HashMap<>();
        for (String[] row : matrix) {
            for (String cell : row) {
                Symbol symbol = config.symbols().get(cell);
                if (symbol != null && symbol.isStandard()) {
                    countMap.put(cell, countMap.getOrDefault(cell, 0L) + 1);
                }
            }
        }
        return countMap;
    }

    private record ResultWithApplied(double multiplier, List<String> combinations) {}
}
