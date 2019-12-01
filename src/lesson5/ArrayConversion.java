package lesson5;


public class ArrayConversion {

    static final int size = 10000000;
    float[] arr = new float[size];
    int numberOfThreads;

    ArrayConversion(int numberOfThreads){
        this.numberOfThreads = numberOfThreads;
        for (int i = 0; i < size ; i++) {
            arr[i] = 1;
        }
    }


    public void arrayChangeInOneThread(){

        long a = System.currentTimeMillis();

        for (int i = 0; i < size ; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));

        }

        System.out.println("Время преобразования массива одним потоком " + (System.currentTimeMillis() - a));
    }

    public void arrayChangeByMultipleThread(){
        int h = size/numberOfThreads;
        float a1[] = new float[h];
        float a2[] = new float[h];

        long a = System.currentTimeMillis();

        System.arraycopy(arr, 0, a1, 0, h);
        System.arraycopy(arr,h,a2,0,h);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < h ; i++) {
                    a1[i] = (float) (a1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < h ; i++) {
                    a2[i] = (float) (a2[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
            }
        });

        t1.start();
        t2.start();

        try{
            t1.join();
            t2.join();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }


        System.arraycopy(a1, 0, arr, 0, h);
        System.arraycopy(a2, 0, arr, h, h);

        System.out.println("Время преобразования массива двумя потоками " + (System.currentTimeMillis() - a));


    }
}
