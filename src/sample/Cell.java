package sample;

import javafx.scene.paint.Color;

import java.util.Random;

public class Cell {

    Color color;
    boolean state;
    double offsetX, offsetY;
    int choice;

    public Cell() {

    }

    public Color getColor() {
        return color;
    }

    public void setCell(Color color){
        this.color = color;
        state = true;
    }

    public Cell(Cell cell) {
        this.color = cell.color;
        this.state = cell.state;
        this.offsetX = cell.offsetX;
        this.offsetY = cell.offsetY;
        this.choice = cell.choice;
    }

    public int getChoice() {
        return choice;
    }

    public void kill(){
        color = Color.WHITE;
        state = false;
        Random r = new Random();
        offsetX = r.nextDouble();
        offsetY = r.nextDouble();
        choice = r.nextInt(3);
    }

    public boolean getState(){
        return this.state;
    }

    public double getOffsetX(){
        return offsetX;
    }

    public double getOffsetY(){
        return offsetY;
    }
}
