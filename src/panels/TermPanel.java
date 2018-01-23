package panels;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import myLibrary.Methods;

import parser.Parser;


public class TermPanel extends JPanel{
	
	
	JComboBox termBox;
	
	public TermPanel() throws IOException{
		
		build();
		
	}
	private void build() throws IOException{
		
		ArrayList<String> termsArrayList = Parser.extractTerms();
		String[] terms = Methods.toStringArray( termsArrayList );
		
		
		termBox = new JComboBox( terms );
		termBox.setOpaque( false );
		termBox.setBorder( null );
		termBox.setPreferredSize( new Dimension( 150,25 ) );
		
		
		this.setLayout( new GridBagLayout() );
		GridBagConstraints c = new GridBagConstraints();
		this.add( termBox , c );
		
		
		//this.add( termBox );
		
		
	}
	
	
	public String getSelectedTerm(){
		
		String selection = (String) termBox.getSelectedItem();
		return Methods.getSubstring( selection , "(" , ")" );
		
	}
	
	
	
	
}
