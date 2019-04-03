package ClientHeartRateTeam;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to design the Client UI for getting data from different servers
 *
 * @version 20190213
 *
 */


public class ClientUI extends JPanel {

    private static ClientUI instance = null;
    private List<UIElement> simulators = new ArrayList<>();
    public GraphModel graphModel;
    public Graph graph;
    private final JPanel gifPanel = new JPanel();
    
	
    
    String[] colors= {"#0d3d56","#4a6b7c","#0d3d56","#4a6b7c","#0d3d56"};
    // Method used to get instance of the Client UI class
    public static ClientUI getInstance() {
        if (instance == null)
            instance = new ClientUI();

        return instance;
    }

    // Constructor for creating the client UI to collect data from servers
    private ClientUI() {
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(600, 500));
        this.setLayout(new GridLayout(2, 2));

        for(int i=0 ;i<2;i++) {
            UIElement uiElement = new UIElement(new ClientSubscriber("",-1),colors[i]);
            simulators.add(uiElement);
            this.add(uiElement);
        }
        graphModel = new GraphModel();
        graph = new Graph(graphModel);
        this.add(graph);
        graph.initializeView();
        //createTrainImage();
        ImageIcon ii = new ImageIcon(this.getClass().getClassLoader().getResource("neutral.png"));
        	JLabel imageLabel = new JLabel();
        	imageLabel.setIcon(ii);
        	gifPanel.add(imageLabel);
        	this.add(gifPanel);
    }
    
    public  void updateSmileImage(String image) {
		ImageIcon ii = new ImageIcon(this.getClass().getClassLoader().getResource(image));
		JLabel imageLabel = new JLabel();
		imageLabel.setIcon(ii);
		gifPanel.removeAll();
		gifPanel.add(imageLabel);
		this.getParent().revalidate();
		this.getParent().repaint();
	}

    private void createTrainImage() {
        List<List<Double>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("totalData.csv"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                List<Double> list = new ArrayList<>();
                list.add(Double.parseDouble(values[6]));
                list.add(Double.parseDouble(values[7]));
                records.add(list);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        GraphModel model = new GraphModel();
        Graph trainGraph = new Graph(model);
        trainGraph.initializeView();

        model.setNoOfChannels(1);
        Color channelColors[] = new Color[] { Color.RED };
        model.setChannelColors(channelColors);

        ArrayList<ArrayList<CoordinatesModel>> graphData = new ArrayList<>();

        for (List<Double> record: records) {
            ArrayList<CoordinatesModel> coordinatesList = new ArrayList<>();
            coordinatesList.add(new CoordinatesModel(record.get(0), record.get(1)));
            graphData.add(coordinatesList);
        }

        model.setXLength(1);
        model.setGraphData(graphData);
        trainGraph.updateGraphView(model, "trainChart.png");
    }

    // Method called when the application is shut down
    private void shutdown() {
        for (UIElement uiElement: simulators) {
            uiElement.getSubscriber().stop();
            uiElement.getService().shutdown();
            try {
                if (!uiElement.getService().awaitTermination(10, TimeUnit.SECONDS)) {
                    uiElement.getService().shutdownNow();
                }
            } catch (InterruptedException ex) {
                System.out.println("Exception: " + ex);
            }
        }
    }

    // Method to run the Client UI application
    public static void main(String[] args) {
        JFrame frame = new JFrame("Client");
        frame.getContentPane().setLayout(new GridLayout(1, 1));
        frame.setLayout(new GridLayout(1, 1));
        frame.getContentPane().add(ClientUI.getInstance());
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                ClientUI.getInstance().shutdown();
                System.exit(0);
            }
        });
        frame.pack();
        frame.setVisible(true);
    }
}