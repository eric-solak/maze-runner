package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.*;
import java.lang.module.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("** Starting Maze Runner");
        String output = null;
        
        try {
            Configuration config = configure(args);
            Maze maze = new Maze(config.filePath);
            
            if (config.mazePath == null) {
                output = maze.traverseMaze();
            } else {
                output = maze.testPath(config.mazePath);
            }
            
        } catch (ParseException | IOException pe) {
            System.err.println(pe.getMessage());
            System.exit(1);
        }

        logger.info("**** Computing path");
        System.out.println(output);
        logger.info("** End of MazeRunner");
    }
    private static Configuration configure(String args[]) throws ParseException, IOException {
        Options options = new Options();
        options.addOption("i", true, "Input maze file");
        options.addOption("p", true, "Input maze path");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);


        String filePath = cmd.getOptionValue("i");
        String mazePath = cmd.getOptionValue("p");


        return new Configuration(filePath, mazePath);
    }
    private record Configuration(String filePath, String mazePath) {
        Configuration {
            if (filePath == null)
                throw new RuntimeException("File not found");
        }
    }


}
