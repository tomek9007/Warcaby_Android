package com.example.tomek.warcaby;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.abs;

public class Soldier implements Figures {
    public boolean isAlive;
    public boolean isWhite;
    public int id;
    public int position_col;
    public int position_row;

    public Soldier() {

    }

    public Soldier(boolean isWhite, int id, int position_col, int position_row) {
        this.isAlive = true;
        this.isWhite = isWhite;
        this.id = id;
        this.position_col = position_col;
        this.position_row = position_row;
    }

    public GrandSoldier becomeGrandSoldier() {
        System.out.println("Number " + this.id + ":I am grand soldier!");
        return new GrandSoldier(this.isWhite, this.id, this.position_col, this.position_row);
    }

    public void drawSoldier(final Button btn_tmp) {
        if (this.isWhite)
            btn_tmp.setBackgroundResource(R.drawable.buttonshape_yellow);
        else {
            btn_tmp.setBackgroundResource(R.drawable.buttonshape_brown2);
        }
    }

    public boolean positionInRange( int row,int column,Field[][] battleField) {
        int colDiff = column - this.position_col;
        int rowDiff = row - this.position_row;

        if (this.isWhite && abs(colDiff) == 1 && rowDiff == -1)
            return true;
        else if (!this.isWhite && abs(colDiff) == 1 && rowDiff == 1)
            return true;
        else
            return false;
    }



    public boolean isEnemyInRange( int row,int column,Field[][] battleField, Soldier[] allSoldiers) {
        int colDiff = column - this.position_col;
        int rowDiff = row - this.position_row;

        if (column<=7&&column>=0&&row<=7&&row>=0&&(abs(colDiff) == 1 && abs(rowDiff) == 1)&&(battleField[row][column].soldierNumber!=0)
                &&(((allSoldiers[battleField[row][column].soldierNumber-2].isWhite)&&!this.isWhite)
                ||((!allSoldiers[battleField[row][column].soldierNumber-2].isWhite)&&this.isWhite))) {
            return true;
        } else
            return false;
    }

    public boolean nonCollisionCheck( int rowOriginal,int columnOriginal, Field[][] battleField) {
        return true;
    }

    public int[][] placeAfterAttack(int rowOriginal,int columnOriginal, Field[][] battlefield) {
        int colDiff = columnOriginal - this.position_col;
        int rowDiff = rowOriginal - this.position_row;
        int col = colDiff * 2;
        int row = rowDiff * 2;

        int[][] placesFull = new int[0][2];



        if(((row+this.position_row)<=7)&&((row+this.position_row)>=0)
                &&((col+this.position_col)<=7)&&((col+this.position_col)>=0)
                &&battlefield[row+this.position_row][col+this.position_col].isFree){
            int[][] place = {{row+this.position_row, col+this.position_col}};
            return place;
        }
        else
            return placesFull;
    }



    public int[][] checkForAttackOpportunity(Field[][] battlefield,Soldier[] allSoldier) {
        List<Integer[]> potentialPositions = new ArrayList<>();
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                if (this.isEnemyInRange( this.position_row + i,this.position_col + j, battlefield,allSoldier)) {
                    int[][] activePlace = placeAfterAttack(this.position_row+i,this.position_col+j, battlefield);
                    if (activePlace.length>0&&battlefield[activePlace[0][0]][activePlace[0][1]].isFree) {
                        Integer[] tempArray = {this.position_row+i, this.position_col+j};
                        potentialPositions.add(tempArray);
                    }
                }
            }
        }
        int[][] compressedPlaces = new int[potentialPositions.size()][2];
        for(int i =0; i<potentialPositions.size(); i++)
        {
            compressedPlaces[i][0]=potentialPositions.get(i)[0];
            compressedPlaces[i][1]=potentialPositions.get(i)[1];
        }
        return compressedPlaces;
    }

}
