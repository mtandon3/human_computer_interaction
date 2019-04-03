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

		ClientUI.getInstance().graphModel.setNoOfChannels(4);
		Color channelColors[] = new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW };
		String[] legendNames = new String[] {"Heart Rate", "Smile", "Furrow Brow", "Clench"};

		ClientUI.getInstance().graphModel.setChannelColors(channelColors);
		ClientUI.getInstance().graphModel.setLegendNames(legendNames);
		double lastX = -1;
		ArrayList<ArrayList<CoordinatesModel>> graphData = ClientUI.getInstance().graphModel.getGraphData();
		if (!graphData.isEmpty()) {
			ArrayList<CoordinatesModel> lastModels = graphData.get(graphData.size() - 1);
			CoordinatesModel lastModel = lastModels.get(lastModels.size() - 1);
			lastX = lastModel.getXCoordinate();
		}

		ArrayList<CoordinatesModel> coordinatesList = new ArrayList<>();
		for (int i = 1; i < values.length; i++)
			coordinatesList.add(new CoordinatesModel(lastX + 1, Double.parseDouble(values[i])));

		graphData.add(coordinatesList);
		if (graphData.size() > 50)
			graphData.remove(0);

		ClientUI.getInstance().graphModel.setXLength(50);
		ClientUI.getInstance().graphModel.setGraphData(graphData);
		ClientUI.getInstance().graph.updateGraphView(ClientUI.getInstance().graphModel);
	}

}
