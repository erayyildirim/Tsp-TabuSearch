package solution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


public class TabuSearch {
	
	static int dimension;
	private static double[][] distanceBetweenCities; // tspenvironmentte
														// tutualacak
	private static TSPEnvironment tspEnvironment;

	public static void getDistanceBetweenCities(ArrayList<City> Cities) {
		int CityNumber = Cities.size();
		distanceBetweenCities = new double[CityNumber][CityNumber];
		for (int i = 0; i < CityNumber-1; i++)
			for (int j = 1; j < CityNumber; j++)
				// 2nokta arası uzaklık (x1-x2)^2 + (y1-y2)^2 karekoku
				distanceBetweenCities[i][j] = Math.sqrt(Math.pow(Cities.get(i).getX() - Cities.get(j).getX(), 2.0)+ Math.pow(Cities.get(i).getY() - Cities.get(j).getY(),2.0));

	}

	public static void printSol(int[] solution) {
		for (int i = 0; i < solution.length; i++) {
			System.out.print(solution[i] + " ");
		}
		System.out.print(solution[0]);
		System.out.println();
	}

	public static int[] getBestNeighbour(TabuList tabuList,
			TSPEnvironment tspEnviromnet, int[] initSolution) {

		int[] bestSol = new int[initSolution.length];
		System.arraycopy(initSolution, 0, bestSol, 0, bestSol.length);
		//amaç fonksiyonunun değeri yani yol maliyeti.
		int bestCost = tspEnviromnet.getObjectiveFunctionValue(initSolution);
		int city1 = 0;
		int city2 = 0;
		boolean firstNeighbor = true;

		for (int i = 1; i < bestSol.length - 1; i++) {
			for (int j = 2; j < bestSol.length - 1; j++) {

				int[] newBestSol = new int[bestSol.length];
				System.arraycopy(bestSol, 0, newBestSol, 0, newBestSol.length);

				newBestSol = swapOperator(i, j, initSolution); 

				int newBestCost = tspEnviromnet.getObjectiveFunctionValue(newBestSol);

				if ((newBestCost < bestCost || firstNeighbor) && tabuList.tabuList[i][j] == 0) { 
														
					firstNeighbor = false;
					city1 = i;
					city2 = j;
					System.arraycopy(newBestSol, 0, bestSol, 0,newBestSol.length);
					bestCost = newBestCost;
				}

			}
		}

		if (city1 != 0) {
			tabuList.decrementTabu(); //tabulist yer acılır
			tabuList.tabuMove(city1, city2); //tabuya eklenır
		}
		return bestSol;
	}

	public static int[] swapOperator(int city1, int city2, int[] solution) {
		int temp = solution[city1];
		solution[city1] = solution[city2];
		solution[city2] = temp;
		return solution;
	}

	/*********************************** MAIN *************************************************/

	public static void main(String[] args) {

		int numberOfIterations = 5000;
		int tabuLength;
		int x = 1, y = 1;

		ArrayList<City> Cities = new ArrayList<>();

		try {
			String Coordinates;
			FileReader fileReader = new FileReader(
					"../TSPProblem/src/dataset/att48"  + ".txt");
			BufferedReader br = new BufferedReader(fileReader);

			while ((Coordinates = br.readLine()) != "EOF") {
				/*
				 * String[] koordinatlar = (Coordinates.split("\\s+"));
				 * 
				 * System.out.println(koordinatlar[0]); City newCity = new
				 * City();
				 * 
				 * newCity.setX(Double.parseDouble(koordinatlar[x]));
				 * newCity.setY(Double.parseDouble(koordinatlar[y]));
				 * Cities.add(newCity);
				 */
				if (Coordinates.equals("NODE_COORD_SECTION")) {
					while ((Coordinates = br.readLine()) != "EOF") {

						String[] koordinatlar = (Coordinates.split("\\s+"));
						City newCity = new City();

						newCity.setX(Double.parseDouble(koordinatlar[x]));
						newCity.setY(Double.parseDouble(koordinatlar[y]));
						Cities.add(newCity);

					}
				}
				try {
					String[] tokens = Coordinates.split(":");
					String key = tokens[0].trim();
					String value = tokens[1].trim();
					if (key.equals("DIMENSION")) {

						dimension = Integer.parseInt(value);

					}
				} catch (NullPointerException e) {

				}
			}

		} catch ( Exception e) {
			
		}

		tabuLength = dimension;

		System.out.println("Dimension= " + tabuLength);
		getDistanceBetweenCities(Cities);
		tspEnvironment = new TSPEnvironment();
		tspEnvironment.distances = distanceBetweenCities;

		int[] currSolution = new int[Cities.size()];
		for (int i = 0; i < Cities.size(); i++) {
			currSolution[i] = i;
		}

		// greedyolmadı.

		printSol(currSolution); // baslangıc cozumu yazdırıldı

		TabuList tabuList = new TabuList(tabuLength);
		tabuList.dimensionLength = dimension;
		
		int[] bestSolution = new int[currSolution.length];
		// baslangıc cozumu en ıyı cozum olarak kabul edılır
		System.arraycopy(currSolution, 0, bestSolution, 0, bestSolution.length);

		// maliyet hesabı
		int bestCost = tspEnvironment.getObjectiveFunctionValue(bestSolution);
		System.out.println("Başlangıç maliyeti: " + bestCost);

		/***** TABU SEARCH ****/

		for (int i = 0; i < numberOfIterations; i++) {

			currSolution = getBestNeighbour(tabuList, tspEnvironment,currSolution);
			printSol(currSolution);
			int currCost = tspEnvironment.getObjectiveFunctionValue(currSolution);
			System.out.println("Suanki cozum maliyeti: " + currCost);

			if (currCost < bestCost) {
				System.arraycopy(currSolution, 0, bestSolution, 0,bestSolution.length);
				bestCost = currCost;
			}

		}
		System.out.println("Arama bitti! \nEn iyi çözüm maliyeti:   "+ bestCost + "\nBest Solution :");

		printSol(bestSolution);

	}

}
