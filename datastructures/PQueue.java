package pa3.project.datastructures;

import pa3.project.Professor;

public class PQueue extends CQueue {
    // CONSTRUCTOR
    public PQueue(int size) {
        super(size);
    }

    // METHODS
    /**
     * Adds an element to the back of the queue.
     *
     * If the queue is full, returns null and does not modify the queue.
     * If the queue is empty, initializes the queue with the given element.
     * Otherwise, inserts the element at the appropriate position based on the
     * natural ordering of elements (determined by the compareTo method).
     * If the tail corresponds to the end of the array, the function moveToFront
     * is called to move all the elements to the beginning.
     *
     * @param element the element to add to the queue.
     * @return the element that was added to the queue, or null if the queue is full.
     */
    @Override
    public Professor enqueue(Professor element){
        if (isFull())
            return null;
        if (isEmpty()) {
            head = 0;
            tail = 0;
            items[tail] = element;
        } else {
            if (tail == (size - 1))
                moveToFront();
            for(int i = tail; i >= head; i--){
                if (element.compareTo(items[i]) > 0) {
                    items[i + 1] = items[i];
                    if (i == head){
                        items[i] = element;
                        tail++;
                        break;
                    }
                } else {
                    items[i + 1] = element;
                    tail++;
                    break;
                }
            }
        }
        return element;
    }

    /**
     * Given an id, search and print the element
     * @param id the id of the element to search for
     */
    public void displayElement(int id){
        for(int i = head; i <= tail; i++){
            if (((Professor)items[i]).getId() == id){
                System.out.println(items[i]);
                return;
            }
        }
    }

    /**
     * Display all elements higher than given prof’s seniority
     * @param element the id of the element to search for
     */
    public void displayHigherPriorityElements(Professor element){
        for(int i = head; i <= tail; i++){
            if (element.compareTo(items[i]) == -1)
                System.out.println(items[i]);
        }
    }

    /**
     * Display all elements lower than given prof’s seniority
     * @param element the element to add to the queue.
     */
    public void displayLowerPriorityElements (Professor element){
        for(int i = head; i <= tail; i++){
            if (element.compareTo(items[i]) == 1)
                System.out.println(items[i]);
        }
    }
}
