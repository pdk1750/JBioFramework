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
/**
 * This class constructs a window to display the protein list and
 * allows the manipulation of that list to alter the protein sequence run on
 * the GelCanvas.
 *
 * @author Amanda Fisher
 */

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.util.Vector;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SingleProteinListFrame extends JFrame {

    Electro2D electro2D;
    JList sequenceOneList;
    Vector sequenceTitlesOne;
    Vector<Integer> positionsOne;
    Vector copySequenceOne;

    /**
     * Constructor for the window; sets up the instance variables and builds
     * the GUI.
     *
     * @param param     The title of the window.
     * @param electro2D Reference to the electro2D where the protein lists are.
     */
    public SingleProteinListFrame(String param, Electro2D electro2D) {
        super(param);
        this.electro2D = electro2D;
        setLayout(new GridBagLayout());
        JLabel sequenceOneLabel = new JLabel("Sequence");
        sequenceOneList = new JList();
        sequenceTitlesOne = new Vector();
        copySequenceOne = (Vector)sequenceTitlesOne.clone();
        positionsOne = new Vector<Integer>();
        JScrollPane sequenceOneScroll = new JScrollPane(sequenceOneList);
        JButton selectedButton = new JButton("Remove Selected Proteins");

        GridBagConstraints c = new GridBagConstraints();
        c.insets = (new Insets(5, 5, 5, 5));
        c.gridx = 0;
        c.gridy = 0;
        add(sequenceOneLabel, c);

        c.gridy = 2;
        add(selectedButton, c);

        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        add(sequenceOneScroll, c);

        selectedButton.addActionListener(new SelectedListener());

        pack();
        setVisible(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    /**
     * This method is called by electro2D's refreshProteinList and
     * refreshProteinList2 method when a new protein sequence is loaded.
     * updateSequences copies the protein sequence titles into its own lists
     * for display.
     */
    public void updateSequences(Vector pL1, Vector pL2) {
        sequenceOneList.setListData(pL1);
        sequenceTitlesOne = new Vector(pL1);
        copySequenceOne = (Vector)sequenceTitlesOne.clone();
        positionsOne = new Vector<Integer>();
    }

    /**
     * This method is called to collect the positions of the elements still within
     * the manipulated sequence into vectors for use with synchronizing Electro2D
     * with the user's manipulations.
     */
    public void updatePositions() {
        positionsOne = new Vector<Integer>();
        for (int x = 0; x < copySequenceOne.size(); x++) {
            if (sequenceTitlesOne.contains(copySequenceOne.get(x))) {
                positionsOne.add(x);
            }
        }
        if (positionsOne.size() == 0) {
            positionsOne.add(-1);
        }

        if (positionsOne.size() > 0) {
            electro2D.setSequencesReady(true);
        }

        if (positionsOne.get(0) < 0) {
            electro2D.setSequencesReady(false);
        }
    }

    /**
     * Listener for the remove selected proteins button.
     * Removes all proteins that are selected.
     */
    private class SelectedListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int[] oneIndexes = sequenceOneList.getSelectedIndices();
            Vector oneProteins = new Vector();
            for(int x = 0; x < oneIndexes.length; x++) {
                oneProteins.add(sequenceTitlesOne.get(oneIndexes[x]));
            }
            sequenceTitlesOne.removeAll(oneProteins);
            // The following line ensures that multiple edits can be made,
            // even before and after a gel run.
            copySequenceOne = (Vector)sequenceTitlesOne.clone();
            
            sequenceOneList.setListData(sequenceTitlesOne);
            sequenceOneList.validate();
            updatePositions();
        }
    }

    /**
     * The following two methods are accessors for the position vectors.
     * Electro2D will call them in its get methods for the vectors used in
     * gel filtration in order to carry over the manipulations done by the user.
     *
     * @return Vector<Integer> the positions of each element still in the sequence.
     */
    public Vector<Integer> getPositionsOne() {
        return positionsOne;
    }

    // This method should currently be unused, as the compare proteins functionality
    // has been temporarily disabled.
    public Vector<Integer> getPositionsTwo() {
        Vector<Integer> positionsTwo = new Vector<Integer>();
        return positionsTwo;
    }

}

