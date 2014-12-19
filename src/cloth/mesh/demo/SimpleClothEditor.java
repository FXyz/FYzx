/*
 * Copyright (C) 2014 F(Y)zx :
 * Authored by : Jason Pollastrini aka jdub1581, 
 * All rights reserved.
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
package cloth.mesh.demo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class SimpleClothEditor extends VBox implements Initializable {
    @FXML
    private VBox divsXSlider;
    @FXML
    private VBox divsYSlider;
    @FXML
    private VBox widthSlider;
    @FXML
    private VBox heightSlider;    
    @FXML
    private Slider dxs;
    @FXML
    private Slider dys;
    @FXML
    private Slider ws;
    @FXML
    private Slider hs;
    @FXML
    private VBox forceControls;
    @FXML
    private VBox gravityControls;
    @FXML
    private TextField gravX;
    @FXML
    private TextField gravY;
    @FXML
    private TextField gravZ;
    @FXML
    private CheckBox gravityOn;
    @FXML
    private VBox mouseControls;
    @FXML
    private TextField mX;
    @FXML
    private TextField mY;
    @FXML
    private TextField mZ;
    @FXML
    private CheckBox mouseOn;

    public SimpleClothEditor() {
        
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("ClothEditor.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(SimpleClothEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
