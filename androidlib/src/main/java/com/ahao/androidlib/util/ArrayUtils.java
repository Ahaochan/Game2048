package com.ahao.androidlib.util;

/**
 * Created by Avalon on 2016/8/11.
 */
public class ArrayUtils {
    private static final String TAG = "ArrayUtils";
    private ArrayUtils(){}

    public static void log(int[] arr, int col){
        if(arr==null || arr.length<=0){
            throw new ArrayIndexOutOfBoundsException("arr must be have elements");
        }
        for(int i = 0; i < arr.length; i++){
            System.out.print(arr[i]+" ");
            if(i%col==0){
                System.out.println("");
            }
        }
    }

    /** array在(pos,array.length)中最后一个非other的数的index位置 */
    public static int lastOtherNum(int[]arr, int pos, int other){
        if(arr==null || arr.length<=0){
            throw new ArrayIndexOutOfBoundsException("arr must be have elements");
        }
        if(pos>=arr.length){
            throw new ArrayIndexOutOfBoundsException("pos must be smaller than arr.length");
        }
        int index = -1;
        for(int i = pos+1; i<arr.length; i++){
            if(arr[i]!=other){
                index =  i;
            }
        }
        return index;
    }
}
