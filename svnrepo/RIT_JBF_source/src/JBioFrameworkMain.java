/*
 * Copyright (C) 2013 Rochester Institute of Technology
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU General Public License for more details.
 */

/*
 * The main frame for the JBioFramework program
 * Adds Electro2D, Electrophoresis, and Ionex applications to itself
 * with a JTabbedPane.
 */

/**
 *
 * @author Amanda Fisher
 */

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.Toolkit;
import one.d.electrophoresis.Electrophoresis;

public class JBioFrameworkMain extends JFrame {

    public static final long serialVersionUID = 1L;
    private static JTabbedPane tabbedPane;

    private Welcome welcome;
    private Electrophoresis oneDE;
    private Electro2D electro2D;
    private MassSpecMain spectrometer;
    private MarvinTab marvin;
    /*private [NameOfClass] [user-created reference]*/

    public static void main(String[] args) {
        JBioFrameworkMain jbfMain = new JBioFrameworkMain();
    }

    public JBioFrameworkMain() {
        super("JBioFramework");

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        welcome = new Welcome();
        marvin = new MarvinTab();
        electro2D = new Electro2D();
        spectrometer = new MassSpecMain();
        oneDE = new Electrophoresis();

        /*[user-created reference] = new [name of class]()*/

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Welcome", welcome);
        tabbedPane.add("Electro1D", oneDE);
        tabbedPane.addTab("Electro2D", electro2D);
        tabbedPane.addTab("Mass Spectrometer", spectrometer);
        tabbedPane.addTab("Marvin Sketch", marvin.createMainPanel());
        /*tabbedPane.addTab(["user-created name (to be displayed)], [user-created reference]);*/

        add(tabbedPane);

        /**
         * Use a toolit to find the screen size of the user's monitor and set
         * the window size to it.
         */

        setSize(Toolkit.getDefaultToolkit().getScreenSize());

        this.pack();
    }

    public static JTabbedPane getTabs() {
        return tabbedPane;
    }
}
