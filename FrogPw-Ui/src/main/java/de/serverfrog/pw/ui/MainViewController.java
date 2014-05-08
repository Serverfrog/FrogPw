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

import com.sun.javafx.collections.ImmutableObservableList;
import de.serverfrog.pw.Configuration;
import de.serverfrog.pw.ConfigurationUtil;
import de.serverfrog.pw.Website;
import de.serverfrog.pw.WebsiteType;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * FXML Controller class
 *
 * @author Bastian Venz
 */
@Getter
@Setter
public class MainViewController implements Initializable {

    @FXML
    private PasswordField masterPasswordField;

    @FXML
    private TextField websiteNameField;

    @FXML
    private ComboBox<WebsiteType> websiteTypeComboBox;

    @FXML
    private TextField passwordLengthField;

    @FXML
    private CheckBox small;

    @FXML
    private Slider smallSlider;

    @FXML
    private CheckBox big;

    @FXML
    private Slider bigSlider;

    @FXML
    private CheckBox numbers;

    @FXML
    private Slider numbersSlider;

    @FXML
    private CheckBox special;

    @FXML
    private TextField specialField;

    @FXML
    private Slider specialSlider;

    @FXML
    private TextField generatedField;

    @FXML
    private CheckBox saveWebsite;

    @FXML
    private Label messageField;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        websiteTypeComboBox.setItems(new ImmutableObservableList<>(WebsiteType.values()));
        websiteTypeComboBox.setValue(WebsiteType.values()[0]);
        websiteTypeComboBox.setConverter(new StringConverter<WebsiteType>() {

            @Override
            public String toString(WebsiteType object) {
                return object.getName();
            }

            @Override
            public WebsiteType fromString(String string) {
                return WebsiteType.getByName(string);
            }
        });
    }

    @FXML
    public void onLoadButton(ActionEvent ae) {
        try {
            if (this.masterPasswordField.getText().isEmpty()) {
                return;
            }
            Stage dialog = new Stage(StageStyle.UTILITY);
            dialog.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("./ConfigLoadDialog.fxml"));
            Parent root = fXMLLoader.<Parent>load();
            ConfigLoadDialogController controller
                    = fXMLLoader.<ConfigLoadDialogController>getController();
            try {
                controller.prepareList(masterPasswordField.getText());
            } catch (IOException exception) {
                this.messageField.setText(exception.getMessage());
                return;
            }
            this.messageField.setText("");
            Scene scene = new Scene(root);
            dialog.setScene(scene);
            controller.setStage(dialog);
            dialog.showAndWait();
            setConfiguration(controller.getConfig());
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void onGenerateButton(ActionEvent ae) {
        long nanoTime = System.nanoTime();

        if (saveWebsite.isSelected()) {
            try {
                ConfigurationUtil.addConfiguration(buildConfiguration(), masterPasswordField.getText().getBytes("UTF-8"));
            } catch (IOException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.generatedField.setText("Generated in " + (System.nanoTime() - nanoTime));
    }

    public Configuration buildConfiguration() {
        Website website = new Website(this.websiteTypeComboBox.getValue(),
                this.websiteNameField.getText());
        return new Configuration(website,
                Integer.valueOf(this.passwordLengthField.getText()),
                this.small.isSelected(), this.big.isSelected(),
                this.numbers.isSelected(), this.special.isSelected(),
                Double.valueOf(this.smallSlider.getValue()).intValue(),
                Double.valueOf(this.bigSlider.getValue()).intValue(),
                Double.valueOf(this.numbersSlider.getValue()).intValue(),
                Double.valueOf(this.specialSlider.getValue()).intValue(),
                this.specialField.getText());
    }

    public void setConfiguration(Configuration c) {
        if (c == null) {
            return;
        }
        this.small.setSelected(c.isShouldSmall());
        this.big.setSelected(c.isShouldBig());
        this.numbers.setSelected(c.isShouldNumbers());
        this.special.setSelected(c.isShouldSpecial());

        this.smallSlider.setValue(c.getSmallWeigth());
        this.bigSlider.setValue(c.getBigWeigth());
        this.numbersSlider.setValue(c.getNumbersWeigth());
        this.specialSlider.setValue(c.getSpecialWeigth());

        this.specialField.setText(c.getSpecialChars());

        this.passwordLengthField.setText(Integer.toString(c.getLenght()));

        this.websiteNameField.setText(c.getWebsite().getAddress());
        this.websiteTypeComboBox.setValue(c.getWebsite().getType());

    }

    /**
     *
     * @param event event
     */
    @FXML
    public void onPasswordLengthKeyTyped(KeyEvent event) {
        if (!StringUtils.isNumeric(event.getCharacter())) {
            event.consume();
        }

    }
}
