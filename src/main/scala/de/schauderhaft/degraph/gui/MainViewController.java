package de.schauderhaft.degraph.gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import de.schauderhaft.degraph.model.Node;

public class MainViewController {
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private AnchorPane contentView;

	@FXML
	private AnchorPane mainView;

	@FXML
	private Label nodeNameLabel;

	@FXML
	private AnchorPane nodeView;

	@FXML
	void onMouseClicked(MouseEvent event) {
		System.out.println("KLickMainView");
	}

	@FXML
	void initialize() {
		assert contentView != null : "fx:id=\"contentView\" was not injected: check your FXML file 'MainView.fxml'.";
		assert mainView != null : "fx:id=\"mainView\" was not injected: check your FXML file 'MainView.fxml'.";
		assert nodeNameLabel != null : "fx:id=\"nodeNameLabel\" was not injected: check your FXML file 'MainView.fxml'.";
		assert nodeView != null : "fx:id=\"nodeView\" was not injected: check your FXML file 'MainView.fxml'.";

		Set<Node> topNodes = DataProvider.getTopNodes();
		assert topNodes != null : "no data";

		Label nodeName = DataProvider.getNodeName(topNodes);
		nodeNameLabel.setText(nodeName.getText());

	}
}
