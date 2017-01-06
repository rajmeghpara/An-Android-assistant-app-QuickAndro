
package com.rarity.apps.quickandro.Modules;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;

public class Contacts {

    private Context context;
    private ArrayList<String> names, phoneNumbers;
    ArrayList<String> finallist = new ArrayList<String>();
    ArrayList<String> numberlist = new ArrayList<String>();

    public Contacts(Context context) {
        this.context = context;
        names = new ArrayList<String>();
        phoneNumbers = new ArrayList<String>();
        storeContactsData();
    }

    private void storeContactsData() {

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cur.getCount() > 0) {

            while (cur.moveToNext()) {

                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)).toLowerCase();

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        names.add(name);
                        phoneNumbers.add(phoneNo);
                    }
                    pCur.close();
                }
            }
        }
    }

    public String findNumber(String nameToFind) {


        int l = 0;

        for(int i = 0;i<names.size();i++){
            if(nameToFind.equalsIgnoreCase(names.get(i))){
                return phoneNumbers.get(i);
            }
        }

        int[] sizeArray = new int[names.size()];
        char[] nameArray = nameToFind.toCharArray();
        nameToFind.toLowerCase();

        int maxCommanLength = sizeArray[0];

        for (int i = 0; i < names.size(); i++) {
            names.get(i).toLowerCase();
            char[] tempArray = names.get(i).toCharArray();

            sizeArray[i] = find(tempArray,nameArray);
            maxCommanLength = Math.max(maxCommanLength,sizeArray[i]);
        }

        for(int i = 0;i<sizeArray.length;i++){
            if(maxCommanLength == sizeArray[i]){
                if(!finallist.contains(names.get(i))) {
                    finallist.add(names.get(i));
                    numberlist.add(phoneNumbers.get(i));
                }
            }
        }

        if(finallist.size() == 1){
            return numberlist.get(0);
        }else{
            for(int i = 0;i<finallist.size();i++){
                if(nameToFind.charAt(0) != finallist.get(i).charAt(0)){
                    finallist.remove(i--);
                }
            }

        }

        return null;
    }


    public int find(char [] A, char [] B){
        int [][]LCS = new int [A.length+1][B.length+1];
        // if A is null then LCS of A, B =0
        for(int i=0;i<=B.length;i++){
            LCS[0][i]=0;
        }

        // if B is null then LCS of A, B =0
        for(int i=0;i<=A.length;i++){
            LCS[i][0]=0;
        }

        //fill the rest of the matrix
        for(int i=1;i<=A.length;i++){
            for(int j=1;j<=B.length;j++){
                if(A[i-1]==B[j-1]){
                    LCS[i][j] =  LCS[i-1][j-1] +1;
                }else{
                    LCS[i][j] = 0;
                }
            }
        }
        int result = -1;
        for(int i=0;i<=A.length;i++){
            for(int j=0;j<=B.length;j++){
                if(result<LCS[i][j]){
                    result = LCS[i][j];
                }
            }
        }
        return result;
    }

    public ArrayList getFinallist(){
        return finallist;
    }

    public ArrayList getnumberlist(){
        return numberlist;
    }


}
