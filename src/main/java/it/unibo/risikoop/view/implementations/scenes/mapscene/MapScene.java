package it.unibo.risikoop.view.implementations.scenes.mapscene;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import it.unibo.risikoop.controller.interfaces.Controller;

public class MapScene extends JPanel {
	private static final long serialVersionUID = 1L;
	private final Controller controller;
	private final JPanel currentPlayerPanel;
	private final JPanel mapPanel;
	private final JPanel cardPanel;
	private final JPanel actionPanel;

	public MapScene(Controller controller) {
		this.controller = controller;

		this.currentPlayerPanel = new CurrentPlayerJPanel();
		this.mapPanel = new MapJPanel();
		this.cardPanel = new CardJpanel();
		this.actionPanel = new ActionJPanel();

		setLayout(new GridBagLayout());
		setGridBagConstraints();
	}

	private void setGridBagConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;

		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.BLACK);
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(Color.DARK_GRAY);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.2;
		gbc.weighty = 1;
		add(leftPanel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.8;
		gbc.weighty = 1;
		add(rightPanel, gbc);

		populateLeftPanel(leftPanel);
		populateRightPanel(rightPanel);
	}

	private void populateLeftPanel(JPanel leftPanel) {
		leftPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;

		// currentPlayerPanel
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0.2;
		leftPanel.add(this.currentPlayerPanel, gbc);

		// cardPanel
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.8;
		leftPanel.add(this.cardPanel, gbc);
	}

	private void populateRightPanel(JPanel rightPanel) {
		rightPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;

		// mapPanel
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0.8;
		rightPanel.add(this.mapPanel, gbc);

		// actionPanel
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.2;
		rightPanel.add(this.actionPanel, gbc);
	}

}
