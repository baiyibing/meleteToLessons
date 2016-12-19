package convierteLessons_v3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * Esta clase realiza el proceso de transformacion, en el directorio temporal CONTENIDOS, de los archivos  
 * exportados desde contenidos, moviendo las imagenes a la ruta principal, haciendo los cambios necesarios 
 * en los archivos HTML y modificando el xml principal imsmanifest.xml
 * @author jgv1976
 * @version 22/11/2016
 */
public class ProcesaArchivos {

	public List<String> listaRecursos;
	public Boolean recortadoTitulo = false;
	public List<String> listaTitulosRecortados = new ArrayList<String>();
	

	/**
	 * Metodo que realiza una lista de los archivos HTML exportados de contenidos que se encuentran 
	 * en el directorio resources y necesitan ser procesados.
	 * Para cada uno de los archivos de la lista se realiza una llamada a transformaHTML.
	 * @param archivo El parametro archivo indica la ruta principal de los archivos y directorios exportados
	 * @throws IOException
	 */
	public void listaArchivosHTML(String archivo) throws IOException{
		File directorio = new File(archivo + "/resources");
		String[] archivos = directorio.list();
		for (String nombre : archivos) {
			if (nombre.contains(".html")) {
				File a = new File(directorio.getAbsolutePath()+"/"+nombre);
				if (a.isFile()) transformaHTML(a);
			}
		}
	}
	
	
	/**
	 * Metodo que realiza los cambios en los archivos HTML, añadiendo cabecera y cierre html y modificando 
	 * los enlaces de las imagenes para que funcionen en el servidor. Durante el proceso se crea un archivo 
	 * temporal que se elimina una vez convertido.
	 * @param archivo El parametro archivo indica la ruta del archivo HTML a procesar
	 */
	public void transformaHTML(File archivo) throws IOException {
		String linea;
		String cabeceraHtml = "<!DOCTYPE html PUBLIC\" -//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n";
		cabeceraHtml += "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">\n";
		cabeceraHtml += "<head>\n";
		cabeceraHtml += "<meta charset=\"UTF-8\"/>\n";
		cabeceraHtml += "</head>\n";
		cabeceraHtml += "<body>\n";
//		cabeceraHtml += cuentaCaracter(archivo);
		String cierreHtml = "</body>\n";
		cierreHtml += "</html>";
		try {
			File convertido = new File(archivo.getAbsolutePath()+"convertido.html");
			
			Scanner lectura = new Scanner(archivo, "UTF-8");
			PrintWriter escritura = new PrintWriter(convertido);
			escritura.println(cabeceraHtml);
			while(lectura.hasNext()) {
				linea = lectura.nextLine().trim();
				if (linea.contains("<img") || linea.contains("src=\"images/")) {
					linea = linea.replace("images", "/access/content/group/"+ConvierteLesson.id_Sitio_Destino+"/Export"+ConvierteLesson.codigoConversion+"1");
					escritura.println(linea);
				}
				else escritura.println(linea);
			}
			escritura.println(cierreHtml);
			lectura.close();
			escritura.close();
			//renombrar archivos temporales
			File renom = new File(archivo.getAbsolutePath()+"orig");
			archivo.renameTo(renom);
			convertido.renameTo(archivo);
			//Eliminar archivos temporales
			renom.delete();
		} catch(IOException e) {
			System.err.println(e +"Hubo un error de lectura/escritura!!!");
		}
	} 


	/**
	 * ESTE METODO NO SE UTILIZA
	 * Metodo para construir la etiqueta <!--fixups: xx, xxx, xxx --> en los archivos HTML indicando 
	 * la posicion de los enlaces a las imagenes para que el servidor pueda enlazarlas al realizar 
	 * la importacion
	 * @param archivo El parametro archivo indica la ruta del archivo HTML sobre el que se genera la etiqueta fixups
	 */
	public String cuentaCaracter(File archivo) throws FileNotFoundException {
		String posiciones="<!--fixups:";
		int contador=0;
		int corrector = 1;
		String linea;
		Scanner lectura = new Scanner(archivo, "UTF-8");
		while(lectura.hasNext()) {
			linea = lectura.nextLine();
			if (linea.indexOf("images/")==-1) contador += linea.length();
			else {
				posiciones += String.valueOf(contador+(linea.indexOf("images/")+corrector))+",";
				contador += linea.length();
				corrector--;
			}
		}
		lectura.close();
		System.out.println("Convertido: " + archivo.getName() + " - posicion de imagenes: " + posiciones.substring(0, posiciones.length()-1)+"-->");
		return posiciones.substring(0, posiciones.length()-1)+"-->";
	}
	

	/**
	 * Metodo que mueve las imagenes de la carpeta /resources/images a la ruta raiz del
	 * directorio exportado de contenidos
	 * @param rutaTemporal El parametro rutaTemporal indica la ruta del directorio
	 * exportado de contenidos
	 * @param archivo El parametro archivo indica la ruta del directorio final procesado
	 */
	public void moverArchivos(String rutaTemporal, File archivo) throws IOException {
		File directorio = new File(rutaTemporal + "/resources/images");
		if(!directorio.exists()) {
			System.out.println("No existe directorio directorio de imagenes");
		}
		else {
			String[] archivos = directorio.list();
			System.out.println("Lista de archivos a mover: "+archivos.length);
			for (String nombre : archivos) {
				File a = new File(directorio.getPath()+"/"+nombre);
				File destino = new File (rutaTemporal+nombre);
				a.renameTo(destino);
				System.out.println("Movida imagen \""+a.getName()+"\" al directorio : "+destino.getParent());
			} 
			String [] dir = directorio.list();
			if (dir.length == 0) 
				if (directorio.delete()) System.out.println("Borrado directorio /images");
		}
		File zip = new File(rutaTemporal+archivo.getName());
		if (zip.renameTo(archivo)) System.out.println("Devuelto archivo fuente");	
	}
	
	
	/**
	 * Metodo que dirige la transformacion del archivo imsmanifest.xml. Inicia los archivos de lectura
	 * y escritura y va llamando a los metodos que procesan las diferentes partes del archivo xml. 
	 * @param ruta El parametro ruta indica la ruta del archivo imsmanifest.xml
	 * que debe ser procesado
	 * @throws IOException
	 */
	public void transformaXML(String ruta) throws IOException {

		try {
			File original = new File(ruta+"imsmanifest.xml");
			//guardamos en memoria la lista de recursos utilizados
			leeResources(original);
			File convertido = new File(ruta + "imsmanifestConvertido.xml");
//			Scanner lectura = new Scanner(original, "UTF-8");
			BufferedReader lectura = new BufferedReader(
			           new InputStreamReader(
			                      new FileInputStream(original), "UTF8"));
			PrintWriter escritura = new PrintWriter(convertido, "UTF-8");
			//transforma la cabecera
			procesaCabecera(escritura);
			//transforma items
			procesaItems(lectura, escritura);
			//transforma resources
			procesaResources(original, escritura);
			lectura.close();
			escritura.close();
			//renombrar archivos
			File origen = new File(original.getParent()+"/imsmanifestOriginal.xml");
			original.renameTo(origen);
			convertido.renameTo(original);
		} catch(IOException e) {
			System.err.println(e +"Hubo un error de lectura/escritura!!!");
		}
	}
	
	
	/**
	 * Metodo que realiza una lista con los recursos contenidos en el archivo imsmanifest.xml que 
	 * se utiliza para comprobar si es un recurso html o es un recurso para descargar.
	 * @param xmlOriginal El parametro xmlOriginal se refiere al archivo imsmanifest.xml exportado
	 * desde contenidos.
	 * @throws FileNotFoundException
	 */
	private void leeResources(File xmlOriginal) throws FileNotFoundException {
		listaRecursos = new ArrayList<String>();
		Scanner lectura = new Scanner(xmlOriginal, "UTF-8");
		Boolean recursos = false;
		String linea;
		while (lectura.hasNext() && !recursos) {
			linea = lectura.nextLine();
			if (linea.contains("<resources>")) recursos = true;
		}
		while (lectura.hasNext()) {
			linea = lectura.nextLine();
			listaRecursos.add(linea);
		}
		lectura.close();
	}
	

	/**
	 * Inicia el nuevo documento imsmanifest.xml escribiendo la cabecera adecuada para 
	 * lesson builder. Tambien introduce la etiqueta inicial <item> que recoje todo el 
	 * contenido de <organization>
	 * @param escribe El parametro escribe recibe el objeto de escritura sobre el nuevo archivo xml 
	 */
	private void procesaCabecera(PrintWriter escribe) {
		

		escribe.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		escribe.println("<manifest identifier=\"cctd0001\"" );
		escribe.println("xmlns=\"http://www.imsglobal.org/xsd/imsccv1p1/imscp_v1p1\"");
		escribe.println("xmlns:lom=\"http://ltsc.ieee.org/xsd/imsccv1p1/LOM/resource\"");
		escribe.println("xmlns:lomimscc=\"http://ltsc.ieee.org/xsd/imsccv1p1/LOM/manifest\"");
		escribe.println("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
		escribe.println("xsi:schemaLocation=\" http://ltsc.ieee.og/xsd/imsccv1p1/LOM/resource ");
		escribe.println("http://www.imsglobal.org/xsd/imsccv1p1/imscp_v1p1 http://www.imsglobal.org/profile/cc/ccv1p1/ccv1p1_imscp_v1p2_v1p0.xsd ");
		escribe.println("http://ltsc.ieee.org/xsd/imsccv1p1/LOM/manifest http://www.imsglobal.org/profile/cc/ccv1p1/LOM/ccv1p1_lommanifest_v1p0.xsd\"> ");
		escribe.println("<metadata> ");
		escribe.println("<schema>IMS Common Cartridge</schema> " );  
		escribe.println("<schemaversion>1.3.0</schemaversion> " );   
		escribe.println("<lomimscc:lom> ");
		escribe.println("<lomimscc:general>");
		escribe.println("<lomimscc:title>");
//		escribe.println("<lomimscc:string language=\"en-US\">Export-" + ConvierteLesson.codigoConversion + "</lomimscc:string> ");
		escribe.println("<lomimscc:string language=\"en-US\">Export" + ConvierteLesson.codigoConversion + "</lomimscc:string> ");
		escribe.println("</lomimscc:title>");
		escribe.println("</lomimscc:general>");
		escribe.println("</lomimscc:lom>");
		escribe.println("</metadata> ");
		escribe.println("<organizations> " );
		escribe.println("<organization identifier=\"Org1\" structure=\"rooted-hierarchy\"> ");
		escribe.println("<item>");
		if (ConvierteLesson.opcionMenu.equals("1")) {
			escribe.println("<item>");
			escribe.println("<title>"+ ConvierteLesson.tituloSitio +"</title>");
		}
	}
	


	/**
	 * Metodo que procesa la parte del archivo imsmanifest.xml referente a los item (unidades, secciones).
	 * Elimina todas las etiquetas <imsmd...> que no son necesarias y añade nuevas definiciones de items y 
	 * bloques de <metadata> necesarios para la visualizacion del contenido en lesson builder. 
	 * Realiza tambien la comprobacion del tamaño maximo de los titulos de unidades y secciones llamando 
	 * al metodo compruebaLongitudTitulo().
	 * @param lectura El parametro lectura recibe el objeto de lectura sobre el archivo imsmanifest.xml original
	 * @param escritura El parametro escritura recibe el objeto de escritura para continuar sobre el nuevo archivo xml
	 * @throws IOException
	 */
	private void procesaItems(BufferedReader lectura, PrintWriter escritura) throws IOException {
		
		String linea;
		String lineaSiguiente;
		String metadata="";
		String recurso;
		Boolean oculto = false;
		Boolean escribe = false;
		Boolean noResource = true;
		metadata = "\t<metadata>\n";
		metadata += "\t\t<lom:lom>\n";
		metadata += "\t\t\t<lom:general>\n";
		metadata += "\t\t\t\t<lom:structure>\n";
		metadata += "\t\t\t\t\t<lom:source>inline.lessonbuilder.sakaiproject.org</lom:source>\n";
		metadata += "\t\t\t\t\t<lom:value>true</lom:value>\n";
		metadata += "\t\t\t\t</lom:structure>\n";
		metadata += "\t\t\t</lom:general>\n";
		metadata += "\t\t</lom:lom>\n";
		metadata += "\t</metadata>";
		
		while ((linea = lectura.readLine()) != null && noResource) {
					
			if (linea.contains("isvisible=\"false\"")) oculto = true;
			if (linea.contains("<title>")) linea = compruebaLongitudTitulo(linea);
			if (linea.trim().startsWith("<item")) escribe = true;
			
			if (escribe){
				if (linea.contains("identifierref") && !oculto) {
					recurso = linea.substring(linea.indexOf("RESOURCE"), linea.indexOf("\">"));

					if (esContenido(recurso)) {
						
						System.out.println(recurso + " es contenido para visualizar.");
						
						lineaSiguiente = lectura.readLine();
						if (lineaSiguiente.contains("<title>")) lineaSiguiente = compruebaLongitudTitulo(lineaSiguiente);
						escritura.println(linea.substring(0, linea.indexOf("identifierref"))+">");
						escritura.println(lineaSiguiente);
						escritura.println(linea.replace(linea.substring(linea.indexOf("r=\""), linea.indexOf("r=\"")+3),"=\"x"));
						escritura.println(lineaSiguiente);
						escritura.println(metadata);
						escritura.println("</item>");
					}
					else {
						System.out.println(recurso + " es contenido DESCARGABLE.");
						escritura.println(linea);
					}
				}
				else if(linea.contains("</organization>")) {
					escritura.println("</item>");
					if (ConvierteLesson.opcionMenu.equals("1")) escritura.println("</item>");
					escritura.println("</organization>");
					noResource = false;
				}
				else if (linea.indexOf("imsmd") == -1 && !oculto) escritura.println(linea);
			}
		}
		if (recortadoTitulo) {
			String mensaje = "Los siguientes títulos (" +listaTitulosRecortados.size() + ") se han recortado a 99 caracteres de longitud:\n\n";
			for (String t : listaTitulosRecortados) {
				mensaje += "\t - " + t + "\n";
			}
			mensaje += "\n";
			JOptionPane.showMessageDialog(null, mensaje);
		}
	}
	
	
	/**
	 * Metodo que comprueba si el recurso referido en imsmanifest.xml se corresponde con un html para visualizar
	 * o se trata de un pdf u otro tipo de archivo descargable.
	 * @param recurso
	 * @return
	 */
	private Boolean esContenido(String recurso) {
		for (String r : listaRecursos) {
			if (r.contains(recurso) && r.contains(".html")) 
				return true;
		}
		return false;
	}
	
	
	/**
	 * Metodo que comprueba que los titulos de las unidades y secciones no superen los 100 caracteres
	 * como maximo admitidos por el campo title de la base de datos.
	 * @param titulo El parametro titulo contiene el string con el titulo a comprobar
	 * @return El titulo original si no supera los 100 caracteres o el titulo recortado a 95 caracteres si lo supera
	 */
	private String compruebaLongitudTitulo(String titulo) {
		
		if (titulo.trim().length()>114){
			System.out.println(titulo + " --> tiene una longitud mayor de 114 caracteres");
			String tituloMayor100 = titulo.substring(titulo.indexOf(">")+1, titulo.indexOf("</"));
			listaTitulosRecortados.add(tituloMayor100);
			System.out.println("Titulo mayor de 100 caracteres: " + tituloMayor100 + " (longitud: "+titulo.length()+")");
			String tituloMenor100 = tituloMayor100.substring(0,99);
			System.out.println("Titulo menor de 100 caracteres: "+ tituloMenor100 +" (longitud: "+titulo.length()+")");
			titulo = "<title>"+tituloMenor100+"</title>";
			System.out.println("Resultado de linea final: "+titulo);
			recortadoTitulo = true;
		}
		return titulo;
	}
	

	/**
	 * Metodo que realiza el procesado de la parte de <resources> del archivo imsmanifest.xml. Para cada imagen enlazada 
	 * se crea un nuevo recurso y una dependencia a ella, eliminando las lineas que contienen imsd que son inservibles.
	 * @param lectura El parametro lectura recibe el objeto de lectura sobre el archivo imsmanifest.xml original
	 * @param escritura El parametro escritura recibe el objeto de escritura para continuar sobre el nuevo archivo xml
	 * @throws IOException
	 */
	private void procesaResources(File xmlOriginal, PrintWriter escritura) throws IOException {
		int contador=0;
		ArrayList<String> listaResources = new ArrayList<String>();
		String linea;
		String resource="";
		String enlaceImagen;
		Boolean recursos = false;
		System.out.println("\n\nProcesando recursos..................\n\n");
		
		Scanner lectura2 = new Scanner(xmlOriginal, "UTF-8");
		escritura.println("</organizations>");
		escritura.println("<resources>");
		while (lectura2.hasNext() && !recursos) {
			linea = lectura2.nextLine();
			if (linea.contains("<resources>")) recursos = true;
		}
		
		while(lectura2.hasNext()) {
			
			linea = lectura2.nextLine().trim();
			if (linea.contains("<file") && linea.contains("images")) {
				escritura.println("<dependency identifierref=\"res"+contador+"\"/>");
				enlaceImagen = linea.replace("<file href=\"resources/images/", "href=\"");
				resource += "<resource type=\"webcontent\" identifier=\"res"+contador+"\" "+enlaceImagen.substring(0, enlaceImagen.length()-2)+">\n";
				resource += linea.replace("href=\"resources/images/", "href=\"")+"\n";
				resource += "</resource>\n";
				listaResources.add(resource);
				resource = "";
				contador++;
			}
			else if(linea.contains("</resources>")) {
				for (String recurso : listaResources) {
					escritura.println(recurso);
				}
				escritura.println(linea);
			}
			else if(!linea.contains("imsm")) escritura.println(linea);
		}
		lectura2.close();
	} 


	
	/**
	 * Metodo que borra archivos .xsd creados en la exportacion de CONTENIDOS 
	 * y el archivo temporal imsmanifest.xml que no son necesarios.
	 * @param ruta El parametro ruta indica la ruta principal del directorio temporal 
	 * CONVERTIDO creado para el procesado de los archivos
	 */
	public void borrarArchivos(String ruta) {
		new File(ruta + "imscp_v1p1.xsd").delete();
		new File(ruta + "imsmd_v1p2.xsd").delete();
		new File(ruta + "imsmanifestOriginal.xml").delete();
		new File(ruta + "xml.xsd").delete();	
	}
}

