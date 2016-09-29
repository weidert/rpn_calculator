package com.heliomug.java.rpncalc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileHandler {
	private static String getHomePath() {
		return System.getProperty("user.home") + File.separator;
		
	}
	
	// savers
	public static boolean saveHere(Object obj, String name) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getHomePath() + name))) {
			oos.writeObject(obj);
			oos.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static boolean saveObject(Saveable obj) {
		JFileChooser fc = new CustomFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter(obj.getFileDescription(), obj.getFileExtension()));
		int response = fc.showSaveDialog(null);
		if (response == JFileChooser.APPROVE_OPTION) {
			String path = fc.getSelectedFile().getAbsolutePath();
			String neededExtension = "." + obj.getFileExtension();
			if (neededExtension != null && !path.endsWith(neededExtension)) path += neededExtension;
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
				oos.writeObject(obj);
				oos.close();
				return true;
			} catch (IOException e) {
				return false;
			}
		} 
		return false;
	}
	
	// loaders
	public static Object loadHere(String name) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getHomePath() + name))) {
			Object obj = ois.readObject();
			ois.close();
			return obj;
		} catch (IOException | ClassNotFoundException e) {
			//e.printStackTrace();
			return null;
		}		
	}
	
	public static Object loadObject(String fileType, String fileExtension) {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter(fileType, fileExtension)); 
		int response = fc.showOpenDialog(null);

		if (response == JFileChooser.APPROVE_OPTION) {
			String path = fc.getSelectedFile().getAbsolutePath();
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
				Object obj = ois.readObject();
				ois.close();
				return obj;
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} 
		} 
		return null;
	}

	// to have confirmation on save over
	private static class CustomFileChooser extends JFileChooser {
		private static final long serialVersionUID = -8589322425733738162L;

		@Override
	    public void approveSelection(){
	        File f = getSelectedFile();
	        if(f.exists() && getDialogType() == SAVE_DIALOG){
	            int result = JOptionPane.showConfirmDialog(this,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
	            switch(result){
	                case JOptionPane.YES_OPTION:
	                    super.approveSelection();
	                    return;
	                case JOptionPane.NO_OPTION:
	                    return;
	                case JOptionPane.CLOSED_OPTION:
	                    return;
	                case JOptionPane.CANCEL_OPTION:
	                    cancelSelection();
	                    return;
	            }
	        }
	        super.approveSelection();
	    }        
	}
}
