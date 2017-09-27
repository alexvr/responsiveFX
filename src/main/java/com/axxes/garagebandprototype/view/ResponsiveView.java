package com.axxes.garagebandprototype.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.Pane;

public interface ResponsiveView {

    Pane getSmallView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height);
    Pane getLargeView(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height);

}
