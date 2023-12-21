public static void main (String[] args) {
		System.out.println("GfG!");
		int[] input = {4, 30, 50};
		solution(input);
	}

	public static int[] solution(int[] pegs) {
		int[] result = {-1, -1};
		Integer totalPegs = pegs.length;
		Map<Integer, AbstractMap.SimpleEntry<Integer, Integer>> gearSizeConstrains = new HashMap<>();
		for(int i = 0; i < totalPegs; i++) {
			gearSizeConstrains.put(i, new AbstractMap.SimpleEntry(-1 , -1));
		}
		for(int i = totalPegs - 1; i >= 0; i--) {
			try {
				addPeg(i, pegs, gearSizeConstrains);
			} catch (IllegalArgumentException iae) {
				
				return result;
			}
		}
		for(int i = 0; i < totalPegs; i++) {
			System.out.println(i + " " + gearSizeConstrains.get(i).getKey() + " " + gearSizeConstrains.get(i).getValue());
		}
		int[] firstGearConstrains = {gearSizeConstrains.get(0).getKey(), gearSizeConstrains.get(0).getValue()};
		int[] lastGearConstrains = {gearSizeConstrains.get(totalPegs - 1).getKey(), gearSizeConstrains.get(totalPegs - 1).getValue()};
		if(lastGearConstrains[1] * 2 < firstGearConstrains[0]) {
			return result;
		}
		if(lastGearConstrains[0] * 2 > firstGearConstrains[1]) {
			return result;
		}
		int lower = lastGearConstrains[0] * 2;
		int upper = lastGearConstrains[1] * 2;
		if(lower < firstGearConstrains[0]) {
			lower = firstGearConstrains[0];
		}
		if(upper > firstGearConstrains[1]) {
			upper = firstGearConstrains[1];
		}
		System.out.println("lower: " + lower + " " + "upper: " + upper);
		result[0] = lower;
		result[1] = 1;
		return result;
	}

	public static void addPeg(int pegIndex, int[] pegs, Map<Integer, AbstractMap.SimpleEntry<Integer, Integer>> gearSizeConstrains) {
		System.out.println("addPeg: " + pegIndex);
		// max calculation
		int maxRadius, minRadius;
		// right side max
		int rightAllowedMax, leftAllowedMax;
		if(pegIndex == pegs.length - 1) {
			rightAllowedMax = Integer.MAX_VALUE;
		} else {
			System.out.println("nextGearConstrain: " + gearSizeConstrains.get(pegIndex + 1).getKey() + " " + gearSizeConstrains.get(pegIndex + 1).getValue());
			int rightGearMinSize = gearSizeConstrains.get(pegIndex + 1).getKey();
			rightAllowedMax =  (pegs[pegIndex + 1] - pegs[pegIndex]) - rightGearMinSize;
			// handle less than 1
		}
		// left side max
		if(pegIndex == 0) {
			leftAllowedMax = Integer.MAX_VALUE;
		} else {
			leftAllowedMax = (pegs[pegIndex] - pegs[pegIndex - 1]) - 1;
			// handle less than 1
		}
		if(rightAllowedMax < leftAllowedMax) {
			maxRadius = rightAllowedMax;
		} else {
			maxRadius = leftAllowedMax;
			if(pegIndex < pegs.length - 1) {
				int setMinTo = (pegs[pegIndex + 1] - pegs[pegIndex]) - maxRadius;
				adjustMin(pegIndex + 1, pegs, gearSizeConstrains, setMinTo);
			}
		}
		if(maxRadius < 1) {
			throw new IllegalArgumentException();
		}
		// min calculation
		int rightAllowedMin, leftAllowedMin;
		// right side min
		if(pegIndex == pegs.length - 1) {
			rightAllowedMin = 1;
		} else {
			int rightGearMaxSize = gearSizeConstrains.get(pegIndex + 1).getValue();
			rightAllowedMin = (pegs[pegIndex + 1] - pegs[pegIndex]) - rightGearMaxSize;
			// handle less than 1
		}
		leftAllowedMin = 1;
		System.out.println("min calculations: " + rightAllowedMin + " " + leftAllowedMin);
		if(rightAllowedMin > leftAllowedMin) {
			minRadius = rightAllowedMin;
		} else {
			minRadius = leftAllowedMin;
		}
		if(minRadius < 1) {
			throw new IllegalArgumentException();
		}
		System.out.println("calculations: " + minRadius + " " + maxRadius);
		if(maxRadius < minRadius) {
			throw new IllegalArgumentException();
		}
		gearSizeConstrains.put(pegIndex, new AbstractMap.SimpleEntry<>(minRadius, maxRadius));
	}

	public static void adjustMin(int pegIndex, int[] pegs, Map<Integer, AbstractMap.SimpleEntry<Integer, Integer>> gearSizeConstrains, int setMinTo) {
		int currentMinRadius = gearSizeConstrains.get(pegIndex).getKey();
		int currentMaxRadius = gearSizeConstrains.get(pegIndex).getValue();
		if(setMinTo < 1) {
			throw new IllegalArgumentException();
		}
		if(setMinTo > currentMaxRadius) {
			throw new IllegalArgumentException();
		} else {
			gearSizeConstrains.put(pegIndex, new AbstractMap.SimpleEntry(setMinTo, currentMaxRadius));
		}
		if(pegIndex < pegs.length - 1) {
			int setMaxTo = (pegs[pegIndex + 1] - pegs[pegIndex]) - setMinTo;
			adjustMax(pegIndex + 1, pegs, gearSizeConstrains, setMaxTo);
		}
	}

	public static void adjustMax(int pegIndex, int[] pegs, Map<Integer, AbstractMap.SimpleEntry<Integer, Integer>> gearSizeConstrains, int setMaxTo) {
		int currentMinRadius = gearSizeConstrains.get(pegIndex).getKey();
		int currentMaxRadius = gearSizeConstrains.get(pegIndex).getValue();
		if(setMaxTo < 1) {
			throw new IllegalArgumentException();
		}
		if(setMaxTo < currentMinRadius) {
			throw new IllegalArgumentException();
		} else {
			gearSizeConstrains.put(pegIndex, new AbstractMap.SimpleEntry(currentMinRadius, setMaxTo));
		}
		if(pegIndex < pegs.length - 1) {
			int setMinTo = (pegs[pegIndex + 1] - pegs[pegIndex]) - setMaxTo;
			adjustMin(pegIndex + 1, pegs, gearSizeConstrains, setMinTo);
		}
	}
