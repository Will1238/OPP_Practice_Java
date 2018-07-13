/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Will
 */
public class GameWindow extends JFrame{
    private DrawRect drawRect;
    private ProduceMaze produceMaze;
    private ControlWindow controlWindow;
    private boolean changeMazeEnable;
    
    public GameWindow(ProduceMaze produceMaze){
        setSize(682, 704);
        setLocation(225, 225);
        setTitle("迷宫-界面");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        this.produceMaze = produceMaze;
        changeMazeEnable = false;
        
        drawRect = new DrawRect(produceMaze);
        drawRect.drawShortestPath = false;
        add(drawRect); 
        
        addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == e.BUTTON1){
                    if(changeMazeEnable){
                        int x = e.getX()/((int)DrawRect.RECTANGLE_SIDE_LENGTH);

                        //Y坐标莫名多出一个单位（25），不确定是否是设备问题，暂且-1
                        int y = e.getY()/((int)DrawRect.RECTANGLE_SIDE_LENGTH) -1;

                        //改变迷宫地图上坐标为（x，y）的点的属性
                        produceMaze.changeMaze(x, y);
                        drawRect.repaint();                        
                    }

                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
               
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
            
        });
        addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
               if(e.getKeyChar() == 'w' || e.getKeyChar() == 'W'){
                   if(produceMaze.currentPathMove(0)){
                       produceMaze.stepForward();
                   }
                   
               }
               if(e.getKeyChar() == 'd' || e.getKeyChar() == 'W'){
                   if(produceMaze.currentPathMove(1)){
                       produceMaze.stepForward();
                   }     
               }
               if(e.getKeyChar() == 's' || e.getKeyChar() == 'S'){
                   if(produceMaze.currentPathMove(2)){
                       produceMaze.stepForward();
                   }               
               }
               if(e.getKeyChar() == 'a' || e.getKeyChar() == 'A'){
                   if(produceMaze.currentPathMove(3)){
                       produceMaze.stepForward();
                   }       
               }
               drawRect.repaint();
               controlWindow.setStepsText(produceMaze.getSteps());               
               if(produceMaze.getMazeFinsh()){
                   JOptionPane.showMessageDialog(null, "完成！" , "恭喜", JOptionPane.INFORMATION_MESSAGE);
               }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                 
            }
            
        });
    }
    
    public void setChangeMazeEnable(){
        changeMazeEnable = !changeMazeEnable;
    }
    
    public void setControlWindow(ControlWindow controlWindow){
        this.controlWindow = controlWindow;
    }
    
    /**
     * 用于控制窗口通过上下左右按钮移动，同时会返回当前步数
     * @param move 要移动的方向，0,1,2,3分别表示向上右下左位移
     * @return 当前移动步数
     */
    public int currentPathMove_Repaint(int move){
        if(produceMaze.currentPathMove(move)){
            drawRect.repaint();
            produceMaze.stepForward();
        }
        
        
        return produceMaze.getSteps();
    }
    
    public void showShortestPath(int sX, int sY, int fX, int fY){
        produceMaze.setStartFinishPosition(sX, sY, fX, fY);
        produceMaze.produceShortestPath();
        drawRect.drawShortestPath = true;
        drawRect.repaint();
    }
    
    public void actionBeforeRefresh(){
        drawRect.drawShortestPath = false;
        drawRect.repaint();
    }
    
}

class DrawRect extends JComponent{
    
    final static double RECTANGLE_SIDE_LENGTH = 25.0;      //迷宫主体的一边的方块数&&小方块边长
    final double POSITION_X = 0.0;      //迷宫外墙的绘制起点
    final double POSITION_Y = 0.0;
    
    private final ProduceMaze produceMaze;
    private int[][] mazeArray;
    private int[][] shortestPathArray;
    private int[][] currentPathArray;
    public boolean drawShortestPath;
    
    public DrawRect(ProduceMaze produceMaze){
        super();
        this.produceMaze = produceMaze;
    }
    
    @Override
    public void paintComponent(Graphics g){
        Graphics2D drawRec = (Graphics2D)g;
        this.mazeArray = produceMaze.getMaze();
        
        //绘制外部粉色围墙
        drawRec.setColor(Color.PINK);  
        drawRec.fill(new Rectangle2D.Double(POSITION_X, POSITION_Y, RECTANGLE_SIDE_LENGTH * 27, RECTANGLE_SIDE_LENGTH));
        drawRec.fill(new Rectangle2D.Double(POSITION_X, POSITION_Y + RECTANGLE_SIDE_LENGTH * 26, RECTANGLE_SIDE_LENGTH * 27, RECTANGLE_SIDE_LENGTH));
        drawRec.fill(new Rectangle2D.Double(POSITION_X, POSITION_Y + RECTANGLE_SIDE_LENGTH * 1, RECTANGLE_SIDE_LENGTH, RECTANGLE_SIDE_LENGTH * 25));
        drawRec.fill(new Rectangle2D.Double(POSITION_X + RECTANGLE_SIDE_LENGTH * 26, POSITION_Y + RECTANGLE_SIDE_LENGTH * 1, RECTANGLE_SIDE_LENGTH, RECTANGLE_SIDE_LENGTH * 25));
        
        //绘制迷宫主体
        drawRec.setColor(Color.ORANGE);
        for(int i = 0; i < RECTANGLE_SIDE_LENGTH; i++){
            for(int j = 0; j < RECTANGLE_SIDE_LENGTH; j++){
                if(mazeArray[j][i] == 1){
                    drawRec.fill(new Rectangle2D.Double(POSITION_X + RECTANGLE_SIDE_LENGTH * (i + 1), 
                    POSITION_Y + RECTANGLE_SIDE_LENGTH * (j + 1), RECTANGLE_SIDE_LENGTH, RECTANGLE_SIDE_LENGTH));
                }
            }
        }
        
        //绘制最短路径或当前路径
        if(drawShortestPath){
            //设置最短路径
            this.shortestPathArray = produceMaze.getShortestPath();
            drawRec.setColor(Color.BLUE);
            for(int i = 0; i < RECTANGLE_SIDE_LENGTH; i++){
                for(int j = 0; j < RECTANGLE_SIDE_LENGTH; j++){
                    if(shortestPathArray[j][i] == 1){
                        drawRec.fill(new Rectangle2D.Double(POSITION_X + RECTANGLE_SIDE_LENGTH * (i + 1), 
                        POSITION_Y + RECTANGLE_SIDE_LENGTH * (j + 1), RECTANGLE_SIDE_LENGTH, RECTANGLE_SIDE_LENGTH));
                    }
                }
            }            
        } else {
            this.currentPathArray = produceMaze.getCurrentPath();
            drawRec.setColor(Color.BLUE);
            for(int i = 0; i < RECTANGLE_SIDE_LENGTH; i++){
                for(int j = 0; j < RECTANGLE_SIDE_LENGTH; j++){
                    if(currentPathArray[j][i] == 1){
                        drawRec.fill(new Rectangle2D.Double(POSITION_X + RECTANGLE_SIDE_LENGTH * (i + 1), 
                        POSITION_Y + RECTANGLE_SIDE_LENGTH * (j + 1), RECTANGLE_SIDE_LENGTH, RECTANGLE_SIDE_LENGTH));
                    }
                }
            }            
        }

    }
     
}
