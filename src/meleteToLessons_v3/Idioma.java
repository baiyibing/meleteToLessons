package convierteLessons_v3;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Esta clase se encarga de asignar el idioma a la aplicacion en funcion del que utiliza el sistema
 * operativo. (Ingles, Español y Valenciano)
 * @author jgv1976
 * @version 22/11/2016
 */
public class Idioma {
	
	static Map<String, String> idioMap = new HashMap<String, String>();
	
	
	public static void seleccionaIdioma() {
		
		Locale locale = Locale.getDefault();
		String idioma = locale.getDisplayLanguage();
//		String idioma = "Valencia";
		
		switch(idioma) {
			case "Español": {
				idioMap.put("titulo", "Conversor Contenidos - Lessons");
				idioMap.put("buscar", "Buscar");
				idioMap.put("convertir", "Convertir");
				idioMap.put("salir", "Salir");
				idioMap.put("idDestino", "Introducir ID sitio DESTINO");
				idioMap.put("selZip", "Seleccionar fuente .ZIP:");
				idioMap.put("selMenu", "Seleccionar tipo de menu:");
				idioMap.put("unicoEnlace", "Unico enlace principal");
				idioMap.put("variosEnlaces", "Un enlace a cada unidad");
				idioMap.put("procesando", "procesando......");			
				idioMap.put("terminado", "¡¡ CONVERSION REALIZADA CON EXITO !!");	
				idioMap.put("modificacion", "Ultima modificacion: ");
				idioMap.put("version", "version");	
				break;
			}
			case "English": {
				idioMap.put("titulo", "Converter Melete - Lessons");
				idioMap.put("buscar", "Search");
				idioMap.put("convertir", "Convert");
				idioMap.put("salir", "Exit");
				idioMap.put("idDestino", "Enter DESTINATION site ID");
				idioMap.put("selZip", "Select source file .ZIP:");
				idioMap.put("selMenu", "Select type of menu:");
				idioMap.put("unicoEnlace", "An unique homelink");
				idioMap.put("variosEnlaces", "A link for each unit");	
				idioMap.put("procesando", "processing......");			
				idioMap.put("terminado", "¡¡ CONVERSION COMPLETED SUCCESSFULLY !!");
				idioMap.put("modificacion", "Last modification: ");
				idioMap.put("version", "version");	
				break;
			}
			case "Valencia": {
				idioMap.put("titulo", "Convertidor Continguts - Lessons");
				idioMap.put("buscar", "Cercar");
				idioMap.put("convertir", "Convertir");
				idioMap.put("salir", "Eixir");
				idioMap.put("idDestino", "Introdueix ID del lloc destí:");
				idioMap.put("selZip", "Selecciona arxiu font .ZIP:");
				idioMap.put("selMenu", "Selecciona tipus de menu:");
				idioMap.put("unicoEnlace", "Unic enllaç principal");
				idioMap.put("variosEnlaces", "Enllaç per a cada unitat");	
				idioMap.put("procesando", "processant......");			
				idioMap.put("terminado", "¡¡ CONVERSIÓ REALITZADA AMB ÈXIT !!");
				idioMap.put("modificacion", "Ultima modificació: ");
				idioMap.put("version", "versio");	
				break;
			}
			default: {
				idioMap.put("titulo", "Conversor Melete - Lessons");
				idioMap.put("buscar", "Buscar");
				idioMap.put("convertir", "Convertir");
				idioMap.put("salir", "Salir");
				idioMap.put("idDestino", "Introducir ID sitio DESTINO");
				idioMap.put("selZip", "Seleccionar fuente .ZIP:");
				idioMap.put("selMenu", "Seleccionar tipo de menu:");
				idioMap.put("unicoEnlace", "Unico enlace principal");
				idioMap.put("variosEnlaces", "Un enlace a cada unidad");	
				idioMap.put("procesando", "procesando......");			
				idioMap.put("terminado", "¡¡ CONVERSION REALIZADA CON EXITO !!");	
				idioMap.put("modificacion", "Ultima modificacion: ");
				idioMap.put("version", "version");	
				break;
			}
		}
	}
}