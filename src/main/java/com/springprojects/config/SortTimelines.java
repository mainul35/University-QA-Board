package com.springprojects.config;

import org.springframework.stereotype.Component;

import com.springprojects.customModel.Timeline;

@Component
public class SortTimelines {
	private Timeline[] timelines = new Timeline[0];
	private int number;
	public int SORT_TYPE;
	public final int SORT_BY_TOTAL_THUMB_UP = 1;
	public final int SORT_BY_TOTAL_THUMB_DOWN = 2;
	public final int SORT_BY_TOTAL_VIEWS = 3;
	public final int SORT_BY_TOTAL_SEEN_BY = 4;
	public final int SORT_BY_TOTAL_COMMENTS = 5;

	public Timeline[] sort(Timeline[] values) {
		// check for empty or null array
		if (values == null || values.length == 0) {
			return values;
		}
		this.timelines = values;
		number = values.length;
		quicksort(0, number - 1);
		return timelines;
	}

	private void quicksort(int low, int high) {
		if(SORT_TYPE==SORT_BY_TOTAL_THUMB_UP) {
			sortByThumbUps(low, high);
		}else if(SORT_TYPE==SORT_BY_TOTAL_THUMB_DOWN) {
			sortByThumbDowns(low, high);
		}else if(SORT_TYPE==SORT_BY_TOTAL_COMMENTS) {
			sortByMostComments(low, high);
		}else if(SORT_TYPE==SORT_BY_TOTAL_SEEN_BY) {
			sortByMostSeenBy(low, high);
		}else if(SORT_TYPE==SORT_BY_TOTAL_VIEWS) {
			sortByMostViews(low, high);
		}
	}

	
	private void sortByThumbUps(int low, int high) {
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
				exchange(i, j);
				i++;
				j--;
			}

		}
		// Recursion
		if (low < j)
			quicksort(low, j);
		if (i < high)
			quicksort(i, high);

	}

	private void sortByThumbDowns(int low, int high) {
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
				exchange(i, j);
				i++;
				j--;
			}

		}
		// Recursion
		if (low < j)
			quicksort(low, j);
		if (i < high)
			quicksort(i, high);

	}

	private void sortByMostComments(int low, int high) {
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
				exchange(i, j);
				i++;
				j--;
			}

		}
		// Recursion
		if (low < j)
			quicksort(low, j);
		if (i < high)
			quicksort(i, high);

	}

	private void sortByMostViews(int low, int high) {
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
				exchange(i, j);
				i++;
				j--;
			}

		}
		// Recursion
		if (low < j)
			quicksort(low, j);
		if (i < high)
			quicksort(i, high);

	}

	private void sortByMostSeenBy(int low, int high) {
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
				exchange(i, j);
				i++;
				j--;
			}

		}
		// Recursion
		if (low < j)
			quicksort(low, j);
		if (i < high)
			quicksort(i, high);

	}

	
	private void exchange(int i, int j) {
		Timeline temp = timelines[i];
		timelines[i] = timelines[j];
		timelines[j] = temp;
	}
}
