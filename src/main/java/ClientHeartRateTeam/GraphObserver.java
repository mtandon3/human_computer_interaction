package ClientHeartRateTeam;

import java.awt.*;
import java.util.*;

public class GraphObserver implements Observer{

	@Override
	public void update(Observable o, Object arg) {
		
		CombinedDataStatefull dataObj = (CombinedDataStatefull) o;
		Queue<String> dataQueue = dataObj.getGlobalQueue();
		String data = dataQueue.poll();
		String values[] = data.split(",");
		values[1] = "" + (Double.parseDouble(values[1]) - 60) / 130;

		ClientUI.getInstance().graphModel.setNoOfChannels(1);
		Color channelColors[] = new Color[] { Color.RED };
		ClientUI.getInstance().graphModel.setChannelColors(channelColors);
		//ClientUI.getInstance().updateSmileImage("sad.png");

		ArrayList<ArrayList<CoordinatesModel>> graphData = ClientUI.getInstance().graphModel.getGraphData();

		ArrayList<CoordinatesModel> coordinatesList = new ArrayList<>();
		coordinatesList.add(new CoordinatesModel(Double.parseDouble(values[1]), Double.parseDouble(values[2])));
		if(Double.parseDouble(values[1]) >= 0.6 && Double.parseDouble(values[2])>=0.6) {
			ClientUI.getInstance().updateSmileImage("smile.png");
		}else if(Double.parseDouble(values[1]) < 0.3 && Double.parseDouble(values[2])<0.6) {
			ClientUI.getInstance().updateSmileImage("sad.png");
		}else {
			ClientUI.getInstance().updateSmileImage("neutral.png");
		}
		graphData.clear();
		graphData.add(coordinatesList);

		ClientUI.getInstance().graphModel.setXLength(1);
		ClientUI.getInstance().graphModel.setGraphData(graphData);
		ClientUI.getInstance().graph.updateGraphView(ClientUI.getInstance().graphModel, "chart.png");
	}

}
