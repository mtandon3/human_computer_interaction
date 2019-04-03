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

		ArrayList<ArrayList<CoordinatesModel>> graphData = ClientUI.getInstance().graphModel.getGraphData();

		ArrayList<CoordinatesModel> coordinatesListStart = new ArrayList<>();
		coordinatesListStart.add(new CoordinatesModel(0, 0));

		ArrayList<CoordinatesModel> coordinatesList = new ArrayList<>();
		coordinatesList.add(new CoordinatesModel(Double.parseDouble(values[1]), Double.parseDouble(values[2])));

		graphData.clear();
		graphData.add(coordinatesListStart);
		graphData.add(coordinatesList);

		ClientUI.getInstance().graphModel.setXLength(1);
		ClientUI.getInstance().graphModel.setGraphData(graphData);
		ClientUI.getInstance().graph.updateGraphView(ClientUI.getInstance().graphModel);
	}

}
