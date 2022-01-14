public class BubbleSortExample {

    public static void BubbleSort(int[] a)
    {
        for(int i =0;i<a.length-1;i++)
        {
            for(int j = i+1; j<a.length;j++)
            {
                if(a[i]>a[j])
                {
                    a[i] = a[i]+a[j];
                    a[j] = a[i] - a[j];
                    a[i] = a[i] - a[j];
                }
            }

        }
    }


}
