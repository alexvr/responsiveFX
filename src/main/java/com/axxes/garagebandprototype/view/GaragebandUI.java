package com.axxes.garagebandprototype.view;

import com.axxes.garagebandprototype.presenter.Presenter;
import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class GaragebandUI {

    @Autowired
    private Presenter presenter;
    private BorderPane rootPane;
    private Pane contentPane;
    private HBox hWrapper;
    private VBox vWrapper;

    @Autowired
    private BeatGrid beatGridView;
    private Pane beatGrid;
    @Autowired
    private InstrumentSelection instrumentSelectionView;
    private Pane instrumentSelection;

    public GaragebandUI() {
        this.rootPane = new BorderPane();
        this.rootPane.getStylesheets().add("/css/garbage.css");

        // Root pane
        this.contentPane = new Pane();

        // Horizontal and vertical layout.
        this.hWrapper = new HBox();
        this.hWrapper.setStyle("-fx-background-color: #193441;");
        this.hWrapper.setPadding(new Insets(10, 10, 10, 10));
        this.hWrapper.setSpacing(10);
        this.vWrapper = new VBox();
        this.vWrapper.setStyle("-fx-background-color: #193441;");
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
        this.beatGrid = new Pane();

        // Instrument selection
        this.instrumentSelection = new Pane();

        this.rootPane.setTop(menuBar);
        this.rootPane.setCenter(contentPane);
    }

    @PostConstruct
    public void init() {
        changeToSmallLayout();
    }

    public void changeToLargeLayout() {
        this.clearLayout();
        this.buildLargeLayout();
    }

    private void clearLayout() {
        this.contentPane.getChildren().clear();
        this.hWrapper.getChildren().clear();
        this.vWrapper.getChildren().clear();
    }

    private void buildLargeLayout() {
        this.instrumentSelection = this.instrumentSelectionView.getLargeView(contentPane.widthProperty(), contentPane.heightProperty());
        this.beatGrid = this.beatGridView.getLargeView(contentPane.widthProperty(), contentPane.heightProperty());

        this.hWrapper.getChildren().addAll(beatGrid, instrumentSelection);
        this.contentPane.getChildren().addAll(hWrapper);
    }

    public void changeToSmallLayout() {
        this.clearLayout();
        this.buildSmallLayout();
    }

    private void buildSmallLayout() {
        this.instrumentSelection = this.instrumentSelectionView.getSmallView(contentPane.widthProperty(), contentPane.heightProperty());
        this.beatGrid = this.beatGridView.getSmallView(contentPane.widthProperty(), contentPane.heightProperty());

        this.vWrapper.getChildren().addAll(beatGrid, instrumentSelection);
        this.contentPane.getChildren().addAll(vWrapper);
    }

    public BorderPane getContentPane() {
        return this.rootPane;
    }

    public void exit() {
        presenter.exit();
    }

}
