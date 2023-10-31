package secondGame;
import hsa2.GraphicsConsole;
import java.awt.Color;
import java.awt.Font;


public class ChessGame {
    public static void main(String [] args) {
        new ChessGame();
    }
    
    final static int WINW = 640, WINH = 640;
    final static int WIDE =80;

    int[][] chess = new int [8][8];   //the chess graph
    int[][] whitebangraph = new int[8][8];  //The areas that the white king cannot reach (king cannot move to a dead place)
    int[][] blackbangraph = new int[8][8];

    Color bgColor = new Color(249,214,91);  //background color
    Color gridColor = new Color(215,162,39);
    Color hintColor = new Color(211,211,211,200);   //the color of pop-ups

    int type = 0;       // the type of piece, determined by number, example: 2 is a black castle, 14 is a white bishop 
    boolean choose = false;     // determines if you have selected a piece to move
    int player = 0;   // the player. 0,2,4,6 is black, 1,3,5,7 is white
    int cx = 0;         // choosen graph x. If you have already selected piece (when choose = true), cx and cy will be the place the piece is
    int cy = 0;

    GraphicsConsole gc = new GraphicsConsole(WINW,WINH);
    
    ChessGame(){
        setup();
        createGrid();   //make the pieces on the graph array
        drawGrid();
        rungame();
    }

    void setup(){
        gc.setTitle("Chess game");
        gc.setAntiAlias(true);
        gc.setLocationRelativeTo(null);
        gc.setBackgroundColor(bgColor);
        gc.enableMouseMotion();
        gc.enableMouse();
        gc.setColor(gridColor);
        gc.setStroke(4);
        gc.setFont(new Font("Impact", Font.PLAIN,33));
        gc.clear();
    }

    void createGrid(){
        for (int y = 0; y <8;y++){  
            for (int x = 0; x <8;x++){

                if (y== 1) chess[x][y] = 1;  // black pawn
                if (y== 6) chess[x][y] = 11;   // white pawn

                if (y == 0){    //The black side
                    if (x == 0||x==7)       chess[x][y] = 2;  //castle
                    else if (x == 1||x==6)  chess[x][y] = 3;   //knight
                    else if (x == 2||x==5)  chess[x][y] = 4;   //bishop
                    else if (x == 3)        chess[x][y] = 5;   //queen
                    else if (x == 4)        chess[x][y] = 6;   //king
                }

                else if (y==7){ //the white side
                    if (x == 0||x==7)       chess[x][y] = 12;  //castle
                    else if (x == 1||x==6)  chess[x][y] = 13;   //knight
                    else if (x == 2||x==5)  chess[x][y] = 14;   //bishop
                    else if (x == 3)        chess[x][y] = 15;   //queen
                    else if (x == 4)        chess[x][y] = 16;   //king
                }  
            }
        }
    }

    void drawGrid(){
        synchronized(gc){
            gc.clear();
            gc.setColor(gridColor); 

            for (int x = 0;x<WINH; x+=WIDE){    // draw the grid color of the chess board
                for (int y = 0;y<WINH; y+=WIDE){

                    if (y%(WIDE*2)==0 && x%(WIDE*2) ==0){
                        gc.fillRect(x,y,80,80);
                    }
                    else if (y%(WIDE*2)==WIDE && x%(WIDE*2) ==WIDE){
                        gc.fillRect(x,y,80,80);
                    }
                }
            }

            drawChessPiece();    //draw the pieces on the graph
        }
    }

    void drawChessPiece(){
        for (int gx = 0; gx<8;gx++){
            for (int gy = 0; gy<8;gy++){

                if (chess[gx][gy]<10){  //1-9 is black side, 10-19 is white
                    gc.setColor(Color.BLACK);
                }
                else{
                    gc.setColor(Color.WHITE);
                }

                gc.setStroke(4);
                // . 9 and 19 are special pawns, meaning they have just moved two blocks. 
                //A pawn can move two block when it is not moved. During this move, it can be En Passant. 
                //9 and 19 are powns that just moved and can be En Passant, after one round, they will be set back to normal
                if (chess[gx][gy] ==1 || chess[gx][gy] ==11 || chess[gx][gy] ==9 || chess[gx][gy] ==19) {
                    drawPawn(gx*WIDE,gy*WIDE);
                }
                else if (chess[gx][gy] ==2 || chess[gx][gy] ==12) {
                    drawCastle(gx*WIDE,gy*WIDE);
                }
                else if (chess[gx][gy] ==3 || chess[gx][gy] ==13) {
                    drawKnight(gx*WIDE,gy*WIDE);
                }
                else if (chess[gx][gy] ==4 || chess[gx][gy] ==14) {
                    drawBishop(gx*WIDE,gy*WIDE);
                }
                else if (chess[gx][gy] ==5 || chess[gx][gy] ==15) {
                    drawQueen(gx*WIDE,gy*WIDE);
                }
                else if (chess[gx][gy] ==6 || chess[gx][gy] ==16) {
                    drawKing(gx*WIDE,gy*WIDE);       
                }  
            }
        }
    }

    // draw all the piece with shapes
    void drawPawn(int x,int y){
        gc.fillOval(x+30,y+15,20,20);

        int []xlists = {x+23,x+40,x+57};
        int []ylists = {y+60,y+20,y+60};
        gc.fillPolygon(xlists, ylists, 3);
    }
    
    void drawCastle(int x,int y){
        y -=5;
        gc.fillRect(x+20,y+20,10,5);
        gc.fillRect(x+35,y+20,10,5);
        gc.fillRect(x+50,y+20,10,5);
        gc.fillRect(x+20,y+25,40,10);
        gc.fillRect(x+28,y+25,24,40);
        gc.fillRect(x+19,y+55,42,15);
    }

    void drawKnight(int x,int y){
        int []xlists = {x+20,x+27,x+45,x+58,x+25,x+40,x+35,x+30};
        int []ylists = {y+40,y+15,y+15,y+60,y+60,y+30,y+30,y+40};
        gc.fillPolygon(xlists, ylists, 8);
        gc.fillRect(x+19,y+55,42,10);

        gc.setColor(Color.GRAY);
        gc.fillOval(x+27,y+25,5,5);
    }

    void drawBishop(int x,int y){
        gc.fillOval(x+35,y+15,10,10);
        gc.fillOval(x+20,y+50,10,10);
        gc.fillOval(x+50,y+50,10,10);

        int []xlists = {x+25,x+40,x+55,x+45,x+35};
        int []ylists = {y+30,y+20,y+30,y+60,y+60};
        gc.fillPolygon(xlists, ylists, 5);
        gc.fillRect(x+20,y+55,40,10);
    }

    void drawQueen(int x,int y){
        y-=5;
        int []xlists = {x+23,x+20,x+25,x+30,x+35,x+40,x+45,x+50,x+55,x+60,x+57};
        int []ylists = {y+70,y+50,y+25,y+50,y+20,y+50,y+20,y+50,y+25,y+50,y+70};
        gc.fillPolygon(xlists, ylists, 11);

        gc.fillOval(x+22,y+25,6,6);
        gc.fillOval(x+32,y+20,6,6);
        gc.fillOval(x+42,y+20,6,6);
        gc.fillOval(x+52,y+25,6,6);
    }

    void drawKing(int x,int y){
        gc.fillRect(x+38,y+15,4,25);
        gc.fillRect(x+30,y+20,20,5);
        gc.fillOval(x+20,y+30,20,20);
        gc.fillOval(x+40,y+30,20,20);
        gc.fillRect(x+20,y+55,40,10);

        int []xlists = {x+20,x+60,x+50,x+30};
        int []ylists = {y+40,y+40,y+60,y+60};
        gc.fillPolygon(xlists, ylists, 4);
    }
    
    void rungame(){
        while (true){
            int x = 0;  // x is the pixel you click
            int y = 0;

            if (gc.getMouseClick()>0){// if you click on the grid
                x = gc.getMouseX();
                y = gc.getMouseY();
                    
                int gx = x/WIDE;    //gx： graph x is the place you ckick belong to the block in the 8x8 graph
                int gy = y/WIDE;
                
                if(checkCastling(gx,gy)) continue; // castling, you click your king and click your castle, and this round end

                if (chess[gx][gy] !=0 && checkSide(player,gx,gy)){     // if you pick a piece on your side   
                    drawGrid(); // update show the graph will tell you you selected it
                    type = chess[gx][gy];   // you get the type of piece you choose

                    if (player%2==0){
                        gc.setColor(Color.WHITE);
                    }
                    else {
                        gc.setColor(Color.BLACK);
                    }

                    gc.drawRect(gx*WIDE,gy*WIDE,WIDE,WIDE); //draw a frame to tell you which piece you choose
                    
                    choose = true; // meaning you choose a block
                    cx = gx;    //save the choosen piece
                    cy = gy;
                }
                    // I have x, cx, and gx
                    // how this works is if you click a place at 85,165. x,y is 85, 165
                    // then, gx,gy is 1,2 where you click on the graph. meaning your block is 1,2
                    // cx, cy (current x) only creates when you select a piece on your side. determining the piece you chosse
                    // This means you click on a piece 1,2. You chosse it. You click a blank place, gx,gy will change, but cx,cy won't. it will still remember what piece you choose
                    // this can be use to move piece. You click you piece (gx,gy,cx,cy = 1,2) you click the place you want to go,for example 3,4, then (cx,cy,=1,2) (gx,gy = 3,4)meaning move the piece at 1,2 to 3,4
             
                    
                
                if (choose && (chess[gx][gy] ==0 ||!checkSide(player,gx,gy))){ //you need to already have a choosen block and check the next choosen blook(target) is a black or enemy

                    switch (type){  //check if the pown can get there or not
                        case 1:
                            if (!checkBlackPown(cx,cy,gx,gy)) continue; //if you choose a blackpown and the place you move is not reachable, then not move and continue
                            break;  //both continue and break are needed here. continue stops the bottom lines work, break end switch
                        
                        case 11:
                            if (!checkWhitePown(cx,cy,gx,gy)) continue; //white pawn and black pawn move different
                            break;

                        case 2:
                            if (!checkCastle(cx,cy,gx,gy)) continue;
                            break;

                        case 12:
                            if (!checkCastle(cx,cy,gx,gy)) continue;
                            break;

                        case 3:
                            if (!checkKnight(cx,cy,gx,gy)) continue;
                            break;

                        case 13:
                            if (!checkKnight(cx,cy,gx,gy)) continue;
                            break;

                        case 4:
                            if (!checkBishop(cx,cy,gx,gy)) continue;
                            break;

                        case 14:
                            if (!checkBishop(cx,cy,gx,gy)) continue;
                            break;

                        case 5:
                            if (!checkQueen(cx,cy,gx,gy)) continue;
                            break;

                        case 15:
                            if (!checkQueen(cx,cy,gx,gy)) continue;
                            break;

                        case 6:
                            if (!checkKing(cx,cy,gx,gy)) continue;
                            if (blackbangraph[gx][gy]==1) { //based on chess rules, you can't move the king to a dead place, so pop up a x sign
                                popUp(gx,gy);
                                gc.setColor(Color.BLACK);
                                drawGrid();   
                                gc.setColor(Color.BLACK);   //drawGrid will change the color
                                gc.drawRect(cx*WIDE,cy*WIDE,WIDE,WIDE);
                                continue;
                            }
                            break;  

                        case 16:
                            if (!checkKing(cx,cy,gx,gy)) continue;
                            if (whitebangraph[gx][gy]==1) {
                                popUp(gx,gy);
                                gc.setColor(Color.WHITE);
                                drawGrid();   
                                gc.setColor(Color.WHITE);
                                gc.drawRect(cx*WIDE,cy*WIDE,WIDE,WIDE);
                                
                                continue;
                            }
                            break;              
                    }   // switch end

                    // the bottom part only happen when the piece can move to the place
                    
                    chess[gx][gy] = type;   // the new place will become your piece
                    chess[cx][cy] = 0;      // the origin place become blank

                    if (gy ==7 && type ==1) {//if a pown reach the other end, it will rise to a queen
                        chess[gx][gy] = 5;  
                    }        
                    else if (gy ==0 && type ==11) {
                        chess[gx][gy] = 15;
                    }

                    updateRestrictionAreas();   // update the white and black king restriction areas
                    drawGrid(); // update graphics

                    if (endGame()) return;  //one side lost their king

                    choose = false;  // you already moved a piece. next time you wanna move, you have to re-active it (choose a piece again)
                    player ++;    // your opposites turn

                    EnpassnetReturn();  // the enpassnet piece only last 1 round. if you don't do it immediatly, you lost the chance.
                }   //end if (choose && (chess[gx][gy] ==0 ||!checkSide(player,gx,gy)))
            }   //end if (gc.getMouseClick()>0)
            gc.sleep(10);   //inside while loop
        }   // while loop
    }   // methord

    void popUp(int gx, int gy){
        gc.setColor(Color.RED);
        gc.drawLine(gx*WIDE+10,gy*WIDE+10,(gx+1)*WIDE-10,(gy+1)*WIDE-10);
        gc.drawLine((gx+1)*WIDE-10,gy*WIDE+10,gx*WIDE+10,(gy+1)*WIDE-10);
        gc.sleep(500);
    }

    boolean endGame(){
        boolean endwhite = false; // if white/black lost thier king
        boolean endblack = false;

        for (int x =0;x<8;x++){
            for (int y =0;y<8;y++){
                if (chess[x][y] ==6  ) endwhite = true;
                if (chess[x][y] ==16 ) endblack = true;   //endblack: end with black wins
            }
        }

        if (!endwhite) {   // white wins
            gc.showDialog("white wins","END");
            return true;    //true means endgame is true, so end game
        }
        else if (!endblack){    //black wins
            gc.showDialog("black wins","END");
            return true;
        }
        return false;   //false means nothing happen, continue
    }

    void EnpassnetReturn(){ // make a Enpassnet pons return normal (turn all 19 to 11 on white turn and 9 to 1 on black turn)
        if (player%2 ==0){
            for (int x =0;x<8;x++){
                for (int y =0;y<8;y++){
                    if (chess[x][y] ==19) chess[x][y] =11;  //if its black's turn, make all the black Enpassnet pons return normal
                }
            }
        }
        else{
            for (int x =0;x<8;x++){
                for (int y =0;y<8;y++){
                    if (chess[x][y] ==9) chess[x][y] =1;    //same with white
                }
            }
        }
    }

    boolean checkCastling(int gx, int gy){  //how castling works is you click a king and then click a castle
        if (choose && chess[cx][cy] == 6 && chess[gx][gy] == 2 && cx == 4 && cy ==0){   // if you choose a black king and black castle
        //I checked it  with both cx, cy and of graph[cx][cy] because based on chess rule, the kings and castle has to be not moved before castling
            if (gx ==7 &&  gy ==0 && chess[5][0] ==0&& chess[6][0] ==0){ // if you click the right castle to move, check if the path between them are empty
                chess[cx+1][cy] =2;     //move the castle
                chess[cx+2][cy] = 6;    //move the king
                chess[gx][gy] =0;   //remove the old one
                chess[cx][cy] =0;

                player++;                    
                choose = false;
                drawGrid();
                return true;        // return true, means a castling happend
            }
            else if (gx ==0 &&gy ==0 && chess[1][0] ==0&& chess[2][0] ==0&& chess[3][0] ==0){ // if you choose the left castle to move
                chess[cx-1][cy] =2;
                chess[cx-2][cy] = 6;
                chess[gx][gy] =0;
                chess[cx][cy] =0;

                choose = false;                    
                player+=1;                        
                drawGrid();
                return true;
            }
        }

        if (choose && chess[cx][cy] == 16 && chess[gx][gy]==12 &&cx == 4 && cy ==7 ){    // if you choose a white king, same thing
            if (gx ==7 && gy ==7 && chess[5][7] ==0&& chess[6][7] ==0){
                chess[cx+1][cy] =12;
                chess[cx+2][cy] = 16;
                chess[gx][gy] =0;
                chess[cx][cy] =0;

                choose = false;
                player++;
                drawGrid();
                return true;
            }
            else if (gx ==0 && gy ==7 && chess[1][7] ==0&& chess[2][7] ==0&& chess[3][7] ==0){
                    chess[cx-1][cy] =12;
                    chess[cx-2][cy] = 16;
                    chess[gx][gy] =0;
                    chess[cx][cy] =0;

                    choose = false;
                    player++;
                    drawGrid();
                    return true;
            }
        }
        return false;   // if nothing happen return false
    }

    boolean checkSide(int player, int gx, int gy){    //if the place you click is your pon or not
        if (player %2==1){
            if (chess[gx][gy]<10) return true;  // return true means it is is your pon, so you can't move to that place or take that pon. 
        }
        else if (chess[gx][gy]>10){
            return true;
        }

        return false;
    }

    boolean checkBlackPown(int cx, int cy, int gx, int gy){
        if (gx == cx && cy == gy-1 && chess[gx][gy] ==0) {
            // you make a normal move 
            return true;
        }
        else if (gx == cx && cy == gy-2 && gy ==3 && chess[gx][gy] ==0) {
            // you start a pown with double a move, it can be en passant
            type = 9;   //meaning the pown become a pown that can be en passant
            return true;
        }
        else if (cy ==gy-1 && (gx == cx-1 || gx == cx+1) &&  chess[gx][gy-1] ==19) {
            // you take an enpassant
            chess[gx][gy-1] =0;
            return true;
        }
        else if (cy ==gy-1 && (gx == cx-1 || gx == cx+1) && chess[gx][gy]>10 ) {
            // you take your opposite by a normal attack
            return true;
        }   

        return false;   //none of the above situation happen
    }

    boolean checkWhitePown(int cx, int cy, int gx, int gy){
        //the white pon, same with black
        if (gx == cx && cy == gy+1 && chess[gx][gy] ==0){
            return true;
        }
        else if (gx == cx && cy == gy+2 && gy ==4 && chess[gx][gy] ==0) {
            type = 19;
            return true;
        }
        else if (cy ==gy+1 && (gx == cx-1 || gx == cx+1) && (chess[gx][gy]<10 && chess[gx][gy+1]==9)){
            chess[gx][gy+1]=0;
            return true;
        }
        else if (cy ==gy+1 && (gx == cx-1 || gx == cx+1) && (chess[gx][gy]<10 && chess[gx][gy] !=0)){
            return true;
        }   

        return false;
    }

    boolean checkCastle(int cx,int cy,int gx,int gy){
        // the castle. rule is gx,cx, gy,cy has to be a line and nothing between them
        if (gy>cy && gx ==cx){  //the target is on top of the origin
            for (int i = 1; cy+i<gy;i++){   //the blocks between the two point
                if (chess[cx][cy+i] !=0) return false; //if there is any pawn between them, then cannot move their (this doen't include the target block)
            }
            return true;
        }
        else if (gy<cy && gx ==cx){ //bottom
            for (int i = -1; cy+i>gy;i--){ 
                if (chess[cx][cy+i] !=0) return false;
            }
            return true;
        }
        else if (gx>cx && gy ==cy){     //left and right
            for (int i = 1; cx+i<gx;i++){
                if (chess[cx+i][cy] !=0) return false;
            }
            return true;
        }
        else if (gx<cx && gy ==cy){
            for (int i = -1; cx+i>gx;i--){
                if (chess[cx+i][cy] !=0) return false;
            }
            return true;
        }

        return false;
    }   

    boolean checkKnight(int cx,int cy,int gx,int gy){   //the eight positions a knight can reach
        if (gx ==cx +2 && gy == cy +1) return true;
        else if (gx ==cx +2 && gy == cy -1) return true;
        else if (gx ==cx -2 && gy == cy +1) return true;
        else if (gx ==cx -2 && gy == cy -1) return true;
        else if (gx ==cx +1 && gy == cy +2) return true;
        else if (gx ==cx +1 && gy == cy -2) return true;
        else if (gx ==cx -1 && gy == cy +2) return true;
        else if (gx ==cx -1 && gy == cy -2) return true;
        return false;
    }

    boolean checkBishop(int cx,int cy,int gx,int gy){
        int tcy = cy+1;   //tcy: temporary cy, the pointer block that is checking between your current piece and target
        int tcx = cx+1;
        while (tcx<8 && tcy<8){     //while in the graph
            if (tcx ==gx && tcy ==gy) {
                return true;   //when reach the target
            }

            if (chess[tcx][tcy] !=0) {
                break; //there is block between them
            }

            tcx ++;
            tcy ++;
        }

        tcy = cy-1;   //reset it to the origin place
        tcx = cx+1;
        while (tcx<8 && tcy>=0){  
            if (tcx ==gx &&tcy ==gy) {
                return true;
            }

            if (chess[tcx][tcy] !=0) {
                break;
            }

            tcx ++;
            tcy --;
        }

        tcy = cy+1;
        tcx = cx-1;
        while (tcx>=0 && tcy<8){
            if (tcx ==gx &&tcy ==gy) {
                return true;
            }

            if (chess[tcx][tcy] !=0) {
                break;
            }

            tcx --;
            tcy ++;
        }

        tcy = cy-1;
        tcx = cx-1;
        while (tcx>=0 && tcy>=0){
            if (tcx ==gx &&tcy ==gy) {
                return true;
            }

            if (chess[tcx][tcy] !=0) {
                break;
            }

            tcx --;
            tcy --;
        }

        return false;
    }

    boolean checkQueen(int cx,int cy,int gx,int gy){
        if (checkCastle(cx,cy,gx,gy) || checkBishop(cx,cy,gx,gy)) {
            return true;  //queen combines both straight and corner
        }

        return false;
    }

    boolean checkKing(int cx,int cy,int gx,int gy){ //king have eight positions
        if (gx==cx+1 && gy ==cy +1) return true;
        else if (gx==cx+1 && gy ==cy -1) return true;
        else if (gx==cx-1 && gy ==cy +1) return true;
        else if (gx==cx-1 && gy ==cy -1) return true;
        else if (gx==cx+1 && gy ==cy) return true;
        else if (gx==cx && gy ==cy +1) return true;
        else if (gx==cx-1 && gy ==cy) return true;
        else if (gx==cx && gy ==cy -1) return true;

        return false;
    }

    void updateRestrictionAreas(){
        whitebangraph = new int[8][8];  //reset the restriction area to empty
        blackbangraph = new int[8][8];

        whiteban(); //ban the areas that black pawn can reach
        blackban();// same
    }

    void whiteban(){    //update the areas that the white king cannot reach
        for (int x = 0;x<8;x++){
            for (int y = 0;y<8;y++){    //the x and y below this line means the position on the 8x8 graph

                if (chess[x][y] ==1 || chess[x][y] ==9){    //1 is normal pawn and 9 is enpassion
                    updateEnemyBlackPawnRestrictedArea(x,y);
                }
                else if (chess[x][y]==2){
                    updateEnemyBlackCastleRestrictedArea(x,y);
                }
                else if (chess[x][y]==3){
                    updateEnemyBlackKnightRestrictedArea(x,y);
                }
                else if (chess[x][y]==4){
                    updateEnemyBlackBishopRestrictedArea(x,y);
                }
                else if (chess[x][y]==5){
                    updateEnemyBlackCastleRestrictedArea(x,y);
                    updateEnemyBlackBishopRestrictedArea(x,y);
                }
                else if (chess[x][y]==6){
                    updateEnemyBlackKingRestrictedArea(x,y);
                }
            }
        }
    }

    void updateEnemyBlackPawnRestrictedArea(int x,int y){
        if (y+1<8){     //pown only have two places, the top left and right cornor
            if (x-1>=0){
                whitebangraph[x-1][y+1]=1;
            }

            if (x+1<8){
                whitebangraph[x+1][y+1]=1;
            }
        }
    }

    void updateEnemyBlackCastleRestrictedArea(int x,int y){
        for (int i =1;i+y<8;i++){   // four for loops, top bottom left and right
            whitebangraph[x][y+i] =1;   
            if (chess[x][y+i]!=0) break; 
        }

        for (int i =-1;i+y>=0;i--){
            whitebangraph[x][y+i] =1;
            if (chess[x][y+i]!=0) break;
        }

        for (int i =1;x+i<8;i++){
            whitebangraph[x+i][y] =1;
            if (chess[x+i][y]!=0) break;
        }

        for (int i =-1;x+i>=0;i--){
            whitebangraph[x+i][y] =1;
            if (chess[x+i][y]!=0) break;
        }
    }

    void updateEnemyBlackBishopRestrictedArea(int x, int y){
        int tx = x+1;   //temporory x, same as check bishop
        int ty = y+1;

        while (ty<8 && tx <8){
            if (chess[tx][ty]!=0){
                break;
            }
            whitebangraph[tx][ty] =1;
            tx++;
            ty++;
        }

        tx = x+1;
        ty = y-1;

        while (ty>=0 && tx <8){
            if (chess[tx][ty]!=0){
                break;
            }
            whitebangraph[tx][ty] =1;
            tx++;
            ty--;
        }

        tx = x-1;
        ty = y+1;

        while (ty<8 && tx >=0){
            if (chess[tx][ty]!=0){
                break;
            }
            whitebangraph[tx][ty] =1;
            tx--;
            ty++;
        }

        tx = x-1;
        ty = y-1;
        
        while (ty>=0 && tx >=0){
            if (chess[tx][ty]!=0){
                break;
            }
            whitebangraph[tx][ty] =1;
            tx--;
            ty--;
        }
    }

    void updateEnemyBlackKnightRestrictedArea(int x, int y){
        if (x+1<8 && y+2<8) whitebangraph[x+1][y+2] =1;
        if (x-1>=0 && y+2<8) whitebangraph[x-1][y+2] =1;
        if (x+2<8 && y+1<8) whitebangraph[x+2][y+1] =1;
        if (x-2>=0 && y+1<8) whitebangraph[x-2][y+1] =1;
        if (x+1<8 && y-2>=0) whitebangraph[x+1][y-2] =1;
        if (x-1>=0 && y-2>=0) whitebangraph[x-1][y-2] =1;
        if (x+2<8 && y-1>=0) whitebangraph[x+2][y-1] =1;
        if (x-2>=0 && y-1>=0) whitebangraph[x-2][y-1] =1;
    }

    void updateEnemyBlackKingRestrictedArea(int x, int y){
        if (x-1>=0 && y-1>=0) whitebangraph[x-1][y-1] =1;
        if (x-1>=0) whitebangraph[x-1][y] =1;
        if (x-1>=0 && y+1<8) whitebangraph[x-1][y+1] =1;
        if (y-1>=0) whitebangraph[x][y-1] =1;
        if (y+1<8) whitebangraph[x][y+1] =1;
        if (x+1<8 && y-1>=0) whitebangraph[x+1][y-1] =1;
        if (x+1<8) whitebangraph[x+1][y] =1;
        if (x+1<8 && y+1<8) whitebangraph[x+1][y+1] =1;
    }

    void blackban(){    // same with black side
        for (int x = 0;x<8;x++){
            for (int y = 0;y<8;y++){
                if (chess[x][y] >=10){
                    //blackbangraph[x][y]=1;  
                }
                if (chess[x][y] ==11 || chess[x][y] ==19){
                    updateEnemyWhitePawnRestrictedArea(x,y);
                }

                if (chess[x][y]==12 ){
                    updateEnemyWhiteCastleRestrictedArea(x,y);
                }
                if (chess[x][y]==13 ){
                    updateEnemyWhiteKnightRestrictedArea(x,y);
                }
                if (chess[x][y]==14){
                    updateEnemyWhiteBishopRestrictedArea(x,y);
                }
                if (chess[x][y]==15){
                    updateEnemyWhiteCastleRestrictedArea(x,y);
                    updateEnemyWhiteBishopRestrictedArea(x,y);

                }
                if (chess[x][y]==16){
                    updateEnemyWhiteKingRestrictedArea(x,y);

                }
            }
        }
    }
    
    void updateEnemyWhitePawnRestrictedArea(int x,int y){
        if (y-1>=0){
            if (x-1>=0){
                blackbangraph[x-1][y-1]=1;
            }
            if (x+1<8){
                blackbangraph[x+1][y-1]=1;
            }

        }
    }
    
    void updateEnemyWhiteCastleRestrictedArea(int x,int y){
        for (int i =1;i+y<8;i++){
            blackbangraph[x][y+i] =1;
            if (chess[x][y+i]!=0 && chess[x][y+i]!=12) break;
        }

        for (int i =-1;i+y>=0;i--){
            blackbangraph[x][y+i] =1;
            if (chess[x][y+i]!=0 && chess[x][y+i]!=12) break;
        }

        for (int i =1;x+i<8;i++){
            blackbangraph[x+i][y] =1;
            if (chess[x+i][y]!=0 && chess[x+i][y]!=12) break;
        }

        for (int i =-1;x+i>=0;i--){
            blackbangraph[x+i][y] =1;
            if (chess[x+i][y]!=0 && chess[x+i][y]!=12) break;
        }
    }
    
    void updateEnemyWhiteBishopRestrictedArea(int x, int y){
        int tx = x+1;
        int ty = y+1;
        while (ty<8 && tx <8){
            if (chess[tx][ty]!=0){
                break;
            }
            blackbangraph[tx][ty] =1;
            tx++;
            ty++;
        }
        tx = x+1;
        ty = y-1;
        while (ty>=0 && tx <8){
            if (chess[tx][ty]!=0){
                break;
            }
            blackbangraph[tx][ty] =1;
            tx++;
            ty--;
        }
        tx = x-1;
        ty = y+1;
        while (ty<8 && tx >=0){
            if (chess[tx][ty]!=0){
                break;
            }
            blackbangraph[tx][ty] =1;
            tx--;
            ty++;
        }
        tx = x-1;
        ty = y-1;
        while (ty>=0 && tx >=0){
            if (chess[tx][ty]!=0){
                break;
            }
            blackbangraph[tx][ty] =1;
            tx--;
            ty--;
        }
    }
    
    void updateEnemyWhiteKnightRestrictedArea(int x, int y){
        if (x+1<8 && y+2<8) blackbangraph[x+1][y+2] =1;
        if (x-1>=0 && y+2<8) blackbangraph[x-1][y+2] =1;
        if (x+2<8 && y+1<8) blackbangraph[x+2][y+1] =1;
        if (x-2>=0 && y+1<8) blackbangraph[x-2][y+1] =1;
        if (x+1<8 && y-2>=0) blackbangraph[x+1][y-2] =1;
        if (x-1>=0 && y-2>=0) blackbangraph[x-1][y-2] =1;
        if (x+2<8 && y-1>=0) blackbangraph[x+2][y-1] =1;
        if (x-2>=0 && y-1>=0) blackbangraph[x-2][y-1] =1;
    }
    
    void updateEnemyWhiteKingRestrictedArea(int x, int y){
        if (x-1>=0 && y-1>=0) blackbangraph[x-1][y-1] =1;
        if (x-1>=0) blackbangraph[x-1][y] =1;
        if (x-1>=0 && y+1<8) blackbangraph[x-1][y+1] =1;
        if (y-1>=0) blackbangraph[x][y-1] =1;
        if (y+1<8) blackbangraph[x][y+1] =1;
        if (x+1<8 && y-1>=0) blackbangraph[x+1][y-1] =1;
        if (x+1<8) blackbangraph[x+1][y] =1;
        if (x+1<8 && y+1<8) blackbangraph[x+1][y+1] =1;
    }
}
