package simpleThreadPool;

class SimpleTesting implements ISimpleTask{
    private int i;
    public SimpleTesting(int i){
        this.i = i;
    }
    @Override
    public void run() {
        System.out.println("Task number: " + i);
    }
    public static void main(String args[]){
        // Initialize thread pool
        SimpleThreadPool pool = new SimpleThreadPool();
        pool.start();
        // Create 20 tasks
        //
        // output is in random order due to the processing time available to
        // the thread scheduler of the JVM and the underlaying operating system
        for(int i = 1; i<=20; i++){
            pool.addTask(new SimpleTesting(i));
        }
        pool.stop();
    }
}

