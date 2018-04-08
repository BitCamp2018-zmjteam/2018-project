package com.scorpions.bcp.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public interface Structure extends Serializable {

	Tile[][] getTiles();
	
	public static boolean exportStructure(Structure c, File f) {
		if(!f.getParentFile().exists()) {
			f.mkdirs();
		}
		try {
			if(!f.exists()) {
				f.createNewFile();
			}
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(c);
			oos.flush();
			oos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Structure fromFile(File f) {
		try {
			if (f.exists()) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
				Object o = ois.readObject();
				ois.close();
				if (o instanceof Structure) {
					return (Structure) o;
				}
				return null;
			} else {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}
}
