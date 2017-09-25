package com.axxes.garagebandprototype;

import com.axxes.garagebandprototype.model.loop.Drumloop;
import com.axxes.garagebandprototype.model.measures.Measure;
import com.axxes.garagebandprototype.presenter.Presenter;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;

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

    private StackPane snareSelection;
    private StackPane hihatSelection;
    private StackPane kickSelection;
    private StackPane cymbalSelection;

    @Autowired
    private Drumloop drumloop;
    private int beats;
    private int beatsPerMeasure;

    public GaragebandUI() {

    }

    @PostConstruct
    public void init() {
        this.beats = drumloop.getMeasures().stream().map(Measure::getBeats).mapToInt(Collection::size).sum();
        this.beatsPerMeasure = drumloop.getBeatsPerMeasure();

        this.borderPane = new BorderPane();
        this.borderPane.getStylesheets().add("/css/garbage.css");

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
        this.snareSelection = new StackPane();
        this.snareSelection.getStyleClass().add("anchor");
        this.snareSelection.setAlignment(Pos.CENTER);
        ImageView snareView = new ImageView();
        snareView.setImage(new Image("/images/snare.png"));
        snareView.setFitWidth(120);
        snareView.setFitHeight(120);
        snareSelection.getChildren().addAll(snareView);

        this.hihatSelection = new StackPane();
        this.hihatSelection.getStyleClass().add("anchor");
        this.hihatSelection.setAlignment(Pos.CENTER);
        ImageView hihatView = new ImageView();
        hihatView.setImage(new Image("/images/hihat.png"));
        hihatView.setFitHeight(120);
        hihatView.setFitWidth(120);
        hihatSelection.getChildren().addAll(hihatView);

        this.kickSelection = new StackPane();
        this.kickSelection.getStyleClass().add("anchor");
        this.kickSelection.setAlignment(Pos.CENTER);
        ImageView kickView = new ImageView();
        kickView.setImage(new Image("/images/kick.png"));
        kickView.setFitWidth(120);
        kickView.setFitHeight(120);
        kickSelection.getChildren().addAll(kickView);

        this.cymbalSelection = new StackPane();
        this.cymbalSelection.getStyleClass().add("anchor");
        this.cymbalSelection.setAlignment(Pos.CENTER);
        ImageView cymbalView = new ImageView();
        cymbalView.setImage(new Image("./images/cymbal.png"));
        cymbalView.setFitHeight(120);
        cymbalView.setFitWidth(120);
        cymbalSelection.getChildren().add(cymbalView);

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
        this.beatGrid.getChildren().clear();
        this.instrumentSelection.getChildren().clear();
        this.vSelection.getChildren().clear();
        this.hSelection.getChildren().clear();
        this.playPauseBpm.getChildren().clear();
    }

    private void initialiseBeatGridLarge() {
        this.beatGridContainer.setStyle("-fx-background-color: #fff328;");
        this.beatGridContainer.setPrefWidth(this.rootPane.getWidth() * 0.75);
        this.beatGridContainer.setPrefHeight(this.rootPane.getHeight());

        this.beatGrid.setPrefWidth((this.rootPane.getWidth() * 0.75) - 20);
        this.beatGrid.setPrefHeight(this.rootPane.getHeight() - 20);

        createBaseGrid();
    }

    private void initialiseInstrumentSelectionLarge() {
        // Substract the padding
        double isWidth = (this.rootPane.getWidth() * 0.25) - 35;
        double isHeight = this.rootPane.getHeight() - 95;

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
        this.beatGridContainer.getChildren().addAll(beatGrid);
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

        this.beatGrid.setStyle("-fx-background-color: #ffca28;");
        this.beatGrid.setPrefWidth(this.rootPane.getWidth() - 20);
        this.beatGrid.setPrefHeight((this.rootPane.getHeight() * 0.75) - 20);

        createBaseGrid();
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
        this.beatGridContainer.getChildren().addAll(beatGrid);
        this.hSelection.getChildren().addAll(snareSelection, hihatSelection, kickSelection, cymbalSelection, playPauseBpm);
        this.instrumentSelection.getChildren().add(hSelection);
        this.vWrapper.getChildren().addAll(beatGridContainer, instrumentSelection);
        this.rootPane.getChildren().add(vWrapper);
    }

    private void createBaseGrid() {
        this.beatGrid.add(createLabel("Beat"), 0, 0);
        for (int i = 1; i <= this.beats; i++) {
            int currentBeat = ((i - 1) % this.beatsPerMeasure) + 1;
            this.beatGrid.add(createLabel(String.valueOf(currentBeat)),i, 0);
        }
        this.beatGrid.setBorder(Border.EMPTY);
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        DoubleProperty dp = new SimpleDoubleProperty((this.rootPane.getWidth() - 20) / this.beats);
        label.prefWidthProperty().bind(dp);
        return label;
    }

    public BorderPane getRootPane() {
        return this.borderPane;
    }

    public void exit() {
        presenter.exit();
    }
}
