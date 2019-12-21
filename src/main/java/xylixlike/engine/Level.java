package xylixlike.engine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Level {
    int gravity;
    boolean won;
    boolean lost;
    private List<Entity> entities;


    private Level() {
        entities = new ArrayList<>();
        gravity = 0;
        won = false;
        lost = false;
    }

    public Level(List<Entity> entities) {
        this();
        this.entities = entities;
    }

    public void ifOver(Runnable action) {
        if (won) {
            action.run();
        }
    }

    public void tick(KeyEvent press) {
        getEntities().forEach(actor -> actor.movementAction(press.getCode()));
        if (won) {
            return;
        } else if (lost) {
            entities.forEach(e -> {
                e.hitbox.setTranslateX(0);
                e.hitbox.setTranslateY(0);
            });
            lost = false;
            return;
        }
        update();
    }

    private void update() {

    }

    private void handleAction(Entity collider, Entity collidee, String action) {
        if (!action.isBlank()) {
            Logger.trace(action);
        }
        if ("victory".equals(action)) {
            System.out.println("You're winner!");
            won = true;
        } else if ("loss".equals(action)) {
            lost = true;
        } else if ("open".equals(action)) {
            collidee.passable = true;
            collidee.hitbox.setFill(Color.TRANSPARENT);
            collider.movable = false;
            collider.hitbox.setFill(Color.TRANSPARENT);
        } else if (!collidee.passable) {
            collider.move(gravity + 540);
        }
    }

    public String getJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public List<Rectangle> getHitboxes() {
        return entities.stream().map(e -> e.hitbox).collect(Collectors.toList());
    }

    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    public Scene getScene() {
        Pane pane = new Pane(this.getHitboxes().toArray(Rectangle[]::new));
        Scene scene = new Scene(pane);
        scene.setOnKeyPressed(press -> {
            tick(press);
        });
        return scene;
    }
}
