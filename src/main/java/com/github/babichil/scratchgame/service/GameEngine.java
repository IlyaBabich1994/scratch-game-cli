package com.github.babichil.scratchgame.service;

import com.github.babichil.scratchgame.model.GameResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameEngine {

    private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

    private final IMatrixGenerator matrixGenerator;
    private final IRewardCalculator rewardCalculator;
    private final IBonusApplier bonusApplier;

    public GameEngine(IMatrixGenerator matrixGenerator,
                      IRewardCalculator rewardCalculator,
                      IBonusApplier bonusApplier) {
        this.matrixGenerator = matrixGenerator;
        this.rewardCalculator = rewardCalculator;
        this.bonusApplier = bonusApplier;
    }

    public GameResult play(int betAmount) {
        logger.info("Starting new game with bet amount: {}", betAmount);

        String[][] matrix = matrixGenerator.generate();
        logger.debug("Generated matrix: {}", (Object) matrix);

        GameResult rewardResult = rewardCalculator.calculate(matrix, betAmount);
        GameResult finalResult = bonusApplier.applyBonus(matrix, rewardResult);
        finalResult = finalResult.withMatrix(matrix);

        logger.info("Game finished. Total reward: {}, Bonus: {}", finalResult.getReward(), finalResult.getAppliedBonusSymbols());
        return finalResult;
    }
}
