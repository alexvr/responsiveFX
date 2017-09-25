package com.axxes.garagebandprototype;

import com.axxes.garagebandprototype.presenter.Presenter;
import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GaragebandUI {

    @Autowired
    private Presenter presenter;

    private BorderPane borderPane;

    private StackPane rootPane;

    // Window > 600
    private HBox hWrapper;
    // Window < 600
    private VBox vWrapper;

    private Pane beatGridContainer;
    private GridPane beatGrid;

    private Pane instrumentSelection;
    private HBox hSelection;
    private VBox vSelection;

    private GridPane playPauseBpm;
    private Rectangle r1;
    private Rectangle r2;
    private Rectangle r3;

    private Pane snareSelection;
    private Pane hihatSelection;
    private Pane kickSelection;
    private Pane cymbalSelection;

    public GaragebandUI() {
        this.borderPane = new BorderPane();

        // Root pane
        this.rootPane = new StackPane();

        // Horizontal and vertical layout.
        this.hWrapper = new HBox();
        this.hWrapper.setStyle("-fx-background-color: #336699;");
        this.hWrapper.setPadding(new Insets(10, 10, 10, 10));
        this.hWrapper.setSpacing(10);
        this.vWrapper = new VBox();
        this.vWrapper.setStyle("-fx-background-color: #e53d2f;");
        this.vWrapper.setPadding(new Insets(10, 10, 10, 10));
        this.vWrapper.setSpacing(10);

        //menubar
        MenuBar menuBar = new MenuBar();
        menuBar.setLayoutY(0);
        Menu menu = new Menu("File");
        MenuItem menuItemSave = new MenuItem("Save");
        menuItemSave.setOnAction(e -> presenter.menuButtonSave());
        MenuItem menuItemLoad = new MenuItem("Import");
        menuItemLoad.setOnAction(e -> presenter.menuButtonLoad());
        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setOnAction(e -> presenter.exit());
        menu.getItems().add(menuItemSave);
        menu.getItems().add(menuItemLoad);
        menu.getItems().add(menuItemExit);
        menuBar.getMenus().add(menu);


        // Beat grid
        this.beatGridContainer = new Pane();

        this.beatGrid = new GridPane();

        // Instrument selection
        this.instrumentSelection = new Pane();
        this.hSelection = new HBox();
        this.hSelection.setStyle("-fx-background-color: #fffb1d;");
        this.hSelection.setPadding(new Insets(10, 10, 10, 10));
        this.hSelection.setSpacing(10);
        this.vSelection = new VBox();
        this.vSelection.setStyle("-fx-background-color: #70ff40;");
        this.vSelection.setPadding(new Insets(10, 10, 10, 10));
        this.vSelection.setSpacing(10);

        // Play pause bpm
        this.playPauseBpm = new GridPane();
        this.playPauseBpm.setVgap(10);
        this.r1 = new Rectangle();
        this.r1.setFill(Color.BLUE);
        this.r2 = new Rectangle();
        this.r2.setFill(Color.ORANGE);
        this.r3 = new Rectangle();
        this.r3.setFill(Color.BROWN);


        // Instruments
        this.snareSelection = new Pane();
        this.hihatSelection = new Pane();
        this.kickSelection = new Pane();
        this.cymbalSelection = new Pane();

        this.borderPane.setTop(menuBar);
        this.borderPane.setCenter(rootPane);

        changeToLargeLayout();
    }

    public void changeToLargeLayout() {
        this.clearLayout();
        this.initialiseBeatGridLarge();
        this.initialiseInstrumentSelectionLarge();
        this.buildLargeLayout();
    }

    private void clearLayout() {
        this.rootPane.getChildren().clear();
        this.hWrapper.getChildren().clear();
        this.vWrapper.getChildren().clear();
        this.beatGridContainer.getChildren().clear();
        this.instrumentSelection.getChildren().clear();
        this.vSelection.getChildren().clear();
        this.hSelection.getChildren().clear();
        this.playPauseBpm.getChildren().clear();
    }

    private void initialiseBeatGridLarge() {
        this.beatGridContainer.setStyle("-fx-background-color: #fff328;");
        this.beatGridContainer.setPrefWidth(this.rootPane.getWidth() * 0.75);
        this.beatGridContainer.setPrefHeight(this.rootPane.getHeight());
    }

    private void initialiseInstrumentSelectionLarge() {
        // Substract the padding
        double isWidth = (this.rootPane.getWidth() * 0.25) - 35;
        double isHeight = this.rootPane.getHeight() - 90;

        this.instrumentSelection.setStyle("-fx-background-color: #73ff42;");
        this.instrumentSelection.setPrefWidth(this.rootPane.getWidth() * 0.25);
        this.instrumentSelection.setPrefHeight(this.rootPane.getHeight());

        System.out.println("Large instrument selection width: " + isWidth + ", height: " + isHeight);
        setInstrumentSelectionDimensions(isWidth, isHeight / 5);
        setPlayPauseBpmDimensions(isWidth, isHeight / 5);
    }

    private void setInstrumentSelectionDimensions(double width, double height) {
        this.playPauseBpm.setPrefWidth(width);
        this.playPauseBpm.setMinHeight(height);
        this.playPauseBpm.setStyle("-fx-background-color: #ffa2fc;");

        this.snareSelection.setPrefWidth(width);
        this.snareSelection.setPrefHeight(height);
        this.snareSelection.setStyle("-fx-background-color: #90fff8;");

        this.hihatSelection.setPrefWidth(width);
        this.hihatSelection.setPrefHeight(height);
        this.hihatSelection.setStyle("-fx-background-color: #90fff8;");

        this.kickSelection.setPrefWidth(width);
        this.kickSelection.setPrefHeight(height);
        this.kickSelection.setStyle("-fx-background-color: #90fff8;");

        this.cymbalSelection.setPrefWidth(width);
        this.cymbalSelection.setPrefHeight(height);
        this.cymbalSelection.setStyle("-fx-background-color: #90fff8;");
    }

    private void setPlayPauseBpmDimensions(double width, double height) {
        this.r1.setHeight(height / 2);
        this.r1.setWidth(width / 2);
        this.r2.setHeight(height / 2);
        this.r2.setWidth(width / 2);
        this.r3.setHeight(height / 2);
        this.r3.setWidth(width);
        this.playPauseBpm.add(r1, 0, 0);
        this.playPauseBpm.add(r2,1, 0);
        this.playPauseBpm.add(r3, 0,1, 2, 1);
    }

    private void buildLargeLayout() {
        this.vSelection.getChildren().addAll(playPauseBpm, snareSelection, hihatSelection, kickSelection, cymbalSelection);
        this.instrumentSelection.getChildren().addAll(vSelection);
        this.hWrapper.getChildren().addAll(beatGridContainer, instrumentSelection);
        this.rootPane.getChildren().addAll(hWrapper);
    }

    public void changeToSmallLayout() {
        this.clearLayout();
        this.initialiseBeatGridSmall();
        this.initialiseInstrumentSelectionSmall();
        this.buildSmallLayout();
    }

    private void initialiseBeatGridSmall() {
        this.beatGridContainer.setStyle("-fx-background-color: #9b37ff;");
        this.beatGridContainer.setPrefWidth(this.rootPane.getWidth());
        this.beatGridContainer.setPrefHeight(this.rootPane.getHeight() * 0.75);
    }

    private void initialiseInstrumentSelectionSmall() {
        // Substract the padding
        double isWidth = this.rootPane.getWidth() - 82.5;
        double isHeight = (this.rootPane.getHeight() * 0.25) - 45;
        this.instrumentSelection.setStyle("-fx-background-color: #73ff42;");
        this.instrumentSelection.setPrefWidth(this.rootPane.getWidth());
        this.instrumentSelection.setPrefHeight(this.rootPane.getHeight() * 0.25);

        System.out.println("Small instrument selection width: " + isWidth + ", height: " + isHeight);

        setInstrumentSelectionDimensions(isWidth / 5, isHeight);
        setPlayPauseBpmDimensions(isWidth / 5, isHeight);
    }

    private void buildSmallLayout() {
        this.hSelection.getChildren().addAll(snareSelection, hihatSelection, kickSelection, cymbalSelection, playPauseBpm);
        this.instrumentSelection.getChildren().add(hSelection);
        this.vWrapper.getChildren().addAll(beatGridContainer, instrumentSelection);
        this.rootPane.getChildren().add(vWrapper);
    }

    public BorderPane getRootPane() {
        return this.borderPane;
    }

    public void exit() {
        presenter.exit();
    }
}
