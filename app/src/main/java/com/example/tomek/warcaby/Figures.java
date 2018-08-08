package com.example.tomek.warcaby;

import android.widget.Button;

public interface Figures {

    public void drawSoldier(final Button btn_tmp);
    public boolean positionInRange(int row, int column, Field[][] battleField);
    public boolean isEnemyInRange(int row, int column, Field[][] battleField);
    public int[][] placeAfterAttack(int rowOriginal, int columnOriginal, Field[][] battlefield);
    //public List<Integer[]> checkForAttackOpportunity(Field[][] battlefield);

}
