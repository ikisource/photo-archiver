package controller;

import javafx.scene.control.TableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;
import model.Photo;
import org.apache.log4j.Logger;

/**
 *
 * @author olivier
 */
public class CheckBoxCellFactory implements Callback {
    
    private static final Logger logger = Logger.getLogger(CheckBoxCellFactory.class);

    @Override
    public TableCell call(Object param) {
        CheckBoxTableCell<Photo, Boolean> checkBoxCell = new CheckBoxTableCell();
        logger.debug("call");
        return checkBoxCell;
    }

}
