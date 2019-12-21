package xylixlike.engine;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.Objects;

import static xylixlike.ui.App.SCALE;
import static xylixlike.utils.FileOperations.loadProperties;
import static xylixlike.utils.FileOperations.loadSprite;

@JsonAdapter(EntitySerializer.class)
public class Entity {
    final String type;
    final int x;
    final int y;

    private HashMap<String, String> actionMap;
    private Integer width;
    private Integer height;
    transient Rectangle hitbox;
    boolean movable;
    boolean passable;

    public Entity(String type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        setProperties(loadProperties(type));
    }

    public Entity(String type, int x, int y, int width, int height) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setProperties(loadProperties(type));
    }

    final void setProperties(Properties source) {
        if (width == null || height == null) {
            width = source.width;
            height = source.height;
        }
        movable = source.movable;
        passable = source.passable;
        actionMap = Objects.requireNonNullElseGet(source.actionMap, HashMap::new);
        this.hitbox = new Rectangle(x * SCALE, y * SCALE, width * SCALE, height * SCALE);
        hitbox.setId(type);
        this.hitbox.setFill(loadSprite(Objects.requireNonNullElseGet(source.graphics, () -> type + ".png")));
    }

    public void movementAction(int direction) {
        if (type.equals("player")) this.move(direction);
    }

    public boolean collide(Entity collidee) {
        Shape collisionBox = Shape.intersect(hitbox, collidee.hitbox);
        return collisionBox.getBoundsInLocal().getWidth() != -1;
    }

    public String collisionAction(Entity collidee) {
        return this.actionMap.getOrDefault(collidee.type, "");
    }

    void move(int i) {
        Logger.trace("Moving in direction: " + i);
        i = Math.abs(i % 360);
        switch (i) {
            case 0:
                hitbox.setTranslateY(hitbox.getTranslateY() + SCALE);
                break;
            case 90:
                hitbox.setTranslateX(hitbox.getTranslateX() + SCALE);
                break;
            case 180:
                hitbox.setTranslateY(hitbox.getTranslateY() - SCALE);
                break;
            case 270:
                hitbox.setTranslateX(hitbox.getTranslateX() - SCALE);
                break;
            default:
                Logger.error("Illegal movement call");
                break;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.type);
        hash = 79 * hash + this.x;
        hash = 79 * hash + this.y;
        hash = 79 * hash + (this.movable ? 1 : 0);
        hash = 79 * hash + (this.passable ? 1 : 0);
        hash = 79 * hash + this.actionMap.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        final Entity other = (Entity) obj;
        return this.getJson().equals(other.getJson());
    }

    public String getJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
