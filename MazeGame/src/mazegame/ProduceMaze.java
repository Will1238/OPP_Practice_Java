/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazegame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Will
 * @version 1.0
 */
public class ProduceMaze {
    /**
     * 迷宫分为迷宫外墙和迷宫主体，本部分只负责生成迷宫主体
     * 以下迷宫主体数组简称迷宫数组
     * MAZE_SIDE_LENGTH: 迷宫主体边长
     * movePosition[0] = up, 1 = right, 2 = down, 3 = left
     * startPoint: 迷宫起点
     * finishPoint: 迷宫终点
     * currentPoint: 玩家当前所在点
     * mazeArray: 1 = Wall, 0 = No Wall
     * shortestPathArray: 1 = shotestPath
     * currentPathArray: 1 = currentPath
     */
    public final static int MAZE_SIDE_LENGTH = 25;
    public final int movePosition[][] = {{0, -2}, {2, 0}, {0, 2}, {-2, 0}};
    private Point startPoint;
    private Point finishPoint;
    private Point currentPoint;
    private int steps;
    private int[][] mazeArray;
    private int[][] shortestPathArray;
    private int[][] currentPathArray;
    
    public ProduceMaze(){
        this(0, 0, 24, 24);
    }
    
    /**
     * 标准构造方法
     * @param startX 起点横坐标
     * @param startY 起点纵坐标
     * @param finishX 终点横坐标
     * @param finishY 终点纵坐标
     */
    public ProduceMaze(int startX, int startY, int finishX, int finishY){
        startPoint = new Point(startX, startY);
        finishPoint = new Point(finishX, finishY);
        currentPoint = new Point(startX, startY);
        currentPathArray = new int[MAZE_SIDE_LENGTH][MAZE_SIDE_LENGTH];
        for(int[] i: currentPathArray){
            for(int j: i){
                j = 0;
            }
        }
        currentPathArray[currentPoint.getY()][currentPoint.getX()] = 1;
        
        produceMazeByPrim();
        produceShortestPath();
    }
    
    /**
     * 基于Prim算法随机生成迷宫
     * @date 2018/7/3
     */
    private void produceMazeByPrim(){
        //初始化迷宫主体数组
        mazeArray = new int[MAZE_SIDE_LENGTH][MAZE_SIDE_LENGTH];
        for(int i = 0; i < MAZE_SIDE_LENGTH; i++){
            for(int j = 0; j < MAZE_SIDE_LENGTH; j++){
                if((i % 2 == 0) && (j % 2 == 0)){
                    mazeArray[i][j] = 0;
                }else{
                    mazeArray[i][j] = 1;
                }
            }
        }
        
        ArrayList<Point> visitedPoints = new ArrayList<Point>();
        Point nowPoint = new Point((int)(Math.random() * (MAZE_SIDE_LENGTH/2 +1)) * 2, (int)(Math.random() * (MAZE_SIDE_LENGTH/2 +1)) * 2);
        visitedPoints.add(nowPoint);
       
        //Prim算法结束条件：所有点都已被访问
        while(visitedPoints.size() < (MAZE_SIDE_LENGTH/2 + 1 ) * (MAZE_SIDE_LENGTH/2 + 1)){
            //判断当前点的四个方向上是否存在可访问的未访问点，如果没有，则随机从已访问列表中取一个新的点
            boolean isCompletelyVisited = true;
            for(int i=0; i < 4; i++){
                if(movePositionTest(nowPoint, i)){
                    if(!visitedTest(visitedPoints, nowPoint, i)){
                        isCompletelyVisited = false;
                        break;                        
                    }
                }
            }            
            if(isCompletelyVisited){        
                nowPoint = visitedPoints.get((int)(Math.random() * visitedPoints.size()));
                continue;
            }
            
            
            while(true){
                //随机得到当前点四个方向上一个可访问的方向moveRandom
                int moveRandom = (int)(Math.random()*4);      
                while(!movePositionTest(nowPoint, moveRandom))      
                    moveRandom = (int)(Math.random()*4);
                
                /*
                如果当前点的moveRandom方向上的点未被访问，则将当前点和其moveRandom方向上的点之前的墙打通，即将其数组值置为0
                然后将当前点的moveRandom方向上的点设置为当前访问点并加入已访问点数组中
                */
                if(!visitedTest(visitedPoints, nowPoint, moveRandom)){
                    mazeArray[nowPoint.getY() + movePosition[moveRandom][1]/2][nowPoint.getX() + movePosition[moveRandom][0]/2] = 0;
                    nowPoint = new Point(nowPoint.getX() + movePosition[moveRandom][0], nowPoint.getY() + movePosition[moveRandom][1]);
                    visitedPoints.add(nowPoint);   
                    break;
                } 
            }    
        }    
    }
    
    /**
     * 检测当前点的move方向是否存在可到达点（防止迷宫数组访问越界） 
     * @param nowPoint 当前所在点
     * @param move the direction to move
     * @return whether the direction to move is legal
     */
    private boolean movePositionTest(Point nowPoint, int move){
        if((nowPoint.getX() + movePosition[move][0]) >= 0 
                && (nowPoint.getX() + movePosition[move][0]) < MAZE_SIDE_LENGTH 
                && (nowPoint.getY() + movePosition[move][1]) >= 0
                && (nowPoint.getY() + movePosition[move][1]) < MAZE_SIDE_LENGTH) {
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Judge whether the direction of nowPoint has been accessed
     * @param visitedPoints 存储已访问点的容器
     * @param nowPoint 当前所在的点
     * @param move 即将移动的方向
     * @return 是否当前点的move方向被访问过，被访问过返回true，未访问false
     */
    private boolean visitedTest(ArrayList<Point> visitedPoints, Point nowPoint, int move){
        for(int i = 0; i < visitedPoints.size(); i++){
            if((nowPoint.getX() + movePosition[move][0]) == visitedPoints.get(i).getX()
                    && (nowPoint.getY() + movePosition[move][1]) == visitedPoints.get(i).getY()){
                return true;
            }
        }
        return false;
    }
    
    /**
     * 基于BFS算法找出迷宫最短路径
     */
    public void produceShortestPath(){
        Queue<Point> toVisitPoints = new LinkedList<Point>(); 
        ArrayList<Point> visitedPoints = new ArrayList<Point>();

        //初始化用于存储最终生成的最短路径的数组
        shortestPathArray = new int[MAZE_SIDE_LENGTH][MAZE_SIDE_LENGTH];
        for(int[] i: shortestPathArray){
            for(int j: i){
                j = 0;
            }
        }
        
        //用于生成最短路径的中间数组，需要初始化，主要记录信息有：1.当前点是否被访问过 2.当前点的回溯点（从哪个点走到这个点）
        Point[][] shortestPath = new Point[MAZE_SIDE_LENGTH][MAZE_SIDE_LENGTH];
        for(int i = 0; i < MAZE_SIDE_LENGTH; i++){
            for(int j = 0; j < MAZE_SIDE_LENGTH; j++)
                shortestPath[i][j] = new Point();
        }
        
        toVisitPoints.offer(shortestPath[startPoint.getX()][startPoint.getY()]);
        
        while(!toVisitPoints.isEmpty()){
            //返回并移除队列头部元素
            Point currentPoint = toVisitPoints.poll();
            
            currentPoint.setVisitedCondition(true);
            
            if(currentPoint.getX() == finishPoint.getX() && currentPoint.getY() == finishPoint.getY()){
                break;
            }
            
            //将当前点四个方向上可以到达（不是墙&&不越界）的点入队
            for(int i = 0; i < 4; i++){
                int nextX = currentPoint.getX() + movePosition[i][0]/2;
                int nextY = currentPoint.getY() + movePosition[i][1]/2;
               
                if(nextX < 0 || nextX >= MAZE_SIDE_LENGTH || nextY < 0 || nextY >= MAZE_SIDE_LENGTH){
                    continue;
                }
                
                if(mazeArray[nextY][nextX] != 1 && shortestPath[nextY][nextX].getVisitedCondition() == false){
                    //设置回溯点（currentPoint即下一个点的回溯点）
                    shortestPath[nextY][nextX].setPosition(currentPoint.getX(), currentPoint.getY());
                    shortestPath[nextY][nextX].setVisitedCondition(true);
                    toVisitPoints.offer(new Point(nextX, nextY));
                }
            }
        }
        
        //通过回溯得到最短路径存入shortestPathArray中
        int backX = finishPoint.getX(), backY = finishPoint.getY();
        while(backX != startPoint.getX() || backY != startPoint.getY()){
            shortestPathArray[backY][backX] = 1;
            
            /*
            必须添加两个中间变量tempBackX和tempBackY而不能用backX和backY直接赋值，
            因为直接赋值时第一个赋值过程改变了backX，会导致第二步的赋值错误。
            */
            int tempBackX = shortestPath[backY][backX].getX();
            int tempBackY = shortestPath[backY][backX].getY();
            backX = tempBackX;
            backY = tempBackY;
        }
        shortestPathArray[startPoint.getY()][startPoint.getX()] = 1;
    }
    
    /**
     * 玩家路径移动的实现
     * @param move 即将移动的方向，0为上，1为右，2为下，3为左
     * @return 步进成功返回true，否则返回false
     */
    public boolean currentPathMove(int move){
        int nextX = currentPoint.getX() + movePosition[move][0]/2;
        int nextY = currentPoint.getY() + movePosition[move][1]/2;
        //检测要移动的位置是否合理（是否越界）
        if(nextX >=0 && nextX < MAZE_SIDE_LENGTH && nextY >=0 && nextY < MAZE_SIDE_LENGTH){
            //检测要移动的位置是否是墙
            if(mazeArray[nextY][nextX] != 1){
                //检测要走的这一次位移是前进还回退并进行不同的处理
                if(currentPathArray[nextY][nextX] == 1){
                    //回退
                    currentPathArray[currentPoint.getY()][currentPoint.getX()] = 0;                   
                } else {
                    //前进
                    currentPathArray[nextY][nextX] = 1;
                }
                currentPoint.setPosition(nextX, nextY);
                return true;
            }
        }
        return false;
    }
    
    /**
     * 设置迷宫的起点和终点
     * @param sX 起点横坐标
     * @param sY 起点纵坐标
     * @param fX 终点横坐标
     * @param fY 终点纵坐标
     */
    public void setStartFinishPosition(int sX, int sY, int fX, int fY){
        startPoint.setPosition(sX, sY);
        finishPoint.setPosition(fX, fY);
    }
    
    /**
     * 用于改变迷宫传入点的状态，如果传入点处是空白则变为障碍，如果是障碍则变为空白
     * 主要用于地图绘制界面单击迷宫修改迷宫
     * @param x 要改变状态的点的横坐标
     * @param y 要改变状态的点的纵坐标
     */
    public void changeMaze(int x, int y){
        if(mazeArray[y-1][x-1] == 1){
            mazeArray[y-1][x-1] = 0;
        } else {
            mazeArray[y-1][x-1] = 1;
        }
    }
    
    /**
     * 行动记步
     */
    public void stepForward(){
        this.steps++;
    }
    
    public void saveMaze(String savePath) throws IOException{
        FileWriter out = new FileWriter(savePath);
        BufferedWriter bw = new BufferedWriter(out);

        for(int[] i: mazeArray){
            for(int j: i){
                bw.write("" + j);
                bw.newLine();
            }
        }
        
        bw.close();
        out.close();    
    }
    
    public void loadMaze(String loadPath) throws IOException{
        FileReader in = new FileReader(loadPath);
        BufferedReader br = new BufferedReader(in);
        
        for(int i = 0; i < MAZE_SIDE_LENGTH; i++){
            for(int j = 0; j < MAZE_SIDE_LENGTH; j++){
                mazeArray[i][j] = Integer.parseInt(""+br.readLine().charAt(0));
            }
        }
        br.close();
        in.close();
    }
    
    /**
     * 用以返回生成的迷宫数组
     * @return 迷宫数组
     */
    public int[][] getMaze(){
        return mazeArray;
    }
    
    /**
     * 用以返回寻得的最短路径
     * @return 最短路径数组
     */
    public int[][] getShortestPath(){
        return shortestPathArray;
    }
    
    /**
     * 用以返回玩家当前路径
     * @return 玩家当前路径
     */
    public int[][] getCurrentPath(){
        return currentPathArray;
    }
    
    /**
     * 用以返回玩家当前迷宫完成状态
     * @return 到达终点返回true，反之返回false
     */
    public boolean getMazeFinsh(){
        return(currentPoint.getX() == finishPoint.getX() && currentPoint.getY() == finishPoint.getY());
    }
    
    /**
     * 返回当前行动步数
     * @return 行动步数
     */
    public int getSteps(){
        return steps;
    }
}

class Point{
    private int x;
    private int y;
    private boolean isVisited;
    
    public Point(){
        this(0, 0);
    }
    public Point(int x, int y){
        this.x = x;
        this.y = y;
        isVisited = false;
    }
    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void setVisitedCondition(boolean condition){
        isVisited = condition;
    }
    public boolean getVisitedCondition(){
        return isVisited;
    }
}
