package com.axxes.garagebandprototype.view;

import javafx.scene.layout.Pane;

public interface ResponsiveView {

    Pane getSmallView(double width, double height);
    Pane getLargeView(double width, double height);

}
