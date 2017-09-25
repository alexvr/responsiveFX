package com.axxes.garagebandprototype;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import org.springframework.stereotype.Component;

@Component
public class GaragebandUI {

    private StackPane rootPane;

    // Window > 600
    private HBox hWrapper;
    // Window < 600
    private VBox vWrapper;

    private Pane beatGrid;

    private Pane instrumentSelection;
    private HBox hSelection;
    private VBox vSelection;
    private Pane playPause;
    private HBox hPlayPause;
    private VBox vPlayPause;
    private Pane play;
    private Pane pause;
    private Pane snareSelection;
    private Pane hihatSelection;
    private Pane kickSelection;
    private Pane cymbalSelection;

    public GaragebandUI() {
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

        // Beat grid
        this.beatGrid = new Pane();

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

        // Play pause
        this.playPause = new Pane();
        this.hPlayPause = new HBox();
        this.hPlayPause.setStyle("-fx-background-color: #ff5c3c;");
        // this.hPlayPause.setPadding(new Insets(10, 10, 10, 10));
        this.hPlayPause.setSpacing(10);
        this.vPlayPause = new VBox();
        this.vPlayPause.setStyle("-fx-background-color: #57c5ff;");
        // this.vPlayPause.setPadding(new Insets(10, 10, 10, 10));
        this.vPlayPause.setSpacing(10);
        this.play = new Pane();
        this.pause = new Pane();

        // Instruments
        this.snareSelection = new Pane();
        this.hihatSelection = new Pane();
        this.kickSelection = new Pane();
        this.cymbalSelection = new Pane();

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
        this.beatGrid.getChildren().clear();
        this.instrumentSelection.getChildren().clear();
        this.vSelection.getChildren().clear();
        this.hSelection.getChildren().clear();
        this.playPause.getChildren().clear();
        this.hPlayPause.getChildren().clear();
        this.vPlayPause.getChildren().clear();
    }

    private void initialiseBeatGridLarge() {
        this.beatGrid.setStyle("-fx-background-color: #fff328;");
        this.beatGrid.setPrefWidth(this.rootPane.getWidth() * 0.75);
        this.beatGrid.setPrefHeight(this.rootPane.getHeight());
    }

    private void initialiseInstrumentSelectionLarge() {
        // Substract the padding
        double isWidth = (this.rootPane.getWidth() * 0.25) - 35;
        double isHeight = this.rootPane.getHeight() - 80;

        this.instrumentSelection.setStyle("-fx-background-color: #73ff42;");
        this.instrumentSelection.setPrefWidth(this.rootPane.getWidth() * 0.25);
        this.instrumentSelection.setPrefHeight(this.rootPane.getHeight());

        System.out.println("Large instrument selection width: " + isWidth + ", height: " + isHeight);
        setInstrumentSelectionDimensions(isWidth, isHeight / 5);
        setPlayPauseDimensions((isWidth / 2) - 5, isHeight / 5);
    }

    private void setInstrumentSelectionDimensions(double width, double height) {
        this.playPause.setPrefWidth(width);
        this.playPause.setMinHeight(height);
        this.playPause.setStyle("-fx-background-color: #ffa2fc;");

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

    private void setPlayPauseDimensions(double width, double height) {
        this.play.setPrefWidth(width);
        this.play.setPrefHeight(height);
        this.play.setStyle("-fx-background-color: #84ff89;");

        this.pause.setPrefWidth(width);
        this.pause.setPrefHeight(height);
        this.pause.setStyle("-fx-background-color: #84ff89;");
    }

    private void buildLargeLayout() {
        this.hPlayPause.getChildren().addAll(play, pause);
        this.playPause.getChildren().add(hPlayPause);
        this.vSelection.getChildren().addAll(playPause, snareSelection, hihatSelection, kickSelection, cymbalSelection);
        this.instrumentSelection.getChildren().addAll(vSelection);
        this.hWrapper.getChildren().addAll(beatGrid, instrumentSelection);
        this.rootPane.getChildren().add(hWrapper);
    }

    public void changeToSmallLayout() {
        this.clearLayout();
        this.initialiseBeatGridSmall();
        this.initialiseInstrumentSelectionSmall();
        this.buildSmallLayout();
    }

    private void initialiseBeatGridSmall() {
        this.beatGrid.setStyle("-fx-background-color: #9b37ff;");
        this.beatGrid.setPrefWidth(this.rootPane.getWidth());
        this.beatGrid.setPrefHeight(this.rootPane.getHeight() * 0.75);
    }

    private void initialiseInstrumentSelectionSmall() {
        // Substract the padding
        double isWidth = this.rootPane.getWidth() - 80;
        double isHeight = (this.rootPane.getHeight() * 0.25) - 45;
        this.instrumentSelection.setStyle("-fx-background-color: #73ff42;");
        this.instrumentSelection.setPrefWidth(this.rootPane.getWidth());
        this.instrumentSelection.setPrefHeight(this.rootPane.getHeight() * 0.25);

        System.out.println("Small instrument selection width: " + isWidth + ", height: " + isHeight);

        setInstrumentSelectionDimensions(isWidth / 5, isHeight);
        setPlayPauseDimensions((isWidth / 5), (isHeight / 2));
    }

    private void buildSmallLayout() {
        this.vPlayPause.getChildren().addAll(play, pause);
        this.playPause.getChildren().add(vPlayPause);
        this.hSelection.getChildren().addAll(snareSelection, hihatSelection, kickSelection, cymbalSelection, playPause);
        this.instrumentSelection.getChildren().add(hSelection);
        this.vWrapper.getChildren().addAll(beatGrid, instrumentSelection);
        this.rootPane.getChildren().add(vWrapper);
    }

    public StackPane getRootPane() {
        return this.rootPane;
    }

}
