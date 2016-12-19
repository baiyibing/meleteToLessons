package convierteLessons_v3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.*;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * Esta clase se encarga de realizar los procesos de extraccion y compresion de los 
 * archivos zip asi como el borrado de la carpeta de trabajo temporal.
 * Para la extraccion del zip se utiliza la libreria zip4j
 * Para la compresion se utiliza zip por defecto de java.
 * @author jgv1976
 * @version 22/11/2016
 */
public class Zip {

	List<String> fileList;
	private String SOURCE_FOLDER;

	/**
	 * Constructor de la clase Zip que inicializa la lista fileList y enlaza SOURCE_FOLDER 
	 * con la direccion de la carpeta 
	 * @param rutaTemp
	 * @param archivoZip
	 */
	Zip(String rutaTemp){
		
		fileList = new ArrayList<String>();
		SOURCE_FOLDER = rutaTemp;	
	}
	
	
	/**
	 * Metodo para extraer los archivos contenidos en el zip exportado por contenidos
	 * @param ruta El parametro ruta indica la ruta donde extraer los archivos
	 * @param archivo El parametro archivo indica el nombre del archivo zip a extraer
	 */
	public void extraer(String ruta, String archivo) {

		try {
			ZipFile zipFile = new ZipFile(ruta+archivo);
			System.out.println("Archivos de "+ruta+archivo + " extraidos en " + ruta);
			zipFile.extractAll(ruta);
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Metodo para comprimirlos todos los archivos necesarios en un fichero zip listo para importar desde lessons
	 * Utiliza el metodo generateFileList() para generar los nombres de los archivos que se incluiran en el zip
	 * @param zipFile el parametro zipFile recibe el nombre/ruta del archivo zip que se genera despues de la conversion
	 */
	public void zipIt(String zipFile){
		
		byte[] buffer = new byte[1024];
		try{
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			System.out.println("Comprimiendo en archivo : " + zipFile);
			for(String file : this.fileList){

				System.out.println("Añadido archivo : " + file);
				ZipEntry ze= new ZipEntry(file);
				zos.putNextEntry(ze);

				FileInputStream in = new FileInputStream(SOURCE_FOLDER + File.separator + file);

				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				in.close();
			}
			zos.closeEntry();
			zos.close();
			System.out.println("Compresion terminada");
		}catch(IOException ex){
			ex.printStackTrace();   
		}
	}

	
	/**
	 * Metodo que genera una lista con los archivos que seran incluidos en el zip
	 * Utiliza el metodo privado generateZipEntry() para generar el nombre / ruta de los archivos
	 * @param node El parametro node indica la ruta principal de la carpeta temporal donde estan los archivos del sitio
	 */
	public void generateFileList(File node){

		if(node.isFile()){
			//con el replace cambiamos la contrabarra de la ruta de los archivos por barra normal para que
			//se reconozcala carpeta al abrirlo en linux
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString().replace("\\", "/"))); 
		}

		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				generateFileList(new File(node, filename));
			}
		}
	}

	
	/**
	 * Metodo que genera el nombre con el que sera comprimido cada archivo a partir de su ruta absoluta
	 * @param file El parametro file recibe la ruta absoluta de cada uno de los archivos que se incluiran 
	 * en la lista de archivos a comprimir
	 * @return Devuelve el nombre/ruta local del archivos para incluir en el zip
	 */
	private String generateZipEntry(String file){
		return file.substring(SOURCE_FOLDER.length(), file.length());
	}
	
	
	/**
	 * Metodo que borra una carpeta y todo su contenido. Utiliza el metodo privado deleteChildren()
	 * @param ruta El parametro ruta indica la ruta de la carpeta a borrar
	 * @return Devuelve true o false segun el resultado del borrado de la carpeta
	 */
	public boolean borrarCarpeta(String ruta) { 

		File file = new File(ruta);  
		if (!file.exists()) {  
			return true;  
		}  
		if (!file.isDirectory()) {  
			return file.delete();  
		}  
		return this.deleteChildren(file) && file.delete();  
	}  

	
	/**
	 * Metodo privado utilizado por borrarCarpeta() para eliminar archivos contenidos en un directorio
	 * @param dir El parametro dir recibe la ruta de la carpeta que contiene los archivos a borrar
	 * @return true o false segun el resultado del borrado del archivo
	 */
	private boolean deleteChildren(File dir) {  
		File[] children = dir.listFiles();  
		boolean childrenDeleted = true;  
		for (int i = 0; children != null && i < children.length; i++) {  
			File child = children[i];  
			if (child.isDirectory()) {  
				childrenDeleted = this.deleteChildren(child) && childrenDeleted;  
			}  
			if (child.exists()) {  
				childrenDeleted = child.delete() && childrenDeleted;  
			}  
		}  
		return childrenDeleted;  
	}  
}

