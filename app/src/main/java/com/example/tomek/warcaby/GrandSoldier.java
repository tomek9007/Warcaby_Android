package com.example.tomek.warcaby;

import android.widget.Button;

import static java.lang.StrictMath.abs;

public class GrandSoldier extends Soldier implements Figures {


    public GrandSoldier(boolean isWhite, int id, int position_col, int position_row) {
        this.isWhite=isWhite;
        this.id=id;
        this.position_col=position_col;
        this.position_row=position_row;
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

        if ((abs(colDiff) == abs(rowDiff))&&nonCollisionCheck(column, row, battleField))
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
            if(!battleField[tmpRowOriginal][tmpColumnOriginal].isFree)
                return false;
        }

    }

    public boolean isEnemyInRange( int row,int column,Field[][] battleField) {
        return positionInRange(row,column, battleField);
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
}
