package com.axxes.garagebandprototype.view;

import com.axxes.garagebandprototype.model.loop.Drumloop;
import com.axxes.garagebandprototype.model.measures.Measure;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;

@Component
public class BeatGrid implements ResponsiveView {

    private Pane beatGridContainer;
    private GridPane beatGrid;

    @Autowired
    private Drumloop drumloop;
    private int beats;
    private int beatsPerMeasure;

    public BeatGrid() {
        this.beatGridContainer = new Pane();
        this.beatGrid = new GridPane();
    }

    @PostConstruct
    public void init() {
        this.beats = drumloop.getMeasures().stream().map(Measure::getBeats).mapToInt(Collection::size).sum();
        this.beatsPerMeasure = drumloop.getBeatsPerMeasure();
    }

    @Override
    public Pane getSmallView(double width, double height) {
        this.clearLayout();
        this.initialiseSmallLayout(width, height);
        this.buildLayout();

        return this.beatGridContainer;
    }

    @Override
    public Pane getLargeView(double width, double height) {
        this.clearLayout();
        this.initialiseLargeLayout(width, height);
        this.buildLayout();

        return this.beatGridContainer;
    }

    private void clearLayout() {
        this.beatGridContainer.getChildren().clear();
        this.beatGrid.getChildren().clear();
    }

    private void initialiseSmallLayout(double width, double height) {
        this.setContainerSize(width, height * 0.75);
        this.setBeatGridSize(width - 20, height * 0.75);
        this.createBaseGrid(width);
    }

    private void setContainerSize(double width, double height) {
        this.beatGridContainer.setPrefWidth(width);
        this.beatGridContainer.setPrefHeight(height);
    }


    private void setBeatGridSize(double width, double height) {
        this.beatGrid.setStyle("-fx-background-color: #ffca28;");
        this.beatGrid.setPrefWidth(width);
        this.beatGrid.setPrefHeight(height);
    }

    private void createBaseGrid(double width) {
        this.beatGrid.add(createLabel("Beat", width), 0, 0);
        for (int i = 1; i <= this.beats; i++) {
            int currentBeat = ((i - 1) % this.beatsPerMeasure) + 1;
            this.beatGrid.add(createLabel(String.valueOf(currentBeat), width),i, 0);
        }
        this.beatGrid.setBorder(Border.EMPTY);
    }

    private Label createLabel(String text, double width) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        DoubleProperty dp = new SimpleDoubleProperty((width - 20) / this.beats);
        label.prefWidthProperty().bind(dp);
        return label;
    }

    private void buildLayout() {
        this.beatGridContainer.getChildren().addAll(beatGrid);
    }

    private void initialiseLargeLayout(double width, double height) {
        this.setContainerSize(width * 0.75, height);
        this.setBeatGridSize((width * 0.75) - 15, height - 20);
        createBaseGrid(width);
    }

}
