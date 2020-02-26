package util;

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class GenerateMassiveArrayInput {
    public static void main(String[] args) throws Exception {
        new GenerateMassiveArrayInput().run();
    }

    private Random random = new Random();

    private void run() throws Exception {
        int length = 200_000;

        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./input.txt"), Charset.forName("UTF-8"));
        writer.write("" + length);
        writer.newLine();

        for (int i = 0; i < length; i++) {
            writer.write("" + random.nextInt(10_000) + " ");
        }

        writer.close();
    }
}
