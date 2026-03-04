package com.atguigu.gmall.Impl.perfImpl;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-9-4
 */
public class sort {

    public static void main(String args[]){

        int [] b = {1,3,5,6,7,4,3,0,};
        int t = 0 ;

        for(int i = 0 ; i<b.length-1 ; i++){

            for(int j = 0 ; j<b.length-1-i;j++){

                if(b [j]>b[j+1]){

                    t = b [j] ;
                    b[j] = b[j+1] ;
                    b[j+1] = t ;
                }


            }


        }

        for(int x = 0; x<b.length;x++){
            System.out.println(""+b[x]);
        }
    }
}
