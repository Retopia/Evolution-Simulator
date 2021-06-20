# Evolution-Simulator
A Basic Evolution Simulator using JavaFX 3D
This is a project I made in Honors Artificial Intelligence class in my Junior year of High School.

![Project View](https://i.imgur.com/xkVzkOf.png)
This project has music in the background! Taken from here.

Essentially, there are "cuboids" populated throughout the terrain, each having 5 pairs of alleles, and each pair controls a genetic trait (I hope I'm using these terms right, it's been a year since Biology). What the genes control are pretty self explanatory, and the only thing I really need to clarify is "Water Resistance", which controls if they slow down in water or not. Also size doesn't matter.

After running it for a few minutes, you should see that most of the cuboids have evolved to have faster speed and water resistance, since the slower ones have died out.

The code is pretty messy, but it's been a year since I created this project and now I focus a lot more on code readability and the quality of the code.

Note that all we had to do for this project was to implement some sort of pathfinding, so I really went overboard on this one! The lava scattered throughout the map is because we needed some sort of obstacle as part of the project's requirements.

Things I would change if I made a 2nd version of this project:
* Make it so that each cuboid has a limited range of what it can see
* Make the cuboids lose energy on a time basis, not an action basis
