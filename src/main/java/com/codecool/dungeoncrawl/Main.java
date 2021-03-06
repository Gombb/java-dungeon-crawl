    package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameSave;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.items.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;


import java.io.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.codecool.dungeoncrawl.logic.GameMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javafx.scene.text.Text;


public class Main extends Application {

    GameMap map = MapLoader.loadMap(1, null);
    Move move = new Move(map);
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Stage primaryStage;
    GameDatabaseManager dbManager;

    HashMap<String, Button> buttonCollection;
    HashMap<String, ButtonType> buttonTypesCollection;
    HashMap<String, Label> labelCollection;
    HashMap<String, Alert> alertCollection;

    TextInputDialog enterName = new TextInputDialog();
    TextField rightPaneInputField = new TextField();
    FileChooser fileChooser = new FileChooser();



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupDbManager();
        this.primaryStage = primaryStage;
        initButtonCollections();
        initLabelCollection();
        initAlertCollection();
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(canvas);
        borderPane.setRight(initUI());
        Scene scene = new Scene(borderPane);
        onGameStart(primaryStage);
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);
        primaryStage.setScene(scene);
        refresh();
        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }

    private void initAlertCollection(){
        this.alertCollection = new HashMap<>();
        Alert inventory = new Alert(Alert.AlertType.INFORMATION);
        inventory.setHeaderText(null);
        inventory.setTitle("Inventory");
        alertCollection.put("inventory", inventory);
        Alert gameOver = new Alert(Alert.AlertType.WARNING);
        gameOver.setHeaderText("WASTED");
        gameOver.setTitle("GameOver");
        alertCollection.put("gameOver", gameOver);
        Alert wrongFileType = new Alert(Alert.AlertType.CONFIRMATION);
        wrongFileType.setHeaderText(null);
        wrongFileType.setTitle("Incorrect file type");
        wrongFileType.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        wrongFileType.setContentText("IMPORT ERROR! Unfortunately the given file is in wrong format. Please try another one!");
        alertCollection.put("wrongFileType", wrongFileType);
        Alert gameStart = new Alert(Alert.AlertType.CONFIRMATION);
        gameStart.getButtonTypes().setAll(buttonTypesCollection.get("newGameBtn"), buttonTypesCollection.get("loadGameBtn"));
        gameStart.setHeaderText(null);
        gameStart.setGraphic(null);
        alertCollection.put("gameStart", gameStart);
    }

    private void initLabelCollection(){
        this.labelCollection = new HashMap<>();
        labelCollection.put("attackLabel", new Label());
        labelCollection.put("defenseLabel", new Label());
        labelCollection.put("healthLabel", new Label());
    }

    private void initButtonCollections() {
        this.buttonCollection = new HashMap<>();
        this.buttonTypesCollection = new HashMap<>();
        buttonCollection.put("pickUpBtn", new Button("Loot"));
        buttonCollection.put("importGameBtn", new Button("Import"));
        buttonCollection.put("exportGameBtn", new Button("Export"));
        buttonTypesCollection.put("newGameBtn", new ButtonType("New Game"));
        buttonTypesCollection.put("loadGameBtn", new ButtonType("Load Game"));
        buttonCollection.get("importGameBtn").setOnAction(e -> {
            try {
                importGameState(primaryStage);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }});

        buttonCollection.get("exportGameBtn").setOnAction(e -> {
            try {
                exportGame();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }


    private GridPane initUI() {
        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));
        ui.add(buttonCollection.get("pickUpBtn"), 0, 0);
        ui.add(rightPaneInputField, 0, 1);
        ui.add(new Label("Health: "), 0, 2);
        ui.add(labelCollection.get("healthLabel"), 1, 2);
        ui.add(new Label("Attack: "), 0, 3);
        ui.add(labelCollection.get("attackLabel"), 1, 3);
        ui.add(new Label("Defense: "), 0, 4);
        ui.add(labelCollection.get("defenseLabel"), 1, 4);
        ui.add(buttonCollection.get("importGameBtn"), 1, 5);
        ui.add(buttonCollection.get("exportGameBtn"), 0, 5);
        ui.setStyle("-fx-background-color: #f26252;");
        enterName.setContentText("Please enter your name: ");
        enterName.setTitle("Welcome to dungeon crawler!");
        enterName.setHeaderText(null);
        enterName.setGraphic(null);
        rightPaneInputField.setMaxWidth(100);

        return ui;
    }

    private void exportGameMapToTxt() throws FileNotFoundException {
        File mapDir = new File(System.getProperty("user.home"), ".dungeon_crawler/maps");
        File newMap = new File(System.getProperty("user.home"), ".dungeon_crawler/maps/" + map.getPlayer().getCharacterName() + ".txt");
        if (!mapDir.exists()) {
            boolean wasFileCreated = mapDir.mkdirs();
            if (wasFileCreated) {
                System.out.println("Maps directory created at home/.dungeon_crawler");
            }
        }
        PrintWriter printWriter = new PrintWriter(newMap);
        String currentMap = map.getCurrentMap();
        for (int i = 0; i < currentMap.length(); i++) {
            printWriter.print(currentMap.charAt(i));
        }
        printWriter.close();
    }

    private void exportGame() throws IOException {
      
        fileChooser.setInitialFileName(map.getPlayer().getCharacterName() + ".json");
        File jsonDir = new File(System.getProperty("user.home"), ".dungeon_crawler/json");
        if (!jsonDir.exists()) {
            boolean wasFileCreated = jsonDir.mkdirs();
            if (wasFileCreated) {
                System.out.println("Json directory created at home/.dungeon_crawler");
            }
        }
        fileChooser.setInitialDirectory(jsonDir);
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            saveNewGame("new save");
            FileWriter writer = new FileWriter(file.getAbsolutePath());
            Gson gson = new GsonBuilder().create();
            gson.toJson(new GameState(map.getCurrentMap(), new Date(System.currentTimeMillis()), new PlayerModel(map.getPlayer())), writer);
            writer.flush();
            writer.close();
            exportGameMapToTxt();
        }
    }

    private void saveNewGame(String title){
        PlayerModel playerModel = new PlayerModel(map.getPlayer());
        dbManager.updatePlayer(playerModel);
        GameState gameState = dbManager.saveGameState(map.getCurrentMap(), playerModel);
        dbManager.addToGameSaves(title, playerModel, gameState);
    }

    private void saveNewPlayer() {
        dbManager.savePlayer(map.getPlayer());
        map.getPlayer().setId(dbManager.getHighestPlayerId());
    }

    private void onGameStart(Stage primaryStage) {
        boolean gameLoaded = false;
        while (!gameLoaded) {
            Optional<ButtonType> startResult = alertCollection.get("gameStart").showAndWait();
            if (startResult.isPresent() && startResult.get() == buttonTypesCollection.get("newGameBtn")){
                Optional<String> nameResult = enterName.showAndWait();
                if (nameResult.isPresent()){
                    map.getPlayer().setCharacterName(nameResult.get());
                    saveNewPlayer();
                    primaryStage.show();
                    gameLoaded = true;
                }
            } else if (startResult.isPresent() && startResult.get() == buttonTypesCollection.get("loadGameBtn")){
                loadGameDialog();
                gameLoaded = true;
            }
        }
    }

    private void initLoadGame(String title, ChoiceDialog<String> choiceDialog) {
        int saveId = dbManager.getGameSavesIdForTitle(title);
        GameSave gameSave = dbManager.getGameSaveForId(saveId);
        PlayerModel playerModel = dbManager.getPlayerModelForId(gameSave.getPlayerId());
        GameState gameState = dbManager.getGameStateModelForId(gameSave.getGameStateId());
        String mapToBeLoaded = gameState.getCurrentMap();
        this.map = MapLoader.loadMap(1, mapToBeLoaded);
        choiceDialog.close();
        alertCollection.get("gameStart").close();
        Player newPlayer = new Player(map.getPlayer().getCell(), playerModel.getHp(), playerModel.getAttack(), playerModel.getDefense());
        map.setPlayer(newPlayer);
        this.move = new Move(map);
    }

    private void loadGameDialog() {
        List <String> saveTitles = dbManager.getSaveTitles();
        ChoiceDialog <String> choiceDialog = new ChoiceDialog <>(saveTitles.get(1), saveTitles);
        choiceDialog.setContentText("Choose a previously saved game");
        choiceDialog.showAndWait().ifPresent(type -> {
            initLoadGame(type.toString(), choiceDialog);
        });

    }

    private void importGameState(Stage primaryStage) throws IOException {
        Alert wrongFileType = alertCollection.get("wrongFileType");
        Gson gson = new Gson();
        boolean returnToGame = false;
        File jsonDir = new File(System.getProperty("user.home"), ".dungeon_crawler/json");
        while (!returnToGame) {
            fileChooser.setInitialDirectory(jsonDir);
            File file = fileChooser.showOpenDialog(primaryStage);
            System.out.println(file.toPath().toString());
            System.out.println(Files.probeContentType(file.toPath()));

            if (file.toString().toLowerCase().endsWith(".json")) {
                GameState gameState = gson.fromJson(new FileReader(file), GameState.class);
                System.out.println(gameState.getCurrentMap());
                returnToGame = true;
                primaryStage.show();
            } else {
                Optional<ButtonType> result = wrongFileType.showAndWait();
                if (result.get().getText().equals("OK")) {
                    wrongFileType.close();
                } else {
                    returnToGame = true;
                    wrongFileType.close();
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
        saveButton.setOnAction(event -> saveOverWriteAlert(saveInput.getText(), dialog));
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


    private boolean checkIfTitleExists(String savesTitle){
        List<String> allTitles = dbManager.getSaveTitles();
        for (String title : allTitles){
            if (title.equals(savesTitle)) return true;
        }
        return false;
    }

    private void saveOverWriteAlert(String savesTitle, Stage modal){
        boolean overWrite = checkIfTitleExists(savesTitle);
        if (overWrite){
            Alert overWriteAlert = new Alert(Alert.AlertType.WARNING);
            overWriteAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            overWriteAlert.setContentText("Would you like to overwrite  the already existing state?");
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.OK_DONE);
            overWriteAlert.getButtonTypes().setAll(yesButton, noButton);
            overWriteAlert.showAndWait().ifPresent(type -> {
                if (type == yesButton) {
                    saveNewGame(savesTitle);
                }else{
                    overWriteAlert.close();
                }
            });
        }else{
            saveNewGame(savesTitle);
            modal.close();
        }
    }

    private String getUserInput(TextField textField, Canvas canvas) {
        String userInput = textField.getText();
        rightPaneInputField.clear();
        canvas.requestFocus();
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
        this.map = MapLoader.loadMap(level, null);
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
                alertCollection.get("inventorry").show();
                refresh();
                break;
        }
        buttonCollection.get("pickUpBtn").setOnAction(event -> onBtnPress(map.getPlayer()));
        rightPaneInputField.setOnAction(event -> {
            map.getPlayer().processCheatCode(getUserInput(rightPaneInputField, canvas));
            refresh();}
            );
        if (map.getPlayer().getHealth() < 1){
            gameOver();
        }
    }

    private void gameOver(){
        alertCollection.get("gameOver").show();
        loadNextLevel(1);
    }

    private boolean isItUnlockedDoor(Directions direction){
        Cell neighbourCell = map.getPlayer().getCell().getNeighbor(direction.getCordX(), direction.getCordY());
        return neighbourCell.getType() == CellType.GATE && map.getPlayer().isHasKey();
    }

    private void refresh() {
        System.out.println();
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
        labelCollection.get("healthLabel").setText("" + map.getPlayer().getHealth());
        labelCollection.get("attackLabel").setText("" + map.getPlayer().getAttack());
        labelCollection.get("defenseLabel").setText("" + map.getPlayer().getDefense());
        alertCollection.get("inventory").setContentText(map.getPlayer().displayInventory());
    }

    private void setupDbManager() {
        dbManager = new GameDatabaseManager();
        try {
            dbManager.setup();
        } catch (SQLException ex) {
            System.out.println("Cannot connect to database.");
        }
    }
    
    private void exit() {
        try {
            stop();
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
