package pa3.project.datastructures;

import pa3.project.Professor;

public class CQueue {
    // ATTRIBUTES
    protected Professor[] items;
    protected int head, tail, size;

    // CONSTRUCTOR
    public CQueue(int size) {
        this.size = size;
        this.items = new Professor[size];
        this.head = -1;
        this.tail = -1;
    }

    // METHODS
    public boolean isFull() {
        return head == 0 && tail == size - 1;
    }
    public boolean isEmpty() {
        return head == -1 && tail == -1;
    }
    public int moveToFront(){
        int nextIndex = 0;
        for(int i = head; i <= tail; i++, nextIndex++){
            items[i - head] = items[i];
        }
        head = 0;
        tail = nextIndex - 1;
        return nextIndex;
    }
    public Professor enqueue(Professor element){
        if (isFull()) {
            System.out.println("Queue is full");
            return null;
        }
        if (isEmpty()) {
            head = 0;
            tail = 0;
        } else if (tail == (size - 1)) {
            tail = moveToFront();
        } else {
            tail++;
        }
        items[tail] = element;
        System.out.println("Enqueued " + element);
        return element;
    }
    public Professor dequeue(){
        if (isEmpty()){
            System.out.println("Queue is empty");
            return null;
        }
        Professor element = items[head];
        if (head == tail){
            head = -1;
            tail = -1;
        } else {
            head++;
        }
        System.out.println("Dequeued " + element);
        return element;
    }
}