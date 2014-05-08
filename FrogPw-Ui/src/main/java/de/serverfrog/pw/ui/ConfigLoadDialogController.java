/*
 * Copyright (C) 2014 Serverfrog.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.serverfrog.pw.ui;

import com.sun.javafx.collections.ObservableListWrapper;
import de.serverfrog.pw.Configuration;
import de.serverfrog.pw.ConfigurationUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;

/**
 * FXML Controller class
 *
 * @author Bastian Venz
 */
@Getter
@Setter
public class ConfigLoadDialogController implements Initializable {

    private class ConfigListCell extends ListCell<Configuration> {

        @Override
        public void updateItem(Configuration config, boolean empty) {
            super.updateItem(config, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setText(config.getWebsite().getAddress()
                        + " | Type: " + config.getWebsite().getType().getName());
                setGraphic(null);
            }
        }
    }

    @FXML
    private ListView<Configuration> websiteList;

    private Stage dialog;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void prepareList(String password) throws IOException {
        if (!ConfigurationUtil.exist()) {
            return;
        }

        websiteList.setCellFactory(new Callback<ListView<Configuration>, ListCell<Configuration>>() {

            @Override
            public ListCell<Configuration> call(ListView<Configuration> param) {
                return new ConfigListCell();
            }
        });
        websiteList.setItems(new ObservableListWrapper<>(ConfigurationUtil.readConfig(password.getBytes("UTF-8"))));

    }

    public Configuration getConfig() {
        return websiteList.getSelectionModel().getSelectedItem();
    }

    public void onLoadButton(ActionEvent actionEvent) {
        dialog.close();

    }

    public void setStage(Stage dialog) {
        this.dialog = dialog;
    }
}
