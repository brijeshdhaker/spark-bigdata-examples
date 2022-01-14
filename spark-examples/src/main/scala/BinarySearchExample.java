public class BinarySearchExample {

    private void BinSearchWithoutRecursion(int[] array,int key,int start,int end){
        int mid;
        boolean isFound = false;
        while(!isFound)
        {
            mid = (start+end)/2;
            if(array[mid]==key || array[start] == key || array[end] == key)
            {
                isFound= true;
            }
            else if(array[mid]>key)
            {
                start = mid+1;
                end = end-1;
            }
            else if(array[mid]<key)
            {
                start = start+1;
                end = mid-1;
            }
        }
    }

    private void BinSearch(int[] array,int key,int start,int end){
        //count++;
        int mid = (start+end)/2;
        boolean isfound = false;
        if(array[mid] == key)
        {
            isfound = true;
        }
        else if(array[mid]<key)
        {
            start = mid+1;
            BinSearch(array, key, start, end);
        }
        else if(array[mid]>key)
        {
            end = mid-1;
            BinSearch(array, key, start, end);
        }
        if(isfound)
            System.out.println("Found");
    }
}
