package com.github.babichil.scratchgame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.babichil.scratchgame.config.FileConfigLoader;
import com.github.babichil.scratchgame.model.GameConfig;
import com.github.babichil.scratchgame.model.GameResult;
import com.github.babichil.scratchgame.service.BonusApplier;
import com.github.babichil.scratchgame.service.MatrixGenerator;
import com.github.babichil.scratchgame.service.RewardCalculator;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (!isValidArgs(args)) {
            printUsage();
            return;
        }

        String configPath = args[1];
        int betAmount = parseBetAmount(args[3]);
        if (betAmount <= 0) {
            System.err.println("Betting amount must be a positive integer.");
            return;
        }

        try {
            FileConfigLoader loader = new FileConfigLoader();
            GameConfig config = loader.loadConfig(configPath);
            String[][] matrix = new MatrixGenerator(config).generate();
            RewardCalculator calculator = new RewardCalculator(config);
            BonusApplier bonusApplier = new BonusApplier(config);

            GameResult result = calculator.calculate(matrix, betAmount);
            result = bonusApplier.applyBonus(matrix, result);

            printResult(result);
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean isValidArgs(String[] args) {
        return args.length == 4 && "--config".equals(args[0]) && "--betting-amount".equals(args[2]);
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar <jar> --config <configPath> --betting-amount <amount>");
    }

    private static int parseBetAmount(String amountArg) {
        try {
            return Integer.parseInt(amountArg);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void printResult(GameResult result) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        System.out.println(json);
    }
}
