/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.rivers;

import java.net.URL;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.rivers.model.Model;
import it.polito.tdp.rivers.model.River;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import jdk.javadoc.internal.doclets.toolkit.util.DocFileIOException.Mode;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxRiver"
    private ComboBox<River> boxRiver; // Value injected by FXMLLoader

    @FXML // fx:id="txtStartDate"
    private TextField txtStartDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtEndDate"
    private TextField txtEndDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtNumMeasurements"
    private TextField txtNumMeasurements; // Value injected by FXMLLoader

    @FXML // fx:id="txtFMed"
    private TextField txtFMed; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxRiver != null : "fx:id=\"boxRiver\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtStartDate != null : "fx:id=\"txtStartDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtEndDate != null : "fx:id=\"txtEndDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNumMeasurements != null : "fx:id=\"txtNumMeasurements\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtFMed != null : "fx:id=\"txtFMed\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    @FXML
    void doAggiorna(ActionEvent event) {
    	try {
			River fiume= boxRiver.getValue();
			if (fiume==null)
			{
				txtResult.setText("Fiume non selezionato");
				return;
			}
			txtStartDate.setText(model.getDataInizio(fiume).toString());
			txtEndDate.setText(model.getDataFine(fiume).toString());
			txtNumMeasurements.setText(String.valueOf(model.getNumeroMisurazioni(fiume)));
			txtFMed.setText(String.format("%.3f", model.getMedia(fiume)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	try {
    		River fiume= boxRiver.getValue();
			if (fiume==null)
			{
				txtResult.setText("Fiume non selezionato");
				return;
			}
			double k=-1;
			k= Double.parseDouble(txtK.getText());
    		if (k<0)
    		{
    			txtResult.setText("Valore di k non ammissibile");
    			return;
    		}
    		
    		model.simula(k, fiume);
    		txtResult.setText(String.format("La capacità Q è %.3f\n", model.getQ()));
    		txtResult.appendText(String.format("L'occupazione giornaliera media C è %.3f\n", model.getOccupazioneMedia()));
    		txtResult.appendText(String.format("Giornate di carenza idrica: %d su un totale di %d ", model.getInsoddisfatti(), model.getGiorniTotali()));
    		double media= model.getInsoddisfatti()*100/model.getGiorniTotali();
    		txtResult.appendText(String.format("(%.2f", media));
    		txtResult.appendText("%)");
    		
			
			
		}
    	catch (InvalidParameterException e) {
			// TODO: handle exception
		}
    	
    	catch (Exception e) {
		}
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	List<River> fiumi= model.getRivers();
    	boxRiver.getItems().addAll(fiumi);
    }
}
