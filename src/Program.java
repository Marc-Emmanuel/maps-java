import java.util.ArrayList;
import java.util.List;

import maps.directions.MapsDirectionsParser;
import maps.objects.ThreadedFileWriter;
import maps.utils.In;

import org.w3c.dom.Document;

public class Program {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("Maps Directions Parser");
		System.out.println("Dossier dans lequel ecrire les fichiers: ");
		String folder=In.readLine();
		
		System.out.println("Nom du fichier de sortie (avec extension): ");
		String fileName=In.readLine();
		System.out.println("Format (si coordonnees Geo, new google.maps.LatLng(, si autre laisser vide): ");
		String format="";
		format+=In.readLine();
		System.out.println("\nRequete: ");
		System.out.println("Nombre de points de passage (Incluant le départ et l'arrivee): ");
		int nbPoints=In.readInt();
		int i=0;
		List<String> wayPoints = new ArrayList<String>();
		while(i<nbPoints){ 
			System.out.println("Etape "+(i+1)+": ");
			wayPoints.add(In.readLine());
			i++;
		}
		System.out.println("Moyen de transport(driving,cycling,walking): ");
		String mode=In.readLine().toLowerCase();
		if(!mode.equalsIgnoreCase("driving")&&!mode.equalsIgnoreCase("walking")&&!mode.equalsIgnoreCase("cycling")){
			mode="driving";
			System.out.println("Impossible de determiner le mode de transport, mode par defaut, driving");
		}
		MapsDirectionsParser parser = new MapsDirectionsParser();
		Document doc = parser.getDocument(wayPoints,
				mode);
		ArrayList<ArrayList<Double>> directions = parser
				.getDirection(doc);
		System.out.println("Requete: "+parser.getRequestUrl());
		System.out.println("Retrieving datas from google's servers");
		System.out.println("Ecrire le fichier? (o/n)");
		char o=In.readChar();
		ThreadedFileWriter writerPoints=null;
		if(o=='o'){
			System.out.println("Si le nombre de points est superieur a 90 000, deux fichiers seront ecrits.");
			System.out.println("Nombre de points GPS: " + directions.size());
			System.out.println("Distance: "+(parser.getDistanceValue(doc)/2000)+" km.");
			System.out.println("Duree: "+parser.getDurationText(doc)+" "+mode);
			writerPoints=new ThreadedFileWriter(folder, fileName, directions, format);
			writerPoints.start();
		}
		
		
		System.out.println("\nEcrire les steps (guidage) dans un fichier(o/n): ");
		char c=In.readChar();
		ThreadedFileWriter instructionsWriter=null;
		if(c=='o'){
			String initWebPage = "<!DOCTYPE html>\n"
					+ "<html>\n"
					+ "<head>\n"
					+ "<meta charset=\"UTF-8\" />\n"
					+ "<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">\n"
					+ "<title> PolyLines Extraction from Directions</title>\n"
					+ "</head>\n" + "<body>\n";
			initWebPage+=parser.getHtmlInstruction(doc)+"</body></html>";
			instructionsWriter=new ThreadedFileWriter(folder, "instructions_"+wayPoints.get(0)+"_"+wayPoints.get(wayPoints.size()-1)+".html", initWebPage, format);
			instructionsWriter.start();
		}
		if(instructionsWriter!=null){
			instructionsWriter.join();
			System.out.println("Le fichier des instructions a ete ecrit.");
		}
		if(writerPoints!=null){
			writerPoints.join();
			System.out.println("Le fichier des points a ete ecrit.");
		}
	}
}
