package ClientHeartRateTeam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class IdxReader {

    private final static Logger LOGGER = LoggerFactory.getLogger(IdxReader.class);

    public static final String INPUT_IMAGE_PATH = "resources/train-images.idx3-ubyte";
    public static final String INPUT_LABEL_PATH = "resources/train-labels.idx1-ubyte";

    public static final String INPUT_IMAGE_PATH_TEST_DATA = "resources/t10k-images.idx3-ubyte";
    public static final String INPUT_LABEL_PATH_TEST_DATA = "resources/t10k-labels.idx1-ubyte";

    public static final int VECTOR_DIMENSION = 784; //square 28*28 as from data set -> array 784 items

    /**
     * @param size
     * @return
     */
    public static List<LabeledImage> loadData(final int size) {
        return getLabeledImages(INPUT_IMAGE_PATH, INPUT_LABEL_PATH, size);
    }

    /**
     * @param size
     * @return
     */
    public static List<LabeledImage> loadTestData(final int size) {
        return getLabeledImages(INPUT_IMAGE_PATH_TEST_DATA, INPUT_LABEL_PATH_TEST_DATA, size);
    }

    private static List<LabeledImage> getLabeledImages(final String inputImagePath,
                                                       final String inputLabelPath,
                                                       final int amountOfDataSet) {

        final List<LabeledImage> labeledImageArrayList = new ArrayList<>(amountOfDataSet);

        try (FileInputStream inImage = new FileInputStream(inputImagePath);
             FileInputStream inLabel = new FileInputStream(inputLabelPath)) {

            inImage.skip(16);
            inLabel.skip(8);

            double[] imgPixels = new double[VECTOR_DIMENSION];

            long start = System.currentTimeMillis();
            for (int i = 0; i < amountOfDataSet; i++) {

                if (i % 1000 == 0) {
                    LOGGER.info("Number of images extracted: " + i);
                }

                for (int index = 0; index < VECTOR_DIMENSION; index++) {
                    imgPixels[index] = inImage.read();
                }

                int label = inLabel.read();

                labeledImageArrayList.add(new LabeledImage(label, imgPixels));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return labeledImageArrayList;
    }

}