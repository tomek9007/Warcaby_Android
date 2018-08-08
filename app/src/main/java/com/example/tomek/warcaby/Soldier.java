package com.example.tomek.warcaby;

import android.widget.Button;

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
        else
            btn_tmp.setBackgroundResource(R.drawable.buttonshape_brown2);
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

    public boolean isEnemyInRange( int row,int column,Field[][] battleField) {      //it is not searching for enemy!
        int colDiff = column - this.position_col;
        int rowDiff = row - this.position_row;

        if (abs(colDiff) == 1 && abs(rowDiff) == 1) {
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
        int[][] place = {{row+this.position_row, col+this.position_col}};

        if(battlefield[row+this.position_row][col+this.position_col].isFree)
        return place;
        else
            return placesFull;
    }


    public int[][] checkForAttackOpportunity(Field[][] battlefield) {
        int counter = 0;
        int[][] places = new int[4][2];
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                if (this.isEnemyInRange( this.position_row + i,this.position_col + j, battlefield)) {
                    int[][] activePlace = placeAfterAttack(this.position_row+i,this.position_col+j, battlefield);
                    if (activePlace.length>0&&battlefield[activePlace[0][0]][activePlace[0][1]].isFree) {
                        places[counter][0] = activePlace[0][0];
                        places[counter][1] = activePlace[0][1];
                        counter++;
                    }
                }
            }
        }
        int[][] compressedPlaces = new int[counter][2];
        for(int i =0; i<counter; i++)
        {
            compressedPlaces[i][0]=places[i][0];
            compressedPlaces[i][1]=places[i][0];
        }
        return compressedPlaces;
    }

}
