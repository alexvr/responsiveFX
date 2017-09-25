package com.axxes.garagebandprototype;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GaragebandPrototypeApplication extends Application {

    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
        launch(GaragebandPrototypeApplication.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.context = SpringApplication.run(GaragebandPrototypeApplication.class);

        try {
            GaragebandUI garagebandUI = this.context.getBean(GaragebandUI.class);

            primaryStage.widthProperty().addListener(e -> {
                if(primaryStage.getWidth() < 1000) {
                    garagebandUI.changeToSmallLayout();
                } else {
                    garagebandUI.changeToLargeLayout();
                }
            });

            primaryStage.setScene(new Scene(garagebandUI.getRootPane(), 1400, 800));
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
