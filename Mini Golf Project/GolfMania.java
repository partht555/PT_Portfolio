//Parth Thakre
//GolfMania.java
//Pd 6

import java.awt.*;  import java.awt.event.*; import java.io.*;
import javax.swing.*; import javax.swing.event.*;import java.util.Scanner;

public class GolfMania extends JFrame //Initializes everything and calls PanelA to create the frame
{
    Color holeBackground, buttonColor, holeColor, holeBorder;
    int powerBarY, powerBarX,strokeCounter, totalStrokes, nextHoleY, ballXPreviousValue, aimX, aimY, i;
    double ballMoverX, ballMoverY, angle, ballVelocity, ballY, ballX;
    JFrame frame;
    boolean mainPage, instructionsRead, playGame, displayInstructions, displayWarning, creditsPage, doOnce, themes, goodJobPanel;
    boolean holeOne, holeTwo, holeThree, holeFour, holeFive, hitShot, nextHole, aimSet, classicTheme, skyTheme, montaVistaTheme, onClassicTheme, onSkyTheme, onMontaVistaTheme;
    Image TitleForGame, backgroundPic, ball, nextHoleBackground, strokesDisplay, alternateStrokesDisplay,totalStrokesDisplay ,alternateTotalStrokesDisplay, holesLeftFour, holesLeftThree, holesLeftTwo, holeLeftOne, goodJob, themesPageBackground, classicThemeExample, skyThemeExample, montaVistaThemeExample, creditsPagePic;
    JButton playButton, themeButton, howTo, creditsButton,  backButton, nextButton, returnButton;
    JTextArea instructions, credits,  userName;
    JLabel warning;
    PanelA goA; PanelB goB;
    Timer balltimer, nextTimer;
    Rectangle powerBar, hitButton, holeUno, holeDos, holeTres, holeCuatro,holeCinco, holeTwoBoundry, holeTwoBlocker1, holeTwoBlocker2, holeTwoBlocker3, holeThreeBoundry, holeThreeReseter1, holeThreeReseter2, holeThreeReseter3, holeFourReseter1, holeFourBlocker1, holeFourBlocker2 ,holeFourReseter2, holeFiveReseter1, holeFiveReseter2, holeFiveReseter3, holeFiveBlocker1, holeFiveBlocker2, holeFiveBlocker3, classicThemeBox, skyThemeBox, montaVistaThemeBox, classicThemeSelectButton, skyThemeSelectButton, montaVistaThemeSelectButton;
    Polygon holeOneBoundry, holeFourBoundry, holeFiveBoundry;
    int []boundryOfOneX = new int[7];
    int []boundryOfFiveX = new int[9];
    int []boundryOfOneY = new int[7];
    int []boundryOfFiveY = new int[9];
    public GolfMania() //INITIALIZES EVERYTHING
    {
        classicTheme = true;
        totalStrokes = 0;
        TitleForGame = null;
        creditsPage = displayWarning = instructionsRead = playGame = displayInstructions = holeOne = holeTwo = holeThree = holeFour = holeFive = false;
        instructions = new JTextArea("\n\n\nOBJECTIVE -\n-Try to get the ball in the hole in the least amount of strokes as possible\n\n\nHOW TO PLAY -\n-To control where the ball goes use the mouse to point (click on the screen) in the direction.\n-After choosing the direction choose your power using the power bar on the right\n-Then hit the HIT! Button\n-On some holes there will be a rectangle the same color as the border, this will make you bounce back if it is hit\n-On some holes there will be red rectagles, if touched they will send you back to the start, so BE CAREFUL!\n-There will be a tiny slope into the hole so if you get the ball colse to the hole,so it is possible to make it if you miss by a bit!\n\n\nTHEMES -\n-When you click on the Change Themes button you go to a different panel.\n-If you hover over the theme you want a select button will pop up\n-Once you select the desired theme, the choice will be highlited and you will be set to play the game\n\nGOOD LUCK!!");
        instructions.setForeground(Color.BLACK);
        instructions.setFont(new Font("Comic Sans", Font.PLAIN, 17));
        credits = new JTextArea("Made by - Parth Thakre\nPeriod 6\nMr.Kim");
        credits.setForeground(Color.BLACK);
        credits.setBackground(Color.WHITE);
        credits.setFont(new Font("Comic Sans", Font.PLAIN, 25));
        userName = new JTextArea("Enter Your Name");
        userName.setForeground(Color.BLACK);
        userName.setBackground(Color.WHITE);
        returnButton = new JButton("Return To Main Page");
        returnButton.setBackground(Color.BLACK);
        returnButton.setForeground(Color.WHITE);
        warning = new JLabel("Please Read The Instructions FIRST!!!");
        warning.setForeground(Color.BLACK);
        backButton = new JButton("< Back");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        playButton = new JButton("Play");
        playButton.setBackground(Color.BLACK);
        playButton.setForeground(Color.WHITE);
        themeButton = new JButton("Change Theme");
        themeButton.setBackground(Color.BLACK);
        themeButton.setForeground(Color.WHITE);
        howTo = new JButton("How To Play");
        howTo.setBackground(Color.BLACK);
        howTo.setForeground(Color.WHITE);
        creditsButton = new JButton("Credits");
        creditsButton.setBackground(Color.BLACK);
        creditsButton.setForeground(Color.WHITE);
        nextButton = new JButton("Next Hole ->");
        nextButton.setBackground(Color.BLACK);
        nextButton.setForeground(Color.WHITE);
        mainPage = true;
        goA= new PanelA();
    }

    public static void main(String[] args) //Calls cunstructor
    {
        GolfMania go2 = new GolfMania();
    }
    class PanelA extends JPanel //Creates frame and adds a panel (aka class PanelB) to it
    {
        public PanelA() // Creates frame and adds class panel B to it
        {
            frame = new JFrame("Golf Mania");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 800);
            frame.setLocation(150, 0);
            goB = new PanelB();
            frame.add(goB, BorderLayout.NORTH);
            frame.setVisible(true);
            frame.setResizable(false);
        }
    }
    class PanelB extends JPanel implements ActionListener,MouseListener, MouseMotionListener // Adds all the stuff to the frame
    {
        public PanelB() //Adds actionlisteners and calls DisplayMethod()
        {
            displayMethod();
            playButton.addActionListener(this);
            themeButton.addActionListener(this);
            howTo.addActionListener(this);
            creditsButton.addActionListener(this);
            backButton.addActionListener(this);
            nextButton.addActionListener(this);
            returnButton.addActionListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        public void displayMethod() //Adds all the components to the frame
        {
            removeAll();
            if(mainPage == true)
            {
                setLayout(null);
                setPreferredSize( new Dimension(800,800) );
                add(playButton);
                playButton.setBounds(420, 200, 175, 50);
                add(themeButton);
                themeButton.setBounds(420, 275, 175, 50);
                add(howTo);
                howTo.setBounds(420, 350, 175, 50);
                add(creditsButton);
                creditsButton.setBounds(420, 425, 175, 50);
                if(displayWarning == true)
                {
                    add(warning);
                    warning.setBounds(400 ,500, 230, 50);
                }
            }
            if(themes == true)
            {
                setLayout(null);
                add(backButton);
                backButton.setBounds(5,5, 100, 50);
            }
            if(displayInstructions == true)
            {
                setLayout(null);
                add(backButton);
                add(instructions);
                backButton.setBounds(5,5, 100, 50);
                instructions.setBounds(0, 55, 1000, 745);
                instructionsRead = true;
            }
            if(creditsPage == true)
            {
                setLayout(null);
                add(backButton);
                backButton.setBounds(5,5, 100, 50);

            }
            if(playGame == true)
            {
                if(holeOne == true)
                {
                    setLayout(null);
                    add(backButton);
                    backButton.setBounds(5,5, 100, 50);
                    if(nextHole == true)
                    {
                        add(nextButton);
                        nextButton.setBounds(425, 400, 150, 50);
                    }
                }
                if(holeTwo == true)
                {
                    setLayout(null);
                    add(backButton);
                    backButton.setBounds(5,5, 100, 50);
                    if(nextHole == true)
                    {
                        add(nextButton);
                        nextButton.setBounds(425, 400, 150, 50);
                    }
                }
                if(holeThree == true) {
                    setLayout(null);
                    add(backButton);
                    backButton.setBounds(5, 5, 100, 50);
                    if (nextHole == true) {
                        add(nextButton);
                        nextButton.setBounds(425, 400, 150, 50);
                    }
                }
                if(holeFour == true)
                {
                    setLayout(null);
                    add(backButton);
                    backButton.setBounds(5,5, 100, 50);
                    if(nextHole == true)
                    {
                        add(nextButton);
                        nextButton.setBounds(425, 400, 150, 50);
                    }
                }
                if(holeFive == true)
                {
                    setLayout(null);
                    add(backButton);
                    backButton.setBounds(5,5, 100, 50);
                    if(goodJobPanel == true)
                    {
                        add(returnButton);
                        returnButton.setBounds(400, 400, 200, 50);
                    }
                }
            }
        }
        public void setValues() //SETS all the values depending on the hole
        {
            if(holeOne == true)
            {
                boundryOfOneX[0] = 150; boundryOfOneX[1] = 400;boundryOfOneX[2] = 400;boundryOfOneX[3] = 800; boundryOfOneX[4] = 800; boundryOfOneX[5] =150; boundryOfOneX[6]  = 150;
                boundryOfOneY[0] = 650;boundryOfOneY[1] = 650;boundryOfOneY[2] = 350;boundryOfOneY[3] = 350;boundryOfOneY[4] = 150;boundryOfOneY[5] = 150;boundryOfOneY[6] = 650;
                nextHole = false;
                strokeCounter = 0;
                ballX = 270;
                ballY = 600;
            }
            if(holeTwo == true)
            {
                strokeCounter = 0;
                ballX = 495;
                ballY = 550;
            }
            if(holeThree == true)
            {
                ballX = 495;
                ballY = 650;
                strokeCounter = 0;
            }
            if(holeFour == true)
            {
                boundryOfOneX[0] = 150; boundryOfOneX[1] = 400;boundryOfOneX[2] = 400;boundryOfOneX[3] = 800; boundryOfOneX[4] = 800; boundryOfOneX[5] =150; boundryOfOneX[6]  = 150;
                boundryOfOneY[0] = 650;boundryOfOneY[1] = 650;boundryOfOneY[2] = 350;boundryOfOneY[3] = 350;boundryOfOneY[4] = 150;boundryOfOneY[5] = 150;boundryOfOneY[6] = 650;
                strokeCounter = 0;
                ballX = 270;
                ballY = 600;
            }
            if(holeFive == true)
            {
                boundryOfFiveX[0] = 150; boundryOfFiveX[1] = 350;boundryOfFiveX[2] = 350;boundryOfFiveX[3] = 600; boundryOfFiveX[4] = 600; boundryOfFiveX[5] = 800;boundryOfFiveX[6] = 800; boundryOfFiveX[7] =150; boundryOfFiveX[8]  = 150;
                boundryOfFiveY[0] = 650;boundryOfFiveY[1] = 650;boundryOfFiveY[2] = 350;boundryOfFiveY[3] = 350;boundryOfFiveY[4] = 650;boundryOfFiveY[5] = 650;boundryOfFiveY[6] = 150;boundryOfFiveY[7] = 150;boundryOfFiveY[8] = 650;
                strokeCounter = 0;
                ballX = 240;
                ballY = 600;
            }
            nextHoleY = 200;
            aimSet = false;
            hitShot = false;
            powerBarX = 51;
            powerBarY = 400;
            onClassicTheme = false;
        }
        public void paintComponent(Graphics g) //Draws all the shapes, and Images
        {
            holeOneBoundry = new Polygon(boundryOfOneX, boundryOfOneY, 7);
            holeTwoBoundry = new Rectangle(200, 100, 600, 500);
            holeThreeBoundry = new Rectangle(300, 100, 400, 600);
            holeFourBoundry = new Polygon(boundryOfOneX, boundryOfOneY, 7);
            holeFiveBoundry = new Polygon(boundryOfFiveX, boundryOfFiveY, 9);
            powerBar = new Rectangle(50, 300, 50, 100);
            hitButton = new Rectangle(25, 425, 100, 50);
            holeUno = new Rectangle(700 ,240, 25, 25);
            holeDos = new Rectangle(488, 125, 25, 25);
            holeTres = new Rectangle(490, 125, 25, 25);
            holeCuatro = new Rectangle(700 ,240, 25, 25);
            holeCinco = new Rectangle(687, 587, 25, 25);
            holeTwoBlocker1 = new Rectangle(200, 340, 200, 10);
            holeTwoBlocker2 = new Rectangle(600, 340, 200, 10);
            holeTwoBlocker3 = new Rectangle(400, 200, 200, 10);
            holeThreeReseter1 = new Rectangle(350, 500, 300, 10);
            holeThreeReseter2 = new Rectangle(350, 300, 300, 10);
            holeThreeReseter3 = new Rectangle(400, 200, 200, 10);
            holeFourReseter1 = new Rectangle(600, 175, 10, 150);
            holeFourReseter2 = new Rectangle(400, 175, 10, 150);
            holeFourBlocker1 = new Rectangle(175, 500, 200 ,10);
            holeFourBlocker2 = new Rectangle(175, 350, 200, 10);
            holeFiveReseter1 = new Rectangle(590, 175, 10, 150);
            holeFiveReseter2 = new Rectangle(350, 175, 10, 150);
            holeFiveReseter3 = new Rectangle(625, 500, 150, 10);
            holeFiveBlocker1 = new Rectangle(175, 500, 150 ,10);
            holeFiveBlocker2 = new Rectangle(175, 350, 150, 10);
            holeFiveBlocker3 = new Rectangle(625, 350, 150, 10);
            classicThemeSelectButton = new Rectangle(125, 500, 150, 50);
            skyThemeSelectButton = new Rectangle(425, 500, 150, 50);
            montaVistaThemeSelectButton = new Rectangle(725, 500, 150, 50);
            if(onClassicTheme == false) {
                classicThemeBox = new Rectangle(75, 100, 250, 500);
            }
            if(onClassicTheme == true) {
                classicThemeBox = new Rectangle(50, 75, 275, 525);
            }
            if(onSkyTheme == false) {
                skyThemeBox = new Rectangle(375, 100, 250, 500);
            }
            if(onSkyTheme == true) {
                skyThemeBox = new Rectangle(350, 75, 300, 525);
            }
            if(onMontaVistaTheme == false)
            {
                montaVistaThemeBox = new Rectangle(675, 100, 250, 500);
            }
            if(onMontaVistaTheme == true)
            {
                montaVistaThemeBox = new Rectangle(650, 75, 300, 525);
            }
            if(classicTheme == true || skyTheme == true) {
                 holeBackground = new Color(255, 220, 68);
            }
            if(montaVistaTheme == true) {
                 holeBackground = new Color(233, 232, 255);
            }
            setBackground(holeBackground);
            super.paintComponent(g);
            TitleForGame = new ImageIcon("TheTitle.gif").getImage();//Add gif on school computers
            backgroundPic = new ImageIcon("TheBackground.png").getImage(); // Add png to school computers
            ball = new ImageIcon("Ball.png").getImage();
            nextHoleBackground = new ImageIcon("NextPageBackground.png").getImage();
            strokesDisplay = new ImageIcon("StrokesDisplay.png").getImage();
            alternateStrokesDisplay = new ImageIcon("AlternateStrokesDisplay.png").getImage();
            totalStrokesDisplay = new ImageIcon("TotalStrokesDisplay.png").getImage();
            alternateTotalStrokesDisplay = new ImageIcon("AlternateTotalStrokesDisplay.png").getImage();
            holesLeftFour = new ImageIcon("HolesLeftFour.png").getImage();
            holesLeftThree = new ImageIcon("HolesLeftThree.png").getImage();
            holesLeftTwo = new ImageIcon("HolesLeftTwo.png").getImage();
            holeLeftOne = new ImageIcon("HoleLeftOne.png").getImage();
            goodJob = new ImageIcon("GoodJob.png").getImage();
            themesPageBackground = new ImageIcon("ThemesBackground.jpg").getImage();
            classicThemeExample = new ImageIcon("ClassicTheme.png").getImage();
            skyThemeExample = new ImageIcon("TheSkyTheme.png").getImage();
            montaVistaThemeExample = new ImageIcon("MVPrideTheme.png").getImage();
            creditsPagePic = new ImageIcon("CreditsPagePic.jpg").getImage();
            if(mainPage == true)
            {
                g.drawImage(backgroundPic, 0,0, 1000, 800, null);
                g.drawImage(TitleForGame, 335, 75, 325, 125, this);
            }
            if(creditsPage == true)
            {
                g.setColor(Color.BLACK);
                g.drawImage(creditsPagePic, 0, 0, 1000, 800, null);
                g.setFont(new Font("Serif", Font.BOLD, 25));
                g.drawString("Made By - Parth Thakre", 375, 250);
                g.drawString("Mr. Kim, Period 6", 395, 295);
            }
            if(themes == true)
            {
                ((Graphics2D)g).setStroke(new BasicStroke(5));
                g.setFont(new Font("Serif", Font.BOLD, 25));
                if(onClassicTheme == false)
                {
                    classicThemeBox = new Rectangle(75, 100, 250, 500);
                    g.drawImage(themesPageBackground, 75, 100, 250, 500, null);
                    g.drawImage(classicThemeExample, 95, 250, 210, 200, null);
                    g.setColor(Color.BLACK);
                    g.drawString("Classic Theme", 125, 150);
                     buttonColor = new Color(255, 115, 84);
                    g.setColor(buttonColor);
                    if(classicTheme == true)g.drawRect(75, 100, 250, 500);
                }
                if(onClassicTheme == true)
                {
                    classicThemeBox = new Rectangle(50, 75, 300, 525);
                    g.drawImage(themesPageBackground, 50, 75, 300, 525, null);
                    g.drawImage(classicThemeExample, 70, 225, 255, 225, null);
                    g.setColor(Color.BLACK);
                    g.drawString("Classic Theme", 125, 140);
                     buttonColor = new Color(255, 115, 84);
                    g.setColor(buttonColor);
                    g.fillRect(125, 500, 150, 50);
                    g.setColor(Color.BLACK);
                    g.drawString("Select", 165, 535);
                     buttonColor = new Color(255, 115, 84);
                    g.setColor(buttonColor);
                    if(classicTheme == true) g.drawRect(50, 75, 300, 525);
                }
                if(onSkyTheme == false) {
                    skyThemeBox = new Rectangle(375, 100, 250, 500);
                    g.drawImage(themesPageBackground, 375, 100, 250, 500, null);
                    g.drawImage(skyThemeExample, 395, 250, 210, 200, null);
                    g.setColor(Color.BLACK);
                    g.drawString("Sky Theme", 445, 150);
                     buttonColor = new Color(255, 115, 84);
                    g.setColor(buttonColor);
                    if(skyTheme == true) g.drawRect(375, 100, 250, 500);
                }
                if(onSkyTheme == true) {
                    skyThemeBox = new Rectangle(350, 75, 300, 525);
                    g.drawImage(themesPageBackground, 350, 75, 300, 525, null);
                    g.drawImage(skyThemeExample, 370, 250, 255, 225, null);
                    g.setColor(Color.BLACK);
                    g.drawString("Sky Theme", 445, 140);
                     buttonColor = new Color(255, 115, 84);
                    g.setColor(buttonColor);
                    g.fillRect(425, 500, 150, 50);
                    g.setColor(Color.BLACK);
                    g.drawString("Select", 465, 535);
                     buttonColor = new Color(255, 115, 84);
                    g.setColor(buttonColor);
                    if(skyTheme == true) g.drawRect(350, 75, 300, 525);

                }
                if(onMontaVistaTheme == false) {
                    montaVistaThemeBox = new Rectangle(675, 100, 250, 500);
                    g.drawImage(themesPageBackground, 675, 100, 250, 500, null);
                    g.drawImage(montaVistaThemeExample, 695, 250, 210, 200, null);
                    g.setColor(Color.BLACK);
                    g.drawString("Monta Vista Theme", 690, 150);
                     buttonColor = new Color(255, 115, 84);
                    g.setColor(buttonColor);
                    if(montaVistaTheme == true) g.drawRect(675, 100, 250, 500);


                }
                if(onMontaVistaTheme == true) {
                    montaVistaThemeBox = new Rectangle(650, 75, 300, 525);
                    g.drawImage(themesPageBackground, 650, 75, 300, 525, null);
                    g.drawImage(montaVistaThemeExample, 670, 250, 255, 225, null);
                    g.setColor(Color.BLACK);
                    g.drawString("Monta Vista Theme", 690, 140);
                     buttonColor = new Color(255, 115, 84);
                    g.setColor(buttonColor);
                    g.fillRect(725, 500, 150, 50);
                    g.setColor(Color.BLACK);
                    g.drawString("Select", 765, 535);
                     buttonColor = new Color(255, 115, 84);
                    g.setColor(buttonColor);
                    if(montaVistaTheme == true) g.drawRect(650, 75, 300, 525);
                }
            }
            if(playGame == true)
            {
                if(holeOne == true)
                {
                    if(doOnce == false)
                    {
                        setValues();
                        doOnce = true;
                    }
                    ((Graphics2D)g).setStroke(new BasicStroke(5));
                    if(classicTheme == true) {
                         holeColor = new Color(0, 153, 0);
                    }
                    if(skyTheme == true)
                    {
                        holeColor = new Color(2, 208, 255);
                    }
                    if(montaVistaTheme == true)
                    {
                        holeColor = new Color(186, 0, 249);
                    }
                    g.setColor(holeColor);
                    g.fillPolygon(boundryOfOneX, boundryOfOneY, 7);
                    if(classicTheme == true) {
                        holeBorder = new Color(102, 51, 0);
                    }
                    if(skyTheme == true)
                    {
                        holeBorder = new Color(117, 117, 117);
                    }
                    if(montaVistaTheme == true)
                    {
                        holeBorder = new Color(255, 204, 51);
                    }
                    g.setColor(holeBorder);
                    g.drawLine(150, 150, 150, 650);
                    g.drawLine(150, 150, 800, 150);
                    g.drawLine(800, 150, 800, 350);
                    g.drawLine(800, 350, 400, 350);
                    g.drawLine(400, 650, 400, 350);
                    g.drawLine(150, 650, 400, 650);
                    g.setColor(Color.BLACK);
                    g.drawString("Power For Shot :", 30, 270);
                    g.drawRect(50, 300, 50, 100);
                    g.drawRect(62,290, 25, 10);
                    g.fillOval(700 ,240, 25, 25); //the hole
                    g.setColor(Color.RED);
                    g.fillRect(powerBarX, powerBarY, 49,400-powerBarY);
                    g.setColor(Color.BLACK);
                    g.drawLine(50, 325, 100, 325);
                    g.drawLine(50, 350, 100, 350);
                    g.drawLine(50, 375, 100, 375);
                    if(aimSet == true)
                    {
                        g.setColor(Color.RED);
                        g.drawLine((int)(ballX+5), (int)(ballY+10), aimX, aimY);
                    }
                    if(hitShot == true)
                    {
                        g.setColor(Color.RED);
                        g.fillRect(25, 425, 100, 50);
                        g.setColor(Color.BLACK);
                        g.drawString("HIT!",60, 455);
                    }
                    g.setColor(Color.WHITE);
                    g.fillOval((int)(ballX), (int)(ballY), 10, 10);
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Serif", Font.BOLD, 50));
                    g.drawString(""+strokeCounter, 370, 50);
                    if(strokeCounter == 1)
                    {
                        g.drawImage(alternateStrokesDisplay ,430, 10, 200, 50, null);
                    }
                    else g.drawImage(strokesDisplay ,430, 10, 200, 50, null);
                    if(nextHole == true)
                    {
                        g.drawImage(nextHoleBackground, 300, nextHoleY,400, 300, null);
                        g.setFont(new Font("Serif", Font.BOLD, 50));
                        g.drawString(""+strokeCounter, 330, 290);
                        if(totalStrokes == 1)
                        {
                            g.drawImage(alternateTotalStrokesDisplay ,380, 250, 275, 50, null);
                        }
                        else g.drawImage(totalStrokesDisplay ,380, 250, 275, 50, null);
                        g.drawImage(holesLeftFour, 375, 325, 250, 50, null);
                    }
                }
                if(holeTwo == true)
                {
                    ((Graphics2D)g).setStroke(new BasicStroke(5));
                    if(classicTheme == true) {
                        holeColor = new Color(0, 153, 0);
                    }
                    if(skyTheme == true)
                    {
                        holeColor = new Color(2, 208, 255);
                    }
                    if(montaVistaTheme == true)
                    {
                        holeColor = new Color(186, 0, 249);
                    }
                    g.setColor(holeColor);
                    g.fillRect(200, 100, 600, 500);
                    if(classicTheme == true) {
                        holeBorder = new Color(102, 51, 0);
                    }
                    if(skyTheme == true)
                    {
                        holeBorder = new Color(117, 117, 117);
                    }
                    if(montaVistaTheme == true)
                    {
                        holeBorder = new Color(255, 204, 51);
                    }
                    g.setColor(holeBorder);
                    g.drawRect(200, 100, 602, 502);
                    g.fillRect(200, 340, 200, 10);//blocker 1
                    g.fillRect(600, 340, 200, 10);// blocker 2
                    g.fillRect(400, 200, 200, 10);// blocker 3
                    g.setColor(Color.BLACK);
                    g.drawString("Power For Shot :", 30, 270);
                    g.drawRect(50, 300, 50, 100);
                    g.drawRect(62,290, 25, 10);
                    g.fillOval(488, 125, 25, 25); // the second hole
                    g.setColor(Color.RED);
                    g.fillRect(powerBarX, powerBarY, 49,400-powerBarY);
                    g.setColor(Color.BLACK);
                    g.drawLine(50, 325, 100, 325);
                    g.drawLine(50, 350, 100, 350);
                    g.drawLine(50, 375, 100, 375);
                    if(aimSet == true)
                    {
                        g.setColor(Color.RED);
                        g.drawLine((int)(ballX+5), (int)(ballY+10), aimX, aimY);
                    }
                    g.setColor(Color.WHITE);
                    g.fillOval((int)(ballX), (int)(ballY), 10, 10);
                    if(hitShot == true)
                    {
                        g.setColor(Color.RED);
                        g.fillRect(25, 425, 100, 50);
                        g.setColor(Color.BLACK);
                        g.drawString("HIT!",60, 455);
                    }
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Serif", Font.BOLD, 50));
                    g.drawString(""+strokeCounter, 370, 50);
                    if(strokeCounter == 1)
                    {
                        g.drawImage(alternateStrokesDisplay ,430, 10, 200, 50, null);
                    }
                    else g.drawImage(strokesDisplay ,430, 10, 200, 50, null);
                    if(nextHole == true)
                    {
                        g.drawImage(nextHoleBackground, 300, nextHoleY,400, 300, null);
                        g.setFont(new Font("Serif", Font.BOLD, 50));
                        g.drawString(""+totalStrokes, 330, 290);
                        if(totalStrokes == 1)
                        {
                            g.drawImage(alternateTotalStrokesDisplay ,380, 250, 275, 50, null);
                        }
                        else g.drawImage(totalStrokesDisplay ,380, 250, 275, 50, null);
                        g.drawImage(holesLeftThree, 375, 325, 250, 50, null);
                    }
                }
                if(holeThree == true)
                {
                    ((Graphics2D)g).setStroke(new BasicStroke(5));
                    if(classicTheme == true) {
                        holeColor = new Color(0, 153, 0);
                    }
                    if(skyTheme == true)
                    {
                        holeColor = new Color(2, 208, 255);
                    }
                    if(montaVistaTheme == true)
                    {
                        holeColor = new Color(186, 0, 249);
                    }
                    g.setColor(holeColor);
                    g.fillRect(300, 100, 400, 600);
                    if(classicTheme == true) {
                        holeBorder = new Color(102, 51, 0);
                    }
                    if(skyTheme == true)
                    {
                        holeBorder = new Color(117, 117, 117);
                    }
                    if(montaVistaTheme == true)
                    {
                        holeBorder = new Color(255, 204, 51);
                    }
                    g.setColor(holeBorder);
                    g.drawRect(298, 98, 402, 602);
                    g.setColor(Color.BLACK);
                    g.drawString("Power For Shot :", 30, 270);
                    g.drawRect(50, 300, 50, 100);
                    g.drawRect(62,290, 25, 10);
                    g.fillOval(490, 125, 25, 25); //the third hole
                    g.setColor(Color.RED);
                    g.fillRect(powerBarX, powerBarY, 49,400-powerBarY);
                    g.fillRect(350, 500, 300, 10); // bolcker1
                    g.fillRect(350, 300, 300, 10); // blocker2
                    g.fillRect(400, 200, 200, 10); // bolcker3
                    g.setColor(Color.BLACK);
                    g.drawLine(50, 325, 100, 325);
                    g.drawLine(50, 350, 100, 350);
                    g.drawLine(50, 375, 100, 375);
                    if(aimSet == true)
                    {
                        g.setColor(Color.RED);
                        g.drawLine((int)(ballX+5), (int)(ballY+10), aimX, aimY);
                    }
                    g.setColor(Color.WHITE);
                    g.fillOval((int)(ballX), (int)(ballY), 10, 10);
                    if(hitShot == true)
                    {
                        g.setColor(Color.RED);
                        g.fillRect(25, 425, 100, 50);
                        g.setColor(Color.BLACK);
                        g.drawString("HIT!",60, 455);
                    }
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Serif", Font.BOLD, 50));
                    g.drawString(""+strokeCounter, 370, 50);
                    if(strokeCounter == 1)
                    {
                        g.drawImage(alternateStrokesDisplay ,430, 10, 200, 50, null);
                    }
                    else g.drawImage(strokesDisplay ,430, 10, 200, 50, null);
                    if(nextHole == true)
                    {
                        g.drawImage(nextHoleBackground, 300, nextHoleY,400, 300, null);
                        g.setFont(new Font("Serif", Font.BOLD, 50));
                        g.drawString(""+totalStrokes, 330, 290);
                        if(totalStrokes == 1)
                        {
                            g.drawImage(alternateTotalStrokesDisplay ,380, 250, 275, 50, null);
                        }
                        else g.drawImage(totalStrokesDisplay ,380, 250, 275, 50, null);
                        g.drawImage(holesLeftTwo, 375, 325, 250, 50, null);
                    }
                }
                if(holeFour == true)
                {
                    ((Graphics2D)g).setStroke(new BasicStroke(5));
                    if(classicTheme == true) {
                        holeColor = new Color(0, 153, 0);
                    }
                    if(skyTheme == true)
                    {
                        holeColor = new Color(2, 208, 255);
                    }
                    if(montaVistaTheme == true)
                    {
                        holeColor = new Color(186, 0, 249);
                    }
                    g.setColor(holeColor);
                    g.fillPolygon(boundryOfOneX, boundryOfOneY, 7);
                    if(classicTheme == true) {
                        holeBorder = new Color(102, 51, 0);
                    }
                    if(skyTheme == true)
                    {
                        holeBorder = new Color(117, 117, 117);
                    }
                    if(montaVistaTheme == true)
                    {
                        holeBorder = new Color(255, 204, 51);
                    }
                    g.setColor(holeBorder);
                    g.drawLine(150, 150, 150, 650);
                    g.drawLine(150, 150, 800, 150);
                    g.drawLine(800, 150, 800, 350);
                    g.drawLine(800, 350, 400, 350);
                    g.drawLine(400, 650, 400, 350);
                    g.drawLine(150, 650, 400, 650);
                    g.fillRect(175, 500, 200 ,10);
                    g.fillRect(175, 350, 200, 10);
                    g.setColor(Color.BLACK);
                    g.drawString("Power For Shot :", 30, 270);
                    g.drawRect(50, 300, 50, 100);
                    g.drawRect(62,290, 25, 10);
                    g.fillOval(700 ,240, 25, 25); //the hole
                    g.setColor(Color.RED);
                    g.fillRect(powerBarX, powerBarY, 49,400-powerBarY);
                    g.fillRect(600, 175, 10, 150);
                    g.fillRect(400, 175, 10, 150);
                    g.setColor(Color.BLACK);
                    g.drawLine(50, 325, 100, 325);
                    g.drawLine(50, 350, 100, 350);
                    g.drawLine(50, 375, 100, 375);
                    if(aimSet == true)
                    {
                        g.setColor(Color.RED);
                        g.drawLine((int)(ballX+5), (int)(ballY+10), aimX, aimY);
                    }
                    g.setColor(Color.WHITE);
                    g.fillOval((int)(ballX), (int)(ballY), 10, 10);
                    if(hitShot == true)
                    {
                        g.setColor(Color.RED);
                        g.fillRect(25, 425, 100, 50);
                        g.setColor(Color.BLACK);
                        g.drawString("HIT!",60, 455);
                    }
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Serif", Font.BOLD, 50));
                    g.drawString(""+strokeCounter, 370, 50);
                    if(strokeCounter == 1)
                    {
                        g.drawImage(alternateStrokesDisplay ,430, 10, 200, 50, null);
                    }
                    else g.drawImage(strokesDisplay ,430, 10, 200, 50, null);
                    if(nextHole == true)
                    {
                        g.drawImage(nextHoleBackground, 300, nextHoleY,400, 300, null);
                        g.setFont(new Font("Serif", Font.BOLD, 50));
                        g.drawString(""+totalStrokes, 330, 290);
                        if(totalStrokes == 1)
                        {
                            g.drawImage(alternateTotalStrokesDisplay ,380, 250, 275, 50, null);
                        }
                        else g.drawImage(totalStrokesDisplay ,380, 250, 275, 50, null);
                        g.drawImage(holeLeftOne, 375, 325, 250, 50, null);
                    }
                }
                if(holeFive == true)
                {
                    ((Graphics2D)g).setStroke(new BasicStroke(5));
                    g.setColor(Color.BLACK);
                    ((Graphics2D)g).setStroke(new BasicStroke(5));
                    if(classicTheme == true) {
                        holeColor = new Color(0, 153, 0);
                    }
                    if(skyTheme == true)
                    {
                        holeColor = new Color(2, 208, 255);
                    }
                    if(montaVistaTheme == true)
                    {
                        holeColor = new Color(186, 0, 249);
                    }
                    g.setColor(holeColor);
                    g.fillPolygon(boundryOfFiveX, boundryOfFiveY, 9);
                    if(classicTheme == true) {
                        holeBorder = new Color(102, 51, 0);
                    }
                    if(skyTheme == true)
                    {
                        holeBorder = new Color(117, 117, 117);
                    }
                    if(montaVistaTheme == true)
                    {
                        holeBorder = new Color(255, 204, 51);
                    }
                    g.setColor(holeBorder);
                    g.drawPolygon(boundryOfFiveX, boundryOfFiveY, 9);
                    g.fillRect(175, 500, 150 ,10);
                    g.fillRect(175, 350, 150, 10);
                    g.fillRect(625, 350, 150, 10);
                    g.setColor(Color.BLACK);
                    g.drawString("Power For Shot :", 30, 270);
                    g.drawRect(50, 300, 50, 100);
                    g.drawRect(62,290, 25, 10);
                    g.fillOval(687, 587, 25, 25);//the hole pls
                    g.setColor(Color.RED);
                    g.fillRect(powerBarX, powerBarY, 49,400-powerBarY);
                    g.fillRect(590, 175, 10, 150);
                    g.fillRect(350, 175, 10, 150);
                    g.fillRect(625, 500, 150, 10);
                    g.setColor(Color.BLACK);
                    g.drawLine(50, 325, 100, 325);
                    g.drawLine(50, 350, 100, 350);
                    g.drawLine(50, 375, 100, 375);
                    if(aimSet == true)
                    {
                        g.setColor(Color.RED);
                        g.drawLine((int)(ballX+5), (int)(ballY+10), aimX, aimY);
                    }
                    g.setColor(Color.WHITE);
                    g.fillOval((int)(ballX), (int)(ballY), 10, 10);
                    if(hitShot == true)
                    {
                        g.setColor(Color.RED);
                        g.fillRect(25, 425, 100, 50);
                        g.setColor(Color.BLACK);
                        g.drawString("HIT!",60, 455);
                    }
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Serif", Font.BOLD, 50));
                    g.drawString(""+strokeCounter, 370, 50);
                    if(strokeCounter == 1)
                    {
                        g.drawImage(alternateStrokesDisplay ,430, 10, 200, 50, null);
                    }
                    else g.drawImage(strokesDisplay ,430, 10, 200, 50, null);
                    if(goodJobPanel == true) {
                        g.drawImage(nextHoleBackground, 300, nextHoleY,400, 300, null);
                        g.setFont(new Font("Serif", Font.BOLD, 50));
                        g.drawString("" + totalStrokes, 330, 360);
                        g.drawImage(totalStrokesDisplay, 380, 325, 275, 50, null);
                        g.drawImage(goodJob, 375, 250, 250, 50, null);
                    }
                }
            }
        }
        public void actionPerformed(ActionEvent e) // IF button is clicked, this helps change the panel
        {
            String command = e.getActionCommand();
            if(command.equals("Play") && instructionsRead == true)
            {playGame = true;mainPage = false;holeOne = true;setValues(); totalStrokes = 0;}
            if(command.equals("Play") && instructionsRead == false)
            {playGame = false;mainPage = true;displayWarning = true;}
            if(command.equals("Change Theme"))
            {themes = true; mainPage = false;}
            if(command.equals("How To Play"))
            {displayInstructions = true;mainPage = false;}
            if(command.equals("Credits"))
            {creditsPage = true;mainPage = false;}
            if(command.equals("< Back"))
            {displayInstructions = false;mainPage = true;displayWarning = false;creditsPage = false;playGame = false; themes = false;}
            if(command.equals("Return To Main Page"))
            {mainPage = true;playGame = false; holeFive = false; doOnce = true; goodJobPanel = false;}
            if(command.equals("Next Hole ->") && holeOne == true)
            {holeTwo = true; holeOne = false; nextHole = false; setValues();}
            else if(command.equals("Next Hole ->") && holeTwo == true)
            {holeThree = true; holeTwo = false;nextHole = false; setValues();}
            else if(command.equals("Next Hole ->") && holeThree == true)
            {holeFour = true; holeThree = false;nextHole = false; setValues();}
            else if(command.equals("Next Hole ->") && holeFour == true)
            {holeFive = true; holeFour = false;nextHole = false; setValues();}
            repaint();displayMethod();
        }
        public void mousePressed (MouseEvent e)// Checks if mouse is pressed
        {
            if(themes == true)
            {
                if(classicThemeSelectButton.contains(e.getX(), e.getY()))
                {
                    classicTheme = true;
                    skyTheme = false;
                    montaVistaTheme = false;
                }
                if(skyThemeSelectButton.contains(e.getX(), e.getY()))
                {
                    skyTheme = true;
                    classicTheme = false;
                    montaVistaTheme = false;
                }
                if(montaVistaThemeSelectButton.contains(e.getX(), e.getY()))
                {
                    montaVistaTheme = true;
                    skyTheme = false;
                    classicTheme = false;
                }
            }
            if(holeOne == true && nextHole == false){
                if(holeOneBoundry.contains(e.getX(), e.getY()))
                {
                    aimX = e.getX();
                    aimY = e.getY();
                    aimSet = true;
                }
                if(powerBar.contains(e.getX(), e.getY()))
                {
                    hitShot = true;
                    powerBarY = e.getY();
                }
                if(hitButton.contains(e.getX(), e.getY()))
                {
                    if(aimX >= ballX && aimY <= ballY+10)
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                    }
                    if(aimX <= ballX && aimY <= ballY+10)
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                        if(ballMoverX >= 0)ballMoverX = ballMoverX*-1;
                        if(ballMoverY >= 0)ballMoverY = ballMoverY*-1;
                    }
                    if(aimX<= ballX && aimY >= ballY+10 )
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                        if(ballMoverX >= 0)ballMoverX = ballMoverX*-1;
                        if(ballMoverY <= 0)ballMoverY = ballMoverY*-1;
                    }
                    if(aimX >= ballX && aimY >= ballY+10 )
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                        if(ballMoverY <= 0)ballMoverY = ballMoverY*-1;
                    }
                    i = 0;
                    BallMover ballmover = new BallMover();
                    balltimer = new Timer(30, ballmover);
                    balltimer.start();
                    strokeCounter ++;
                }
            }
            if(holeTwo == true && nextHole == false){
                if(holeTwoBoundry.contains(e.getX(), e.getY()))
                {
                    aimX = e.getX();
                    aimY = e.getY();
                    aimSet = true;
                }
                if(powerBar.contains(e.getX(), e.getY()))
                {
                    hitShot = true;
                    powerBarY = e.getY();
                }
                if(hitButton.contains(e.getX(), e.getY()))
                {
                    if(aimX >= ballX && aimY <= ballY+10)
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                    }
                    if(aimX <= ballX && aimY <= ballY+10)
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                        if(ballMoverX >= 0)ballMoverX = ballMoverX*-1;
                        if(ballMoverY >= 0)ballMoverY = ballMoverY*-1;
                    }
                    if(aimX<= ballX && aimY >= ballY+10 )
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                        if(ballMoverX >= 0)ballMoverX = ballMoverX*-1;
                        if(ballMoverY <= 0)ballMoverY = ballMoverY*-1;
                    }
                    if(aimX >= ballX && aimY >= ballY+10 )
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                        if(ballMoverY <= 0)ballMoverY = ballMoverY*-1;
                    }
                    i = 0;
                    BallMover ballmover = new BallMover();
                    balltimer = new Timer(30, ballmover);
                    balltimer.start();
                    strokeCounter ++;
                }
            }
            if(holeThree == true && nextHole == false){
                if(holeThreeBoundry.contains(e.getX(), e.getY()))
                {
                    aimX = e.getX();
                    aimY = e.getY();
                    aimSet = true;
                }
                if(powerBar.contains(e.getX(), e.getY()))
                {
                    hitShot = true;
                    powerBarY = e.getY();
                }
                if(hitButton.contains(e.getX(), e.getY()))
                {
                    if(aimX >= ballX && aimY <= ballY+10)
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                    }
                    if(aimX <= ballX && aimY <= ballY+10)
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                        if(ballMoverX >= 0)ballMoverX = ballMoverX*-1;
                        if(ballMoverY >= 0)ballMoverY = ballMoverY*-1;
                    }
                    if(aimX<= ballX && aimY >= ballY+10 )
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                        if(ballMoverX >= 0)ballMoverX = ballMoverX*-1;
                        if(ballMoverY <= 0)ballMoverY = ballMoverY*-1;
                    }
                    if(aimX >= ballX && aimY >= ballY+10 )
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                        if(ballMoverY <= 0)ballMoverY = ballMoverY*-1;
                    }
                    i = 0;
                    BallMover ballmover = new BallMover();
                    balltimer = new Timer(30, ballmover);
                    balltimer.start();
                    strokeCounter ++;
                }
            }
            if(holeFour == true && nextHole == false){
                if(holeFourBoundry.contains(e.getX(), e.getY()))
                {
                    aimX = e.getX();
                    aimY = e.getY();
                    aimSet = true;
                }
                if(powerBar.contains(e.getX(), e.getY()))
                {
                    hitShot = true;
                    powerBarY = e.getY();
                }
                if(hitButton.contains(e.getX(), e.getY()))
                {
                    if(aimX >= ballX && aimY <= ballY+10)
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                    }
                    if(aimX <= ballX && aimY <= ballY+10)
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                        if(ballMoverX >= 0)ballMoverX = ballMoverX*-1;
                        if(ballMoverY >= 0)ballMoverY = ballMoverY*-1;
                    }
                    if(aimX<= ballX && aimY >= ballY+10 )
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                        if(ballMoverX >= 0)ballMoverX = ballMoverX*-1;
                        if(ballMoverY <= 0)ballMoverY = ballMoverY*-1;
                    }
                    if(aimX >= ballX && aimY >= ballY+10 )
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                        if(ballMoverY <= 0)ballMoverY = ballMoverY*-1;
                    }
                    i = 0;
                    BallMover ballmover = new BallMover();
                    balltimer = new Timer(30, ballmover);
                    balltimer.start();
                    strokeCounter ++;
                }
            }
            if(holeFive == true && nextHole == false){
                if(holeFiveBoundry.contains(e.getX(), e.getY()))
                {
                    aimX = e.getX();
                    aimY = e.getY();
                    aimSet = true;
                }
                if(powerBar.contains(e.getX(), e.getY()))
                {
                    hitShot = true;
                    powerBarY = e.getY();
                }
                if(hitButton.contains(e.getX(), e.getY()))
                {
                    if(aimX >= ballX && aimY <= ballY+10)
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                    }
                    if(aimX <= ballX && aimY <= ballY+10)
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                        if(ballMoverX >= 0)ballMoverX = ballMoverX*-1;
                        if(ballMoverY >= 0)ballMoverY = ballMoverY*-1;
                    }
                    if(aimX<= ballX && aimY >= ballY+10 )
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                        if(ballMoverX >= 0)ballMoverX = ballMoverX*-1;
                        if(ballMoverY <= 0)ballMoverY = ballMoverY*-1;
                    }
                    if(aimX >= ballX && aimY >= ballY+10 )
                    {
                        angle = Math.atan((ballY+10 - aimY)/(ballX - aimX));
                        ballVelocity = 5;
                        ballMoverX = ballVelocity*(Math.cos(angle));
                        ballMoverY = ballVelocity*(Math.sin(angle));
                        if(ballMoverY <= 0)ballMoverY = ballMoverY*-1;
                    }
                    i = 0;
                    BallMover ballmover = new BallMover();
                    balltimer = new Timer(30, ballmover);
                    balltimer.start();
                    strokeCounter ++;
                }
            }
            repaint();
            displayMethod();
        }
        class BallMover implements ActionListener // timer class, helps in moving the ball
        {
            public void actionPerformed(ActionEvent e)// timer, it makes the ball move
            {
                if (holeOne == true) {
                    ballX += ballMoverX;
                    ballY += ballMoverY;
                    i++;
                    if ((400 - powerBarY) * 2 == i)
                    {
                        balltimer.stop();
                    }
                    if (holeUno.contains(ballX, ballY) || holeUno.contains(ballX + 5, ballY + 5) || holeUno.contains(ballX, ballY + 5) || holeUno.contains(ballX + 5, ballY)) {
                        ballX = 708;
                        ballY = 248;
                        totalStrokes += strokeCounter;
                        nextHole = true;
                        balltimer.stop();
                    }
                    hitShot = false;
                    aimSet = false;
                    if (ballX <= 145) {
                        ballMoverX = ballMoverX * -1;
                    }
                    if (ballX >= 795 && ballY < 350)
                    {
                        ballMoverX = ballMoverX * -1;
                    }
                    if (ballX >= 395 && ballY >= 350)
                    {
                        ballMoverX = ballMoverX * -1;
                    }
                    if (ballY <= 145) {
                        ballMoverY = ballMoverY * -1;
                    }
                    if (ballY >= 645) {
                        ballMoverY = ballMoverY * -1;
                    }
                    if (ballY >= 345 && ballX >= 400)
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    repaint();
                    displayMethod();
                }
                if (holeTwo == true) {
                    hitShot = false;
                    aimSet = false;
                    ballX += ballMoverX;
                    ballY += ballMoverY;
                    i++;
                    if ((400 - powerBarY)*2 == i)
                    {
                        balltimer.stop();
                    }
                    if (holeDos.contains(ballX, ballY) || holeDos.contains(ballX + 5, ballY + 5) || holeDos.contains(ballX, ballY + 5) || holeDos.contains(ballX + 5, ballY)) {
                        ballX = 495;
                        ballY = 132;
                        totalStrokes += strokeCounter;
                        nextHole = true;
                        balltimer.stop();
                    }
                    if(holeTwoBlocker1.contains(ballX, ballY) || holeTwoBlocker1.contains(ballX+10, ballY+10))
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    if(ballX == 400 && holeTwoBlocker1.contains(ballX-5,ballY))
                    {
                        ballMoverX = ballMoverX * -1;
                    }
                    if( holeTwoBlocker2.contains(ballX, ballY) || holeTwoBlocker2.contains(ballX+10, ballY+10))
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    if( holeTwoBlocker3.contains(ballX, ballY) || holeTwoBlocker3.contains(ballX+10, ballY+10))
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    if (ballY >= 595)
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    if (ballY <= 105)
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    if (ballX <= 205)
                    {
                        ballMoverX = ballMoverX * -1;
                    }
                    if (ballX >= 795)
                    {
                        ballMoverX = ballMoverX * -1;
                    }
                    repaint();
                    displayMethod();
                }
                if(holeThree == true)
                {
                    hitShot = false;
                    aimSet = false;
                    ballX += ballMoverX;
                    ballY += ballMoverY;
                    i++;
                    if ((400 - powerBarY) * 2 == i)
                    {
                        balltimer.stop();
                    }
                    if (holeTres.contains(ballX, ballY) || holeTres.contains(ballX + 5, ballY + 5) || holeTres.contains(ballX, ballY + 5) || holeTres.contains(ballX + 5, ballY)) {
                        ballX = 497;
                        ballY = 132;
                        totalStrokes += strokeCounter;
                        nextHole = true;
                        balltimer.stop();
                    }
                    if(holeThreeReseter1.contains(ballX, ballY) || holeThreeReseter1.contains(ballX+8, ballY+8) || holeThreeReseter1.contains(ballX+8, ballY) || holeThreeReseter1.contains(ballX, ballY+8)|| holeThreeReseter2.contains(ballX, ballY) || holeThreeReseter2.contains(ballX+8, ballY+8) || holeThreeReseter2.contains(ballX+8, ballY) || holeThreeReseter2.contains(ballX+8, ballY) || holeThreeReseter3.contains(ballX, ballY) || holeThreeReseter3.contains(ballX+8, ballY+8) || holeThreeReseter3.contains(ballX, ballY+8) || holeThreeReseter3.contains(ballX+8, ballY))
                    {
                        ballX = 490;
                        ballY = 645;
                        balltimer.stop();
                    }
                    if(ballX <= 305)
                    {
                        ballMoverX = ballMoverX * -1;
                    }
                    if(ballX >= 695)
                    {
                        ballMoverX = ballMoverX * -1;
                    }
                    if(ballY <= 105)
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    if(ballY >= 695)
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    repaint();
                    displayMethod();
                }
                if (holeFour == true) {
                    ballX += ballMoverX;
                    ballY += ballMoverY;
                    i++;
                    hitShot = false;
                    aimSet = false;
                    if ((400 - powerBarY) * 2 == i)
                    {
                        balltimer.stop();
                    }
                    if (holeCuatro.contains(ballX, ballY) || holeCuatro.contains(ballX + 5, ballY + 5) || holeCuatro.contains(ballX, ballY + 5) || holeCuatro.contains(ballX + 5, ballY)) {
                        ballX = 708;
                        ballY = 248;
                        totalStrokes += strokeCounter;
                        nextHole = true;
                        balltimer.stop();
                    }
                    if(holeFourReseter1.contains(ballX, ballY) || holeFourReseter1.contains(ballX+9, ballY+9) || holeFourReseter1.contains(ballX+9, ballY) || holeFourReseter1.contains(ballX, ballY+9) || holeFourReseter2.contains(ballX, ballY) || holeFourReseter2.contains(ballX+9, ballY+9) || holeFourReseter2.contains(ballX+9, ballY) || holeFourReseter2.contains(ballX, ballY+9))
                    {
                        ballX = 270;
                        ballY = 600;
                        balltimer.stop();
                    }
                    if(holeFourBlocker1.contains(ballX, ballY)||holeFourBlocker1.contains(ballX+10, ballY+10) || holeFourBlocker2.contains(ballX, ballY)|| holeFourBlocker2.contains(ballX+10, ballY+10))
                    {
                        ballMoverY = ballMoverY* -1;
                    }
                    if (ballX <= 145) {
                        ballMoverX = ballMoverX * -1;
                    }
                    if (ballX >= 795 && ballY < 350)
                    {
                        ballMoverX = ballMoverX * -1;
                    }
                    if (ballX >= 395 && ballY >= 350)
                    {
                        ballMoverX = ballMoverX * -1;
                    }
                    if (ballY <= 145)
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    if (ballY >= 645)
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    if (ballY >= 345 && ballX >= 400)
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    repaint();
                    displayMethod();
                }
                if(holeFive == true)
                {
                    ballX += ballMoverX;
                    ballY += ballMoverY;
                    i++;
                    hitShot = false;
                    aimSet = false;
                    if ((400 - powerBarY) * 2 == i)
                    {
                        balltimer.stop();
                    }
                    if(holeCinco.contains(ballX, ballY) || holeCinco.contains(ballX + 5, ballY + 5) || holeCinco.contains(ballX, ballY + 5) || holeCinco.contains(ballX + 5, ballY))
                    {
                        ballX = 695;
                        ballY = 595;
                        totalStrokes += strokeCounter;
                        goodJobPanel = true;
                        balltimer.stop();
                    }
                    if(holeFiveReseter1.contains(ballX, ballY) || holeFiveReseter1.contains(ballX+8, ballY+8) || holeFiveReseter1.contains(ballX+8, ballY) || holeFiveReseter1.contains(ballX, ballY+8)|| holeFiveReseter2.contains(ballX, ballY) || holeFiveReseter2.contains(ballX+8, ballY+8) || holeFiveReseter2.contains(ballX+8, ballY) || holeFiveReseter2.contains(ballX+8, ballY) || holeFiveReseter3.contains(ballX, ballY) || holeFiveReseter3.contains(ballX+8, ballY+8) || holeFiveReseter3.contains(ballX, ballY+8) || holeFiveReseter3.contains(ballX+8, ballY))
                    {
                        ballX = 240;
                        ballY = 600;
                        balltimer.stop();
                    }
                    if(holeFiveBlocker1.contains(ballX, ballY) || holeFiveBlocker1.contains(ballX+10, ballY+10))
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    if(holeFiveBlocker2.contains(ballX, ballY) || holeFiveBlocker2.contains(ballX+10, ballY+10))
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    if(holeFiveBlocker3.contains(ballX, ballY) || holeFiveBlocker3.contains(ballX+10, ballY+10))
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    if (ballX <= 145)
                    {
                        ballMoverX = ballMoverX * -1;
                    }
                    if (ballX >= 795)
                    {
                        ballMoverX = ballMoverX * -1;
                    }
                    if (ballX >= 345 && ballX <= 350 && ballY >= 350 && ballY <= 650)
                    {
                        ballMoverX = ballMoverX * -1;
                    }
                    if (ballX <= 605 && ballX >= 600 && ballY >= 350 && ballY <= 650)
                    {
                        ballMoverX = ballMoverX * -1;
                    }
                    if (ballY <= 145)
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    if (ballY >= 645)
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    if (ballY >= 345 && (ballX >= 350 && ballX <= 600))
                    {
                        ballMoverY = ballMoverY * -1;
                    }
                    repaint();
                    displayMethod();
                }
            }
        }
        public void mouseReleased (MouseEvent e) // Checks if mouse is released
        {
        }

        public void mouseClicked (MouseEvent e) {}  //Checks if mouse is clicked

        public void mouseEntered (MouseEvent e) {} //Checks if mouse has entered the frame

        public void mouseExited (MouseEvent e) {} //checks if mouse is exited the frame

        public void mouseDragged (MouseEvent e)//Checks if mouse is dragged
        {}
        public void mouseMoved (MouseEvent e)//Checks if mouse is moved for the themes panel
        {
            if(themes) {
                if (classicThemeBox.contains(e.getX(), e.getY()))
                {
                    onClassicTheme = true;
                }
                if (!classicThemeBox.contains(e.getX(), e.getY()))
                {
                    onClassicTheme = false;
                }
                if (skyThemeBox.contains(e.getX(), e.getY()))
                {
                    onSkyTheme = true;
                }
                if (!skyThemeBox.contains(e.getX(), e.getY()))
                {
                    onSkyTheme = false;
                }
                if (montaVistaThemeBox.contains(e.getX(), e.getY()))
                {
                    onMontaVistaTheme = true;
                }
                if (!montaVistaThemeBox.contains(e.getX(), e.getY()))
                {
                    onMontaVistaTheme = false;
                }
            }
        }
    }
}