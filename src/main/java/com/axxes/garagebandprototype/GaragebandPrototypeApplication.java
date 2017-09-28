package com.axxes.garagebandprototype;

import com.axxes.garagebandprototype.view.GaragebandUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class GaragebandPrototypeApplication extends Application {

    private double previousWidth;

    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
        launch(GaragebandPrototypeApplication.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.context = SpringApplication.run(GaragebandPrototypeApplication.class);

        try {
            GaragebandUI garagebandUI = this.context.getBean(GaragebandUI.class);

            primaryStage.setMinWidth(800);

            primaryStage.widthProperty().addListener(e -> {
                if(primaryStage.getWidth() <= 1000 && previousWidth > 1000) {
                    System.out.println("SMALL LAYOUT: width: " + primaryStage.getWidth() + ", height: " + primaryStage.getHeight());
                    garagebandUI.changeToSmallLayout();
                } else if (primaryStage.getWidth() > 1000 && previousWidth <= 1000){
                    System.out.println("LARGE LAYOUT: width: " + primaryStage.getWidth() + ", height: " + primaryStage.getHeight());
                    garagebandUI.changeToLargeLayout();
                }

                previousWidth = primaryStage.getWidth();

            });

            primaryStage.setScene(new Scene(garagebandUI.getContentPane(), 1400, 800));
            primaryStage.setOnHidden(event -> garagebandUI.exit());
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
