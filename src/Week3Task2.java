import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Week3Task2 {
    public static void main(String[] args) throws Exception {
        new Week3Task2().run();
    }

    private final static byte alphabetOffset = 97;
    private final static byte alphabetSize = 26;

    private int[] lineOrder;
    private int linesAmount;
    private int lineLength;
    private int sortIterations;
    private byte[] input;
    private int inputOffset;
    private int inputLineLength;
    private int[] countsArray;
    private int[] zeroCountsArray;

    private void run() throws Exception {
        readInitialInput();
        digitSort();
        writeOutput();
    }

    private void digitSort() {
        countsArray = new int[alphabetSize];
        zeroCountsArray = new int[alphabetSize];

        byte[] array = new byte[linesAmount];
        byte[] buffer = new byte[linesAmount];
        int[] lineOrderBuffer = new int[linesAmount];

        lineOrder = new int[linesAmount];
        for (int i = 0; i < linesAmount; i++) {
            lineOrder[i] = i;
        }

        int[] lineOrderTmp;
        byte[] arrayTmp;
        for (int digit = 0; digit < sortIterations; digit++) {
            countingSort(digit, array, buffer, lineOrderBuffer);

            arrayTmp = array;
            array = buffer;
            buffer = arrayTmp;

            lineOrderTmp = lineOrder;
            lineOrder = lineOrderBuffer;
            lineOrderBuffer = lineOrderTmp;
        }
    }

    private void countingSort(int digit, byte[] source, byte[] target, int[] lineOrderTarget) {
        System.arraycopy(zeroCountsArray, 0, countsArray, 0, alphabetSize);

        int inputIndex = inputLineLength * (lineLength - digit - 1) + inputOffset;
        for (int i = 0; i < linesAmount; i++) {
            byte value = (byte) (input[inputIndex + lineOrder[i]] - alphabetOffset);
            source[i] = value;
            countsArray[value]++;
        }

        int prevCounter = countsArray[0];
        for (int i = 1; i < countsArray.length; i++) {
            if (countsArray[i] > 0) {
                countsArray[i] += prevCounter;
                prevCounter = countsArray[i];
            }
        }

        for (int i = source.length - 1; i >= 0; i--) {
            int newIndex = --countsArray[source[i]];
            target[newIndex] = source[i];
            lineOrderTarget[newIndex] = lineOrder[i];
        }
    }

    private void readInitialInput() throws Exception {
        Path path = Paths.get("./input.txt");
        BufferedReader reader = Files.newBufferedReader(path);
        String inputString = reader.readLine();
        StringTokenizer st = new StringTokenizer(inputString);

        int lineEndingLength = System.lineSeparator().length();
        linesAmount = Integer.parseInt(st.nextToken());
        lineLength = Integer.parseInt(st.nextToken());
        sortIterations = Integer.parseInt(st.nextToken());

        reader.close();

        input = Files.readAllBytes(path);
        inputOffset = inputString.length() + lineEndingLength;
        inputLineLength = linesAmount + lineEndingLength;
    }

    private void writeOutput() throws Exception {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output.txt"), Charset.forName("UTF-8"));

        for (int line : lineOrder) {
            writer.write((line + 1) + " ");
        }

        writer.close();
    }
}