package com.axxes.garagebandprototype.view;

import com.axxes.garagebandprototype.presenter.Presenter;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Highlighter {

    private Rectangle highlighter;
    private Slider slider;
    private boolean programmaticSliderChange;
    @Autowired
    private Presenter presenter;

    public Highlighter() {
        this.highlighter = new Rectangle();
        this.highlighter.setFill(Color.RED);
        this.highlighter.setOpacity(0.5);
        this.highlighter.setMouseTransparent(true);
        this.highlighter.setY(30);

        this.slider = new Slider();
        this.slider.setShowTickMarks(true);
        this.slider.setBlockIncrement(1);
        this.slider.setSnapToTicks(true);
        this.slider.setMajorTickUnit(1);
        this.slider.setMinorTickCount(0);

        this.programmaticSliderChange = false;

        setHighlightStep(0);
    }

    @PostConstruct
    public void init(){
        this.slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!programmaticSliderChange) {
                presenter.changeHighlighterPosition(newValue.intValue());
            }
        });
    }

    public void setSliderParameters(DoubleBinding positionX, int max, int value, DoubleBinding width) {
        this.slider.layoutXProperty().bind(positionX);
        this.slider.setMin(0);
        this.slider.setMax(max);
        this.slider.setValue(value);
        this.slider.prefWidthProperty().bind(width);
    }

    public void setSliderValue(int value){
        this.slider.setValue(value);
    }

    public void setWidthProperty(DoubleBinding width) {
        this.highlighter.widthProperty().bind(width);
    }

    public void setHeightProperty(DoubleBinding height) {
        this.highlighter.heightProperty().bind(height);
    }

    public void setHighlightStep(int step) {
        this.slider.setValue(step);
        step++;
        this.highlighter.layoutXProperty().bind(highlighter.widthProperty().multiply(step));
    }

    public Rectangle getHighlighter() {
        return this.highlighter;
    }

    public Slider getSlider() {
        return slider;
    }
}
