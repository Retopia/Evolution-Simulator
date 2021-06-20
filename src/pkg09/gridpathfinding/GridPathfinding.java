package pkg09.gridpathfinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.ChoiceDialog;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.PAGE_DOWN;
import static javafx.scene.input.KeyCode.PAGE_UP;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Preston Tang
 */
public class GridPathfinding extends Application {

    private PerspectiveCamera camera;
    private PointLight single;

    private boolean camW, camA, camS, camD, camPgDn, camPgUp;

    public long startingTime;

    private boolean ran;
    private boolean lost;

    private int death;
    private int birth;

    @Override
    public void start(Stage primaryStage) {
        List<String> choices = new ArrayList<>();
        choices.add("Classic");
        choices.add("Desert");
        choices.add("Rocky");
        choices.add("Snow");
        choices.add("Hell");
        choices.add("Ocean");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Classic", choices);
        dialog.setTitle("Pick A Biome");
        dialog.setHeaderText("");
        dialog.setContentText("Choose your biome: ");

        Optional<String> result = dialog.showAndWait();

        ran = false;
        lost = false;
        death = 0;
        birth = 0;

        Clip clip;
        try {
            clip = AudioSystem.getClip();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("mochi.wav"));
            clip.open(audioInputStream);
            clip.loop(Integer.MAX_VALUE);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
        }

        startingTime = System.nanoTime();

        camera = new PerspectiveCamera(true);
        camera.setTranslateX(300);
        camera.setTranslateY(300);
        camera.setTranslateZ(-900);
        camera.getTransforms().add(new Rotate(15, Rotate.X_AXIS));
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);

        World root = World.getInstance();

        single = new PointLight();
        single.setTranslateX(450);
        single.setTranslateY(2000);
        single.setTranslateZ(-4000);

        root.getChildren().add(single);

        root.getChildren().add(camera);

        FastNoise fn = new FastNoise();
        fn.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
        fn.SetSeed(new Random().nextInt(21114693));
        fn.SetFrequency(0.011f);
        fn.SetFractalOctaves(2);

        root.init(fn, result.get());

        SubScene subScene = new SubScene(root, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.TRANSPARENT);
        subScene.setCamera(camera);

        Text info = new Text("Starting Cuboids: " + root.getCuboids().size()
                + "\nStarting Foods: " + root.getFoods().size());
        info.setX(3);
        info.setY(16);

        Text info2 = new Text("Current Cuboids: " + root.getCuboids().size()
                + "\nCurrent Foods: " + root.getFoods().size()
                + "\nDeaths: " + death + "\nBirths: " + birth);
        info2.setX(3);
        info2.setY(88);

        Rectangle base = new Rectangle();
        base.setWidth(175);
        base.setHeight(75);
        base.setFill(Color.GRAY);

        Rectangle base2 = new Rectangle();
        base2.setY(75);
        base2.setWidth(120);
        base2.setHeight(70);
        base2.setFill(Color.WHITESMOKE);

        for (Cuboid cube : root.getCuboids()) {
            cube.setOnMouseEntered((MouseEvent t) -> {
                info.setText("Name: " + cube.getName() + "        Speed: " + cube.getSpeed() + " " + cube.speedAlleles
                        + "\nGender: " + cube.getGender() + "        Color: " + cube.getColor() + " " + cube.colorAlleles
                        + "\nEnergy: " + cube.getEnergy() + "        Size: " + cube.size + " " + cube.sizeAlleles
                        + "\nWater Resistant: " + cube.getResistant() + " " + cube.waterAlleles);
            });
        }
        Pane p = new Pane();
        p.getChildren().add(subScene);

        p.getChildren().addAll(base, info, base2, info2);

        for (Cuboid c : root.getCuboids()) {
            c.i = 0;

            for (Food f : root.getFoods()) {
                c.foo.add(f);
                f.setDistance(getDistance(c, f));
            }

            Collections.sort(c.foo);

            c.path = c.AStar(root.getBlocks()[(int) c.getTranslateY() / 20][(int) c.getTranslateX() / 20], root.getBlocks()[(int) c.foo.get(0).getTranslateY() / 20][(int) c.foo.get(0).getTranslateX() / 20]);
        }
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                info2.setText("Current Cuboids: " + root.getCuboids().size()
                        + "\nCurrent Foods: " + root.getFoods().size()
                        + "\nDeaths: " + death + "\nBirths: " + birth);

                if ((System.nanoTime() - startingTime) / 1000000000L % 10 == 0) { //Every 10 seconds
                    if (!ran) {
                        ran = true;

                        ArrayList<Food> temp = new ArrayList<>(root.getFoods());

                        for (int r = 0; r < root.getBlocks().length; r++) {
                            for (int c = 0; c < root.getBlocks()[r].length; c++) {
                                if (new Random().nextInt(150) < 1 && !root.getBlocks()[r][c].isLava() && !root.getBlocks()[r][c].isWater() && !root.getBlocks()[r][c].hasFood() && root.getFoods().size() < 101) {
                                    root.getBlocks()[r][c].setFood(true);
                                    Food f = new Food(root.getBlocks()[r][c].getTranslateX(), root.getBlocks()[r][c].getTranslateY(), root.getBlocks()[r][c].getTranslateZ(), Color.RED);
                                    root.getFoods().add(f);
                                } else {
                                    if (!root.getBlocks()[r][c].hasFood()) {
                                        root.getBlocks()[r][c].setFood(false);
                                    }
                                }
                            }
                        }

                        ArrayList<Food> temp2 = new ArrayList<>(root.getFoods());
                        temp2.removeAll(temp);

                        root.getChildren().addAll(temp2);
                    }
                } else {
                    ran = false;
                }

                ArrayList<Cuboid> values = new ArrayList<>();
                for (Cuboid c : root.getCuboids()) {
                    if (c.getEnergy() < 1) {
                        death++;
                        values.add(c);
                    }
                }

                for (Cuboid c1 : values) {
                    root.getBlocks()[(int) c1.getTranslateY() / 20][(int) c1.getTranslateX() / 20].setColor(Color.DARKRED);
                }
                root.getCuboids().removeAll(values);
                root.getChildren().removeAll(values);

                ArrayList<Cuboid> toAdd = new ArrayList<>();

                for (Cuboid c : root.getCuboids()) {

                    for (Cuboid cube : root.getCuboids()) {
                        cube.setOnMouseEntered((MouseEvent t) -> {
                            info.setText("Name: " + cube.getName() + "        Speed: " + cube.getSpeed() + " " + cube.speedAlleles
                                    + "\nGender: " + cube.getGender() + "        Color: " + cube.getColor() + " " + cube.colorAlleles
                                    + "\nEnergy: " + cube.getEnergy() + "        Size: " + cube.size + " " + cube.sizeAlleles
                                    + "\nWater Resistant: " + cube.getResistant() + " " + cube.waterAlleles);
                        });
                    }

                    if (c.getGender().equals("F") && c.getEnergy() > 3) {
                        c.mating = true;
                        //stop moving
                    } else if (c.getGender().equals("M") && (c.getEnergy() > 3)) {
                        if ((c.mPath != null && !c.mPath.isEmpty()) && c.mI < c.mPath.size() && getDistance(c, c.mPath.get(c.mI)) < 28.3 || (c.getTranslateX() % 20 != 0
                                && c.getTranslateY() % 20 != 0)) { //Find female
                            c.moveToBlock(c.mPath.get(c.mI));
                        } else {
                            if (c.mPath != null && !c.mPath.isEmpty()) {
                                for (Cuboid cu : root.getCuboids()) {
                                    if (cu.getGender().equals("F")
                                            && Math.floor(cu.getTranslateX()) == c.mPath.get(c.mPath.size() - 1).getTranslateX()
                                            && Math.floor(cu.getTranslateY()) == c.mPath.get(c.mPath.size() - 1).getTranslateY()) {
                                        Cuboid cuboid = c.mate(c, cu);
                                        if (cuboid != null) {
                                            birth++;
                                            toAdd.add(cuboid);
                                            cu.setEnergy(cu.getEnergy() - 1);
                                            for (Food f : root.getFoods()) {
                                                cuboid.foo.add(f);
                                                f.setDistance(getDistance(cuboid, f));
                                            }

                                            Collections.sort(cuboid.foo);

                                            if (!cuboid.foo.isEmpty()) {
                                                cuboid.path = cuboid.AStar(root.getBlocks()[(int) cuboid.getTranslateY() / 20][(int) cuboid.getTranslateX() / 20], root.getBlocks()[(int) cuboid.foo.get(0).getTranslateY() / 20][(int) cuboid.foo.get(0).getTranslateX() / 20]);
                                            }
                                        }
                                    }
                                }

                                c.mPath.clear();
                                c.mates.clear();
                                c.mI = 0;
                            }

                            for (Cuboid f : root.getCuboids()) {
                                if (f.getGender().equals("F") && f.mating) {
                                    c.mates.add(f);
                                    f.setDistance(getDistance(c, f));
                                }
                            }

                            Collections.sort(c.mates);

                            if (!c.mates.isEmpty()) {
                                c.setEnergy(c.getEnergy() - 1);
                                c.mPath = c.AStar(root.getBlocks()[(int) c.getTranslateY() / 20][(int) c.getTranslateX() / 20], root.getBlocks()[(int) c.mates.get(0).getTranslateY() / 20][(int) c.mates.get(0).getTranslateX() / 20]);
                            }
                        }
                        if (c.canMove()) {
                            c.mI++;
                        }
                    } else if (c.path != null && !c.path.isEmpty()) {
                        if (c.i < c.path.size() && (c.path.get(c.path.size() - 1).hasFood() && getDistance(c, c.path.get(c.i)) < 28.3 || (c.getTranslateX() % 20 != 0
                                && c.getTranslateY() % 20 != 0))) { //Find food
                            c.mating = false;
                            c.moveToBlock(c.path.get(c.i));
                        } else {
                            if (c.path.get(c.path.size() - 1).hasFood()
                                    && c.getTranslateX() == c.path.get(c.path.size() - 1).getTranslateX()
                                    && c.getTranslateY() == c.path.get(c.path.size() - 1).getTranslateY()) {
                                c.path.get(c.path.size() - 1).setFood(false);
                                root.getFoods().remove(c.foo.get(0));
                                root.getChildren().remove(c.foo.get(0));
                                c.setEnergy(c.getEnergy() + 3);
                            }
                            c.path.clear();
                            c.foo.clear();
                            c.i = 0;

                            for (Food f : root.getFoods()) {
                                c.foo.add(f);
                                f.setDistance(getDistance(c, f));
                            }

                            Collections.sort(c.foo);

                            if (!c.foo.isEmpty()) {
                                c.setEnergy(c.getEnergy() - 1);
                                c.path = c.AStar(root.getBlocks()[(int) c.getTranslateY() / 20][(int) c.getTranslateX() / 20], root.getBlocks()[(int) c.foo.get(0).getTranslateY() / 20][(int) c.foo.get(0).getTranslateX() / 20]);
                            }
                        }

                        if (c.canMove()) {
                            c.i++;
                        }
                    }
                }

                for (Cuboid cub : toAdd) {
                    root.getChildren().add(cub);
                    root.getCuboids().add(cub);
                }

                single.setTranslateX(camera.getTranslateX() + 1000);
                single.setTranslateY(camera.getTranslateY());
                //<editor-fold defaultstate="collapsed" desc="camera movement">
                if (camW) {
                    camera.setTranslateY(camera.getTranslateY() - 15);
                }
                if (camA) {
                    camera.setTranslateX(camera.getTranslateX() - 15);
                }
                if (camS) {
                    camera.setTranslateY(camera.getTranslateY() + 15);
                }
                if (camD) {
                    camera.setTranslateX(camera.getTranslateX() + 15);
                }
                if (camPgUp && camera.getTranslateZ() >= -3000) {
                    camera.setTranslateZ(camera.getTranslateZ() - 15);
                }
                if (camPgDn) {
                    camera.setTranslateZ(camera.getTranslateZ() + 15);
                }
//</editor-fold>
            }
        };
        timer.start();

        Scene scene = new Scene(p, 800, 600, true);
        //<editor-fold defaultstate="collapsed" desc="camera input checking">
        scene.setOnKeyPressed(
                (KeyEvent event) -> {
                    switch (event.getCode()) {
                        case UP:
                            camW = true;
                            break;
                        case DOWN:
                            camS = true;
                            break;
                        case LEFT:
                            camA = true;
                            break;
                        case RIGHT:
                            camD = true;
                            break;
                        case PAGE_UP:
                            camPgUp = true;
                            break;
                        case PAGE_DOWN:
                            camPgDn = true;
                        default:
                            break;
                    }
                }
        );

        scene.setOnKeyReleased(
                (KeyEvent event) -> {
                    switch (event.getCode()) {
                        case UP:
                            camW = false;
                            break;
                        case DOWN:
                            camS = false;
                            break;
                        case LEFT:
                            camA = false;
                            break;
                        case RIGHT:
                            camD = false;
                            break;
                        case PAGE_UP:
                            camPgUp = false;
                            break;
                        case PAGE_DOWN:
                            camPgDn = false;
                        default:
                            break;
                    }
                }
        //</editor-fold>
        );

        primaryStage.setTitle(
                "08-Pathfinding (Genetic Evolution Simulation) - By Preston Tang");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public double getDistance(Cuboid a, Food b) {
        return Math.sqrt((b.getTranslateY() - a.getTranslateY())
                * (b.getTranslateY() - a.getTranslateY())
                + (b.getTranslateX() - a.getTranslateX())
                * (b.getTranslateX() - a.getTranslateX()));
    }

    public double getDistance(Cuboid a, Cuboid b) {
        return Math.sqrt((b.getTranslateY() - a.getTranslateY())
                * (b.getTranslateY() - a.getTranslateY())
                + (b.getTranslateX() - a.getTranslateX())
                * (b.getTranslateX() - a.getTranslateX()));
    }

    public double getDistance(Cuboid a, Block b) {
        return Math.sqrt((b.getTranslateY() - a.getTranslateY())
                * (b.getTranslateY() - a.getTranslateY())
                + (b.getTranslateX() - a.getTranslateX())
                * (b.getTranslateX() - a.getTranslateX()));
    }

    public static void main(String[] args) {
        launch(args);
    }

}
