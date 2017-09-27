package com.axxes.garagebandprototype.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.springframework.stereotype.Component;

@Component
public class InstrumentSelection implements ResponsiveView {

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

    public InstrumentSelection() {
        // Instrument selection pane
        this.instrumentSelection = new Pane();
        this.hSelection = new HBox();
        this.hSelection.setStyle("-fx-background-color: #fffb1d;");
        this.hSelection.setPadding(new Insets(10, 10, 10, 10));
        this.hSelection.setSpacing(10);
        this.vSelection = new VBox();
        this.vSelection.setStyle("-fx-background-color: #ff4033;");
        this.vSelection.setPadding(new Insets(10, 10, 10, 10));
        this.vSelection.setSpacing(10);

        // Play pause bpm pane
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
        this.snareSelection.getChildren().addAll(snareView);

        this.hihatSelection = new StackPane();
        this.hihatSelection.getStyleClass().add("anchor");
        this.hihatSelection.setAlignment(Pos.CENTER);
        ImageView hihatView = new ImageView();
        hihatView.setImage(new Image("/images/hihat.png"));
        hihatView.setFitHeight(120);
        hihatView.setFitWidth(120);
        this.hihatSelection.getChildren().addAll(hihatView);

        this.kickSelection = new StackPane();
        this.kickSelection.getStyleClass().add("anchor");
        this.kickSelection.setAlignment(Pos.CENTER);
        ImageView kickView = new ImageView();
        kickView.setImage(new Image("/images/kick.png"));
        kickView.setFitWidth(120);
        kickView.setFitHeight(120);
        this.kickSelection.getChildren().addAll(kickView);

        this.cymbalSelection = new StackPane();
        this.cymbalSelection.getStyleClass().add("anchor");
        this.cymbalSelection.setAlignment(Pos.CENTER);
        ImageView cymbalView = new ImageView();
        cymbalView.setImage(new Image("./images/cymbal.png"));
        cymbalView.setFitHeight(120);
        cymbalView.setFitWidth(120);
        this.cymbalSelection.getChildren().add(cymbalView);
    }

    @Override
    public Pane getSmallView(double width, double height) {
        this.clearLayout();
        this.initialiseSmallLayout(width, height);
        this.buildSmallLayout();
        return this.instrumentSelection;
    }

    @Override
    public Pane getLargeView(double width, double height) {
        this.clearLayout();
        this.initialiseLargeLayout(width, height);
        this.buildLargeLayout();
        return this.instrumentSelection;
    }

    private void clearLayout() {
        this.instrumentSelection.getChildren().clear();
        this.vSelection.getChildren().clear();
        this.hSelection.getChildren().clear();
        this.playPauseBpm.getChildren().clear();
    }

    private void initialiseSmallLayout(double width, double height) {
        // Substract the padding
        double calculatedWidth = width - 82.5;
        double calculatedHeight = (height * 0.25) - 60;
        System.out.println("Small instrument selection width: " + calculatedWidth + ", height: " + calculatedHeight);

        setContainerSize(width, height * 0.25);
        setInstrumentSelectionDimensions(calculatedWidth / 5, calculatedHeight);
        setPlayPauseBpmDimensions(calculatedWidth / 5, calculatedHeight);
    }

    private void setContainerSize(double width, double height) {
        this.instrumentSelection.setPrefWidth(width);
        this.instrumentSelection.setPrefHeight(height * 0.25);
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

    private void buildSmallLayout() {
        this.hSelection.getChildren().addAll(snareSelection, hihatSelection, kickSelection, cymbalSelection, playPauseBpm);
        this.instrumentSelection.getChildren().add(hSelection);
    }

    private void initialiseLargeLayout(double width, double height) {
        // Substract the padding
        double calculatedWidth = (width * 0.25) - 35;
        double calculatedHeight = height - 95;
        System.out.println("Large instrument selection width: " + calculatedWidth + ", height: " + calculatedHeight);

        setContainerSize(width * 0.25, height);
        setInstrumentSelectionDimensions(calculatedWidth, calculatedHeight / 5);
        setPlayPauseBpmDimensions(calculatedWidth, calculatedHeight / 5);
    }

    private void buildLargeLayout() {
        this.vSelection.getChildren().addAll(playPauseBpm, snareSelection, hihatSelection, kickSelection, cymbalSelection);
        this.instrumentSelection.getChildren().addAll(vSelection);
    }

}
