package xylixlike.engine;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static xylixlike.utils.Parsing.toDirection;

public class Level {
    boolean won;
    boolean lost;
    private List<Entity> entities;


    private Level() {
        entities = new ArrayList<>();
        won = false;
        lost = false;
    }

    public void ifOver(Runnable action) {
        if (won) {
            action.run();
        }
    }

    public void tick(KeyEvent press) {
        int movementDirection = toDirection(press.getCode());
        getEntities().forEach(actor -> actor.movementAction(movementDirection));
        collisionHandle(movementDirection);

        if (lost) {
            entities.forEach(e -> {
                e.hitbox.setTranslateX(0);
                e.hitbox.setTranslateY(0);
            });
            lost = false;
            return;
        }
    }

    private void collisionHandle(int direction) {
        int undoDirection = direction + 180;
        entities.stream().filter(e -> (e.movable)).forEach(collider -> {
            entities.stream().filter(collider::collide).forEach(collidee -> {
                String action = collider.collisionAction(collidee);
                handleAction(collider, collidee, action, undoDirection);
            });
        });
    }


    private void handleAction(Entity collider, Entity collidee, String action, int undo) {
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
            collider.move(undo);
        }
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
        scene.setOnKeyPressed(this::tick);
        return scene;
    }
}
