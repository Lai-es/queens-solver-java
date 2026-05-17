package com.queens.io;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.nio.file.Files;

public class QueensFetcher {

    public static void fetch8x8Puzzles() throws IOException {
        //start a new chrome driver
        WebDriver driver = new ChromeDriver();

        //make a directory where the puzzles are stored
        String puzzleDirectory = "./puzzles/8x8/";
        Files.createDirectories(Path.of(puzzleDirectory));

        for (int i = 1; i <= 300; i++) {
            try {
                //open the browser for the respective puzzle and wait for the element to be loaded
                driver.get("https://www.playqueensgame.com/puzzles/8x8/" + i);
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-row]")));

                //find grid cells
                List<WebElement> cells = driver.findElements(By.cssSelector("[data-row]"));

                //the hashmaps associates every unique RGB value to a Integer value (1-8)
                HashMap<String, Integer> RGBtoInteger = new HashMap<>();

                //integer 2D array to store RGB vals
                int[][] RGBtoBoard = new int[8][8];

                for (WebElement cell : cells) {
                    //find rgb value and convert it into integer
                    String style = cell.getAttribute("style");
                    String rgb = style.substring(style.indexOf("rgb"), style.indexOf(")") + 1);
                    if (!RGBtoInteger.containsKey(rgb)) {
                        RGBtoInteger.put(rgb, RGBtoInteger.size());
                    }

                    //assign row and cols
                    int row = Integer.parseInt(cell.getAttribute("data-row"));
                    int col = Integer.parseInt(cell.getAttribute("data-col"));
                    RGBtoBoard[row][col] = RGBtoInteger.get(rgb);
                }

                //store extracted board in text file
                List<String> lines = new ArrayList<>();
                for (int row = 0; row < 8; row++) {
                    StringBuilder sb = new StringBuilder();
                    for (int col = 0; col < 8; col++) {
                        if (col > 0) sb.append(" ");
                        sb.append(RGBtoBoard[row][col]);
                    }
                    lines.add(sb.toString());
                }
                Files.write(Path.of(puzzleDirectory + "puzzle_" + i + ".txt"), lines);
                System.out.println("Saved puzzle " + i);
            } catch (Exception e) {
                System.out.println("Skipping puzzle " + i + ": " + e.getMessage());
            }
        }

        //close Webscraper
        driver.quit();
    }
}
