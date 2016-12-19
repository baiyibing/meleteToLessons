package convierteLessons_v3;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Esta clase maneja la parte grafica de la aplicacion, asignando las funciones a los diferentes elementos
 * e iniciando la ejecucion del proceso de conversion una vez introducidos los datos necesarios.
 * @author jgv1976
 * @version 22/11/2016
 */
public class ConvierteLesson extends JFrame implements ActionListener {

	private String version = "22/11/2016";
	
	private static final long serialVersionUID = 1L;
	public static String id_Sitio_Destino="";
	public static String opcionMenu="";
	public static String tituloSitio="";
	public static String codigoConversion="";
	private JButton bt1, bt2, bt3;
	private JLabel l1, l2, l3;
	static JLabel l4;
	private JTextField t1,t2;
	private JComboBox<String> cmb1;
	String [] opciones = {"",Idioma.idioMap.get("unicoEnlace"), Idioma.idioMap.get("variosEnlaces")};
	String estado="";
	int respuesta;
	File archivoElegido;
	String archivoOrigen;
	String ruta;
	Boolean opcionSeleccionada = false;

	public static void main(String[] args) throws IOException {
		Idioma.seleccionaIdioma();
		JFrame frame = new ConvierteLesson(Idioma.idioMap.get("titulo"));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(555, 300);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public ConvierteLesson(String t) {
		super(t);
		setLayout(null);
		l1=new JLabel(Idioma.idioMap.get("idDestino"));
        l1.setBounds(35,25,340,30);
        add(l1);
        
        t1= new JTextField(20);
        t1.setBounds(250,25,275,25);
        t1.setBorder(BorderFactory.createCompoundBorder(t1.getBorder(), 
        		BorderFactory.createEmptyBorder(0, 5, 0, 0)));
        
        t1.getDocument().addDocumentListener(new DocumentListener() {
        	  public void changedUpdate(DocumentEvent e) {
        		    warn();
        		  }
        		  public void removeUpdate(DocumentEvent e) {
        		    warn();
        		  }
        		  public void insertUpdate(DocumentEvent e) {
        		    warn();
        		  }

        		  public void warn() {
        			  if(!t1.getText().equals("") && opcionSeleccionada) bt1.setEnabled(true);
        			  else bt1.setEnabled(false);
        		  }
        		});
        
        add(t1);

        l3=new JLabel(Idioma.idioMap.get("selZip"));
        l3.setBounds(35,75,400,30);
        add(l3);
        
        t2= new JTextField(20);
        t2.setBounds(250,75,180,25);
        t2.setBorder(BorderFactory.createCompoundBorder(t2.getBorder(), 
        		BorderFactory.createEmptyBorder(0, 5, 0, 0)));
        
        t2.getDocument().addDocumentListener(new DocumentListener() {
      	  public void changedUpdate(DocumentEvent e) {
      		    warn();
      		  }
      		  public void removeUpdate(DocumentEvent e) {
      		    warn();
      		  }
      		  public void insertUpdate(DocumentEvent e) {
      		    warn();
      		  }

      		  public void warn() {
      			if(!t2.getText().equals("") && opcionSeleccionada) bt1.setEnabled(true);
      		    else bt1.setEnabled(false);
      		  }
      		});
        add(t2);
        
        bt2=new JButton(Idioma.idioMap.get("buscar"));
        bt2.setBounds(440,75,85,25);
        add(bt2);
        bt2.addActionListener(this);
        bt2.setEnabled(true);
        
        l2=new JLabel(Idioma.idioMap.get("selMenu"));
        l2.setBounds(35,125,400,30);
        add(l2);
        
        cmb1= new JComboBox<String>(opciones);
        cmb1.setBounds(250, 125, 275, 25);
        cmb1.setBackground(Color.white);
        cmb1.addActionListener(this);
        add(cmb1);
        
        l4=new JLabel(Idioma.idioMap.get("procesando"));
      
        l4.setBounds(35,160,400,30);
        add(l4);
        l4.setVisible(false);
        
        bt1=new JButton(Idioma.idioMap.get("convertir"));
        bt1.setBounds(200,210,150,40);
        add(bt1);
        bt1.addActionListener(this);
        bt1.setEnabled(false);
        
        bt2=new JButton(Idioma.idioMap.get("salir"));
        bt2.setBounds(200,210,150,40);
        add(bt2);
        bt2.addActionListener(this);
        bt2.setVisible(false);
        
        bt3=new JButton(Idioma.idioMap.get("version"));
        bt3.setBounds(520,230,20,20);
        add(bt3);
        bt3.addActionListener(this);
        bt3.setEnabled(true);
	}
		

	//captura los eventos de los elementos de la ventana principal y les asigna funciones
	public void actionPerformed (ActionEvent e) {

		if (e.getSource() instanceof JButton) {
			String nombreBoton = e.getActionCommand();
			
			if (nombreBoton.equals(Idioma.idioMap.get("convertir"))){
				id_Sitio_Destino=t1.getText().trim().toUpperCase();    
				tituloSitio=id_Sitio_Destino;
				t1.setText(id_Sitio_Destino);
				l4.setVisible(true);
			try {
				ejecutar();
			} catch (IOException e1) {
				e1.printStackTrace();
				}
			}else if (nombreBoton.equals(Idioma.idioMap.get("buscar"))) {
				seleccionaArchivo();
			}else if (nombreBoton.equals(Idioma.idioMap.get("salir"))) {
				System.exit(0);
			}
			else if (nombreBoton.equals(Idioma.idioMap.get("version"))) {
				JOptionPane.showMessageDialog(null,Idioma.idioMap.get("modificacion") + version);
			}
			
		}else if (e.getSource() instanceof JComboBox) {
			@SuppressWarnings("unchecked")
			JComboBox<String> cb = (JComboBox<String>) e.getSource();
			int seleccion = cb.getSelectedIndex();
			switch (seleccion) {
			
				case 0: opcionSeleccionada=false;break;
				case 1: opcionMenu ="1";opcionSeleccionada=true;break;
				case 2: opcionMenu ="2";opcionSeleccionada=true;break;
				
			}
		}else if (e.getSource() instanceof JTextField) {
			t1.setText(t1.getText().toUpperCase());
		}
		if(t1.getText().equals("") || t2.getText().equals("") || !opcionSeleccionada)
			bt1.setEnabled(false);
		else bt1.setEnabled(true);
	}
	
	
	
	/**
	 * Metodo que abre la ventana de explorador de archivos para seleccionar el archivo a convertir
	 */
	public void seleccionaArchivo() {
		JFileChooser fc = new JFileChooser();
		respuesta = fc.showOpenDialog(fc);
		archivoElegido = fc.getSelectedFile();
		archivoOrigen = archivoElegido.getName();
		ruta = archivoElegido.getParent()+File.separator;//"/";
		if (respuesta == JFileChooser.APPROVE_OPTION) { 
			t2.setText(archivoOrigen);
		}
	}
	
	String cadenaRandom (int longitud){
		String cadenaAleatoria = "";
		long milis = new java.util.GregorianCalendar().getTimeInMillis();
		Random r = new Random(milis);
		int i = 0;
		while ( i < longitud){
			char c = (char)r.nextInt(255);
			if ( (c >= '0' && c <='9') /*|| (c >='A' && c <='Z')*/ ){
				cadenaAleatoria += c;
				i ++;
			}
		}
		return cadenaAleatoria;
	}
	
	
	/**
	 * Metodo que se ejecuta al finalizar la conversion indicando que todo ha ido bien
	 */
	public void conversionRealizada() {
		l4.setText(Idioma.idioMap.get("terminado"));
		l4.setForeground(Color.decode("#009900"));
		bt1.setVisible(false);
		bt2.setVisible(true);
	}

	
	/**
	 * Metodo que va realizando las llamadas ordenadas a los diferentes metodos que realizan la conversion
	 * de los archivos.
	 * @throws IOException
	 */
	public void ejecutar() throws IOException {
		
		System.out.println("Seleccionado \"" + id_Sitio_Destino + "\" como sitio DESTINO");	
		ConvierteLesson.codigoConversion = cadenaRandom(3);
		System.out.println("Nombre de la carpeta recursos: Export" + ConvierteLesson.codigoConversion + "1");
		File temp = new File(ruta + "CONVERTIDO");
		temp.mkdir();
		if (temp.exists()) System.out.println("Se ha creado el directorio " + ruta +"temporal");
		String rutaTemp = ruta + "CONVERTIDO"+File.separator;//";
		File destino = new File(rutaTemp + archivoOrigen);
		archivoElegido.renameTo(destino);
		ProcesaArchivos c = new ProcesaArchivos();
		Zip zip = new Zip(rutaTemp);
		zip.extraer(rutaTemp, archivoOrigen);

		
		c.listaArchivosHTML(rutaTemp);
		c.moverArchivos(rutaTemp, archivoElegido);
		c.transformaXML(rutaTemp);
		c.borrarArchivos(rutaTemp);
		zip.generateFileList(new File(rutaTemp));
		zip.zipIt(ruta+"XXX_"+archivoOrigen+"_CONVERTIDO_LESSONS_XXX.zip");
		zip.borrarCarpeta(ruta+"CONVERTIDO");
		conversionRealizada();
		
	}	
}
