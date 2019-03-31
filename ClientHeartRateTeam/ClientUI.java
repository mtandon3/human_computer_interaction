package ClientHeartRateTeam;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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
    
    String[] colors= {"#0d3d56","#4a6b7c","#0d3d56","#4a6b7c","#0d3d56"};
    // Method used to get instance of the Client UI class
    private static ClientUI getInstance() {
        if (instance == null)
            instance = new ClientUI();

        return instance;
    }

    // Constructor for creating the client UI to collect data from servers
    private ClientUI() {
        this.setBackground(Color.WHITE);
        this.setLayout(new GridLayout(5, 1));

        for(int i=0 ;i<5;i++) {
            UIElement uiElement = new UIElement(new ClientSubscriber("",-1),colors[i]);
            simulators.add(uiElement);
            this.add(uiElement);
        }
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