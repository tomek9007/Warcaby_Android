package com.example.tomek.warcaby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isWhiteTurn = true;
    private Soldier checkedSoldier = null;
    private Button lastButton = null;
    public static Field[][] battleField = new Field[8][8];
    private int rowsOfBlack = 2;
    private int rowsOfWhite = 2;
    Soldier[] allSoldiers = new Soldier[rowsOfBlack * 4 + rowsOfWhite * 4];
    private int State = 0;
    private int tempColumnAttack;
    private int tempRowAttack;
    private List<Button> buttonsToRestore = new ArrayList<>();

    private void prepareArmy() {
        int SoldiersNumber = rowsOfBlack * 4 + rowsOfWhite * 4 + 1;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < rowsOfBlack; j++) {
                if ((i + j) % 2 == 0) {
                    battleField[j][i] = new Field(false);
                } else {
                    battleField[j][i] = new Field(SoldiersNumber);
                    allSoldiers[SoldiersNumber - 2] = new Soldier(false, SoldiersNumber, i, j);
                    SoldiersNumber--;
                }
            }
            for (int j = rowsOfBlack; j < 8 - rowsOfWhite; j++)
                if ((i + j) % 2 == 0) {
                    battleField[j][i] = new Field(false);
                } else {
                    battleField[j][i] = new Field(true);
                }
            for (int j = (8 - rowsOfWhite); j < 8; j++)
                if ((i + j) % 2 == 0) {
                    battleField[j][i] = new Field(false);
                } else {
                    battleField[j][i] = new Field(SoldiersNumber);
                    allSoldiers[SoldiersNumber - 2] = new Soldier(true, SoldiersNumber, i, j);
                    SoldiersNumber--;
                    /*[SoldiersNumber - 2]: -2 is necessary to fit array dimension with number,
                    soldier number 0 is for "there is nobody"*/
                }
        }

    }

    public final Button getButton(int i, int j) {
        int col = i + 1;
        int row = j + 1;
        String button = "button" + col + row;
        final Button btn_tmp;
        int resID = getResources().getIdentifier(button, "id", getPackageName());
        btn_tmp = ((Button) findViewById(resID));
        return btn_tmp;
    }

    public final Button getButton(String buttonName) {
        final Button btn_tmp_black;
        int resIDblack = getResources().getIdentifier(buttonName, "id", getPackageName());
        btn_tmp_black = ((Button) findViewById(resIDblack));
        return btn_tmp_black;
    }


    private void drawDetails(int i, int j) {

        final Button btn_tmp = getButton(i, j);

        if (battleField[i][j].soldierNumber == 0) {
            if (battleField[i][j].isBrown)
                btn_tmp.setBackgroundResource(R.drawable.brown_field);
            else
                btn_tmp.setBackgroundResource(R.drawable.background);

        } else if (battleField[i][j].soldierNumber != 0) {
            allSoldiers[battleField[i][j].soldierNumber - 2].drawSoldier(btn_tmp);
        }
    }

    private void drawCheckedSoldier(final Button btn_tmp) {
        if (!isWhiteTurn)
            btn_tmp.setBackgroundResource(R.drawable.checked_brown_figure);
        else
            btn_tmp.setBackgroundResource(R.drawable.checked_yellow_figure);
    }

    private void unCheckedSoldier(final Button btn_tmp) {
        if (!isWhiteTurn)
            btn_tmp.setBackgroundResource(R.drawable.buttonshape_brown2);
        else
            btn_tmp.setBackgroundResource(R.drawable.buttonshape_yellow);
    }

    private void changeTurn() {
        final Button btn_tmp_white = getButton("whiteTurn");
        final Button btn_tmp_black = getButton("blackTurn");

        if (isWhiteTurn) {
            btn_tmp_white.setBackgroundResource(R.drawable.turn_button);
            btn_tmp_black.setBackgroundResource(R.drawable.actual_turn_button);
        } else {
            btn_tmp_white.setBackgroundResource(R.drawable.actual_turn_button);
            btn_tmp_black.setBackgroundResource(R.drawable.turn_button);
        }
        isWhiteTurn = !isWhiteTurn;
    }

    public void drawArmy() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                drawDetails(i, j);
            }
    }

    public void confirmAttack(int column, int row) {

        int[][] placeAfterAttack = checkedSoldier.placeAfterAttack(row,column,  battleField);
        for (int i = 0; i < placeAfterAttack.length; i++) {
            Button buttonX = getButton( placeAfterAttack[i][0],
                    placeAfterAttack[i][1]
            );
            String buttonTagRow = String.valueOf( placeAfterAttack[i][0]);
            String buttonTagCol = String.valueOf( placeAfterAttack[i][1]);
            buttonX.setTag(buttonTagRow + buttonTagCol);
            buttonX.setBackgroundResource(R.drawable.pottential_field);
            State = 1;
            tempColumnAttack = column;                                  //position of attacked soldier
            tempRowAttack = row;                                        //position of attacked soldier
            // Register the onClick listener with the implementation above
            buttonsToRestore.add(buttonX);

            buttonX.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String position = (String) v.getTag();
                    String buttonPositionRow = position.substring(position.length() - 2, position.length() - 1);
                    String buttonPositionCol = position.substring(position.length() - 1);
                    int buttonPositionC = Integer.valueOf(buttonPositionCol);
                    int buttonPositionR = Integer.valueOf(buttonPositionRow);
                    restoreAllButtonsFunction();
                    attackSoldier(tempColumnAttack, tempRowAttack, buttonPositionC, buttonPositionR);


                    checkedSoldier = null;
                    changeTurn();
                    State = 0;
                }
            });
        }
    }

    private void restoreAllButtonsFunction() {
        for (Button button : buttonsToRestore
                ) {
            button.setBackgroundResource(R.drawable.brown_field);
            if (buttonsToRestore.size() > 0)
                //buttonsToRestore.remove(button);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        defaultOnClick(v);
                    }
                });
        }
        buttonsToRestore.clear();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareArmy();
        drawArmy();
    }

    public void markSoldier(String buttonName, int row, int column) {
        String button = buttonName;
        final Button btn_tmp;
        int resID = getResources().getIdentifier(button, "id", getPackageName());
        btn_tmp = ((Button) findViewById(resID));
        if (checkedSoldier != null) {
            unCheckedSoldier(lastButton);
        }

        drawCheckedSoldier(btn_tmp);                                                //mark soldier
        lastButton = btn_tmp;
        checkedSoldier = allSoldiers[battleField[row][column].soldierNumber - 2];
    }

    public void attackSoldier(int column, int row, int potentialColumn, int potentialRow) {
        moveSoldier(potentialRow, potentialColumn);
        System.out.println("Soldier " + checkedSoldier.id
                + " killed soldier " +
                String.valueOf(allSoldiers[battleField[row][column].soldierNumber - 2].id));
        killSoldier(row, column);
        int[][] newOpportunities = checkedSoldier.checkForAttackOpportunity(battleField);
        if(newOpportunities.length>0)
            for(int g=0; g<newOpportunities.length; g++)
            {
                int potentialSoldierNumber = battleField[newOpportunities[g][0]][newOpportunities[g][1]].soldierNumber;
                if(potentialSoldierNumber!=0) {
                    if (allSoldiers[battleField[newOpportunities[g][0]][newOpportunities[g][1]]
                            .soldierNumber - 2].isWhite = !isWhiteTurn)
                        confirmAttack(newOpportunities[g][0], newOpportunities[g][1]);
                }
            }

    }

    public void defaultOnClick(View v) {
        String text2 = getId(v);

        int column = Integer.parseInt(String.valueOf(text2.charAt(text2.length() - 1))) - 1;
        int row = Integer.parseInt(String.valueOf(text2.charAt(text2.length() - 2))) - 1;


        if (battleField[row][column].isFree && State == 0)        //clicked on empty
        {
            if (checkedSoldier != null && checkedSoldier.positionInRange(row,column,  battleField)) {
                moveSoldier(row, column);
                checkedSoldier = null;
                changeTurn();
            }
        } else if (!battleField[row][column].isFree && State == 0)       //clicked on not empty place
        {
            if (canIMarkSoldier(row, column)) {
                markSoldier(text2, row, column);
            } else if (canIAttack(row, column)) {

                //checkedSoldier.confirmAttack();
                confirmAttack(column, row);
            }
        }
    }

    public void onClick(View v) {

        defaultOnClick(v);
    }

    private boolean canIAttack(int row, int column) {
        return (checkedSoldier != null && checkedSoldier.nonCollisionCheck( row,column, battleField) &&
                ((isWhiteTurn && checkedSoldier.isWhite) || (!isWhiteTurn && !checkedSoldier.isWhite)))
                && checkedSoldier.isEnemyInRange( row,column, battleField)
                && (((allSoldiers[battleField[row][column].soldierNumber - 2].isWhite)
                && !checkedSoldier.isWhite)
                || ((!allSoldiers[battleField[row][column].soldierNumber - 2].isWhite) && checkedSoldier.isWhite));
    }

    private boolean canIMarkSoldier(int row, int column) {
        return (isWhiteTurn && allSoldiers[battleField[row][column].soldierNumber - 2].isWhite)                    //check soldier
                || (!isWhiteTurn && !allSoldiers[battleField[row][column].soldierNumber - 2].isWhite);
    }

    private void moveSoldier(int row, int column) {
        battleField[row][column].isFree = false;
        battleField[row][column].soldierNumber = checkedSoldier.id;

        battleField[checkedSoldier.position_row][checkedSoldier.position_col].isFree = true;
        battleField[checkedSoldier.position_row][checkedSoldier.position_col].soldierNumber = 0;

        System.out.println("Soldier " + checkedSoldier.id + " moved from ["
                + checkedSoldier.position_row + "]["
                + checkedSoldier.position_col + "] to ["
                + row + "]["
                + column + "]");

        if (isWhiteTurn && row == 0 || !isWhiteTurn && row == 7) {

            allSoldiers[checkedSoldier.id - 2] = checkedSoldier.becomeGrandSoldier();
        }

        drawDetails(row, column);                                                   //draw soldier in new place
        drawDetails(checkedSoldier.position_row, checkedSoldier.position_col);     //draw empty place after move
        allSoldiers[checkedSoldier.id - 2].position_col = column;
        allSoldiers[checkedSoldier.id - 2].position_row = row;
    }

    private void killSoldier(int row, int column) {
        battleField[row][column].isFree = true;
        allSoldiers[battleField[row][column].soldierNumber - 2].isAlive = false;
        battleField[row][column].soldierNumber = 0;
        drawDetails(row, column);
    }


    public static String getId(View view) {
        return view.getResources().getResourceName(view.getId());
    }

}



