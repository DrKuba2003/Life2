package com.example.android.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Grid {

    public static final String TYPE_MAN = "M";
    public static final String TYPE_WOMAN = "W";
    private static final int minPeopleOnStart = 2;
    private static  final int maxPeopleOnStart = 6;

    private int sideLength;
    private String[][] grid;

    public Grid(int length){
        sideLength = length;
        grid = new String[sideLength][sideLength];
        int random = new Random().nextInt((maxPeopleOnStart-minPeopleOnStart) + 1) + minPeopleOnStart;
        while (random>0){
            int x = new Random().nextInt(sideLength);
            int y = new Random().nextInt(sideLength);
            if(grid[x][y]==null){
               if(new Random().nextBoolean()){
                   grid[x][y] = TYPE_MAN;
               }else {
                   grid[x][y] = TYPE_WOMAN;
               }
               random--;
            }
        }
    }


    public void makeMove(){
        String[][] updatedGrid = new String[sideLength][sideLength];
        for(int x=0; x<sideLength; x++){
            for(int y=0; y<sideLength; y++){
                updatedGrid[x][y] = grid[x][y];
            }
        }

        for(int x=0; x<sideLength; x++){
            for(int y=0; y<sideLength; y++){
                String type = grid[x][y];
                if(type!=null){
                    List<int[]> nearFields = getFieldsNearAround(x, y);
                    Collections.shuffle(nearFields);

                    for(int[] field : nearFields){
                        int posX = field[0];
                        int posY = field[1];
                        if(posX==x && posY==y) {
                            break;
                        }else if(updatedGrid[posX][posY]==null && grid[posX][posY]==null){
                            updatedGrid[posX][posY] = type;
                            updatedGrid[x][y] = null;
                            break;
                        }
                    }
                }
            }
        }
        for(int x=0; x<sideLength; x++){
            for(int y=0; y<sideLength; y++){
                grid[x][y] = updatedGrid[x][y];
            }
        }
    }

    public void assassinatePeopleNearBy(){
        for(int x=0; x<sideLength; x++){
            for(int y=0; y<sideLength; y++){
                String gender = grid[x][y];
                if(gender!=null){
                    List<int[]> nearFields = getFieldsNearAround(x, y);

                    for(int[] field : nearFields){
                        int posX = field[0];
                        int posY = field[1];
                        String nearType = grid[posX][posY];
                        if((posX!=x || posY!=y) && (nearType!=null) && (nearType.equals(gender))) {
                            grid[posX][posY] = null;
                            grid[x][y] = null;
                            break;
                        }
                    }
                }
            }
        }
    }

    public void makeChildren(){
        int noBabies = 0;
        for(int x=0; x<sideLength; x++){
            for(int y=0; y<sideLength; y++){
                String type = grid[x][y];
                if(type!=null){
                    List<int[]> nearFields = getFieldsNearAround(x, y);

                    for(int[] field : nearFields){
                        int posX = field[0];
                        int posY = field[1];
                        String nearType = grid[posX][posY];
                        if((posX!=x || posY!=y) && (nearType!=null) && (!nearType.equals(type))){
                            noBabies++;
                            break;
                        }
                    }
                }
            }
        }
        noBabies = noBabies / 2;
        while (noBabies>0){
            int randX = new Random().nextInt(sideLength);
            int randY = new Random().nextInt(sideLength);
            if(grid[randX][randY]==null){
                if(new Random().nextBoolean()){
                    grid[randX][randY] = TYPE_MAN;
                }else {
                    grid[randX][randY] = TYPE_WOMAN;
                }
                noBabies--;
            }
        }


    }

    public boolean isItEnd(){
        int people = countPeople();
        int tooMuchPeople = ((sideLength*sideLength) * 3)/4;
        if(people>=tooMuchPeople || countMen()==0 || countWomen()==0){
            return true;
        }else {
            return false;
        }
    }

    public int countPeople(){
        int count = 0;
        for(int x=0; x<sideLength; x++){
            for(int y=0; y<sideLength; y++) {
                if(grid[x][y]!=null){
                    count++;
                }
            }
        }
        return count;
    }

    public int countWomen(){
        int count = 0;
        for(int x=0; x<sideLength; x++){
            for(int y=0; y<sideLength; y++) {
                if(grid[x][y]!=null){
                    if(grid[x][y].equals(TYPE_WOMAN)){
                        count++;
                    }
                }


            }
        }
        return count;
    }

    public int countMen(){
        int count = 0;
        for(int x=0; x<sideLength; x++){
            for(int y=0; y<sideLength; y++) {
                if(grid[x][y]!=null){
                    if(grid[x][y].equals(TYPE_MAN)){
                        count++;
                    }
                }


            }
        }
        return count;
    }

    public List<String> convertGridToList(){
        List<String> list = new ArrayList<>();
        for(int x=0; x<sideLength; x++){
            for(int y=0; y<sideLength; y++){
                list.add(grid[x][y]);
            }
        }
        return list;
    }

    private List<int[]> getFieldsNearAround(int x, int y){
       List<int[]> fields = new ArrayList<>();
       int bound = sideLength-1;
       fields.add(new int[]{x,y});
       if(y!=0){
           fields.add(new int[]{x,y-1});
           if(x!=0){
               fields.add(new int[]{x-1,y-1});
           }
           if(x!=bound){
               fields.add(new int[]{x+1, y-1});
           }
       }
       if(y!=bound){
           fields.add(new int[]{x, y+1});
           if(x!=0){
               fields.add(new int[]{x-1,y+1});
           }
           if(x!=bound){
               fields.add(new int[]{x+1,y+1});
           }
       }
        if(x!=0){
            fields.add(new int[]{x-1,y});
        }
        if(x!=bound){
            fields.add(new int[]{x+1,y});
        }
       return fields;
    }

}
