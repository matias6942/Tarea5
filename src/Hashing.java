// 1 10 2 3 12 -3
// 1 10 15 15 25 35 -35 45 -8
// 1 10 1 2 3 4 -3 22

// 2 10 9 10 30 40 2 79



///////////     LAS TILDES SE HAN OMITIDO POR COMPATIBILIDAD    //////////////

import java.util.Scanner;

public class Hashing {

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String StrInput;
        String[] StrInputList;

        int Value;                          // Valor a insertar
        int index;                          // Valor retornado por la funcion de hashing


        while (in.hasNextLine()) {
            StrInput = in.nextLine();
            StrInputList = StrInput.split(" ");

            // Construimos la tabla de hashing incialmente vacia
            int m = Integer.parseInt(StrInputList[1]);
            String[] HashingTable = new String[m];

            for (int i = 0; i < m; i++){
                HashingTable[i] = "0";
            }

            // Transformamos el resto del input a int[]
            int[] InputIntList = new int[StrInputList.length-2];

            for (int i = 2; i < StrInputList.length; i++){
                int j = i-2;
                InputIntList[j] = Integer.parseInt(StrInputList[i]);
                Value = InputIntList[j];

                // Tecnica de Hashing a utilizar para el direccionamiento abierto

                // Linear Probing
                if (StrInputList[0].equals("1")){

                    if (Value > 0){

                        index = Value % m;

                        if (Retrieval(Value, index, m, HashingTable) != -1){
                            System.out.println("ERROR: valor ya existe");
                            //System.out.println("");
                        }

                        else {
                            HashingTable[Insert_Linear_Probing(Value, index, m, HashingTable)] = String.valueOf(Value);
                            System.out.println(StrList_To_Str(HashingTable));
                            System.out.println("");
                        }
                    }

                    else {

                        Value = Math.abs(Value);
                        index = Value % m;

                        if (Retrieval(Value, index, m, HashingTable) == -1){
                            System.out.println("ERROR: valor no existe");
                            //System.out.println("");
                        }

                        else {
                            HashingTable[Retrieval(Value, index, m, HashingTable)] = "X";
                            System.out.println(StrList_To_Str(HashingTable));
                            System.out.println("");
                        }
                    }
                }

                // Robin Hood Hashing
                else if (StrInputList[0].equals("2")){

                    if (Value > 0){
                        index = Value % m;

                        if (Retrieval(Value, index, m, HashingTable) != -1){
                            System.out.println("ERROR: valor ya existe");
                            //System.out.println("");
                        }

                        else {
                            HashingTable = Insert_Robin_Hood(Value, index, m, HashingTable);
                            System.out.println(StrList_To_Str(HashingTable));
                            //System.out.println("");
                        }
                    }

                    Value = Math.abs(Value);
                    index = Value % m;

                    if (Retrieval(Value, index, m, HashingTable) == -1){
                        System.out.println("ERROR: valor no existe");
                        //System.out.println("");
                    }

                    else {
                        HashingTable[Retrieval(Value, index, m, HashingTable)] = "X";
                        System.out.println(StrList_To_Str(HashingTable));
                        //System.out.println("");
                    }
                }
            }
        }
    }

    /*
    *   El metodo Retrieval indica si Value existe (retorna su posicion) en una tabla de hashing que
    *   contiene m elementos y que es revisada a partir del indice retornado por la primera funcion
    *   de hashing index. Si no existe retorna -1.
    *
     */

    public static int Retrieval(int Value, int index, int m, String[] HashingTable) {

        for (int k = index; k < m; k++) {

            if (HashingTable[k].equals(String.valueOf(Value))) {
                return k;

            } else if (k == m) {

                for (int l = 0; l < index; l++) {
                    if (HashingTable[l].equals(String.valueOf(Value))) {
                        return l;
                    }
                }
            }
        }
        return -1;
    }

    /*
    *   El metodo Insert_Linear_Probing retorna la posicion final en la cual ha quedado Value en
    *   una HashingTable de tamanio m. Primero se verifica que el casillero este disponible
    *   para insertar, es decir, que tenga 0 o X. Si esta ocupado se aumenta el index en 1 y
    *   se intenta insertar nuevamente.
    *
     */

    public static int Insert_Linear_Probing(int Value, int index, int m, String[] HashingTable){

        if (HashingTable[index].equals("0") || HashingTable[index].equals("X")){
            return index;
        }

        else {

            index++;

            if (index == m){
                for (int l = 0; l < index; l++){
                    if (HashingTable[l].equals("0") || HashingTable[l].equals("X")){
                        return l;
                    }
                }
            }
            return Insert_Linear_Probing(Value, index, m, HashingTable);
            }
    }

    public static String[] Insert_Robin_Hood(int NewValue, int index, int m, String[] HashingTable) {

        if (HashingTable[index].equals("0") || HashingTable[index].equals("X")){
            HashingTable[index] = String.valueOf(NewValue);
            return HashingTable;
        }

        else {

            int CurrentValue = Integer.parseInt(HashingTable[index]);

            if (DIB(CurrentValue, index, m, HashingTable) < DIB(NewValue, index, m, HashingTable) || DIB(CurrentValue, index, m, HashingTable) == DIB(NewValue, index, m, HashingTable)){
                HashingTable[index] = String.valueOf(NewValue);
                index++;

                if (index == m){
                    index = 0;
                }

                return Insert_Robin_Hood(CurrentValue, index, m, HashingTable);
                //return index;
            }

            else {
                index++;

                if (index == m){
                    index = 0;
                }

                return Insert_Robin_Hood(NewValue, index, m, HashingTable);
            }
        }
    }

    /*
    *   El metodo DIB retorna la distancia al casillero inicial considerando un arreglo
    *   circular.
    *
     */

    public static int DIB(int Value, int index, int m, String[] HashingTable){

        int InitialBucket = Value % m;
        //int FinalBucket = Retrieval(Value, index, m, HashingTable);
        int FinalBucket = index;
        int DIB;

            if (InitialBucket < FinalBucket) {
                DIB = FinalBucket - InitialBucket;
                return DIB;
            }
            else {

                DIB = 0;

                int k = InitialBucket;

                while (k != FinalBucket) {

                    if (k == m - 1) {
                        k = 0;
                        DIB++;
                    } else {
                        k++;
                        DIB++;
                    }
                }
                return DIB;
            }
    }

    public static String StrList_To_Str(String[] StrList)
    {
        int Dimension = StrList.length;

        String StrOutput = "";

        for (int index = 0; index < Dimension; index++)
        {
            if (index == Dimension - 1)
            {
                StrOutput += String.valueOf(StrList[index]);
            }

            else
            {
                StrOutput += String.valueOf(StrList[index]) + " ";
            }
        }
        return StrOutput;
    }
}
