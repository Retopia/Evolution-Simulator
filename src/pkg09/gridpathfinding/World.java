package pkg09.gridpathfinding;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;

/**
 *
 * @author Preston Tang
 */
public class World extends Group {
    
    public final ArrayList<String> names = new ArrayList<String>() {
        {
            add("Avery");
            add("Riley");
            add("Jordan");
            add("Angel");
            add("ParkeAr");
            
            add("Sawyer");
            add("Peyton");
            add("Quinn");
            add("Blake");
            add("Hayden");
            
            add("Taylor");
            add("Alexis");
            add("Rowan");
            add("Charlie");
            add("Emerson");
            
            add("Finley");
            add("River");
            add("Ariel");
            add("Emery");
            add("Morgan");
            
        }
    };
    
    private final Block[][] blocks;
    private final ArrayList<Food> foods;
    private final ArrayList<Cuboid> cuboids;
    private static World instance;
    
    public final int size = 20;
    
    private World() {
        blocks = new Block[75][75];
        foods = new ArrayList<>();
        cuboids = new ArrayList<>();
    }
    
    public static World getInstance() {
        if (instance == null) {
            instance = new World();
        }
        return instance;
    }
    
    public void init(FastNoise fn, String biome) {
        for (int r = 0; r < blocks.length; r++) {
            for (int c = 0; c < blocks[r].length; c++) {
                Block rec = new Block();
                rec.setWidth(size);
                rec.setHeight(size);
                rec.setTranslateX(size * c);
                rec.setTranslateY(size * r);
                rec.setTranslateZ(-size * Math.abs(Math.floor(fn.GetSimplexFractal(r, c) * 20)));
                
                switch (biome) {
                    case "Classic":
                        if (Math.abs(rec.getTranslateZ() / 20) < 2) {
                            rec.setTranslateZ(-20);
                            rec.setColor(Color.CORNFLOWERBLUE);
                            rec.setWater(true);
                        } else if (Math.abs(rec.getTranslateZ() / 20) < 3) {
                            rec.setColor(Color.LEMONCHIFFON);
                        } else if (Math.abs(rec.getTranslateZ() / 20) < 12) {
                            rec.setColor(Color.MEDIUMSEAGREEN);
                        } else {
                            rec.setColor(Color.WHITESMOKE);
                        }
                        
                        rec.setDepth(size);
                        rec.setCullFace(CullFace.BACK);
                        rec.setDrawMode(DrawMode.FILL);
                        
                        if (new Random().nextInt(100) < 15 && Math.abs(rec.getTranslateZ() / 20) > 2) {
                            rec.setLava(true);
                        }
                        
                        super.getChildren().add(rec);
                        blocks[r][c] = rec;
                        
                        if (new Random().nextInt(150) < 1 && !rec.isLava() && !rec.isWater()) {
                            rec.setFood(true);
                            Food f = new Food(rec.getTranslateX(), rec.getTranslateY(), rec.getTranslateZ(), Color.RED);
                            foods.add(f);
                            super.getChildren().add(f);
                        } else {
                            rec.setFood(false);
                        }
                        
                        if (new Random().nextInt(250) < 1 && !rec.isLava() && !rec.isWater()) {
                            String gender = (int) Math.round(Math.random()) == 0 ? "M" : "F";
                            
                            Cuboid cuboid = new Cuboid(rec.getTranslateX(), rec.getTranslateY(), rec.getTranslateZ(), names.get(new Random().nextInt(19)), gender);
                            cuboids.add(cuboid);
                            super.getChildren().add(cuboid);
                        }
                        break;
                    
                    case "Desert":
                        if (Math.abs(rec.getTranslateZ() / 20) < 2) {
                            rec.setTranslateZ(-20);
                            rec.setColor(Color.CORNFLOWERBLUE);
                            rec.setWater(true);
                        } else {
                            rec.setColor(Color.LEMONCHIFFON);
                        }
                        
                        rec.setDepth(size);
                        rec.setCullFace(CullFace.BACK);
                        rec.setDrawMode(DrawMode.FILL);
                        
                        if (new Random().nextInt(100) < 15 && Math.abs(rec.getTranslateZ() / 20) > 2) {
                            rec.setLava(true);
                        }
                        
                        super.getChildren().add(rec);
                        blocks[r][c] = rec;
                        
                        if (new Random().nextInt(150) < 1 && !rec.isLava() && !rec.isWater()) {
                            rec.setFood(true);
                            Food f = new Food(rec.getTranslateX(), rec.getTranslateY(), rec.getTranslateZ(), Color.RED);
                            foods.add(f);
                            super.getChildren().add(f);
                        } else {
                            rec.setFood(false);
                        }
                        
                        if (new Random().nextInt(250) < 1 && !rec.isLava() && !rec.isWater()) {
                            String gender = (int) Math.round(Math.random()) == 0 ? "M" : "F";
                            
                            Cuboid cuboid = new Cuboid(rec.getTranslateX(), rec.getTranslateY(), rec.getTranslateZ(), names.get(new Random().nextInt(19)), gender);
                            cuboids.add(cuboid);
                            super.getChildren().add(cuboid);
                        }
                        break;
                    
                    case "Rocky":
                        if (Math.abs(rec.getTranslateZ() / 20) < 2) {
                            rec.setTranslateZ(-20);
                            rec.setColor(Color.CORNFLOWERBLUE);
                            rec.setWater(true);
                        } else if (Math.abs(rec.getTranslateZ() / 20) < 3) {
                            rec.setColor(Color.LEMONCHIFFON);
                        } else if (Math.abs(rec.getTranslateZ() / 20) < 4) {
                            rec.setColor(Color.MEDIUMSEAGREEN);
                        } else {
                            rec.setColor(Color.rgb(155, 155, 155));
                        }
                        
                        rec.setDepth(size);
                        rec.setCullFace(CullFace.BACK);
                        rec.setDrawMode(DrawMode.FILL);
                        
                        if (new Random().nextInt(100) < 15 && Math.abs(rec.getTranslateZ() / 20) > 2) {
                            rec.setLava(true);
                        }
                        
                        super.getChildren().add(rec);
                        blocks[r][c] = rec;
                        
                        if (new Random().nextInt(150) < 1 && !rec.isLava() && !rec.isWater()) {
                            rec.setFood(true);
                            Food f = new Food(rec.getTranslateX(), rec.getTranslateY(), rec.getTranslateZ(), Color.RED);
                            foods.add(f);
                            super.getChildren().add(f);
                        } else {
                            rec.setFood(false);
                        }
                        
                        if (new Random().nextInt(250) < 1 && !rec.isLava() && !rec.isWater()) {
                            String gender = (int) Math.round(Math.random()) == 0 ? "M" : "F";
                            
                            Cuboid cuboid = new Cuboid(rec.getTranslateX(), rec.getTranslateY(), rec.getTranslateZ(), names.get(new Random().nextInt(19)), gender);
                            cuboids.add(cuboid);
                            super.getChildren().add(cuboid);
                        }
                        break;
                    
                    case "Snow":
                        if (Math.abs(rec.getTranslateZ() / 20) < 2) {
                            rec.setTranslateZ(-20);
                            rec.setColor(Color.CORNFLOWERBLUE);
                            rec.setWater(true);
                        } else {
                            rec.setColor(Color.WHITESMOKE);
                        }
                        
                        rec.setDepth(size);
                        rec.setCullFace(CullFace.BACK);
                        rec.setDrawMode(DrawMode.FILL);
                        
                        if (new Random().nextInt(100) < 15 && Math.abs(rec.getTranslateZ() / 20) > 2) {
                            rec.setLava(true);
                        }
                        
                        super.getChildren().add(rec);
                        blocks[r][c] = rec;
                        
                        if (new Random().nextInt(150) < 1 && !rec.isLava() && !rec.isWater()) {
                            rec.setFood(true);
                            Food f = new Food(rec.getTranslateX(), rec.getTranslateY(), rec.getTranslateZ(), Color.RED);
                            foods.add(f);
                            super.getChildren().add(f);
                        } else {
                            rec.setFood(false);
                        }
                        
                        if (new Random().nextInt(250) < 1 && !rec.isLava() && !rec.isWater()) {
                            String gender = (int) Math.round(Math.random()) == 0 ? "M" : "F";
                            
                            Cuboid cuboid = new Cuboid(rec.getTranslateX(), rec.getTranslateY(), rec.getTranslateZ(), names.get(new Random().nextInt(19)), gender);
                            cuboids.add(cuboid);
                            super.getChildren().add(cuboid);
                        }
                        break;
                    
                    case "Hell":
                        if (Math.abs(rec.getTranslateZ() / 20) < 2) {
                            rec.setTranslateZ(-20);
                            rec.setLava(true);
                        } else if (Math.abs(rec.getTranslateZ() / 20) < 3) {
                            rec.setColor(Color.rgb(44, 38, 0));
                        } else {
                            rec.setColor(Color.BROWN);
                        }
                        
                        rec.setDepth(size);
                        rec.setCullFace(CullFace.BACK);
                        rec.setDrawMode(DrawMode.FILL);
                        
                        super.getChildren().add(rec);
                        blocks[r][c] = rec;
                        
                        if (new Random().nextInt(150) < 1 && !rec.isLava() && !rec.isWater()) {
                            rec.setFood(true);
                            Food f = new Food(rec.getTranslateX(), rec.getTranslateY(), rec.getTranslateZ(), Color.RED);
                            foods.add(f);
                            super.getChildren().add(f);
                        } else {
                            rec.setFood(false);
                        }
                        
                        if (new Random().nextInt(250) < 1 && !rec.isLava() && !rec.isWater()) {
                            String gender = (int) Math.round(Math.random()) == 0 ? "M" : "F";
                            
                            Cuboid cuboid = new Cuboid(rec.getTranslateX(), rec.getTranslateY(), rec.getTranslateZ(), names.get(new Random().nextInt(19)), gender);
                            cuboids.add(cuboid);
                            super.getChildren().add(cuboid);
                        }
                        break;
                    
                    case "Ocean":
                        
                        if (Math.abs(rec.getTranslateZ() / 20) < 4) {
                            rec.setTranslateZ(-60);
                            rec.setColor(Color.CORNFLOWERBLUE);
                            rec.setWater(true);
                        } else {
                            rec.setColor(Color.MEDIUMSEAGREEN);
                        }
                        
                        rec.setDepth(size);
                        rec.setCullFace(CullFace.BACK);
                        rec.setDrawMode(DrawMode.FILL);
                        
                        if (new Random().nextInt(100) < 15 && Math.abs(rec.getTranslateZ() / 20) > 2 && !rec.isWater()) {
                            rec.setLava(true);
                        }
                        
                        super.getChildren().add(rec);
                        blocks[r][c] = rec;
                        
                        if (new Random().nextInt(150) < 1 && !rec.isLava() && !rec.isWater()) {
                            rec.setFood(true);
                            Food f = new Food(rec.getTranslateX(), rec.getTranslateY(), rec.getTranslateZ(), Color.RED);
                            foods.add(f);
                            super.getChildren().add(f);
                        } else {
                            rec.setFood(false);
                        }
                        
                        if (new Random().nextInt(250) < 1 && !rec.isLava() && !rec.isWater()) {
                            String gender = (int) Math.round(Math.random()) == 0 ? "M" : "F";
                            
                            Cuboid cuboid = new Cuboid(rec.getTranslateX(), rec.getTranslateY(), rec.getTranslateZ(), names.get(new Random().nextInt(19)), gender);
                            cuboids.add(cuboid);
                            super.getChildren().add(cuboid);
                        }
                        break;
                    
                    default:
                        if (Math.abs(rec.getTranslateZ() / 20) < 2) {
                            rec.setTranslateZ(-20);
                            rec.setColor(Color.CORNFLOWERBLUE);
                            rec.setWater(true);
                        } else if (Math.abs(rec.getTranslateZ() / 20) < 3) {
                            rec.setColor(Color.LEMONCHIFFON);
                        } else if (Math.abs(rec.getTranslateZ() / 20) < 12) {
                            rec.setColor(Color.MEDIUMSEAGREEN);
                        } else {
                            rec.setColor(Color.WHITESMOKE);
                        }
                        
                        rec.setDepth(size);
                        rec.setCullFace(CullFace.BACK);
                        rec.setDrawMode(DrawMode.FILL);
                        
                        if (new Random().nextInt(100) < 15 && Math.abs(rec.getTranslateZ() / 20) > 2) {
                            rec.setLava(true);
                        }
                        
                        super.getChildren().add(rec);
                        blocks[r][c] = rec;
                        
                        if (new Random().nextInt(150) < 1 && !rec.isLava() && !rec.isWater()) {
                            rec.setFood(true);
                            Food f = new Food(rec.getTranslateX(), rec.getTranslateY(), rec.getTranslateZ(), Color.RED);
                            foods.add(f);
                            super.getChildren().add(f);
                        } else {
                            rec.setFood(false);
                        }
                        
                        if (new Random().nextInt(250) < 1 && !rec.isLava() && !rec.isWater()) {
                            String gender = (int) Math.round(Math.random()) == 0 ? "M" : "F";
                            
                            Cuboid cuboid = new Cuboid(rec.getTranslateX(), rec.getTranslateY(), rec.getTranslateZ(), names.get(new Random().nextInt(19)), gender);
                            cuboids.add(cuboid);
                            super.getChildren().add(cuboid);
                        }
                        break;
                    
                }
                
            }
        }
    }
    
    public Block[][] getBlocks() {
        return this.blocks;
    }
    
    public ArrayList<Cuboid> getCuboids() {
        return this.cuboids;
    }
    
    public ArrayList<Food> getFoods() {
        return this.foods;
    }
    
    public double getDistance(Block a, Block b) {
        return Math.sqrt((b.getTranslateY() - a.getTranslateY())
                * (b.getTranslateY() - a.getTranslateY())
                + (b.getTranslateX() - a.getTranslateX())
                * (b.getTranslateX() - a.getTranslateX()));
    }
}
