package ClientHeartRateTeam;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.io.IOException;

public class PredictData {

    private MultiLayerNetwork model;

    public void loadModel() throws IOException {
        File location = new File("model.zip");
        model = ModelSerializer.restoreMultiLayerNetwork(location);
    }

    public int getResult(double a, double b, double c, double d){

        INDArray test = Nd4j.create(new double[] {a,b,c,d});

        int[] output = model.predict(test);

        System.out.println(output[0]);
        return output[0];
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        Classifier.train();

        PredictData obj = new PredictData();
        obj.loadModel();
        obj.getResult(0.290973605,0.51151998,0.008557452,0.676923077);
        obj.getResult(0.290973605,0.286469015,0.386109669,0.876923077);
        obj.getResult(0.290973605,0.146007744,0.421984663,0.815384615);
        obj.getResult(0.290973605,0.573980061,0.004215424,0.676923077);
        obj.getResult(0.290973605,0.577866395,0.472755787,0.838461538);
    }
}
