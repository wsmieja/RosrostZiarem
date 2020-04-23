package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

public class Main extends Application {

    int canvasSize=600;

    @Override
    public void start(Stage primaryStage) {

        Random r = new Random();
        primaryStage.setTitle("Rozrost ziaren");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label x = new Label("Wymiar X:");
        grid.add(x, 0, 0);
        TextField xTextField = new TextField();
        xTextField.setMinWidth(100);
        xTextField.setMaxWidth(100);
        grid.add(xTextField, 0, 1);
        xTextField.setText("12");

        Label y = new Label("Wymiar Y:");
        grid.add(y, 1, 0);
        TextField yTextField = new TextField();
        yTextField.setMinWidth(100);
        yTextField.setMaxWidth(100);
        grid.add(yTextField, 1, 1);
        yTextField.setText("12");

        Label zarodkowanie = new Label("Zarodkowanie:");
        grid.add(zarodkowanie, 2, 0, 1, 1);
        ChoiceBox zarodkowanieChoiceBox = new ChoiceBox();
        zarodkowanieChoiceBox.setMinWidth(100);
        zarodkowanieChoiceBox.setMaxWidth(100);
        grid.add(zarodkowanieChoiceBox, 2, 1, 1, 1);
        zarodkowanieChoiceBox.getItems().add("");
        zarodkowanieChoiceBox.getItems().add("Jednorodne");
        zarodkowanieChoiceBox.getItems().add("Z promieniem");
        zarodkowanieChoiceBox.getItems().add("Losowe");
        zarodkowanieChoiceBox.setValue("");

        Label warunkiBrzegowe = new Label("Warunki brzegowe:");
        grid.add(warunkiBrzegowe, 3, 0);
        ChoiceBox warunkiBrzegoweChoiceBox = new ChoiceBox();
        warunkiBrzegoweChoiceBox.setMinWidth(100);
        warunkiBrzegoweChoiceBox.setMaxWidth(100);
        grid.add(warunkiBrzegoweChoiceBox, 3, 1);
        warunkiBrzegoweChoiceBox.getItems().add("Periodyczne");
        warunkiBrzegoweChoiceBox.getItems().add("Absorbujące");
        warunkiBrzegoweChoiceBox.setValue("Periodyczne");

        Label sąsiedztwo = new Label("Sąsiedztwo:");
        grid.add(sąsiedztwo, 4, 0);
        ChoiceBox sąsiedztwoChoiceBox = new ChoiceBox();
        sąsiedztwoChoiceBox.setMinWidth(100);
        sąsiedztwoChoiceBox.setMaxWidth(100);
        grid.add(sąsiedztwoChoiceBox,
                4, 1);
        sąsiedztwoChoiceBox.getItems().add("Von Neumanna");
        sąsiedztwoChoiceBox.getItems().add("Moore'a");
        sąsiedztwoChoiceBox.getItems().add("Heksagonalne lewe");
        sąsiedztwoChoiceBox.getItems().add("Heksagonalne prawe");
        sąsiedztwoChoiceBox.getItems().add("Heksagonalne losowe");
        sąsiedztwoChoiceBox.getItems().add("Pentagonalne losowe");
        sąsiedztwoChoiceBox.getItems().add("Z promieniem");
        sąsiedztwoChoiceBox.setValue("Von Neumanna");

        Label l1 = new Label("");
        grid.add(l1, 0, 2);
        TextField l1TextField = new TextField();
        l1TextField.setMinWidth(100);
        l1TextField.setMaxWidth(100);
        grid.add(l1TextField, 0, 3);
        l1TextField.setText("1");

        Label l2 = new Label("");
        grid.add(l2, 1, 2);
        TextField l2TextField = new TextField();
        l2TextField.setMinWidth(100);
        l2TextField.setMaxWidth(100);
        grid.add(l2TextField, 1, 3);
        l2TextField.setText("1");

        Label l3 = new Label("");
        grid.add(l3, 2, 2);
        TextField l3TextField = new TextField();
        l3TextField.setMinWidth(100);
        l3TextField.setMaxWidth(100);
        grid.add(l3TextField, 2, 3);
        l3TextField.setText("1");

        Button zastosuj1 = new Button("Zastosuj");
        zastosuj1.setMinWidth(65);
        zastosuj1.setMinHeight(45);
        grid.add(zastosuj1, 5, 0, 1, 2);

        Button zastosuj2 = new Button("Zastosuj");
        zastosuj2.setMinWidth(65);
        zastosuj2.setMinHeight(45);
        grid.add(zastosuj2, 3, 2, 1, 2);

        Button start = new Button("Start");
        start.setMinWidth(65);
        start.setMaxWidth(65);
        start.setMinHeight(45);
        grid.add(start, 4, 2, 1, 2);

        Button stop = new Button("Stop");
        stop.setMinWidth(65);
        stop.setMaxWidth(65);
        stop.setMinHeight(45);
        grid.add(stop, 5, 2, 1, 2);

        Canvas canvas = new Canvas(canvasSize, canvasSize);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GridPane.setColumnSpan(canvas, 6);

        Label czas = new Label("Czas:");
        grid.add(czas, 0,6,4,1);

        //......................................................

        Timeline play=new Timeline();

        zastosuj1.setOnAction(actionEvent ->  {

            gc.clearRect(0,0,canvasSize,canvasSize);

            play.stop();
            play.getKeyFrames().removeAll(play.getKeyFrames());

            String nucleation = (String) zarodkowanieChoiceBox.getValue();
            String boundaryConditions = (String) warunkiBrzegoweChoiceBox.getValue();
            String neighbourhood = (String) sąsiedztwoChoiceBox.getValue();
            int width=Integer.parseInt(xTextField.getText());
            int height = Integer.parseInt(yTextField.getText());

            Cell[][] gd=new Cell[height][width];
            for(int i =0; i< height; i++){
                for (int j = 0; j < width; j++) {
                    gd[i][j]=new Cell();
                    gd[i][j].kill();
                }
            }

            switch(nucleation){
                case "Jednorodne":
                    l1.setText("Ilość w wierszu");
                    l2.setText("Ilość w kolumnie");
                    break;
                case "Z promieniem":
                    l1.setText("Odległość");
                    l2.setText("Ilość");
                    break;
                case "Losowe":
                    l1.setText("Ilość");
                    l2.setText("");
            }

            if(neighbourhood == "Z promieniem"){
                l3.setText("Promień");}
            else
                l3.setText("");

                draw(gc, czas, gd, width, height);

            canvas.setOnMouseClicked(canvasEvent-> {

                int size;
                if(width<height)
                    size=height;
                else
                    size=width;
                int xValue=(int)((canvasEvent.getX()/canvasSize)*size);
                int yValue=(int)((canvasEvent.getY()/canvasSize)*size);
                if(xValue>=0 && yValue>=0 && xValue<width && yValue<height){
                    double red, green, blue;
                    red=r.nextDouble();
                    green=r.nextDouble();
                    blue=r.nextFloat();
                    gd[yValue][xValue].setCell(Color.color(red, green, blue));
                    draw(gc, czas, gd, width, height);
                }

            });

            zastosuj2.setOnAction(actionEvent1 -> {

                play.stop();
                play.getKeyFrames().removeAll(play.getKeyFrames());

                gc.clearRect(0,0,canvasSize,canvasSize);

                for(int i =0; i< height; i++){
                    for (int j = 0; j < width; j++) {
                        gd[i][j].kill();
                    }
                }

                int X, Y, n, counter;
                switch(nucleation){
                    case "Jednorodne":
                        int nW = Integer.parseInt(l1TextField.getText());
                        if(nW>width)
                            nW=width-1;
                        int nH = Integer.parseInt(l2TextField.getText());
                        if(nH>width)
                            nH=height-1;
                        int dX = (int) Math.floor(width/nW);
                        int dY = (int) Math.floor(height/nH);
                        double red, green, blue;
                        X = (int) Math.floor(dX/2);
                        Y = (int) Math.floor(dY/2);
                        for (int i = 0 ; i < nH; i++) {
                            for (int j = 0; j < nW; j++) {
                                red=r.nextDouble();
                                green=r.nextDouble();
                                blue=r.nextFloat();
                                gd[Y][X].setCell(Color.color(red, green, blue));
                                X+=dX;
                            }
                            Y+=dY;
                            X = (int) Math.floor(dX/2);
                        }
                        break;
                    case "Z promieniem":
                        float minDistance = Float.parseFloat(l1TextField.getText());
                        float distance;
                        float distanceX, distanceY;
                        n = Integer.parseInt(l2TextField.getText());
                        counter = 0;
                        boolean emptySpace=true;
                        while (counter<n) {
                            X = r.nextInt(width - 1);
                            Y = r.nextInt(height - 1);
                            for (int i = (int) (Y - minDistance); i <= (int)(Y + minDistance) ; i++) {
                                for (int j = (int) (X - minDistance); j <= (int)(X + minDistance); j++) {
                                  if((i>=0)&&i<height&&j>=0&&j<width) {

                                      distanceY = (float) ((Y + gd[Y][X].getOffsetY()) - (i + gd[i][j].getOffsetY()));
                                      distanceX = (float) ((X + gd[Y][X].getOffsetX()) - (j + gd[i][j].getOffsetX()));
                                      distance = (float) Math.sqrt(Math.pow(distanceY, 2) + Math.pow(distanceX, 2));
                                      if (distance <= minDistance && gd[i][j].getState() == true)
                                          emptySpace = false;
                                  }
                                }
                            }

                            if(emptySpace){
                                red=r.nextDouble();
                                green=r.nextDouble();
                                blue=r.nextFloat();
                                gd[Y][X].setCell(Color.color(red, green, blue));
                                counter ++;
                            }
                        }
                        break;
                    case "Losowe":
                        if(Integer.parseInt(l1TextField.getText())<((width-1)*(height-1)))
                            n = Integer.parseInt(l1TextField.getText());
                        else
                            n = (width-1)*(height-1);
                        counter = 0;
                        while (counter < n){
                            X = r.nextInt(width - 1);
                            Y = r.nextInt(height - 1);
                            if(!gd[Y][X].getState()){
                                red=r.nextDouble();
                                green=r.nextDouble();
                                blue=r.nextFloat();
                                gd[Y][X].setCell(Color.color(red, green, blue));
                                counter++;
                            }
                        }

                }
                draw(gc, czas, gd, width, height);
            });


            stop.setOnAction(stopEvent ->{

                play.stop();
                play.getKeyFrames().removeAll(play.getKeyFrames());

            });

            start.setOnAction(startEvent -> {

                play.stop();
                play.getKeyFrames().removeAll(play.getKeyFrames());

                Cell[][] temp =new Cell[height][width];
                for (int k = 0; k < 100; k++)
                {

                    KeyFrame kf = new KeyFrame(Duration.seconds(k+1), actionEvent2 -> {
                        for (int i = 0; i < height; i++) {
                            for (int j = 0; j < width; j++) {
                                temp[i][j] = new Cell(gd[i][j]);
                            }
                        }
                        for (int i = 0; i < height ; i++) {
                            for (int j = 0; j < width ; j++) {
                                if(temp[i][j].getState()==false){
                                    int[] ab = new int[4];
                                    ab[0]=i - 1;
                                    ab[1]=i + 1;
                                    ab[2]=j - 1;
                                    ab[3]=j + 1;
                                    for (int l = 0; l < 4; l++) {
                                        if(l<2 && ab[l]>=height)
                                            if(boundaryConditions=="Periodyczne")
                                                ab[l] -= height;
                                            else
                                                ab[l] = height-1;
                                        if(l>1 && ab[l]>=width)
                                            if(boundaryConditions=="Periodyczne")
                                                ab[l] -= width;
                                            else
                                                ab[l] = width-1;
                                        if(l<2 && ab[l]<0)
                                            if(boundaryConditions=="Periodyczne")
                                                ab[l] += height;
                                            else
                                                ab[l] = 0;
                                        if(l>1 && ab[l]<0)
                                            if(boundaryConditions=="Periodyczne")
                                                ab[l] += width;
                                            else
                                                ab[l] = 0;
                                    }
                                    Cell[] neighbour = new Cell[8];
                                    neighbour[0] = temp[ab[0]][j];
                                    neighbour[1] = temp[i][ab[3]];
                                    neighbour[2] = temp[i][ab[2]];
                                    neighbour[3] = temp[ab[1]][j];
                                    neighbour[4] = temp[ab[0]][ab[2]];
                                    neighbour[5] = temp[ab[0]][ab[3]];
                                    neighbour[6] = temp[ab[1]][ab[2]];
                                    neighbour[7] = temp[ab[1]][ab[3]];

                                    Color[] colorSet = new Color[8];
                                    int[] colorPower = new int[8];
                                    for (int l = 0; l < 8; l++) {
                                        colorSet[l] = neighbour[l].getColor();
                                        colorPower[l] = 0;
                                    }

                                    int indexMax = 0;
                                    int valMax = 0;

                                    Color[] colorSet1 = new Color[50];
                                    int counter = 0;
                                    int[] colorPower1 = new int[500];
                                    for (int l = 0; l < 20; l++) {
                                        colorPower1[l] = 0;
                                    }

                                    switch (neighbourhood) {
                                        case "Von Neumanna":

                                            for (int l = 0; l < 4; l++) {
                                                if (!neighbour[l].state)
                                                    continue;
                                                for (int m = 0; m < 4; m++) {
                                                    if (colorSet[l] == colorSet[m])
                                                        colorPower[l]++;
                                                }
                                            }

                                            for (int l = 0; l < 4; l++) {
                                                if (colorPower[l] >= valMax && neighbour[l].state)
                                                    indexMax = l;
                                            }
                                            if(neighbour[indexMax].state)
                                                gd[i][j].setCell(colorSet[indexMax]);

                                            break;

                                        case "Moore'a":

                                            for (int l = 0; l < 8; l++) {
                                                if (!neighbour[l].state)
                                                    continue;
                                                for (int m = 0; m < 8; m++) {
                                                    if (colorSet[l] == colorSet[m])
                                                        colorPower[l]++;
                                                }
                                            }

                                            for (int l = 0; l < 8; l++) {
                                                if (colorPower[l] > valMax && neighbour[l].state)
                                                    indexMax = l;
                                            }
                                            if(neighbour[indexMax].state)
                                                gd[i][j].setCell(colorSet[indexMax]);

                                            break;

                                        case "Heksagonalne lewe":
                                            for (int l = 0; l < 8; l++) {
                                                if (!neighbour[l].state || l==5||l==6)
                                                    continue;
                                                for (int m = 0; m < 8; m++) {
                                                    if (colorSet[l] == colorSet[m])
                                                        colorPower[l]++;
                                                }
                                            }

                                            for (int l = 0; l < 8; l++) {
                                                if (colorPower[l] > valMax&& neighbour[l].state)
                                                    indexMax = l;
                                            }
                                            if(neighbour[indexMax].state)
                                                gd[i][j].setCell(colorSet[indexMax]);

                                            break;
                                        case "Heksagonalne prawe":
                                            for (int l = 0; l < 8; l++) {
                                                if (! neighbour[l].state || l==4||l==7)
                                                    continue;
                                                for (int m = 0; m < 8; m++) {
                                                    if (colorSet[l] == colorSet[m])
                                                        colorPower[l]++;
                                                }
                                            }

                                            for (int l = 0; l < 8; l++) {
                                                if (colorPower[l] > valMax&& neighbour[l].state)
                                                    indexMax = l;
                                            }
                                            if(neighbour[indexMax].state)
                                                gd[i][j].setCell(colorSet[indexMax]);

                                            break;
                                        case "Heksagonalne losowe":
                                            if( temp[i][j].getChoice() < 2){
                                                for (int l = 0; l < 8; l++) {
                                                    if (!neighbour[l].state || l==4||l==7)
                                                        continue;
                                                    for (int m = 0; m < 8; m++) {
                                                        if (colorSet[l] == colorSet[m])
                                                            colorPower[l]++;
                                                    }
                                                }
                                            }
                                            else{
                                                for (int l = 0; l < 8; l++) {
                                                    if (!neighbour[l].state || l==5||l==6)
                                                        continue;
                                                    for (int m = 0; m < 8; m++) {
                                                        if (colorSet[l] == colorSet[m])
                                                            colorPower[l]++;
                                                    }
                                                }
                                            }
                                            for (int l = 0; l < 8; l++) {
                                                if (colorPower[l] > valMax&& neighbour[l].state)
                                                    indexMax = l;
                                            }
                                            if(neighbour[indexMax].state)
                                                gd[i][j].setCell(colorSet[indexMax]);
                                            break;
                                        case "Pentagonalne losowe":
                                            int[] rejected;
                                            rejected = new int[]{0,0,0};
                                            if(temp[i][j].getChoice()==0){
                                                rejected = new int[]{2, 4, 6};
                                            }
                                            else if(temp[i][j].getChoice()==1) {
                                                rejected = new int[]{0, 4, 5};
                                            }
                                            else if(temp[i][j].getChoice()==2){
                                                rejected = new int[]{1, 5, 7};
                                            }
                                            else if(temp[i][j].getChoice()==3){
                                                rejected = new int[]{3, 6, 7};
                                            }


                                                for (int l = 0; l < 8; l++) {
                                                if (! neighbour[l].state || l==rejected[0] || l==rejected[1]
                                                        || l==rejected[2] )
                                                    continue;
                                                for (int m = 0; m < 8; m++) {
                                                    if (colorSet[l] == colorSet[m])
                                                        colorPower[l]++;
                                                }

                                            }

                                            for (int l = 0; l < 8; l++) {
                                                if (colorPower[l] > valMax&& neighbour[l].state)
                                                    indexMax = l;
                                            }
                                            if(neighbour[indexMax].state)
                                                gd[i][j].setCell(colorSet[indexMax]);
                                            break;
                                        case "Z promieniem":
                                            float minDistance = Float.parseFloat(l3TextField.getText());
                                            float distance;
                                            float distanceX, distanceY;



                                            for (int m = (int) (i - minDistance); m <= (int)(i + minDistance) ; m++) {
                                                for (int l = (int) (j - minDistance); l <= (int)(j + minDistance); l++) {

                                                    if((m<0)&&m>=height&&l<0&&l>=width&&!(temp[m][l].getColor() == Color.WHITE)){

                                                    distanceY = (float) ((i) - (m));
                                                    distanceX = (float) ((j) - (l));
                                                    distance = (float) Math.sqrt(Math.pow(distanceY, 2)+ Math.pow(distanceX, 2));
                                                    if(distance<=minDistance){
                                                        colorSet1[counter]=temp[m][l].getColor();
                                                        counter ++;}
                                                    }
                                                }
                                            }
                                            for (int l = 0; l < counter; l++) {
                                                if (colorSet1[l] == Color.WHITE)
                                                    continue;
                                                for (int m = 0; m < counter; m++) {
                                                    if (colorSet1[l] == colorSet1[m])
                                                        colorPower1[l]++;
                                            }
                                            }

                                            for (int l = 0; l < counter; l++) {
                                                if (colorPower1[l] > valMax)
                                                    indexMax = l;
                                                }
                                            if(!(colorSet1[indexMax]==Color.WHITE))
                                                gd[i][j].setCell(colorSet1[indexMax]);
                                            counter = 0;
                                                break;
                                    }
                                }
                            }
                        }
                        draw(gc, czas, gd, width, height);
                    });
                    play.getKeyFrames().add(kf);
                }

                play.play();
            });

        });

        grid.add(canvas, 0, 5);

        Scene scene = new Scene(grid, 650, 800);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    private void draw(GraphicsContext gc, Label l, Cell[][] grid, int width, int height) {
        gc.clearRect(0,0,canvasSize, canvasSize);
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        l.setText("Czas: "+time.toString());
        int a;
        if(width<height)
            a=canvasSize/height;
        else
            a=canvasSize/width;
        for(int i =0; i< height; i++){
            for (int j = 0; j < width; j++) {
                gc.setFill(grid[i][j].color);
                gc.fillRect(j*a, i*a, a, a);
                gc.setFill(Color.BLACK);
                gc.fillRect((j+grid[i][j].getOffsetX())*a, (i+grid[i][j].getOffsetY())*a, 1, 1);

            }
        }

    }
    public static void main(String[] args) {
        launch(args);
    }
}
