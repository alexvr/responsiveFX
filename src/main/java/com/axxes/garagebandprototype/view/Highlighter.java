package com.axxes.garagebandprototype.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Highlighter {

    private Rectangle highlighter;

    public Highlighter() {
        this.highlighter = new Rectangle();
        this.highlighter.setFill(Color.RED);
        this.highlighter.setOpacity(0.5);
        this.highlighter.setMouseTransparent(true);
    }

    public void setWidthProperty(DoubleBinding width) {
        this.highlighter.widthProperty().bind(width);
        setHighlightStep(0);
    }

    public void setHeightProperty(DoubleBinding height) {
        this.highlighter.heightProperty().bind(height);
    }

    public void setHighlightStep(int step) {
        step++;
        this.highlighter.layoutXProperty().bind(highlighter.widthProperty().multiply(step));
    }

    public Rectangle getHighlighter() {
        return this.highlighter;
    }

}
