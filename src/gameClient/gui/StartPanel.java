package gameClient.gui;

import gameClient.Id_Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPanel extends JPanel implements ActionListener {

    private JFrame frame;
    private JButton startButton;
    private JLabel idLabel, levelLabel, errorLabel;
    private JTextField idTextField, levelTextField;
    private Id_Level id_level;


    public StartPanel(Id_Level lock) {
        super(null);
        //initialize the object that will contain the id and level numbers
        id_level = lock;
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


    public void startScreen() {
        frame = new JFrame();
        frame.setTitle("start page");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setVisible(true);


    }


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

