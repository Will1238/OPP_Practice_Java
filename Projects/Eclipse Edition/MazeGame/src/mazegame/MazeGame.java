/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazegame;

/**
 *
 * @author Will
 */
public class MazeGame {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        final ProduceMaze produceMaze = new ProduceMaze();
        final GameWindow gameWindow = new GameWindow(produceMaze);
        final ControlWindow controlWindow = new ControlWindow(gameWindow, produceMaze);
        controlWindow.setVisible(true);
        gameWindow.setVisible(true);
        gameWindow.setControlWindow(controlWindow);
    }
    
}
