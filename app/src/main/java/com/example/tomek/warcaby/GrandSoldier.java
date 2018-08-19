package com.example.tomek.warcaby;

import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.abs;

public class GrandSoldier extends Soldier implements Figures {


    public GrandSoldier(boolean isWhite, int id, int position_col, int position_row) {
        this.isWhite=isWhite;
        this.id=id;
        this.position_col=position_col;
        this.position_row=position_row;
        this.isAlive=true;
    }

    @Override
    public void drawSoldier(final Button btn_tmp) {
        if (this.isWhite)
            btn_tmp.setBackgroundResource(R.drawable.buttonshape_grand_yellow);
        else
            btn_tmp.setBackgroundResource(R.drawable.buttonshape_grand_brown);
    }

    public boolean positionInRange( int row,int column, Field[][] battleField) {
        int colDiff = column - super.position_col;
        int rowDiff = row - super.position_row;

        if ((abs(colDiff) == abs(rowDiff))&&nonCollisionCheck( row,column, battleField))
            return true;
        else
            return false;
    }

    public boolean nonCollisionCheck( int rowTarget,int columnTarget, Field[][] battleField) {
        int colDiff = columnTarget - super.position_col;
        int rowDiff = rowTarget - super.position_row;
        int tmpColumnOriginal=this.position_col;
        int tmpRowOriginal=this.position_row;
        //tmpColumnOriginal+=Integer.signum(colDiff);
        //tmpRowOriginal+=Integer.signum(rowDiff);

        for(;;) {                                       //check place behind target
            tmpColumnOriginal+=Integer.signum(colDiff);
            tmpRowOriginal+=Integer.signum(rowDiff);
            if(tmpColumnOriginal==columnTarget)
                return true;
            if(!battleField[tmpRowOriginal][tmpColumnOriginal].isFree)      //tutaj sie psuje
                return false;
        }

    }

    public boolean isEnemyInRange( int row,int column,Field[][] battleField, Soldier[] allSoldiers) {
        if (column<=7&&column>=0&&row<=7&&row>=0&&
                positionInRange(row,column, battleField)&& (battleField[row][column].soldierNumber!=0)
                &&(((allSoldiers[battleField[row][column].soldierNumber-2].isWhite)&&!this.isWhite)
                ||((!allSoldiers[battleField[row][column].soldierNumber-2].isWhite)&&this.isWhite))) {
            return true;
        }
        else
            return false;
    }

    public int[][] placeAfterAttack( int rowOriginal,int columnOriginal, Field[][] battlefield) {

        int colDiff = columnOriginal - super.position_col;
        int rowDiff = rowOriginal - super.position_row;
        int tmpColumnOriginal = columnOriginal;
        int tmpRowOriginal = rowOriginal;
        int[][] places = new int[8][2];
        int counter=0;

        for(;;) {                                       //check place behind target     //gdzies tu sie sypie

            tmpColumnOriginal+=Integer.signum(colDiff);
            tmpRowOriginal+=Integer.signum(rowDiff);
            if((tmpColumnOriginal<0)||(tmpRowOriginal<0)
                    ||(tmpColumnOriginal>7)||(tmpRowOriginal>7))
                break;
            if(!battlefield[tmpRowOriginal][tmpColumnOriginal].isFree)
                break;
            places[counter][0]=tmpRowOriginal;
            places[counter][1]=tmpColumnOriginal;
            rowDiff+=Integer.signum(rowDiff);
            colDiff+=Integer.signum(colDiff);
            counter++;
        }
        int[][] placesCompressed = new int[counter][2];
        for(int rows=0; rows <  counter; rows++)
        {
            placesCompressed[rows][0]=places[rows][0];
            placesCompressed[rows][1]=places[rows][1];
        }
        return placesCompressed;
    }

    public int[][] checkForAttackOpportunity(Field[][] battlefield,Soldier[] allSoldier) {
        List<Integer[]> potentialPositions = new ArrayList<>();
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                for(int multiple=1; multiple<7;multiple++) {
                    if (this.isEnemyInRange(this.position_row + i*multiple, this.position_col + j*multiple, battlefield, allSoldier)) {
                        int[][] activePlace = placeAfterAttack(this.position_row + i*multiple, this.position_col + j*multiple, battlefield);
                        if (activePlace.length > 0 && battlefield[activePlace[0][0]][activePlace[0][1]].isFree) {
                            Integer[] tempArray = {this.position_row + i*multiple, this.position_col + j*multiple};
                            potentialPositions.add(tempArray);
                        }
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
