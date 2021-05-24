package controller;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import model.Photo;

public class PhotoDateCellFactory extends TableCell<Photo, String> {

	private TextField textField;

	public PhotoDateCellFactory() {
		super();
	}

	@Override
	public void startEdit() {
		super.startEdit();

		if (textField == null) {
			createDate();
		}
		setGraphic(textField);
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		textField.setText(getItem());
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();

		setContentDisplay(ContentDisplay.TEXT_ONLY);
	}

	@Override
	public void commitEdit(String value) {
		super.commitEdit(value);

		setContentDisplay(ContentDisplay.TEXT_ONLY);
	}

	@Override
	public void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);

		if (getTableRow().getItem() != null && !empty) {
			if (getTableRow().getItem().isValid()) {
				getTableRow().setStyle("-fx-background-color: gray;");
			}
			else {
				getTableRow().setStyle("-fx-background-color: red;");
			}
		}
		if (empty) {
			setGraphic(null);
			setText(null);
			setStyle(null);
			getTableRow().setStyle(null);
		} else if (item == null || item.isEmpty()) {
			setText(null);
			setStyle("-fx-background-color: red;");
		} else {
			if (item != null) {
				setContentDisplay(ContentDisplay.TEXT_ONLY);
				setText(item);
				setStyle("-fx-background-color: lightgreen;");
				if (getTableRow().getItem() != null) {
					getTableRow().getItem().formatName();
				}
			}
		}
	}

	private void createDate() {

		textField = new TextField();
		textField.setOnKeyPressed(t -> {
			if (t.getCode() == KeyCode.ENTER) {
				commitEdit(textField.getText());
				t.consume();
			}
		});
	}

}
