/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cloth.editor;

import cloth.particle.demo.Particle;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.geometry.Side;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * FXML Controller class
 *
 * @author Dub-Laptop
 */
public class ClothEditor extends AnchorPane implements Initializable {
    @FXML
    private Label widthLabel;
    @FXML
    private Label heightLabel;
    @FXML
    private Label spacingLabel;
    
    

    public ClothEditor() {
        
        try {
            FXMLLoader loader = new FXMLLoader(ClothEditor.this.getClass().getResource("ClothEditor.fxml"));
            loader.setRoot(ClothEditor.this);
            loader.setController(ClothEditor.this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ClothEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final PhongMaterial vertMaterial = new PhongMaterial(Color.CHARTREUSE);
    private final PhongMaterial vertPinnedMaterial = new PhongMaterial(Color.RED);
    private PointLight pointLight, headLight;
    private AmbientLight ambientLight;
    private Group root3D, axisGroup, cloth;
    
    private static final Rotate cameraXRotate = new Rotate(-20,0,0,0,Rotate.X_AXIS);
    private static final Rotate cameraYRotate = new Rotate(-20,0,0,0,Rotate.Y_AXIS);
    private static final Rotate cameraLookXRotate = new Rotate(0,0,0,0,Rotate.X_AXIS);
    private static final Rotate cameraLookZRotate = new Rotate(0,0,0,0,Rotate.Z_AXIS);
    private static final Translate cameraPosition = new Translate(0,0,-7);
    private double dragStartX, dragStartY, dragStartRotateX, dragStartRotateY;
    private Box xAxis,yAxis,zAxis;
    
    private Rotate rx = new Rotate();
    {rx.setAxis(Rotate.X_AXIS);}
    private Rotate ry = new Rotate();
    {ry.setAxis(Rotate.Y_AXIS);}
    private Rotate rz = new Rotate();
    {rz.setAxis(Rotate.Z_AXIS);}
    
    @FXML
    private Slider widthSlider;
    @FXML
    private Slider heightSlider;
    @FXML
    private Slider spacingSlider;
    @FXML
    private ColorPicker vertColorPicker;
    @FXML
    private ColorPicker pinColorPicker;
    @FXML
    private AnchorPane frame;
    @FXML
    private Button refreshClothButton;
    @FXML
    private VBox vlothAttributes;
    @FXML
    private VBox lighting;
    @FXML
    private VBox forceControls;
    @FXML
    private Slider gravXSlider;
    @FXML
    private Slider gravYSlider;
    @FXML
    private Slider gravZSlider;
    @FXML
    private Button resetForces;
    @FXML
    private ToggleButton animateForces;
    @FXML
    private Label frameRateLabel;
    @FXML
    private Label currentTimeLabel;
    @FXML
    private Label elapsedTimeLabel;
    @FXML
    private Label deltaTimeLabel;
    @FXML
    private VBox width;
    @FXML
    private VBox spacing;
    @FXML
    private Label numPhysObjects;
    @FXML
    private Label numConstraints;
    @FXML
    private ProgressBar efficiencyMeter;
    @FXML
    private StackPane logo;
    @FXML
    private Text textLogo;
    @FXML
    private Button startAnimationButton;
    @FXML
    private CheckBox pickingEnabled;
    @FXML
    private StackPane subScenePane;
    @FXML
    private SubScene subScene;
    @FXML
    private VBox navControl;
    @FXML
    private FourWayNavControl eyeNav;
    @FXML
    private FourWayNavControl camNav;
    @FXML
    private ScrollBar zoomBar;
    @FXML
    private VBox editor;
    private Label pixelSkipValueLabel;
    private Slider pixelSkipSlider;
    @FXML
    private VBox height;
    private Label maxHeightValueLabel;
    private Slider maxHeightSlider;
    private Label scaleValueLabel;
    private Slider scaleSlider;
    @FXML
    private HBox materialColors;
    @FXML
    private VBox header;
    @FXML
    private CheckBox axesOn;
    @FXML
    private CheckBox headLightOn;
    @FXML
    private CheckBox ambientLightOn;
    @FXML
    private CheckBox pointLightOn;
    @FXML
    private ColorPicker ambientColorPicker;
    @FXML
    private ColorPicker pointColorPicker;
    @FXML
    private ColorPicker headColorPicker;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initAll();     
        timer.start();
    }    

    @FXML
    private void refreshCloth(ActionEvent event) {
    }

    @FXML
    private void resetGravity(ActionEvent event) {
    }

    @FXML
    private void toggleRandomForces(ActionEvent event) {
    }

    @FXML
    private void allowMousePicking(ActionEvent event) {
    }

    @FXML
    private void showAxes(ActionEvent event) {
        if(axisGroup.isVisible()){
            axisGroup.setVisible(false);
        }else{
            axisGroup.setVisible(true);
        }
    }

    @FXML
    private void headLightOn(ActionEvent event) {
        if(headLightOn.isSelected()){
            headLight.setLightOn(true);
        }else{
            headLight.setLightOn(false);
        }
        //getChildren().add(MeshUtils.getPointsListView(meshView));
    }

    @FXML
    private void ambientLightOn(ActionEvent event) {
        if(ambientLightOn.isSelected()){
            ambientLight.setLightOn(true);
        }else{
            ambientLight.setLightOn(false);
        }
    }

    @FXML
    private void pointLightOn(ActionEvent event) {
        if(pointLightOn.isSelected()){
            pointLight.setLightOn(true);
        }else{
            pointLight.setLightOn(false);
        }
    }

    
    //======== Initializers ====================================================
    
    private void initAll(){
        initSubScene();
        initLayoutBindings();
        initHeader();
        initEditor();
        initNavControls();
        initLights();
        createAxes();
        buildCloth();       
    }
    
    private void initLights(){
        headLight = new PointLight();        
        headLight.translateXProperty().bindBidirectional(camera.translateXProperty());
        headLight.translateYProperty().bindBidirectional(camera.translateYProperty());
        headLight.translateZProperty().bindBidirectional(camera.translateZProperty());
        headLight.setLightOn(headLightOn.isSelected());
        headLight.colorProperty().bind(headColorPicker.valueProperty());
        headLight.setRotationAxis(Rotate.Y_AXIS);
        headLight.setRotate(cameraYRotate.getAngle());
        
        
        pointLight = new PointLight();
        pointLight.setTranslateX(-1000);
        pointLight.setTranslateY(-1000);
        pointLight.setTranslateZ(-1000);
        pointLight.setLightOn(pointLightOn.isSelected());
        pointLight.colorProperty().bind(pointColorPicker.valueProperty());
        
        ambientLight = new AmbientLight();
        ambientLight.setTranslateY(-1000);
        ambientLight.setLightOn(ambientLightOn.isSelected());
        ambientLight.colorProperty().bind(ambientColorPicker.valueProperty());
        
        root3D.getChildren().addAll(pointLight, headLight, ambientLight);
    }
    
    private void initHeader(){
        
    }
    
    private void initLayoutBindings(){
     subScene.widthProperty().bind(subScenePane.widthProperty());
     subScene.heightProperty().bind(subScenePane.heightProperty());
     
     efficiencyMeter.prefWidthProperty().bind(subScene.widthProperty());
    }
    
    
    private void initSubScene(){
        // Subscene is already added from fxml, but we cannot specify antialiasing in fxml so..
        // we build a new on and replace it
        subScenePane.getChildren().remove(subScene);        
        root3D = new Group();
        
        subScene = new SubScene(root3D, 20,20,true, SceneAntialiasing.BALANCED);
        Stop[] stops = new Stop[]{new Stop(0, Color.BLACK), new Stop(1, Color.RED)};
        LinearGradient lg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        
        subScene.setFill(lg);
        //subScene.setFill(Color.BLACK);
        subScenePane.getChildren().add(0, subScene); // make sure it is the first child so it doesnt hide other nodes
        
                
        // CAMERA
        camera.setNearClip(0.1);
        camera.setFarClip(100000.0);
        camera.getTransforms().addAll(
               // yUpRotate,
                cameraXRotate,
                cameraYRotate,
                cameraPosition,
                cameraLookXRotate,
                cameraLookZRotate
        );
        subScene.setCamera(camera);
        root3D.getChildren().add(camera);
        
        // SCENE EVENT HANDLING FOR CAMERA NAV
        subScene.addEventHandler(MouseEvent.ANY, (MouseEvent event) -> {
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                dragStartX = event.getSceneX();
                dragStartY = event.getSceneY();
                dragStartRotateX = cameraXRotate.getAngle();
                dragStartRotateY = cameraYRotate.getAngle();
            } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                double xDelta = event.getSceneX() -  dragStartX;
                double yDelta = event.getSceneY() -  dragStartY;
                cameraXRotate.setAngle(dragStartRotateX - (yDelta*0.7));
                cameraYRotate.setAngle(dragStartRotateY + (xDelta*0.7));
            }
        });
        subScene.addEventHandler(ScrollEvent.ANY, (ScrollEvent event) -> {
            double z = cameraPosition.getZ()-(event.getDeltaY()*0.2);
            z = Math.max(z,-10000);
            z = Math.min(z,0);
            cameraPosition.setZ(z);
        });
        
    }
    
    private void initNavControls(){
        zoomBar.setMin(-10000);
        zoomBar.setMax(0);
        zoomBar.setBlockIncrement(10);
        zoomBar.setValue(this.getCameraPosition().getZ());
        zoomBar.setVisibleAmount(5);
        this.getCameraPosition().zProperty().bindBidirectional(zoomBar.valueProperty());
        eyeNav.setListener((Side direction, double amount) -> {
            switch (direction) {
                case TOP:
                    this.getCameraLookXRotate().setAngle(this.getCameraLookXRotate().getAngle()+amount);
                    break;
                case BOTTOM:
                    this.getCameraLookXRotate().setAngle(this.getCameraLookXRotate().getAngle()-amount);
                    break;
                case LEFT:
                    this.getCameraLookZRotate().setAngle(this.getCameraLookZRotate().getAngle()-amount);
                    break;
                case RIGHT:
                    this.getCameraLookZRotate().setAngle(this.getCameraLookZRotate().getAngle()+amount);
                    break;
            }
        });
        camNav.setListener((Side direction, double amount) -> {
            switch (direction) {
                case TOP:
                    this.getCameraXRotate().setAngle(this.getCameraXRotate().getAngle()-amount);
                    break;
                case BOTTOM:
                    this.getCameraXRotate().setAngle(this.getCameraXRotate().getAngle()+amount);
                    break;
                case LEFT:
                    this.getCameraYRotate().setAngle(this.getCameraYRotate().getAngle()+amount);
                    break;
                case RIGHT:
                    this.getCameraYRotate().setAngle(this.getCameraYRotate().getAngle()-amount);
                    break;
            }
        });
    }
    
    private void initEditor(){
        
        widthSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) -> {
            widthLabel.setText("Width : " + t1.intValue());
        });
        
        heightSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) -> {
            heightLabel.setText("Height : " + t1.floatValue());
        });
        
        spacingSlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) -> {
            spacingLabel.setText("Spacing : " + t1.floatValue());
        });
        
        gravXSlider.valueProperty().bindBidirectional(rx.angleProperty());
        gravYSlider.valueProperty().bindBidirectional(ry.angleProperty());
        gravZSlider.valueProperty().bindBidirectional(rz.angleProperty());
    }
    
    private void createAxes() {
        axisGroup = new Group();
        
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);
        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);
        final Sphere red = new Sphere(50);
        red.setMaterial(redMaterial);
        final Sphere blue = new Sphere(50);
        blue.setMaterial(blueMaterial);
        xAxis = new Box(24.0, 0.05, 0.05);
        yAxis = new Box(0.05, 24.0, 0.05);
        zAxis = new Box(0.05, 0.05, 24.0);
        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);
        
        axisGroup.getChildren().addAll(xAxis,yAxis,zAxis);
        root3D.getChildren().add(axisGroup);
    }
    
    //=========== Getters / Setters ============================================
    public PerspectiveCamera getCamera() {
        return camera;
    }

    public Rotate getCameraXRotate() {
        return cameraXRotate;
    }

    public Rotate getCameraYRotate() {
        return cameraYRotate;
    }

    public Rotate getCameraLookXRotate() {
        return cameraLookXRotate;
    }

    public Rotate getCameraLookZRotate() {
        return cameraLookZRotate;
    }

    public Translate getCameraPosition() {
        return cameraPosition;
    }

    private void buildCloth() {
        createCurtain();
    }

    /*
     Animation Timer
     */

    private long previousTime = System.currentTimeMillis();
    private long currentTime;
    private final int fixedDeltaTime = 16; // in Milliseconds
    private final double fixedDeltaTimeSeconds = (double) fixedDeltaTime / 1000.0f;
    private int leftOverDeltaTime;
    private final int constraintAccuracy = 8;

    private final AnimationTimer timer = new AnimationTimer() {

        @Override
        public void handle(long now) {

            // calculate elapsed time
            currentTime = System.currentTimeMillis();
            long deltaTimeMS = currentTime - previousTime;
            previousTime = currentTime;
            int timeStepAmt = (int) ((double) (deltaTimeMS + leftOverDeltaTime) / (double) fixedDeltaTime);
            timeStepAmt = Math.min(timeStepAmt, 5);
            leftOverDeltaTime = (int) deltaTimeMS - (timeStepAmt * fixedDeltaTime);
            mouseInfluenceScalar = 1.0f / timeStepAmt;

            for (int iteration = 1; iteration <= timeStepAmt; iteration++) {
                for (int x = 0; x < constraintAccuracy; x++) {
                    masses.parallelStream().forEach(Particle::solveConstraints);
                }
                //log.log(Level.INFO, "Done Solving Constraints");
                masses.parallelStream().forEach(p -> {
                    p.applyForce(Math.random(), gravity, 12 * Math.random());
                    //p.updateInteractions(); 
                    p.updatePhysics(fixedDeltaTimeSeconds);
                });
                if (iteration == timeStepAmt) {
                    masses.forEach(Particle::updateUI);
                }
                //log.log(Level.INFO, "Iteration {0}", (iteration));
            }

            //log.log(Level.INFO, "Updated Physics and UI {0}", (System.currentTimeMillis()-currentTime));
            //log.log(Level.INFO, "TimeStepAmount {0}", (timeStepAmt));
        }
    };
       
    /*==========================================================================
     Cloth curtain
     */
    private List<Particle> masses;
    private final double mouseInfluenceSize = 20;
    private final double mouseTearSize = 35;
    private double mouseInfluenceScalar = 5;
    private final double gravity = 300;
    private final int curtainHeight = 60;
    private final int curtainWidth = 150;
    private final int yStart = 75;
    private final double spc = 5;
    private final double stiffnesses = 1;

    private void createCurtain() {
        masses = new ArrayList<>();
        cloth = new Group();
        int midWidth = (int) (1024 / 2 - (curtainWidth * spc) / 2);
        for (int y = 0; y <= curtainHeight; y++) {
            for (int x = 0; x <= curtainWidth; x++) {
                Particle thisParticle = new Particle(midWidth + x * spc, y * spc + yStart, 0);

                if (x != 0) {
                    thisParticle.attachTo((masses.get(masses.size() - 1).getPointMass()), spc, stiffnesses);
                }
                if (y != 0) {
                    thisParticle.attachTo((masses.get((y - 1) * (curtainWidth + 1) + x).getPointMass()), spc, stiffnesses);
                }

                //pin the 3 verts in the top 2 corners
                if (y == 0 && x == 0) {
                    thisParticle.pinTo(thisParticle.getX(), thisParticle.getY(), thisParticle.getZ());
                }
                if (y == 1 && x == 0) {
                    thisParticle.pinTo(thisParticle.getX(), thisParticle.getY(), thisParticle.getZ());
                }
                if (y == 0 && x % 15 < 2) {
                    thisParticle.pinTo(thisParticle.getX(), thisParticle.getY(), thisParticle.getZ());
                }
                if (y == 0 && x == curtainWidth) {
                    thisParticle.pinTo(thisParticle.getX(), thisParticle.getY(), thisParticle.getZ());
                }
                if (y == 1 && x == curtainWidth) {
                    thisParticle.pinTo(thisParticle.getX(), thisParticle.getY(), thisParticle.getZ());
                }

                masses.add(thisParticle);
            }
        }
        cloth.getChildren().addAll(masses);
        cloth.setRotationAxis(Point3D.ZERO.add(1,0,0));
        cloth.setRotate(-90);
        cloth.setTranslateX(axisGroup.getTranslateX());
        cloth.setTranslateY(axisGroup.getTranslateY());
        cloth.setTranslateZ(axisGroup.getTranslateZ());
        root3D.getChildren().addAll(cloth);
        //log.log(Level.INFO, "Created masses");
    }
    
    


    
}
