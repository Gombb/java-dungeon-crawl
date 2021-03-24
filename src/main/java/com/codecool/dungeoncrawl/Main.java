    package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Player;
import javafx.application.Application;
import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.items.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.util.Optional;

import javafx.scene.text.Text;


public class Main extends Application {
    Alert gameStart = new Alert(Alert.AlertType.CONFIRMATION);
    TextInputDialog enterName = new TextInputDialog();

    GameMap map = MapLoader.loadMap(1);
    Move move = new Move(map);
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    GameDatabaseManager dbManager;
    Button pickUpBtn = new Button("Loot");
    Label healthLabel = new Label();
    Label attackLabel = new Label();
    Label defenseLabel = new Label();
    Text combatLog = new Text();
    Alert inventory = new Alert(Alert.AlertType.INFORMATION);
    Alert gameOver = new Alert(Alert.AlertType.WARNING);
    TextField console = new TextField();
  
    ButtonType newGame = new ButtonType("New game");
    ButtonType loadGame = new ButtonType("Load game");
    FileChooser fileChooser = new FileChooser();

    Stage primaryStage;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupDbManager();
        GridPane ui = new GridPane();

        enterName.setContentText("Please enter your name: ");
        enterName.setTitle(null);
        gameStart.setTitle("Welcome to dungeon crawler!");
        gameStart.getButtonTypes().setAll(newGame, loadGame);

        enterName.setHeaderText(null);
        enterName.setGraphic(null);
        gameStart.setHeaderText(null);
        gameStart.setGraphic(null);

        this.primaryStage = primaryStage;
      
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
        scene.setOnKeyReleased(this::onKeyReleased);
        primaryStage.setTitle("Dungeon Crawl");

        onGameStart(primaryStage);
    }

    private void onGameStart(Stage primaryStage) {
        boolean gameLoaded = false;
        while (!gameLoaded) {
            Optional<ButtonType> startResult = gameStart.showAndWait();
            if (startResult.isPresent() && startResult.get() == newGame){
                Optional<String> nameResult = enterName.showAndWait();
                if (nameResult.isPresent()){
                    System.out.println("Your name: " + nameResult.get());
                    primaryStage.show();
                    gameLoaded = true;
                }
            } else if (startResult.isPresent() && startResult.get() == loadGame){
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    primaryStage.show();
                    gameLoaded = true;
                }
            }
        }
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        KeyCombination exitCombinationMac = new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
        KeyCombination exitCombinationWin = new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);
        KeyCombination saveCombination = new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN);
        if (exitCombinationMac.match(keyEvent)
                || exitCombinationWin.match(keyEvent)
                || keyEvent.getCode() == KeyCode.ESCAPE) {
            exit();
        }
        if (saveCombination.match(keyEvent)) saveModal();

    }

    private void saveModal(){
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        TextField saveInput = new TextField();
        Text nameLabel = new Text("Name:");
        saveInput.setPrefSize(150, 25);
        Stage dialog = new Stage();
        GridPane grid = new GridPane();
        Scene saveScene = new Scene(grid, 300, 250, Color.BLACK);
        grid.add(saveInput, 1, 0);
        grid.add(nameLabel, 0, 0);
        grid.add(saveButton, 0, 1);
        grid.add(cancelButton,1, 1);
        saveButton.setOnAction(event -> saveOverWriteAlert());
        cancelButton.setOnAction(event -> dialog.close());
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(50);
        grid.setHgap(5);
        dialog.initOwner(primaryStage);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Save menu");
        dialog.setScene(saveScene);
        dialog.showAndWait();
    }

    private void saveOverWriteAlert(){
        boolean overWrite = true;
        if (overWrite){
            Alert overWriteAlert = new Alert(Alert.AlertType.WARNING);
            overWriteAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            overWriteAlert.setContentText("Would you like to overwrite  the already existing state?");
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.OK_DONE);
            overWriteAlert.getButtonTypes().setAll(yesButton, noButton);
            overWriteAlert.showAndWait().ifPresent(type -> {
                if (type == yesButton) {
                    System.out.println("YESYES");
                }else{
                    System.out.println("NONONO");
                }
            });
        }
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
        pickUpBtn.setOnAction(event -> onBtnPress(map.getPlayer()));
        console.setOnAction(event -> {
            map.getPlayer().processCheatCode(getUserInput(console, canvas));
            refresh();}
            );
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
    }

    private void setupDbManager() {
        dbManager = new GameDatabaseManager();
        try {
            dbManager.setup();
        } catch (SQLException ex) {
            System.out.println("Cannot connect to database.");
        }attackLabel.setText("" + map.getPlayer().getAttack());
        defenseLabel.setText("" + map.getPlayer().getDefense());
        inventory.setContentText(map.getPlayer().displayInventory());
    }
    
    private void exit() {
        try {
            stop();
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);
        attackLabel.setText("" + map.getPlayer().getAttack());
        defenseLabel.setText("" + map.getPlayer().getDefense());
        inventory.setContentText(map.getPlayer().displayInventory());    }
}
