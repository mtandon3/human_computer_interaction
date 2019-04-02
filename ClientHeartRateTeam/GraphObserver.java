package ClientHeartRateTeam;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

public class GraphObserver implements Observer{

	@Override
	public void update(Observable o, Object arg) {
		
		CombinedDataStatefull dataObj = (CombinedDataStatefull) o;
		Queue<String> dataQueue = dataObj.getGlobalQueue();
		System.out.println(dataQueue.size());
		Iterator<String> checkListItor = dataQueue.iterator();
		
		
	}

}
