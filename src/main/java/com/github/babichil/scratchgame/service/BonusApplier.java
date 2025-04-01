package com.github.babichil.scratchgame.service;

import com.github.babichil.scratchgame.bonus.BonusStrategy;
import com.github.babichil.scratchgame.bonus.BonusStrategyFactory;
import com.github.babichil.scratchgame.model.GameConfig;
import com.github.babichil.scratchgame.model.GameResult;
import com.github.babichil.scratchgame.model.Symbol;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class BonusApplier implements IBonusApplier {

    private final GameConfig config;

    @Override
    public GameResult applyBonus(String[][] matrix, GameResult result) {
        if (result.reward() <= 0) {
            log.debug("Reward is 0, skipping bonus application.");
            return result;
        }

        Map<String, Symbol> symbolMap = config.symbols();
        List<String> appliedBonuses = new ArrayList<>();
        GameResult updatedResult = result;

        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[0].length; c++) {
                String name = matrix[r][c];
                if (name == null) {
                    log.warn("Matrix cell at [{}][{}] is null, skipping", r, c);
                    continue;
                }
                Symbol symbol = symbolMap.get(name);
                if (symbol != null && symbol.isBonus()) {
                    BonusStrategy strategy = BonusStrategyFactory.getStrategy(symbol.getImpact());
                    updatedResult = strategy.apply(updatedResult, symbol);
                    appliedBonuses.add(name);
                    log.debug("Applied bonus '{}' with impact '{}'. Current reward: {}", name, symbol.getImpact(), updatedResult.reward());
                }
            }
        }

        return updatedResult.withAppliedBonusSymbols(appliedBonuses);
    }
}
