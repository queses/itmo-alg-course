import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Week2Task4Back {
    public static void main(String[] args) throws Exception {
        new Week2Task4Back().run();
    }

    private final int ioChunkSize = 3;
    private final int ioStringChunkSize = ioChunkSize * 10;

    private int arrayLength;
    private int ratioA;
    private int ratioB;
    private int ratioC;
    private int firstNeedleIndex;
    private int secondNeedleIndex;
    private int prevArrayItem;
    private int currentArrayItem;
    private int currentSupportElem;

    private int currentExposedBuffer;
    private String readBufferFilename;
    private String writeBufferFilename;

    private void run() throws Exception {
        readInput();
        createAndWriteArray();
        writeItemsBelowSupport();

//        writeOutput();
    }

    private void readInput() throws Exception {
        String inputString = new String(Files.readAllBytes(Paths.get("./input.txt")));
        StringTokenizer st = new StringTokenizer(inputString);
        arrayLength = Integer.parseInt(st.nextToken());
        firstNeedleIndex = Integer.parseInt(st.nextToken()) - 1;
        secondNeedleIndex = Integer.parseInt(st.nextToken()) - 1;
        ratioA = Integer.parseInt(st.nextToken());
        ratioB = Integer.parseInt(st.nextToken());
        ratioC = Integer.parseInt(st.nextToken());
        prevArrayItem = Integer.parseInt(st.nextToken());
        currentArrayItem = Integer.parseInt(st.nextToken());
    }

    private BufferedWriter getNextBufferWriter () throws Exception {
        this.currentExposedBuffer = this.currentExposedBuffer % 2 + 1;
        return Files.newBufferedWriter(Paths.get(getReadBufferFileName()), Charset.forName("UTF-8"));
    }

    private String getReadBufferFileName () {
        return "./buffer" + this.currentExposedBuffer + ".txt";
    }

    private void createAndWriteArray() throws Exception {
        BufferedWriter writer = getNextBufferWriter();

        int index = 2;

        int middleIndex = arrayLength / 2;
        int[] tripleMedianValues = new int[3];
        tripleMedianValues[1] = prevArrayItem;

        StringBuilder sb = new StringBuilder("" + prevArrayItem + " " + currentArrayItem + " ");
        while (index < arrayLength) {
            int chunkSize = (arrayLength - index >= ioChunkSize) ? ioChunkSize : arrayLength - index;
            for (int chunkIndex = 0; chunkIndex < chunkSize; chunkIndex++) {
                int nextItem = ratioA * prevArrayItem + ratioB * currentArrayItem + ratioC;
                sb.append(nextItem).append(" ");
                prevArrayItem = currentArrayItem;
                currentArrayItem = nextItem;

                if (index == middleIndex) {
                    tripleMedianValues[1] = nextItem;
                } else if (index == arrayLength - 1) {
                    tripleMedianValues[2] = nextItem;
                }

                index++;
            }

            writer.write(sb.toString());
            sb = new StringBuilder();
        }

        writer.close();

        currentSupportElem = getTripleMedianSupportItem(tripleMedianValues);
    }

    private int getTripleMedianSupportItem (int[] values) {
        for (int j = 0; j < 3 - 1; j++) {
            int extemumIndex = j;
            long extremumNum = values[j];

            for (int i = j + 1; i < 3; i++) {
                if (values[i] < extremumNum) {
                    extemumIndex = i;
                    extremumNum = values[i];
                }
            }

            if (extemumIndex != j) {
                int temp = values[j];
                values[j] = values[extemumIndex];
                values[extemumIndex] = temp;
            }
        }

        return values[1];
    }

    private void writeItemsBelowSupport () throws Exception {
        BufferedReader reader = Files.newBufferedReader(Paths.get(getReadBufferFileName()));
        BufferedWriter writer = getNextBufferWriter();

        char[] excessChunkPart = new char[10];
        char[] chunkInput = new char[ioStringChunkSize];
        while (reader.read(chunkInput, 0, ioStringChunkSize) > 0) {
            StringTokenizer st = new StringTokenizer(new String(excessChunkPart).trim().concat(new String(chunkInput)));
            for (int i = 0; i <= st.countTokens(); i++) {
                int nextInt = Integer.parseInt(st.nextToken());

                if (nextInt < currentSupportElem) {
                    writer.write("" + nextInt + " ");
                }
            }

            excessChunkPart = new char[10];
            for (int i = chunkInput.length - 1; i >= 0; i--) {
                if (chunkInput[i] == ' ') {
                    int copyLength = chunkInput.length - 1 - i;
                    if (copyLength > 0) {
                        System.arraycopy(chunkInput, i + 1, excessChunkPart, 0, copyLength);
                    }

                    break;
                }
            }
        }

        writer.close();
    }

    private void writeOutput(int num1, int num2) throws Exception {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output.txt"), Charset.forName("UTF-8"));

        writer.write("" + num1 + " " + num2);
        writer.close();
    }
}