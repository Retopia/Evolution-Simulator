package pkg09.gridpathfinding;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 *
 * @author Preston Tang
 */
public class Food extends Box implements Comparable<Food> {

    private double distance;

    public Food(double x, double y, double z, Color c) {
        super.setTranslateX(x);
        super.setTranslateY(y);
        super.setTranslateZ(z - 20);

        super.setHeight(7);
        super.setWidth(7);
        super.setDepth(7);

        PhongMaterial pM = new PhongMaterial();
        pM.setDiffuseColor(c);

        super.setMaterial(pM);
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public int compareTo(Food o) {
        if (this.getDistance() < o.getDistance()) {
            return -1;
        } else if (o.getDistance() < this.getDistance()) {
            return 1;
        }
        return 0;
    }

}
