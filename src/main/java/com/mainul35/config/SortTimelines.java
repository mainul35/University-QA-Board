package com.mainul35.config;

import org.springframework.stereotype.Component;

import com.mainul35.customModel.Timeline;

@Component
public class SortTimelines {

	public int SORT_TYPE;
	public static final int SORT_BY_TOTAL_THUMB_UP = 1;
	public static final int SORT_BY_TOTAL_THUMB_DOWN = 2;
	public static final int SORT_BY_TOTAL_VIEWS = 3;
	public static final int SORT_BY_TOTAL_SEEN_BY = 4;
	public static final int SORT_BY_TOTAL_COMMENTS = 5;

	public Timeline[] sort(Timeline[] values) {
		// check for empty or null array
		if (values == null || values.length == 0) {
			return values;
		}
		int number = values.length;
		quicksort(0, number - 1, values);
		return values;
	}

	private void quicksort(int low, int high, Timeline[] timelines) {
		if(SORT_TYPE==SORT_BY_TOTAL_THUMB_UP) {
			sortByThumbUps(low, high, timelines);
		}else if(SORT_TYPE==SORT_BY_TOTAL_THUMB_DOWN) {
			sortByThumbDowns(low, high, timelines);
		}else if(SORT_TYPE==SORT_BY_TOTAL_COMMENTS) {
			sortByMostComments(low, high, timelines);
		}else if(SORT_TYPE==SORT_BY_TOTAL_SEEN_BY) {
			sortByMostSeenBy(low, high, timelines);
		}else if(SORT_TYPE==SORT_BY_TOTAL_VIEWS) {
			sortByMostViews(low, high,timelines);
		}
	}

	
	private void sortByThumbUps(int low, int high, Timeline[] timelines) {
		int i = low, j = high;
		// Get the pivot element from the middle of the list
		Timeline pivot = timelines[low + (high - low) / 2];

		// Divide into two lists
		while (i <= j) {
			// If the current value from the left list is smaller than the pivot
			// element then get the next element from the left list
			while (timelines[i].getTotalThumbUp() < pivot.getTotalThumbUp()) {
				i++;
			}
			// If the current value from the right list is larger than the pivot
			// element then get the next element from the right list
			while (timelines[j].getTotalThumbUp() > pivot.getTotalThumbUp()) {
				j--;
			}

			// If we have found a value in the left list which is larger than
			// the pivot element and if we have found a value in the right list
			// which is smaller than the pivot element then we exchange the
			// values.
			// As we are done we can increase i and j
			if (i <= j) {
				exchange(i, j,timelines);
				i++;
				j--;
			}

		}
		// Recursion
		if (low < j)
			quicksort(low, j, timelines);
		if (i < high)
			quicksort(i, high, timelines);

	}

	private void sortByThumbDowns(int low, int high, Timeline[] timelines) {
		int i = low, j = high;
		// Get the pivot element from the middle of the list
		Timeline pivot = timelines[low + (high - low) / 2];

		// Divide into two lists
		while (i <= j) {
			// If the current value from the left list is smaller than the pivot
			// element then get the next element from the left list
			while (timelines[i].getTotalThumbDown() < pivot.getTotalThumbDown()) {
				i++;
			}
			// If the current value from the right list is larger than the pivot
			// element then get the next element from the right list
			while (timelines[j].getTotalThumbDown() > pivot.getTotalThumbDown()) {
				j--;
			}

			// If we have found a value in the left list which is larger than
			// the pivot element and if we have found a value in the right list
			// which is smaller than the pivot element then we exchange the
			// values.
			// As we are done we can increase i and j
			if (i <= j) {
				exchange(i, j,timelines);
				i++;
				j--;
			}

		}
		// Recursion
		if (low < j)
			quicksort(low, j,timelines);
		if (i < high)
			quicksort(i, high,timelines);

	}

	private void sortByMostComments(int low, int high, Timeline[] timelines) {
		int i = low, j = high;
		// Get the pivot element from the middle of the list
		Timeline pivot = timelines[low + (high - low) / 2];

		// Divide into two lists
		while (i <= j) {
			// If the current value from the left list is smaller than the pivot
			// element then get the next element from the left list
			while (timelines[i].getIdea().getComments().size() < pivot.getIdea().getComments().size()) {
				i++;
			}
			// If the current value from the right list is larger than the pivot
			// element then get the next element from the right list
			while (timelines[j].getIdea().getComments().size() > pivot.getIdea().getComments().size()) {
				j--;
			}

			// If we have found a value in the left list which is larger than
			// the pivot element and if we have found a value in the right list
			// which is smaller than the pivot element then we exchange the
			// values.
			// As we are done we can increase i and j
			if (i <= j) {
				exchange(i, j,timelines);
				i++;
				j--;
			}

		}
		// Recursion
		if (low < j)
			quicksort(low, j,timelines);
		if (i < high)
			quicksort(i, high,timelines);

	}

	private void sortByMostViews(int low, int high, Timeline[] timelines) {
		int i = low, j = high;
		// Get the pivot element from the middle of the list
		Timeline pivot = timelines[low + (high - low) / 2];

		// Divide into two lists
		while (i <= j) {
			// If the current value from the left list is smaller than the pivot
			// element then get the next element from the left list
			while (timelines[i].getIdea().getCountViews() < pivot.getIdea().getCountViews()) {
				i++;
			}
			// If the current value from the right list is larger than the pivot
			// element then get the next element from the right list
			while (timelines[j].getIdea().getCountViews() > pivot.getIdea().getCountViews()) {
				j--;
			}

			// If we have found a value in the left list which is larger than
			// the pivot element and if we have found a value in the right list
			// which is smaller than the pivot element then we exchange the
			// values.
			// As we are done we can increase i and j
			if (i <= j) {
				exchange(i, j,timelines);
				i++;
				j--;
			}

		}
		// Recursion
		if (low < j)
			quicksort(low, j,timelines);
		if (i < high)
			quicksort(i, high,timelines);

	}

	private void sortByMostSeenBy(int low, int high, Timeline[] timelines) {
		int i = low, j = high;
		// Get the pivot element from the middle of the list
		Timeline pivot = timelines[low + (high - low) / 2];

		// Divide into two lists
		while (i <= j) {
			// If the current value from the left list is smaller than the pivot
			// element then get the next element from the left list
			while (timelines[i].getTotalSeenBy() < pivot.getTotalSeenBy()) {
				i++;
			}
			// If the current value from the right list is larger than the pivot
			// element then get the next element from the right list
			while (timelines[j].getTotalSeenBy() > pivot.getTotalSeenBy()) {
				j--;
			}

			// If we have found a value in the left list which is larger than
			// the pivot element and if we have found a value in the right list
			// which is smaller than the pivot element then we exchange the
			// values.
			// As we are done we can increase i and j
			if (i <= j) {
				exchange(i, j, timelines);
				i++;
				j--;
			}

		}
		// Recursion
		if (low < j)
			quicksort(low, j,timelines);
		if (i < high)
			quicksort(i, high,timelines);

	}

	
	private void exchange(int i, int j, Timeline[] timelines) {
		Timeline temp = timelines[i];
		timelines[i] = timelines[j];
		timelines[j] = temp;
	}
}
