import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;

public class Search {


    public static void main(String[] args){
        Location p = new Location(7,8);
        System.out.println(p);
        System.out.println(p.row());
        System.out.println(p.column());
        Location q = new Location(7,8);

        Set<Location> visitedLocations = new HashSet<>();
        visitedLocations.add(p);
        System.out.println(visitedLocations.contains(q));

        char[][] grid = {
            {'o', 'o', 'o', 'o', 'c', 'w', 'c', 'o'},
            {'w', 'o', 'o', 'w', 'w', 'c', 'w', 'o'},
            {'o', 'o', 'o', 'o', 'R', 'w', 'o', 'o'},
            {'o', 'o', 'w', 'w', 'w', 'o', 'o', 'o'},
            {'o', 'o', 'o', 'o', 'c', 'o', 'o', 'o'}
            };
        System.out.println(nearestCheese(grid));
        
        // Set<int[]> visited = new HashSet<>();
        // int[] a = new int[]{6,7};
        // int[] b = new int[]{2,1};
        // int[] c = new int[]{6,7};

        // visited.add(a);

        // System.out.println(visited.contains(a));
        // System.out.println(visited.contains(b));
        // System.out.println(visited.contains(c));
    }
    //TODO: change comment to reflect we're returning path instead of final location now
     /**
     * Finds the location of the nearest reachable cheese from the rat's position.
     * Returns a 2-element int array: [row, col] of the closest 'c'. If there are multiple
     * cheeses that are tied for the same shortest distance to reach, return
     * any one of them.
     * 
     * 'R' - the rat's starting position (exactly one)
     * 'o' - open space the rat can walk on
     * 'w' - wall the rat cannot pass through
     * 'c' - cheese that the rat wants to reach
     * 
     * If no rat is found, throws EscapedRatException.
     * If more than one rat is found, throws CrowdedMazeException.
     * If no cheese is reachable, throws HungryRatException
     *
     * oooocwco
     * woowwcwo
     * ooooRwoo
     * oowwwooo
     * oooocooo
     *
     * The method will return [0,4] as the nearest cheese.
     *
     * @param maze 2D char array representing the maze
     * @return int[] location of the closest cheese in row, column format
     * @throws EscapedRatException if there is no rat in the maze
     * @throws CrowdedMazeException if there is more than one rat in the maze
     * @throws HungryRatException if there is no reachable cheese
     */
    public static List<Location> nearestCheese(char[][] maze) throws EscapedRatException, CrowdedMazeException, HungryRatException {
        Location start = ratLocation(maze);

        Set<Location> hasBeenQueued = new HashSet<>();
        Queue<Location> queue = new ArrayDeque<>();

        Map<Location, Location> prevs = new HashMap<>();

        queue.add(start);

        while(!queue.isEmpty()){
            //remove better than poll here 
            Location current =  queue.remove();

            if(hasBeenQueued.contains(current)){
                continue;
            }
            hasBeenQueued.add(current);


            if(maze[current.row()][current.column()] == 'c') {
                Location step = current;
                List<Location> path = new LinkedList<>();
                //return new int []{current.row(), current.column()};
                while(step != null){
                    path.addFirst(step);
                    step.prevs.get(step);
                }

                return path;
            }

            List<Location> neighbors = possibleMoves(maze, current);
            for(Location neighbor : neighbors){
                if(!hasBeenQueued.contains(current)) {
                    hasBeenQueued.add(current);
                    prevs.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        
        }
        throw new HungryRatException();

    }

    public static List<Location> possibleMoves(char[][] maze, Location current){
       int[][] moves = new int[][] {
            {-1,0}, //up
            {1,0},  //down
            {0,-1}, //left
            {0,1}  //right
       };

       List<Location> validMoves = new ArrayList<>();

       for(int[] move : moves){
        Location possibleMoves = new Location(current.row()+move[0],
                                    current.column() + move[1]);
        if(possibleMoves.row()>=0 && possibleMoves.row()<maze[0].length &&
            maze[possibleMoves.row()][possibleMoves.column()] != 'w') {
                validMoves.add(possibleMoves);
    }
       }
    }

    public static Location ratLocation(char[][] maze){
        Location rat = null;

        for(int r = 0; r < maze.length; r++){
            for(int c = 0; c < maze[r].length; c++){
                if(maze[r][c] == 'R') {
                    if(rat != null){
                        throw new CrowdedMazeException();
                    }
                    rat = new Location(r, c);
            }
        }
    }
        
        if(rat == null) throw new EscapedRatException();
        return rat;
    }


}