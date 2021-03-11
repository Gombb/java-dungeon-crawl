package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.*;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.Cell;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;


public class Main extends Application {
    GameMap map = MapLoader.loadMap(1);
    Move move = new Move(map);
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Button pickUpBtn = new Button("Loot");
    Label healthLabel = new Label();
    Label attackLabel = new Label();
    Label defenseLabel = new Label();
    Text combatLog = new Text();
    Alert inventory = new Alert(Alert.AlertType.INFORMATION);
<<<<<<< HEAD
    Alert gameOver = new Alert(Alert.AlertType.WARNING);
=======
    TextField console = new TextField();
>>>>>>> 9976cca839ac97a5d0e2ee273556be7d4ad7883b


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane ui = new GridPane();

        inventory.setHeaderText(null);
        inventory.setTitle("Inventory");
        gameOver.setHeaderText("WASTED");
        gameOver.setTitle("GameOver");

        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        ui.add(pickUpBtn, 0, 0);
        console.setMaxWidth(100);
        ui.add(console, 0, 1);

        ui.add(new Label("Health: "), 0, 2);
        ui.add(healthLabel, 1, 2);

        ui.add(new Label("Attack: "), 0, 3);
        ui.add(attackLabel, 1, 3);

        ui.add(new Label("Defense: "), 0, 4);
        ui.add(defenseLabel, 1, 4);
        ui.add(combatLog, 0, 5);
        ui.setStyle("-fx-background-color: #f26252;");

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);
        pickUpBtn.setOnAction(event -> onBtnPress(map.getPlayer()));
        console.setOnAction(event -> {
            map.getPlayer().processCheatCode(getUserInput(console, canvas));
            refresh();}
            );
        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();

    }

    private String getUserInput(TextField textField, Canvas canvas) {
        String userInput = textField.getText();
        console.clear();
        canvas.requestFocus();
        System.out.println(userInput);
        return userInput;

    }

    private void onBtnPress(Player player) {
        Item item = map.getPlayer().getCell().getItem();
        if (item != null) {
            player.lootItem(item);
            map.getPlayer().getCell().setItem(null);
            refresh();
        }
    }
    public void loadNextLevel(int level){
        this.map = MapLoader.loadMap(level);
        this.move = new Move(this.map);
        refresh();
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case W:
                move.initRound(Directions.North);
                if (isItUnlockedDoor(Directions.North)) loadNextLevel(2);
                refresh();
                break;
            case S:
                move.initRound(Directions.South);
                if (isItUnlockedDoor(Directions.South)) loadNextLevel(2);
                refresh();
                break;
            case A:
                move.initRound(Directions.West);
                if (isItUnlockedDoor(Directions.West)) loadNextLevel(2);
                refresh();
                break;
            case D:
                move.initRound(Directions.East);
                if (isItUnlockedDoor(Directions.East)) loadNextLevel(2);
                refresh();
                break;
            case I:
                inventory.show();
                refresh();
                break;
        }
        if (map.getPlayer().getHealth() < 1){
            gameOver();
        }
    }

    private void gameOver(){
        gameOver.show();
        loadNextLevel(1);
    }

    private boolean isItUnlockedDoor(Directions direction){
        Cell neighbourCell = map.getPlayer().getCell().getNeighbor(direction.getCordX(), direction.getCordY());
        return neighbourCell.getType() == CellType.GATE && map.getPlayer().isHasKey();
    }

    private void refresh() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
        attackLabel.setText("" + map.getPlayer().getAttack());
        defenseLabel.setText("" + map.getPlayer().getDefense());
        inventory.setContentText(map.getPlayer().displayInventory());
    }
}
