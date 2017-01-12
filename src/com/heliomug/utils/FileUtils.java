package com.heliomug.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.CancellationException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileUtils {
	public static File selectDirectory() {
		return selectDirectory("Select Directory");
	}
	
	public static File selectDirectory(String title) {
		JFileChooser fc = new JFileChooser();
    	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	fc.setDialogTitle(title);
		int response = fc.showOpenDialog(null);

		if (response == JFileChooser.APPROVE_OPTION) {
			File directory = fc.getSelectedFile();
			return directory;
		} 
		
		return null;
    }

	public static File selectFile(String title, String type, String extension) {
		JFileChooser fc = new JFileChooser();
		if (type != null && extension != null) {
			fc.setFileFilter(new FileNameExtensionFilter(type, extension));
		}
		fc.setDialogTitle(title);
		int response = fc.showOpenDialog(null);

		if (response == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			return file;
		} 
		
		return null;
	}
	
    public static File selectFile(String title) {
    	return selectFile(title, null, null);
    }
    
    public static File selectFile() {
    	return selectFile("Select File");
    }

	public static Object readObject(String fileType, String fileExtension) throws FileNotFoundException, IOException, ClassNotFoundException {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter(fileType, fileExtension)); 
		int response = fc.showOpenDialog(null);

		if (response == JFileChooser.APPROVE_OPTION) {
			String path = fc.getSelectedFile().getAbsolutePath();
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
				Object obj = ois.readObject();
				return obj;
			} 
		} 
		
		return null;
	}
    
	public static Object readObject(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			Object obj = ois.readObject();
			return obj;
		} 
	}

	public static boolean saveObject(Object obj, String type, String extension) throws IOException {
		JFileChooser fc = new CustomFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter(type, extension));
		int response = fc.showSaveDialog(null);
		if (response == JFileChooser.APPROVE_OPTION) {
			String path = fc.getSelectedFile().getAbsolutePath();
			String neededExtension = extension;
			if (extension != null && !path.endsWith("." + neededExtension)) {
				path += "." + neededExtension;
			}
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
				oos.writeObject(obj);
				return true;
			} 
		} 
		return false;
	}

	public static boolean saveObject(Object obj, File file) throws FileNotFoundException, IOException {
		return saveObject(obj, file.getAbsolutePath());
	}
	
    public static boolean saveObject(Object obj, String path) throws FileNotFoundException, IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
			oos.writeObject(obj);
			oos.close();
			return true;
		}
    }
    
    public static String saveTextAs(String text, String title) throws FileNotFoundException, CancellationException {
		File file = FileUtils.selectFile(title);
		if (file == null) throw new CancellationException();
		try (PrintWriter pw = new PrintWriter(file)) {
			pw.println(text);
		}
		return file.getAbsolutePath();
    }
    
    public static String saveTextAs(String text) throws FileNotFoundException {
    	return saveTextAs(text, "Save Text File As...");
    }
    
    public static String saveComponentImage(JComponent comp) throws IOException {
    	return saveComponentImage(comp, "Save Image File As...");
    }
    
	public static String saveComponentImage(JComponent comp, String title) throws CancellationException, IOException {
		File file = FileUtils.selectFile(title);
		if (file == null) throw new CancellationException();
		if (!file.getPath().endsWith(".png")) {
			file = new File(file.getPath() + ".png");
		}
		BufferedImage bi = new BufferedImage(comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		comp.paint(g);
		ImageIO.write(bi, "png", file);
		g.dispose();
		return file.getAbsolutePath();
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

	
    public static void maing(String[] args) {
    	System.out.println(selectDirectory().getAbsolutePath());
    }
    
}

