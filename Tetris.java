/*KEYS 
left arrow key: move left
right arrow key: move right
up arrow key: rotate CW
down array key: soft drop by 1
space bar: hard drop
Z key: rotate CCW
c key: hold tetromino
(these are the same keys as tetris.com)
*/
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.* ;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Tetris extends JFrame implements ChangeListener, ActionListener, KeyListener{
   //declare variables
   nextUpPanel nextUpPanel;
   holdPanel holdPanel;
   AnimationPanel ap;
   JButton resetButton;
   JPanel sidePanel, textsPanel, accelPanel, buttonsPanel, tetrominosPanel, blankPanel;
   JRadioButton accelOn, accelOff;
   ButtonGroup accelGroup;
   //i made these variables static because I call them throughout the code in methods
   static JSlider diffSlid;
   static JTextField linesText;
   static int xPointer, yPointer, shape, rotation=0,totalLines=0, totalScore,pause=1000, nextShape,holdShape, valWhenAccTurnedOn;
   static boolean holdingAPiece, acceleration, firstPiece, reset;
   //the number stored in the array will be 0-7, 7 will mean empty, 0-6 will symbolize the color in that space
   //the game board is 20x10 in size, but I put a 1 block layer around the board to prevent ArrayOutOfBounds errors
   static int[][] board= new int[22][12];
   //the index of these colours correspond exactly to the colour of the shapes
   final Color[] COLOURS=new Color[]{Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, Color.pink, Color.red, Color.gray};
   //a 4d array to store all the states of the tetrominos. the first dimension stores what shape, the second dimension stores which rotation state, the third dimension stores the columns, the forth dimension stores the rows
   //therefore, the array size is [7][4][4][4]
   //shape 0= I Shape, shape 1= O Shape, shape 2= T Shape, shape 3= L Shape, shape 4= J Shape, shape 5= S Shape, shape 6= Z Shape
   final static public int[][][][] TETROMINO={
                      {{{0,0,0,0},
                        {1,1,1,1},
                        {0,0,0,0},
                        {0,0,0,0}}, 
                       {{0,0,1,0},
                        {0,0,1,0},
                        {0,0,1,0},
                        {0,0,1,0}},
                       {{0,0,0,0},
                        {0,0,0,0},
                        {1,1,1,1},
                        {0,0,0,0}},
                       {{0,1,0,0},
                        {0,1,0,0},
                        {0,1,0,0},
                        {0,1,0,0}}},
                      
                      {{{0,0,0,0},
                        {0,1,1,0},
                        {0,1,1,0},
                        {0,0,0,0}},
                       {{0,0,0,0},
                        {0,1,1,0},
                        {0,1,1,0},
                        {0,0,0,0}},
                       {{0,0,0,0},
                        {0,1,1,0},
                        {0,1,1,0},
                        {0,0,0,0}},
                       {{0,0,0,0},
                        {0,1,1,0},
                        {0,1,1,0},
                        {0,0,0,0}}},
                        
                      {{{0,0,0,0},
                        {0,0,0,0},
                        {1,1,1,0},
                        {0,1,0,0}},
                       {{0,0,0,0},
                        {0,1,0,0},
                        {1,1,0,0},
                        {0,1,0,0}},
                       {{0,0,0,0},
                        {0,1,0,0},
                        {1,1,1,0},
                        {0,0,0,0}},
                       {{0,0,0,0},
                        {0,1,0,0},
                        {0,1,1,0},
                        {0,1,0,0}}},
                      
                      {{{0,0,0,0},
                        {0,0,0,0},
                        {1,1,1,0},
                        {1,0,0,0}},
                       {{0,0,0,0},
                        {1,1,0,0},
                        {0,1,0,0},
                        {0,1,0,0}},
                       {{0,0,0,0},
                        {0,0,1,0},
                        {1,1,1,0},
                        {0,0,0,0}},
                       {{0,0,0,0},
                        {0,1,0,0},
                        {0,1,0,0},
                        {0,1,1,0}}},
                        
                      {{{0,0,0,0},
                        {0,0,0,0},
                        {1,1,1,0},
                        {0,0,1,0}},
                       {{0,0,0,0},
                        {0,1,0,0},
                        {0,1,0,0},
                        {1,1,0,0}},
                       {{0,0,0,0},
                        {1,0,0,0},
                        {1,1,1,0},
                        {0,0,0,0}},
                       {{0,0,0,0},
                        {0,1,1,0},
                        {0,1,0,0},
                        {0,1,0,0}}},
                        
                      {{{0,0,0,0},
                        {0,1,1,0},
                        {1,1,0,0},
                        {0,0,0,0}},
                       {{0,0,0,0},
                        {0,1,0,0},
                        {0,1,1,0},
                        {0,0,1,0}},
                       {{0,0,0,0},
                        {0,0,0,0},
                        {0,1,1,0},
                        {1,1,0,0}},
                       {{0,0,0,0},
                        {1,0,0,0},
                        {1,1,0,0},
                        {0,1,0,0}}},
                     
                      {{{0,0,0,0},
                        {1,1,0,0},
                        {0,1,1,0},
                        {0,0,0,0}},
                       {{0,0,0,0},
                        {0,0,1,0},
                        {0,1,1,0},
                        {0,1,0,0}},
                       {{0,0,0,0},
                        {0,0,0,0},
                        {1,1,0,0},
                        {0,1,1,0}},
                       {{0,0,0,0},
                        {0,1,0,0},
                        {1,1,0,0},
                        {1,0,0,0}}}};

   public Tetris(){
      super("Tetris");  
      //the three panels where I will be drawing tetrominos       
      ap = new AnimationPanel();
      ap.setPreferredSize(new Dimension(400,800));    
      holdPanel = new holdPanel();
      holdPanel.setPreferredSize(new Dimension(180,140));
      nextUpPanel = new nextUpPanel();
      nextUpPanel.setPreferredSize(new Dimension(180,140));
      
      //grouped the two smaller panels for cleaner gui      
      tetrominosPanel=new JPanel();
      tetrominosPanel.setLayout(new FlowLayout());
      tetrominosPanel.add(new JLabel("Next Up"));
      tetrominosPanel.add(nextUpPanel);
      tetrominosPanel.add(new JLabel("Hold    "));    
      tetrominosPanel.add(holdPanel);
      tetrominosPanel.setPreferredSize(new Dimension(200,350));
      
      //difficulty slider to change the pause duration(aka speed)
      diffSlid = new JSlider( JSlider.HORIZONTAL, 1, 10, 1);
      diffSlid.setMajorTickSpacing( 1 );
      diffSlid.setMinorTickSpacing( 1 );
      diffSlid.setPaintTicks( true );
      diffSlid.setPaintLabels( true );
      diffSlid.addChangeListener( this );
      diffSlid.setFocusable(false);
      diffSlid.setName("diffSlid"); 
      
      //buttons to turn on/off acceleration
      accelOn = new JRadioButton("ON", false );
      accelOn.setActionCommand( "AccelOn" );
      accelOn.addActionListener( this );
      accelOn.setFocusable(false);
      accelOff = new JRadioButton("OFF", true );
      accelOff.setActionCommand( "AccelOff" );
      accelOff.addActionListener( this );
      accelOff.setFocusable(false);     
      accelGroup = new ButtonGroup();
      accelGroup.add( accelOn );  
      accelGroup.add( accelOff );
      
      //grouped the buttons for cleaner gui
      accelPanel = new JPanel();
      accelPanel.setLayout( new BoxLayout( accelPanel, BoxLayout.X_AXIS ) );
      accelPanel.add( new JLabel("       Acceleration ") );
      accelPanel.add( accelOn ); 
      accelPanel.add( accelOff ); 
      
      //text field that prints out the current lines cleared
      linesText = new JTextField( 3 );
      linesText.setEditable( false );
      linesText.setText(totalLines + " ");
      
      //button to reset the game(even if it ended)
      resetButton = new JButton("Reset");
      resetButton.addActionListener(this);
      resetButton.setFocusable(false);
      resetButton.setPreferredSize(new Dimension(150,50));
      
      //blank panel for cleaner gui spacing
      blankPanel=new JPanel();
      blankPanel.setPreferredSize(new Dimension(200,100));
      
      //a panel to group all of the panels that will go on the side
      sidePanel=new JPanel();
      sidePanel.setLayout( new FlowLayout());
      sidePanel.add(tetrominosPanel);
      sidePanel.add(blankPanel);
      sidePanel.add(new JLabel("Difficulty"));
      sidePanel.add(diffSlid);
      sidePanel.add(accelPanel);
      sidePanel.add(new JLabel("Lines"));
      sidePanel.add(linesText);   
      sidePanel.add(resetButton);
      sidePanel.setPreferredSize(new Dimension(200,800));           
      
      //now only have to add two panels. the Tetris game panel, and side panel
      setLayout(new FlowLayout());
      add(ap);
      add(sidePanel);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      addKeyListener(this);
      setVisible(true);
      setResizable(false);
      //focus the window so that keyListener will actually listen
      setFocusable(true);
      requestFocusInWindow();
      setSize(650, 850);
      setLocation(200,0); 
   }
   //the panel where the actual game is played
   private class AnimationPanel extends JPanel {
        
      public void paintComponent(Graphics g){
         Graphics2D g2d = (Graphics2D) g;
         super.paintComponent(g);
         //set the blackground to gray
         g2d.setColor(Color.gray);
         g2d.fillRect(0,0, 400, 800);
         //first, fill the board with all the colours stored in the board[][] right now. 
         //these will be the pieces that are already solidified into the bottom of the board.
         for(int i=0;i<20;i++){
            for(int j=0;j<10;j++){
               //if the board isn't empty, draw the squares
               //its i+1 and j+1 to compensate for the 1 block protective layer(see setUpBoard())
               if(board[i+1][j+1]!=7){
                  g2d.setColor(COLOURS[board[i+1][j+1]]);
                  //the size of square is 39 instead of 40 to create a more 3d effect
                  g2d.fillRect(j*40,i*40,39,39);
                  //makes the top and left sides brighter, and the bottom and right sides darker for 3D effect
                  g2d.setColor(COLOURS[board[i+1][j+1]].brighter());
                  g2d.drawLine(j*40,i*40,j*40,(i+1)*40-1);
                  g2d.drawLine(j*40,i*40,(j+1)*40-1,i*40);
               
                  g2d.setColor(COLOURS[board[i+1][j+1]].darker());
                  g2d.drawLine((j+1)*40-1,i*40+1,(j+1)*40-1,(i+1)*40-1);
                  g2d.drawLine(j*40+1,(i+1)*40-1,(j+1)*40-1,(i+1)*40-1);               
               }
            }
         }
         //now draw the piece that the user is currently controlling
         //I have an xPointer and yPointer that the user controls. the co-ordinates of the pointer will always be in the middle of the 4x4 TETROMINO array
         //so when I move pieces, I am just moving the x and y Pointers and drawing the corresponding tetris piece around that (x,y) co-ordinate
         for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
               if(TETROMINO[shape][rotation][i][j]==1){
                  //set colour to the corresponding shape colour
                  g2d.setColor(COLOURS[shape]);
                  //since the pointer is in the middle of the 4x4 array, subtract by 2 to adjust
                  g2d.fillRect(40*(xPointer+(j-2)),40*(yPointer+(i-2)),39,39);
                  //makes the top and left sides brighter, and the bottom and right sides darker for 3D effect
                  g2d.setColor(COLOURS[shape].brighter());
                  g2d.drawLine(40*(xPointer+(j-2)),40*(yPointer+(i-2)),40*(xPointer+(j-2)),40*(yPointer+(i-2)+1)-1);
                  g2d.drawLine(40*(xPointer+(j-2)),40*(yPointer+(i-2)),40*(xPointer+(j-2)+1)-1,40*(yPointer+(i-2)));
               
                  g2d.setColor(COLOURS[shape].darker());
                  g2d.drawLine(40*(xPointer+(j-2)+1)-1,40*(yPointer+(i-2))+1,40*(xPointer+(j-2)+1)-1,40*(yPointer+(i-2)+1)-1);
                  g2d.drawLine(40*(xPointer+(j-2))+1,40*(yPointer+(i-2)+1)-1,40*(xPointer+(j-2)+1)-1,40*(yPointer+(i-2)+1)-1);
               
               }
            }
         }
         //if the game resets, then reset the panel to all gray
         if(reset){
            g2d.setColor(Color.gray);
            g2d.fillRect(0,0, 400, 800); 
         }
         //if the game is over, then print you lose
         if(gameOver()){
            g2d.setColor(Color.gray);
            g2d.fillRect(0,0, 400, 800); 
            g2d.setColor(Color.black);
            g2d.drawString("YOU LOSE!!", 200, 400);
         }          
         repaint();
      }
   
   }

   //displays which tetromino is next up
   private class nextUpPanel extends JPanel {
   
      public void paintComponent(Graphics g) {
         Graphics2D g2d = (Graphics2D) g;
         super.paintComponent(g);
         //sets the backgroud to white
         //the squares are 40x40 pixels, the widest piece is 4 squares wide, and the tallest piece is 3 squares tall. 
         //so leaving a 10 pixel border on the outside for cleaner looks, set size of rect to (10+(40*4)+10,10+(40*3)+10) 
         g2d.setColor(Color.white);
         g2d.fillRect(0,0, 180, 140);
         //draws the tetromino shape that is currently held in nextShape
         for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
               if(TETROMINO[nextShape][0][i][j]==1){         
                  g2d.setColor(COLOURS[nextShape]);
                  g2d.fillRect(40*j+10,40*(i-1)+10,39,39);
                  //makes the top and left sides brighter, and the bottom and right sides darker for 3D effect
                  g2d.setColor(COLOURS[nextShape].brighter());
                  g2d.drawLine(j*40+10,(i-1)*40+10,j*40+10,i*40-1+10);
                  g2d.drawLine(j*40+10,(i-1)*40+10,(j+1)*40-1+10,(i-1)*40+10);
               
                  g2d.setColor(COLOURS[nextShape].darker());
                  g2d.drawLine((j+1)*40-1+10,(i-1)*40+1+10,(j+1)*40-1+10,i*40-1+10);
                  g2d.drawLine(j*40+1+10,i*40-1+10,(j+1)*40-1+10,i*40-1+10);
               }
            }
         }
         repaint();
      }
   }
   //same as nextUpPanel, but displays which tetromino is on hold
   private class holdPanel extends JPanel {
   
      public void paintComponent(Graphics g) {
         Graphics2D g2d = (Graphics2D) g;
         super.paintComponent(g);
      
         g2d.setColor(Color.white);
         g2d.fillRect(0,0, 180, 140);
         //since you start out the game not holding a piece, only draw when you are
         if(holdingAPiece==true){
            for(int i=0;i<4;i++){
               for(int j=0;j<4;j++){
                  if(TETROMINO[holdShape][0][i][j]==1){         
                     g2d.setColor(COLOURS[holdShape]);
                     g2d.fillRect(40*j+10,40*(i-1)+10,39,39);
                  
                     g2d.setColor(COLOURS[holdShape].brighter());
                     g2d.drawLine(j*40+10,(i-1)*40+10,j*40+10,i*40-1+10);
                     g2d.drawLine(j*40+10,(i-1)*40+10,(j+1)*40-1+10,(i-1)*40+10);
                  
                     g2d.setColor(COLOURS[holdShape].darker());
                     g2d.drawLine((j+1)*40-1+10,(i-1)*40+1+10,(j+1)*40-1+10,i*40-1+10);
                     g2d.drawLine(j*40+1+10,i*40-1+10,(j+1)*40-1+10,i*40-1+10);
                  }
               }
            }
         }
         repaint();
      }
   } 
     
   //method for moving the tetromino piece. parameters are how you want to move it (x,y,rotation)
   public static void movePiece(int deltaX, int deltaY,int deltaRota){
      //if that move is valid, then move the piece
      if(isMoveValid(deltaX, deltaY, deltaRota)==true){
         yPointer+=deltaY;
         xPointer+=deltaX; 
         //modulo the rotation by 4 to keep it cycling
         rotation= (rotation+deltaRota)%4;    
      }
   }
   //method to check if a move is valid. parameters are how you want to move it (x,y,rotation)
   public static boolean isMoveValid(int deltaX, int deltaY,int deltaRota){
      int counter=0,row=-1,col=-1;
      //to keep the rotation number between 0 and 3, +4 if it becomes negative
      if((rotation+deltaRota)<0) 
         rotation+=4;
      //checks if the space where the shape that has been rotated by deltaRota and moved by deltaX and deltaY is empty
      //since there are 4 squares to a tetromino, if the all 4 squares are empty, then it is a valid move  
      for(int i=0;i<4;i++){
         for(int j=0;j<4;j++){ 
            if(TETROMINO[shape][(rotation+deltaRota)%4][i][j]==1){
               //because the I shape can increase in x or y by 2 at a time when it rotates, the 1 block protective layer isn't enough to prevent ArrayOutOfBounds
               //so check premtively, and if it is out of bounds, make the move invalid by not increasing the counter
                  if(yPointer+deltaY+i-1<0||yPointer+deltaY+i-1>21||xPointer+deltaX+j-1<0||xPointer+deltaX+j-1>21)
                     break;
               //if the "would be" space is empty, then add a counter        
               if(board[yPointer+deltaY+i-1][xPointer+deltaX+j-1]==7){
                  counter++;
               }
               else{
                  col=j;
                  row=i;
               }
            }
         }
      }
      //if all 4 squares were empty, then move is Valid
      if(counter==4){
         return true;
      }
      //if the move was invalid due to the rotation, adjust the piece's position to allow it to rotate
      if(deltaRota!=0){
      //however, since the I shape increases 2 at a time, the code got extremely confusing and couldn't make it adjust like the other pieces
         if(shape!=0){       
            adjustRotation(rotation,rotation+deltaRota);
            return true;
         }           
      } 
      
      
      //if the move was invalid and it was moving downwards, then that means the piece has collided and will be solidified
      if(deltaY>0){
         for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
               if(TETROMINO[shape][rotation][i][j]==1){
                  //add that piece's shape(colour) to the board 
                  board[yPointer+i-1][xPointer+j-1]=shape;
               }
            }
         }
         //check for clear lines
         clearLines();
         //create a new piece
         newPiece();
      }    
      return false;
   }
   
   //method to adjust the piece's position to allow it to rotate
   public static void adjustRotation(int rotation1, int rotation2){
      //start off the max and min at the opposite values
      int maxY1=0,maxX1=0,minY1=3,minX1=3;
      int maxY2=0,maxX2=0,minY2=3,minX2=3;
      //get the max and min of the first rotation state 
      for(int i=0;i<4;i++){
         for(int j=0;j<4;j++){ 
            if(TETROMINO[shape][rotation1%4][i][j]==1){
               if(j>maxX1)
                  maxX1=j;
               else if(j<minX1)
                  minX1=j;
               if(i>maxY1)
                  maxY1=i;
               else if(j<minY1)
                  minY1=j;
            }
         }
      }
      //get the max and min of the second rotation state
      for(int i=0;i<4;i++){
         for(int j=0;j<4;j++){ 
            if(TETROMINO[shape][rotation2%4][i][j]==1){
               if(j>maxX2)
                  maxX2=j;
               else if(j<minX2)
                  minX2=j;
               if(i>maxY2)
                  maxY2=i;
               else if(j<minY2)
                  minY2=j;
            }
         }
      }
      //get the difference
      int maxXDiff=maxX2-maxX1;
      int maxYDiff=maxY2-maxY1;
      int minXDiff=minX2-minX1;
      int minYDiff=minY2-minY1;
      //if the max increased, then move over the piece by the difference
      if(maxXDiff>0)
         xPointer-=maxXDiff;
      if(maxYDiff>0)
         yPointer-=maxYDiff;
      //if the min decreased, then move over the piece by the difference
      if(minXDiff<0)
         xPointer+=Math.abs(minXDiff);
      if(minYDiff<0)      
         yPointer+=Math.abs(minYDiff);
   }
   
   //method to clear lines   
   public static void clearLines(){
      int numFullLines = 0;
      //for each row, check if the row is full
      for(int i=1;i<21;i++){
         //assume the row is full at first
         boolean isLineFull =true;
         for(int j=1;j<11;j++){
            //if there is an empty space, then row is not full
            if(board[i][j]==7){
               isLineFull=false;
               break;
            }
         }
         //if the row was full, add to counter
         if(isLineFull==true){
            numFullLines++;
            for(int j=i;j>1;j--){
               for(int k=1;k<11;k++){
                  //move everything above it down by 1
                  board[j][k]=board[j-1][k];   
               }
            }
         }
      }
      //if lines were cleared, increase the lines total. also increase speed if acceleration is turned on
      if (numFullLines > 0) {
         totalLines += numFullLines;
         linesText.setText( totalLines+"" );
         //the acceleration will begin from the moment you press the accelOn button, speeding up from whatever was the current speed
         if(acceleration==true){
            diffSlid.setValue((totalLines-valWhenAccTurnedOn)/2+1);
         }
      }
   }
   //method for creating a new piece
   public static void newPiece(){
      //if it is the first piece, create an extra random next piece first
      if(firstPiece){
         randomNextShape();
         firstPiece=false;
      }
      //the shape will be what the nextShape is, and then nextShape will just pick another random shape
      shape=nextShape;
      randomNextShape();
      //always start the piece with rotation at0
      rotation=0;
      //some pieces start at row 1, some start at row 2 in their 4x4 tetromino array, so adjust accordingly
      if(shape==0||shape==1||shape==5||shape==6){
         xPointer=5;
         yPointer=2;
      }
      else{
         xPointer=5;
         yPointer=1;
      }
   }
   //stores a random number in nextShape
   public static void randomNextShape(){
      Random rand = new Random();
      nextShape=rand.nextInt(6);   
   }
   //method that holds the current tetromino and replaces it with the tetromino in hold
   public static void holdPiece(){
      //the game starts out with no piece on hold, so the first time I press hold, I will use the newPiece method where I swap with the nextShape instead
      if(!holdingAPiece){
         holdShape=shape;
         newPiece();
         holdingAPiece=true;
      }
      ////swap the shape with the hold shape
      else{
         int temp=shape;
         shape=holdShape;
         holdShape=temp;
         rotation=0;
         //set the new shape's position accordingly
         if(shape==0||shape==1||shape==5||shape==6){
            xPointer=5;
            yPointer=2;
         }
         else{
            xPointer=5;
            yPointer=1;
         }
      }
      
   }
   //method for when I press spacebar
   //drops the tetromino instantly 
   public void hardDrop(){
      //the maximum times it can loop is if it hits the bottom of the board, so loop until y<20   
      int y=yPointer;
      while (y < 20) {
         //moves the piece down by 1
         movePiece(0,1,0);
         //if the move is invalid, then stop
         if(!isMoveValid(0,1,0))
            break;
         y++;
      }
   }
   //method to set up the board. fill the board array with all 7's(empty), and fill the outer layer with 1 (could've been any number between 0-6. just needs to be not empty)  
   public static void setUpBoard(){
      for(int i=0;i<22;i++){
         for(int j=0;j<12;j++){
            board[i][j]=7;
            if(i==0||i==21||j==0||j==11){
               board[i][j]=1;
            }
         }
      }
   }
   
   //method to reset the game
   public static void resetGame(){
      //momentarily set reset to true so that I can reset the Animation Panel
      reset=true; 
      //just reset all variables back to their starting state
      firstPiece=true;
      holdingAPiece=false;
      acceleration=false;
      setUpBoard();
      newPiece();
      totalLines=0;
      linesText.setText( totalLines+"" );
      totalScore=0;
      diffSlid.setValue(1);
      reset=false; 
   }
   
   //method to check if game is over
   //game is over when the new piece spawn location is blocked
   public static  boolean gameOver(){
      if(!isMoveValid(0,0,0))
         return true;   
      else
         return false;
   }

   public void actionPerformed( ActionEvent evt) {      
      //if you press the reset button, reset the game
      if (evt.getActionCommand().equals("Reset")){
         resetGame();
      }
      //if you turn on accleration, set acceleration=true and start acceleration from that moment on
      if (evt.getActionCommand().equals("AccelOn")){
         acceleration=true;
         valWhenAccTurnedOn=totalLines;
      }      
      if (evt.getActionCommand().equals("AccelOff")){
         acceleration=false;
      }
      
      
   }  
   public void stateChanged( ChangeEvent evt ){
      JSlider source;
      source = (JSlider)evt.getSource();
      //if you move the slider, change the pause accordingly.
      //the pause variable will decrease when speed increases
      if ( source.getName().equals("diffSlid") ){
         pause=1000/source.getValue();
      }
   }
   @Override
   public void keyTyped(KeyEvent e){
   }
   @Override
   public void keyReleased(KeyEvent e){
   }
   @Override
   public void keyPressed(KeyEvent e){
      //keylistener for user inputs. self explanatory
      int keyCode = e.getKeyCode();
      switch(keyCode) { 
         case KeyEvent.VK_UP:
            movePiece(0,0,1);
            break;
         case KeyEvent.VK_DOWN:
            movePiece(0,1,0);
            break;
         case KeyEvent.VK_LEFT:
            movePiece(-1,0,0);
            break;
         case KeyEvent.VK_RIGHT:
            movePiece(1,0,0);
            break;
         case KeyEvent.VK_Z:
            movePiece(0,0,-1);
            break;
         case KeyEvent.VK_SPACE:
            hardDrop();
            break;
         case KeyEvent.VK_C:
            holdPiece();
      }  
   }
   //main method
   public static void main(String[] args){
      new Tetris();
      //reset game method can also be used to set up game
      resetGame();
      while(true){
         //makes it so that even if game is over, you can press the reset button to keep playing
         if(reset==false){
            while(!gameOver()){
               //pause for the pause duration 
               try {    
                  Thread.sleep(pause);
               }
               catch (Exception exc){ 
                  exc.printStackTrace();
               }
               //move the piece down by 1
               movePiece(0,1,0); 
            }
         
         }
      }
   }
}
  
