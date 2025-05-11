package it.unibo.risikoop.controller.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

public class MapExtractor {
    public static Graph MapExtractorFromFile(final File file) {
        final Graph newMap = new MultiGraph(file.getName(), false, true);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("-");
                newMap.addEdge(line, parts[0], parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Fix the exception's catch
        }
        return newMap;
    }

    public static Graph MapExtractorFromFile(final String fileName) {
        return MapExtractorFromFile(new File(fileName));
    }

}
