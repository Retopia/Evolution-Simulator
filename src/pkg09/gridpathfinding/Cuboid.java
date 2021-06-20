package pkg09.gridpathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 *
 * @author Preston Tang
 */
public class Cuboid extends Box implements Comparable<Cuboid> {

    private boolean canMove = true;

    private String name, gender;
    private double speed;

    public int i = 0;
    public int mI = 0;
    public ArrayList<Block> path = new ArrayList<>();
    public ArrayList<Block> mPath = new ArrayList<>();
    public ArrayList<Food> foo = new ArrayList<>();
    public ArrayList<Cuboid> mates = new ArrayList<>();

    public String color;

    private int energy;

    private double distance;

    public boolean mating = false;
    public boolean fooding = true;

    public double size;

    public String colorAlleles;
    public String waterAlleles;
    public String speedAlleles;
    public String sizeAlleles;

    private boolean waterResistant;

    public Cuboid(double x,
            double y,
            double z,
            String name,
            String gender) {
        this.energy = 3;
        this.name = name;
        this.gender = gender;

        Color c = (int) Math.round(Math.random()) == 0 ? Color.BLUE : Color.YELLOW;

        color = c == Color.BLUE ? "Blue" : "Yellow";
        colorAlleles = color.equals("Blue") ? "CC" : "cc";

        this.waterResistant = (int) Math.round(Math.random()) == 0;

        waterAlleles = waterResistant ? "ww" : "WW";

        this.speed = (int) Math.round(Math.random()) == 0 ? 4.0 : 3.0;
        speedAlleles = this.speed == 4.0 ? "SS" : "ss";

        this.size = (int) Math.round(Math.random()) == 0 ? 15.0 : 10.0;
        sizeAlleles = size == 10.0 ? "ZZ" : "zz";

        super.setTranslateX(x);
        super.setTranslateY(y);
        super.setTranslateZ(z - 30);

        super.setHeight(size);
        super.setWidth(size);
        super.setDepth(size * 3);

        PhongMaterial pM = new PhongMaterial();
        pM.setDiffuseColor(c);

        super.setMaterial(pM);
    }

    public Cuboid(double x,
            double y,
            double z,
            double size,
            double speed,
            String name,
            String gender,
            Color c) {

        this.energy = 3;
        this.name = name;
        this.gender = gender;
        this.speed = speed;
        this.size = size;

        super.setTranslateX(x);
        super.setTranslateY(y);
        super.setTranslateZ(z - 30);

        super.setHeight(size);
        super.setWidth(size);
        super.setDepth(size * 3);

        PhongMaterial pM = new PhongMaterial();
        pM.setDiffuseColor(c);

        super.setMaterial(pM);
    }

    public Cuboid mate(Cuboid a, Cuboid b) {
        if (a.waterAlleles.equals("") || a.speedAlleles.equals("")
                || a.sizeAlleles.equals("") || a.colorAlleles.equals("")) {
            a.setEnergy(0); //die
        } else if (b.waterAlleles.equals("") || b.speedAlleles.equals("")
                || b.sizeAlleles.equals("") || b.colorAlleles.equals("")) {
            b.setEnergy(0); //die
        } else {

            World.getInstance().getBlocks()[(int) a.getTranslateY() / 20][(int) a.getTranslateX() / 20].setColor(Color.MAGENTA);

            String wA = "";
            boolean water = false;

            char[] wA1 = new char[2];
            wA1[0] = a.waterAlleles.charAt(0);
            wA1[1] = a.waterAlleles.charAt(1);

            char[] wA2 = new char[2];
            wA2[0] = b.waterAlleles.charAt(0);
            wA2[1] = b.waterAlleles.charAt(1);

            if (Character.isUpperCase(wA1[0]) && Character.isUpperCase(wA1[1])
                    && Character.isUpperCase(wA2[0]) && Character.isUpperCase(wA2[1])) { //both homo dom
                wA = "WW";
                water = false;
            } else if (!Character.isUpperCase(wA1[0]) && !Character.isUpperCase(wA1[1])
                    && !Character.isUpperCase(wA2[0]) && !Character.isUpperCase(wA2[1])) { //both homo recess
                wA = "ww";
                water = true;
            } else if ((Character.isUpperCase(wA1[0]) && Character.isUpperCase(wA1[1])
                    && !Character.isUpperCase(wA2[0]) && !Character.isUpperCase(wA2[1]))
                    || (!Character.isUpperCase(wA1[0]) && !Character.isUpperCase(wA1[1])
                    && Character.isUpperCase(wA2[0]) && Character.isUpperCase(wA2[1]))) { //one homo dom, one homo recess
                wA = "Ww";
                water = false;
            } else if ((!Character.isUpperCase(wA1[0]) && !Character.isUpperCase(wA1[1])
                    && (!Character.isUpperCase(wA2[0]) || !Character.isUpperCase(wA2[1])))
                    || (!Character.isUpperCase(wA1[0]) || !Character.isUpperCase(wA1[1])
                    && (!Character.isUpperCase(wA2[0]) && !Character.isUpperCase(wA2[1])))) { //one homo recess, one hetero
                wA = (int) Math.round(Math.random()) == 0 ? "ww" : "Ww";
                water = !wA.equals("Ww");
            } else if ((Character.isUpperCase(wA1[0]) && Character.isUpperCase(wA1[1])
                    && (!Character.isUpperCase(wA2[0]) || !Character.isUpperCase(wA2[1])))
                    || (!Character.isUpperCase(wA1[0]) || !Character.isUpperCase(wA1[1])
                    && (Character.isUpperCase(wA2[0]) && Character.isUpperCase(wA2[1])))) { //one homo dom, one hetero
                wA = (int) Math.round(Math.random()) == 0 ? "WW" : "Ww";
                water = false;
            } else if (Character.isUpperCase(wA1[0]) && !Character.isUpperCase(wA1[1])
                    && Character.isUpperCase(wA2[0]) && !Character.isUpperCase(wA2[1])) { //both hetero
                int temp = new Random().nextInt(100);
                if (temp < 25) {
                    wA = "ww";
                    water = true;
                } else if (temp < 75) {
                    wA = "Ww";
                    water = false;
                } else {
                    wA = "WW";
                    water = false;
                }
            }

            String cA = "";
            Color col = Color.BLACK;
            String colN = "";

            char[] cA1 = new char[2];
            cA1[0] = a.colorAlleles.charAt(0);
            cA1[1] = a.colorAlleles.charAt(1);

            char[] cA2 = new char[2];
            cA2[0] = b.colorAlleles.charAt(0);
            cA2[1] = b.colorAlleles.charAt(1);

            if (Character.isUpperCase(cA1[0]) && Character.isUpperCase(cA1[1])
                    && Character.isUpperCase(cA2[0]) && Character.isUpperCase(cA2[1])) { //both homo dom
                cA = "CC";
                col = Color.BLUE;
                colN = "Blue";
            } else if (!Character.isUpperCase(cA1[0]) && !Character.isUpperCase(cA1[1])
                    && !Character.isUpperCase(cA2[0]) && !Character.isUpperCase(cA2[1])) { //both homo recess
                cA = "cc";
                col = Color.YELLOW;
                colN = "Yellow";
            } else if ((Character.isUpperCase(cA1[0]) && Character.isUpperCase(cA1[1])
                    && !Character.isUpperCase(cA2[0]) && !Character.isUpperCase(cA2[1]))
                    || (!Character.isUpperCase(cA1[0]) && !Character.isUpperCase(cA1[1])
                    && Character.isUpperCase(cA2[0]) && Character.isUpperCase(cA2[1]))) { //one homo dom, one homo recess
                cA = "Cc";
                col = Color.BLUE;
                colN = "Blue";
            } else if ((Character.isUpperCase(cA1[0]) && Character.isUpperCase(cA1[1])
                    && (!Character.isUpperCase(cA2[0]) || !Character.isUpperCase(cA2[1])))
                    || (!Character.isUpperCase(cA1[0]) || !Character.isUpperCase(cA1[1])
                    && (Character.isUpperCase(cA2[0]) && Character.isUpperCase(cA2[1])))) { //one homo dom, one hetero
                cA = (int) Math.round(Math.random()) == 0 ? "CC" : "Cc";
                col = cA.equals("CC") ? Color.BLUE : Color.YELLOW;
                colN = cA.equals("CC") ? "Blue" : "Yellow";
            } else if ((!Character.isUpperCase(cA1[0]) && !Character.isUpperCase(cA1[1])
                    && (!Character.isUpperCase(cA2[0]) || !Character.isUpperCase(cA2[1])))
                    || (!Character.isUpperCase(cA1[0]) || !Character.isUpperCase(cA1[1])
                    && (!Character.isUpperCase(cA2[0]) && !Character.isUpperCase(cA2[1])))) { //one homo recess, one hetero
                cA = (int) Math.round(Math.random()) == 0 ? "cc" : "Cc";
                col = cA.equals("Cc") ? Color.BLUE : Color.YELLOW;
                colN = cA.equals("Cc") ? "Blue" : "Yellow";
            } else if (Character.isUpperCase(cA1[0]) && !Character.isUpperCase(cA1[1])
                    && Character.isUpperCase(cA2[0]) && !Character.isUpperCase(cA2[1])) { //both hetero
                int temp = new Random().nextInt(100);
                if (temp < 25) {
                    cA = "cc";
                    col = Color.YELLOW;
                    colN = "Yellow";
                } else if (temp < 75) {
                    cA = "Cc";
                    col = Color.BLUE;
                    colN = "Blue";
                } else {
                    cA = "CC";
                    col = Color.BLUE;
                    colN = "Blue";
                }
            }

            String sA = "";
            double sped = 0.0;

            char[] sA1 = new char[2];
            sA1[0] = a.speedAlleles.charAt(0);
            sA1[1] = a.speedAlleles.charAt(1);

            char[] sA2 = new char[2];
            sA2[0] = b.speedAlleles.charAt(0);
            sA2[1] = b.speedAlleles.charAt(1);

            if (Character.isUpperCase(sA1[0]) && Character.isUpperCase(sA1[1])
                    && Character.isUpperCase(sA2[0]) && Character.isUpperCase(sA2[1])) { //both homo dom
                sA = "SS";
                sped = 4.0;
            } else if (!Character.isUpperCase(sA1[0]) && !Character.isUpperCase(sA1[1])
                    && !Character.isUpperCase(sA2[0]) && !Character.isUpperCase(sA2[1])) { //both homo recess
                sA = "ss";
                sped = 3.0;
            } else if ((Character.isUpperCase(sA1[0]) && Character.isUpperCase(sA1[1])
                    && !Character.isUpperCase(sA2[0]) && !Character.isUpperCase(sA2[1]))
                    || (!Character.isUpperCase(sA1[0]) && !Character.isUpperCase(sA1[1])
                    && Character.isUpperCase(sA2[0]) && Character.isUpperCase(sA2[1]))) { //one homo dom, one homo recess
                sA = "Ss";
                sped = 4.0;
            } else if ((Character.isUpperCase(sA1[0]) && Character.isUpperCase(sA1[1])
                    && (!Character.isUpperCase(sA2[0]) || !Character.isUpperCase(sA2[1])))
                    || (!Character.isUpperCase(sA1[0]) || !Character.isUpperCase(sA1[1])
                    && (Character.isUpperCase(sA2[0]) && Character.isUpperCase(sA2[1])))) { //one homo dom, one hetero
                sA = (int) Math.round(Math.random()) == 0 ? "SS" : "Ss";
                sped = sA.equals("SS") ? 4.0 : 3.0;
            } else if ((!Character.isUpperCase(sA1[0]) && !Character.isUpperCase(sA1[1])
                    && (!Character.isUpperCase(sA2[0]) || !Character.isUpperCase(sA2[1])))
                    || (!Character.isUpperCase(sA1[0]) || !Character.isUpperCase(sA1[1])
                    && (!Character.isUpperCase(sA2[0]) && !Character.isUpperCase(sA2[1])))) { //one homo recess, one hetero
                sA = (int) Math.round(Math.random()) == 0 ? "ss" : "Ss";
                sped = sA.equals("Ss") ? 4.0 : 3.0;
            } else if (Character.isUpperCase(sA1[0]) && !Character.isUpperCase(sA1[1])
                    && Character.isUpperCase(sA2[0]) && !Character.isUpperCase(sA2[1])) { //both hetero
                int temp = new Random().nextInt(100);
                if (temp < 25) {
                    sA = "ss";
                    sped = 3.0;
                } else if (temp < 75) {
                    sA = "Ss";
                    sped = 4.0;
                } else {
                    sA = "SS";
                    sped = 4.0;
                }
            }

            String sIA = "";
            double siz = 0.0;

            char[] sIA1 = new char[2];
            sIA1[0] = a.sizeAlleles.charAt(0);
            sIA1[1] = a.sizeAlleles.charAt(1);

            char[] sIA2 = new char[2];
            sIA2[0] = b.sizeAlleles.charAt(0);
            sIA2[1] = b.sizeAlleles.charAt(1);

            if (Character.isUpperCase(sIA1[0]) && Character.isUpperCase(sIA1[1])
                    && Character.isUpperCase(sIA2[0]) && Character.isUpperCase(sIA2[1])) { //both homo dom
                sIA = "ZZ";
                siz = 10;
            } else if (!Character.isUpperCase(sA1[0]) && !Character.isUpperCase(sA1[1])
                    && !Character.isUpperCase(sA2[0]) && !Character.isUpperCase(sA2[1])) { //both homo recess
                sIA = "zz";
                siz = 15;
            } else if ((Character.isUpperCase(sIA1[0]) && Character.isUpperCase(sIA1[1])
                    && !Character.isUpperCase(sIA2[0]) && !Character.isUpperCase(sIA2[1]))
                    || (!Character.isUpperCase(sIA1[0]) && !Character.isUpperCase(sIA1[1])
                    && Character.isUpperCase(sIA2[0]) && Character.isUpperCase(sIA2[1]))) { //one homo dom, one homo recess
                sIA = "Zz";
                siz = 10;
            } else if ((Character.isUpperCase(sIA1[0]) && Character.isUpperCase(sIA1[1])
                    && (!Character.isUpperCase(sIA2[0]) || !Character.isUpperCase(sIA2[1])))
                    || (!Character.isUpperCase(sIA1[0]) || !Character.isUpperCase(sIA1[1])
                    && (Character.isUpperCase(sIA2[0]) && Character.isUpperCase(sIA2[1])))) { //one homo dom, one hetero
                sIA = (int) Math.round(Math.random()) == 0 ? "ZZ" : "Zz";
                siz = sIA.equals("ZZ") ? 10 : 15;
            } else if ((!Character.isUpperCase(sA1[0]) && !Character.isUpperCase(sA1[1])
                    && (!Character.isUpperCase(sA2[0]) || !Character.isUpperCase(sA2[1])))
                    || (!Character.isUpperCase(sA1[0]) || !Character.isUpperCase(sA1[1])
                    && (!Character.isUpperCase(sA2[0]) && !Character.isUpperCase(sA2[1])))) { //one homo recess, one hetero
                sIA = (int) Math.round(Math.random()) == 0 ? "zz" : "Zz";
                siz = sIA.equals("Zz") ? 10 : 15;
            } else if (Character.isUpperCase(sIA1[0]) && !Character.isUpperCase(sIA1[1])
                    && Character.isUpperCase(sIA2[0]) && !Character.isUpperCase(sIA2[1])) { //both hetero
                int temp = new Random().nextInt(100);
                if (temp < 25) {
                    sIA = "zz";
                    siz = 15;
                } else if (temp < 75) {
                    sIA = "Zz";
                    siz = 10;
                } else {
                    sIA = "ZZ";
                    siz = 10;
                }
            }

            Cuboid cuboid = new Cuboid(a.getTranslateX(), a.getTranslateY(), a.getTranslateZ() + 30, siz, sped, World.getInstance().names.get(new Random().nextInt(19)),
                    new Random().nextInt(100) < 70 ? "M" : "F", col);

            cuboid.colorAlleles = cA;
            cuboid.waterAlleles = wA;
            cuboid.speedAlleles = sA;
            cuboid.sizeAlleles = sIA;
            cuboid.setResistance(water);
            cuboid.color = colN;

//            System.out.println(cuboid.getName() + " child: " + cuboid.colorAlleles + " " + cuboid.sizeAlleles + " " + cuboid.speedAlleles + " " + cuboid.waterAlleles);
//            System.out.println(a.getName() + " " + a.colorAlleles + " " + a.sizeAlleles + " " + a.speedAlleles + " " + a.waterAlleles);
//            System.out.println(b.getName() + " " + b.colorAlleles + " " + b.sizeAlleles + " " + b.speedAlleles + " " + b.waterAlleles + "\n\n");
            return cuboid;
        }
        return null;
    }

    public void moveToBlock(Block b) {
        canMove = false;

        Box[][] blocks = World.getInstance().getBlocks();

        if (getDistance(b) < 3.0) {
            this.setTranslateX(b.getTranslateX());
            this.setTranslateY(b.getTranslateY());

            canMove = true;
        } else {
            for (int r = 0; r < blocks.length; r++) {
                for (int c = 0; c < blocks[r].length; c++) {
                    if (blocks[r][c].getTranslateX()
                            == (this.getTranslateX() + 20) - ((this.getTranslateX() + 20) % 20)
                            && blocks[r][c].getTranslateY()
                            == (this.getTranslateY() + 20) - ((this.getTranslateY() + 20) % 20)) {
                        this.setTranslateZ(blocks[r][c].getTranslateZ() - 30);
                    }
                }
            }

            if (World.getInstance().getBlocks()[(int) Math.round(super.getTranslateY() / 20)][(int) Math.round(super.getTranslateX() / 20)].isWater() && !this.waterResistant) {
                this.setTranslateX(this.getTranslateX() + (1.0 * Math.cos(getAngle(b))));
                this.setTranslateY(this.getTranslateY() + (1.0 * Math.sin(getAngle(b))));
            } else {
                this.setTranslateX(this.getTranslateX() + (speed * Math.cos(getAngle(b))));
                this.setTranslateY(this.getTranslateY() + (speed * Math.sin(getAngle(b))));
            }
        }
    }

    public ArrayList<Block> AStar(Block start, Block end) {
        super.setTranslateX(start.getTranslateX());
        super.setTranslateY(start.getTranslateY());
        super.setTranslateZ(start.getTranslateZ() - 30);

        ArrayList<Block> open = new ArrayList<>();
        ArrayList<Block> closed = new ArrayList<>();

        ArrayList<Block> path = new ArrayList<>();

        Block current = null;

        open.add(start);

        while (!open.isEmpty()) {
            Collections.sort(open);
            current = open.get(0);

            for (int i = 0; i < open.size(); i++) {
                if (open.get(i).fCost < current.fCost || open.get(i).fCost == current.fCost) {
                    if (open.get(i).hCost < current.hCost) {
                        current = open.get(i);
                    }
                }
            }

            open.remove(current);
            closed.add(current);

            if (current == end) {
                path = retracePath(start, end);

                path.add(0, start);

//                if (this.getGender().equals("M")) {
//                    for (Block b : path) {
//                        b.setColor(Color.GOLD);
//                    }
//                }
                return path;
            }

            int x = (int) Math.round(current.getTranslateX() / 20);
            int y = (int) Math.round(current.getTranslateY() / 20);
            ArrayList<Block> connections = new ArrayList<>();
            if (y == 0 && x == 0) {
                connections.add(World.getInstance().getBlocks()[y][x + 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y + 1][x + 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y + 1][x].meetsConditions());
            } else if (y == 0 && (x != 0 && x != World.getInstance().getBlocks().length - 1)) {
                connections.add(World.getInstance().getBlocks()[y][x - 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y + 1][x - 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y + 1][x].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y + 1][x + 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y][x + 1].meetsConditions());
            } else if (y == 0 && x == World.getInstance().getBlocks().length - 1) {
                connections.add(World.getInstance().getBlocks()[y][x - 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y + 1][x - 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y + 1][x].meetsConditions());
            } else if (x == 0 && (y != 0 && y != World.getInstance().getBlocks()[0].length - 1)) {
                connections.add(World.getInstance().getBlocks()[y - 1][x].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y - 1][x + 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y][x + 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y + 1][x + 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y + 1][x].meetsConditions());
            } else if (x == 0 && y == World.getInstance().getBlocks()[0].length - 1) {
                connections.add(World.getInstance().getBlocks()[x][y - 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[x + 1][y - 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[x + 1][y].meetsConditions());
            } else if (y == World.getInstance().getBlocks()[0].length - 1 && (x != 0 && x != World.getInstance().getBlocks().length - 1)) {
                connections.add(World.getInstance().getBlocks()[y][x - 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y - 1][x - 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y - 1][x].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y - 1][x + 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y][x + 1].meetsConditions());
            } else if (y == World.getInstance().getBlocks()[0].length - 1 && x == World.getInstance().getBlocks().length - 1) {
                connections.add(World.getInstance().getBlocks()[y][x - 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y - 1][x - 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y - 1][x].meetsConditions());
            } else if (x == World.getInstance().getBlocks().length - 1 && (y != 0 && y != World.getInstance().getBlocks()[0].length - 1)) {
                connections.add(World.getInstance().getBlocks()[y + 1][x].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y + 1][x - 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y][x - 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y - 1][x - 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y - 1][x].meetsConditions());
            } else {
                connections.add(World.getInstance().getBlocks()[y - 1][x - 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y - 1][x].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y - 1][x + 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y][x - 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y][x + 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y + 1][x - 1].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y + 1][x].meetsConditions());
                connections.add(World.getInstance().getBlocks()[y + 1][x + 1].meetsConditions());
            }

            connections.removeAll(Collections.singleton(null));

            for (Block b : connections) {
                if (!closed.contains(b)) {
                    double newCost = b.gCost + getDistance(current, b);

                    if (newCost < b.gCost || !open.contains(b)) {
                        b.gCost = newCost;
                        b.hCost = getDistance(b, end);
                        b.parent = current;

                        if (!open.contains(b)) {
                            open.add(b);
                        }
                    }
                }
            }
            connections.clear();
        }
        return null;
    }

    public ArrayList<Block> retracePath(Block start, Block end) {
        ArrayList<Block> path = new ArrayList<>();
        Block currentBlock = end;

        while (currentBlock != start) {
            path.add(currentBlock);
            currentBlock = currentBlock.parent;
        }

        Collections.reverse(path);

        return path;
    }

    public double getDistance(Block b) {
        return Math.sqrt((b.getTranslateY() - super.getTranslateY())
                * (b.getTranslateY() - super.getTranslateY())
                + (b.getTranslateX() - super.getTranslateX())
                * (b.getTranslateX() - super.getTranslateX()));
    }

    public double getDistance(Cuboid b) {
        return Math.sqrt((b.getTranslateY() - super.getTranslateY())
                * (b.getTranslateY() - super.getTranslateY())
                + (b.getTranslateX() - super.getTranslateX())
                * (b.getTranslateX() - super.getTranslateX()));
    }

    public double getDistance(Block b1, Block b2) {
        return Math.sqrt((b1.getTranslateY() - b2.getTranslateY())
                * (b1.getTranslateY() - b2.getTranslateY())
                + (b1.getTranslateX() - b2.getTranslateX())
                * (b1.getTranslateX() - b2.getTranslateX()));
    }

    public double getAngle(Block b) {
        return Math.atan2((b.getTranslateY() - super.getTranslateY()),
                (b.getTranslateX() - super.getTranslateX()));
    }

    public boolean getResistant() {
        return this.waterResistant;
    }

    public void setResistance(boolean hi) {
        this.waterResistant = hi;
    }

    public String getName() {
        return this.name;
    }

    public String getGender() {
        return this.gender;
    }

    public double getSpeed() {
        return this.speed;
    }

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public String getColor() {
        return this.color;
    }

    public void removePath() {
        for (Block b : path) {
            b.setColor(Color.WHITE);
        }
    }

    public boolean canMove() {
        return this.canMove;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public int compareTo(Cuboid o) {
        if (this.getDistance() < o.getDistance()) {
            return -1;
        } else if (o.getDistance() < this.getDistance()) {
            return 1;
        }
        return 0;
    }

}
