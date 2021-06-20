package pkg09.gridpathfinding;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 *
 * @author Preston Tang
 */
public class Block extends Box implements Comparable<Block> {

    private double distance = 0.0;

    public Block parent;
    public double gCost;
    public double hCost;
    public double fCost = gCost + hCost;

    private PhongMaterial pm;
    private String file;

    private boolean isLava, isWater;

    private boolean hasFood, hasPlayer;

    public Block(String file) {
        this.file = file;

        pm = new PhongMaterial();
        pm.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/" + file)));

        super.setMaterial(pm);
    }

    public Block(Color c, boolean breakable) {
        pm = new PhongMaterial();
        pm.setDiffuseColor(c);

        super.setMaterial(pm);
    }

    public Block() {
    }

    public boolean isLava() {
        return this.isLava;
    }

    public boolean hasPlayer() {
        return this.hasPlayer;
    }

    public void setPlayer(boolean hasPlayer) {
        this.hasPlayer = hasPlayer;
    }

    public boolean hasFood() {
        return this.hasFood;
    }

    public Block meetsConditions() {
        if (!hasPlayer && !isLava) {
            return this;
        }

        return null;
    }

    public void setFood(boolean hasFood) {
        this.hasFood = hasFood;
    }

    public void setLava(boolean isLava) {
        if (isLava) {
            this.setTexture("lava");
        }

        this.isLava = isLava;
    }

    public boolean isWater() {
        return this.isWater;
    }

    public void setWater(boolean isWater) {
        if (isWater) {
            this.setTexture("water");
        }

        this.isWater = isWater;
    }

    public void setColor(Color c) {
        pm = new PhongMaterial();
        pm.setDiffuseColor(c);

        super.setMaterial(pm);
    }

    public void setTexture(String file) {
        pm = new PhongMaterial();
        pm.setDiffuseMap(new Image(getClass().getResourceAsStream(file + ".png")));
        super.setMaterial(pm);
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return this.distance;
    }

    @Override
    public int compareTo(Block b) {
        if (this.getDistance() < b.getDistance()) {
            return -1;
        } else if (b.getDistance() < this.getDistance()) {
            return 1;
        }
        return 0;
    }

}
