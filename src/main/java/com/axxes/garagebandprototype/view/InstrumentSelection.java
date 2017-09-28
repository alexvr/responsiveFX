package com.axxes.garagebandprototype.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

    private ReadOnlyDoubleProperty rootWidth;
    private ReadOnlyDoubleProperty rootHeight;

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
        this.rootWidth = new SimpleDoubleProperty(0);
        this.rootHeight = new SimpleDoubleProperty(0);

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

    public Pane getSmallView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        this.rootWidth = width;
        this.rootHeight = height;

        this.clearLayout();
        this.initialiseSmallLayout();
        this.buildSmallLayout();
        return this.instrumentSelection;
    }

    public Pane getLargeView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
        this.clearLayout();
        this.initialiseLargeLayout();
        this.buildLargeLayout();
        return this.instrumentSelection;
    }

    private void clearLayout() {
        this.instrumentSelection.getChildren().clear();
        this.vSelection.getChildren().clear();
        this.hSelection.getChildren().clear();
        this.playPauseBpm.getChildren().clear();
    }

    private void initialiseSmallLayout() {
        // Substract the padding
        if (this.rootWidth.getValue() > 0) {
            DoubleBinding calculatedWidth = rootWidth.subtract(82.5).divide(5);
            DoubleBinding calculatedHeight = rootHeight.multiply(0.25).subtract(60);
            System.out.println("Small instrument selection width: " + calculatedWidth.get() + ", height: " + calculatedHeight.get());

            setContainerSize(rootWidth.multiply(1), rootHeight.multiply(0.25));
            setInstrumentSelectionDimensions(calculatedWidth, calculatedHeight);
            setPlayPauseBpmDimensions(calculatedWidth, calculatedHeight);
        }
    }

    private void setContainerSize(DoubleBinding width, DoubleBinding height) {
        this.instrumentSelection.prefWidthProperty().bind(width);
        this.instrumentSelection.prefWidthProperty().bind(height);
    }

    private void setInstrumentSelectionDimensions(DoubleBinding width, DoubleBinding height) {
        this.playPauseBpm.prefWidthProperty().bind(width);
        this.playPauseBpm.prefHeightProperty().bind(height);
        this.playPauseBpm.setStyle("-fx-background-color: #ffa2fc;");

        this.snareSelection.prefWidthProperty().bind(width);
        this.snareSelection.prefHeightProperty().bind(height);
        this.snareSelection.setStyle("-fx-background-color: #90fff8;");

        this.hihatSelection.prefWidthProperty().bind(width);
        this.hihatSelection.prefHeightProperty().bind(height);
        this.hihatSelection.setStyle("-fx-background-color: #90fff8;");

        this.kickSelection.prefWidthProperty().bind(width);
        this.kickSelection.prefHeightProperty().bind(height);
        this.kickSelection.setStyle("-fx-background-color: #90fff8;");

        this.cymbalSelection.prefWidthProperty().bind(width);
        this.cymbalSelection.prefHeightProperty().bind(height);
        this.cymbalSelection.setStyle("-fx-background-color: #90fff8;");
    }

    private void setPlayPauseBpmDimensions(DoubleBinding width, DoubleBinding height) {
        DoubleBinding halfWidth = width.divide(2);
        DoubleBinding halfHeight = height.divide(2);
        this.r1.heightProperty().bind(halfHeight);
        this.r1.widthProperty().bind(halfWidth);
        this.r2.heightProperty().bind(halfHeight);
        this.r2.widthProperty().bind(halfWidth);
        this.r3.heightProperty().bind(halfHeight);
        this.r3.widthProperty().bind(width);
        this.playPauseBpm.add(r1, 0, 0);
        this.playPauseBpm.add(r2,1, 0);
        this.playPauseBpm.add(r3, 0,1, 2, 1);
    }

    private void buildSmallLayout() {
        this.hSelection.getChildren().addAll(snareSelection, hihatSelection, kickSelection, cymbalSelection, playPauseBpm);
        this.instrumentSelection.getChildren().add(hSelection);
    }

    private void initialiseLargeLayout() {
        if (this.rootWidth.getValue() > 0) {
            // Substract the padding
            DoubleBinding calculatedWidth = rootWidth.multiply(0.25).subtract(35);
            DoubleBinding calculatedHeight = rootHeight.subtract(95).divide(5);
            System.out.println("Large instrument selection width: " + calculatedWidth.get() + ", height: " + calculatedHeight.get());

            setContainerSize(rootWidth.multiply(0.25), rootHeight.multiply(1));
            setInstrumentSelectionDimensions(calculatedWidth, calculatedHeight);
            setPlayPauseBpmDimensions(calculatedWidth, calculatedHeight);
        }
    }

    private void buildLargeLayout() {
        this.vSelection.getChildren().addAll(playPauseBpm, snareSelection, hihatSelection, kickSelection, cymbalSelection);
        this.instrumentSelection.getChildren().add(vSelection);
    }

}
