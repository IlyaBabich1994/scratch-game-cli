package com.github.babichil.scratchgame;

import com.github.babichil.scratchgame.config.ClasspathConfigLoader;
import com.github.babichil.scratchgame.config.ConfigLoader;
import com.github.babichil.scratchgame.model.GameConfig;
import com.github.babichil.scratchgame.service.*;

import java.util.Arrays;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ConfigLoader configLoader = new ClasspathConfigLoader();
        GameConfig config = configLoader.loadConfig("config.json");
        System.out.println(config);
        IMatrixGenerator matrixGenerator = new MatrixGenerator(config);
        String[][] matrix = matrixGenerator.generate();
        System.out.println(Arrays.deepToString(matrix));
        IBonusApplier bonusApplier = new BonusApplier(config);
        IRewardCalculator rewardCalculator = new RewardCalculator(config);
        GameEngine gameEngine = new GameEngine(
                matrixGenerator,
                rewardCalculator,
                bonusApplier
        );

        System.out.println(gameEngine);
        System.out.println(gameEngine.play(1000));
    }
}