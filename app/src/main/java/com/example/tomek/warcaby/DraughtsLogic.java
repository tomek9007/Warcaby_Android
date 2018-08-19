package com.example.tomek.warcaby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DraughtsLogic extends AppCompatActivity implements View.OnClickListener {
    private boolean isWhiteTurn = true;

    private Soldier checkedSoldier = null;
    private Button lastButton = null;
    public static Field[][] battleField = new Field[8][8];
    private int rowsOfBlack;// = howManyRows();
    private int rowsOfWhite;// = howManyRows();
    int howManyBlack;// = rowsOfBlack*4;
    int howManyWhite;// = rowsOfWhite*4;
    public Soldier[] allSoldiers;// = new Soldier[rowsOfBlack * 4 + rowsOfWhite * 4];

    private int State = 0;
    private int[] tempColumnAttack = {0,0,0,0};     //NE, SE, SW, NW
    private int[] tempRowAttack={0,0,0,0};          //NE, SE, SW, NW
    private List<Button> buttonsToRestore = new ArrayList<>();
    private int possibilitiesForAll[][][];// = new int[allSoldiers.length][][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draughts);

        rowsOfBlack = howManyRows();
        rowsOfWhite = howManyRows();
        howManyBlack = rowsOfBlack*4;
        howManyWhite = rowsOfWhite*4;
        allSoldiers = new Soldier[rowsOfBlack * 4 + rowsOfWhite * 4];
        possibilitiesForAll= new int[allSoldiers.length][][];

        prepareArmy();
        drawArmy();
    }

    private int howManyRows() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            Integer value = extras.getInt("key");
            //The key argument here must match that used in the other activity
            System.out.println(value);

            return value;
        }
        else
            return 2;
    }

    private void checkEnd()
    {
        String value="";

        if(isWhiteTurn)
        {
            howManyBlack--;
            if(howManyBlack==0) {
                value="WHITE WINS";
            }
        }
        else if(!isWhiteTurn)
        {
            howManyWhite--;
            if(howManyWhite==0) {
                value="BLACK WINS";
            }
        }

        if(!value.equals("")) {
            Intent i = new Intent(DraughtsLogic.this, WinnerScreen.class);
            i.putExtra("key", value);
            startActivity(i);
        }
    }

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
        final Button btn_attack = getButton("attack");


        if (isWhiteTurn && checkedSoldier.position_row == 0             //check for GrandSodlier
                || !isWhiteTurn && checkedSoldier.position_row == 7) {
            allSoldiers[checkedSoldier.id - 2] = checkedSoldier.becomeGrandSoldier();
            drawDetails(checkedSoldier.position_row, checkedSoldier.position_col);
        }

        checkedSoldier = null;

        if (isWhiteTurn) {
            btn_tmp_white.setBackgroundResource(R.drawable.turn_button);
            btn_tmp_black.setBackgroundResource(R.drawable.actual_turn_button);
        } else {
            btn_tmp_white.setBackgroundResource(R.drawable.actual_turn_button);
            btn_tmp_black.setBackgroundResource(R.drawable.turn_button);
        }
        isWhiteTurn = !isWhiteTurn;

        if(State==2)                                                        //check for obligation to attack at turn start
        {
            State = 0;
            btn_attack.setBackgroundResource(R.drawable.turn_button);
        }
        else
            for (int counter = 0; counter < allSoldiers.length; counter++) {
                //Soldier thisSoldier = allSoldiers[counter];
                if (((allSoldiers[counter].isWhite && isWhiteTurn)||(!allSoldiers[counter].isWhite&&!isWhiteTurn))
                        &&allSoldiers[counter].isAlive)
                {
                    if(allSoldiers[counter].checkForAttackOpportunity(battleField, allSoldiers).length>0)
                    {
                        State = 2;
                        btn_attack.setBackgroundResource(R.drawable.attack_opportunity);
                        break;
                    }
                }
            }
    }

    public void drawArmy() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                drawDetails(i, j);
            }
    }

    private void setAttackedSoldierPosition(int row, int column)        //direction from mark soldier to attacked one
    {
        int attackedSoldierPosition=0;
        if(row<checkedSoldier.position_row&&column>checkedSoldier.position_col)     //NE
            attackedSoldierPosition=0;
        else if(row>checkedSoldier.position_row&&column>checkedSoldier.position_col)     //SE
            attackedSoldierPosition=1;
        else if(row>checkedSoldier.position_row&&column<checkedSoldier.position_col)     //SW
            attackedSoldierPosition=2;
        else if(row<checkedSoldier.position_row&&column<checkedSoldier.position_col)     //NW
            attackedSoldierPosition=3;
        tempColumnAttack[attackedSoldierPosition]=column;
        tempRowAttack[attackedSoldierPosition]=row;
    }

    private int getAttackedSoldierPosition(int row, int column)         //direction from mark soldier to attacked one
    {
        int attackedSoldierPosition=0;
        if(row<checkedSoldier.position_row&&column>checkedSoldier.position_col)     //NE
            attackedSoldierPosition=0;
        else if(row>checkedSoldier.position_row&&column>checkedSoldier.position_col)     //SE
            attackedSoldierPosition=1;
        else if(row>checkedSoldier.position_row&&column<checkedSoldier.position_col)     //SW
            attackedSoldierPosition=2;
        else if(row<checkedSoldier.position_row&&column<checkedSoldier.position_col)     //NW
            attackedSoldierPosition=3;
        return attackedSoldierPosition;
    }

    public void confirmAttack(int row, int column) {

        int[][] placeAfterAttack = checkedSoldier.placeAfterAttack(row, column, battleField);
        for (int i = 0; i < placeAfterAttack.length; i++) {
            Button buttonX = getButton(placeAfterAttack[i][0],placeAfterAttack[i][1]);
            String buttonTagRow = String.valueOf(placeAfterAttack[i][0]);
            String buttonTagCol = String.valueOf(placeAfterAttack[i][1]);
            buttonX.setTag(buttonTagRow + buttonTagCol);
            buttonX.setBackgroundResource(R.drawable.pottential_field);
            State = 1;
            setAttackedSoldierPosition(row, column);
            //tempColumnAttack = column;                                  //position of attacked soldier
            //tempRowAttack = row;                                        //position of attacked soldier
            // Register the onClick listener with the implementation above
            buttonsToRestore.add(buttonX);

            buttonX.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    restoreAllButtonsFunction();
                    String position = (String) v.getTag();
                    String buttonPositionRow = position.substring(position.length() - 2, position.length() - 1);
                    String buttonPositionCol = position.substring(position.length() - 1);
                    int buttonPositionC = Integer.valueOf(buttonPositionCol);
                    int buttonPositionR = Integer.valueOf(buttonPositionRow);
                    int attackedSoldierPosition = getAttackedSoldierPosition(buttonPositionR, buttonPositionC);
                    attackSoldier(tempColumnAttack[attackedSoldierPosition], tempRowAttack[attackedSoldierPosition],
                            buttonPositionC, buttonPositionR);

                    if (State == 0) {
                        restoreAllButtonsFunction();
                        changeTurn();
                    }
                }
            });
        }
    }

    private void restoreAllButtonsFunction() {
        for (Button button : buttonsToRestore
                ) {
            button.setBackgroundResource(R.drawable.brown_field);
            drawDetails(checkedSoldier.position_row, checkedSoldier.position_col);
            if (buttonsToRestore.size() > 0)
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        defaultOnClick(v);
                    }
                });
        }
        buttonsToRestore.clear();
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
        checkEnd();

        int[][] newOpportunities = checkedSoldier.checkForAttackOpportunity(battleField, allSoldiers);
        if (newOpportunities.length > 0)

            for (int g = 0; g < newOpportunities.length; g++) {
                confirmAttack(newOpportunities[g][0], newOpportunities[g][1]);
            }
        else {
            State = 0;
            final Button btn_attack = getButton("attack");
            btn_attack.setBackgroundResource(R.drawable.turn_button);

        }

    }

    public void defaultOnClick(View v) {
        String text2 = getId(v);

        int column = Integer.parseInt(String.valueOf(text2.charAt(text2.length() - 1))) - 1;
        int row = Integer.parseInt(String.valueOf(text2.charAt(text2.length() - 2))) - 1;


        if (battleField[row][column].isFree && State == 0)        //clicked on empty
        {
            if (checkedSoldier != null && checkedSoldier.positionInRange(row, column, battleField)) {
                moveSoldier(row, column);

                changeTurn();
            }
        } else if (!battleField[row][column].isFree && (State == 0||State==2))       //clicked on not empty place
        {
            if (canIMarkSoldier(row, column)) {
                markSoldier(text2, row, column);
            } else if (canIAttack(row, column)) {
                confirmAttack(row, column);
            }
        }
    }

    public void onClick(View v) {
        if(State==0||State==2)
            defaultOnClick(v);
    }

    private boolean canIAttack(int row, int column) {
        return (checkedSoldier != null && checkedSoldier.nonCollisionCheck(row, column, battleField) &&
                ((isWhiteTurn && checkedSoldier.isWhite) || (!isWhiteTurn && !checkedSoldier.isWhite)))
                && checkedSoldier.isEnemyInRange(row, column, battleField, allSoldiers);

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

        drawDetails(checkedSoldier.position_row, checkedSoldier.position_col);     //draw empty place after move
        allSoldiers[checkedSoldier.id - 2].position_col = column;
        allSoldiers[checkedSoldier.id - 2].position_row = row;

        int tmpSoldierNumber = checkedSoldier.id;
        checkedSoldier = allSoldiers[tmpSoldierNumber - 2];
        drawDetails(row, column);                                                   //draw soldier in new place

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



