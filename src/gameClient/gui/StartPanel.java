package gameClient.gui;

import gameClient.Id_Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * this class is a gui login panel which accept from the user
 */
public class StartPanel extends JPanel implements ActionListener {

    private JFrame frame;
    private JButton startButton;
    private JLabel idLabel, levelLabel, errorLabel;
    private JTextField idTextField, levelTextField;
    private Id_Level id_level;

    /**
     * Constructor.
     * organize the graphical interface, inorder to be able to open  the screen start panel,
     * which except login information for the game from the user.
     * @param id_lev object to put the information from the user in.
     */


    public StartPanel(Id_Level id_lev) {
        super(null);
        //initialize the object that will contain the id and level numbers
        id_level = id_lev;
        // id label
        idLabel = new JLabel();
        idLabel.setText("login id :");
        idLabel.setBounds(10, 20, 80, 25);
        idTextField = new JTextField();
        idTextField.setBounds(100, 20, 165, 25);
        // level label
        levelLabel = new JLabel();
        levelLabel.setBounds(10, 50, 80, 25);
        levelLabel.setText("level [0-23] :");
        levelTextField = new JTextField();
        levelTextField.setBounds(100, 50, 165, 25);

        // start button
        startButton = new JButton("start");
        startButton.setBounds(10, 80, 80, 25);
        startButton.addActionListener(this);

        //error label
        errorLabel = new JLabel("");//create empty lable. to be used if error will occor
        errorLabel.setBounds(10, 110, 300, 25);
        this.add(errorLabel);


        this.add(idLabel);
        this.add(idTextField);
        this.add(levelLabel);
        this.add(levelTextField);
        this.add(startButton);


    }

    /**
     * create the frame, add the panel and show the start screen
     */


    public void startScreen() {
        frame = new JFrame();
        frame.setTitle("start page");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setVisible(true);


    }

    /**
     * when press the button, take the id and level and if they are int close the screen and keep the information
     * if not- show error to the screen and wait.
     * @param e
     */


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.startButton) {

            String idStr = idTextField.getText();
            String levelStr = levelTextField.getText();


            if (!id_level.setID_Level(idStr, levelStr)) {
                //clear text fields
                idTextField.setText("");
                levelTextField.setText("");
                //show error message
                errorLabel.setText("wrong value, \n please enter again");
                errorLabel.setForeground(Color.RED);
                return;
            }

            System.out.println(idStr + " level:" + levelStr);
            frame.setVisible(false);
            this.setVisible(false);


        }
    }
}

