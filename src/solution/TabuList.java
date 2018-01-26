package solution;

public class TabuList {
	int[][] tabuList;
	int dimensionLength;
	
	public TabuList(int numberOfCities) {
		tabuList = new int[numberOfCities][numberOfCities];

	}

	public void tabuMove(int city1, int city2) {
		tabuList[city1][city2] += dimensionLength/4;
		tabuList[city2][city1] += dimensionLength/4;
	}

	public void decrementTabu() {
		for (int i = 0; i < tabuList.length; i++) {
			for (int j = 0; j < tabuList.length; j++) {

				if (tabuList[i][j] > 0) {
					tabuList[i][j] -= 1;
				}
				// tabuList[i][j]-=tabuList[i][j];
				// tabuList[i][j]-=tabuList[i][j]<=0?0:1;
			}
		}
	}

}
