package de.adihubba.swing;


import javax.swing.*;


public class JBasicFormattedLabel extends JLabel {

    public JBasicFormattedLabel() {
        setHorizontalAlignment(SwingConstants.LEFT);
    }

    public JBasicFormattedLabel(String text) {
        this();
        setText(text);
    }

}
