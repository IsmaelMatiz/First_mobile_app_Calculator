package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String str_operation;
    boolean allow_point;
    boolean resultGiven;
    TextView result ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        str_operation = "";
        allow_point = true;
        resultGiven = false;
        result = (TextView) findViewById(R.id.result);

    }

    public void numPressed(View view){
        if (resultGiven) delete();
        resultGiven = false;
        Button b = (Button)view;
        String number_selected = b.getText().toString();
        str_operation += number_selected;
        result.setText(str_operation);
        result.setVisibility(View.VISIBLE);

    }

    public void pointButton(View view){
        if (resultGiven) delete();
        resultGiven = false;
        if (allow_point) {
            str_operation += ".";
            result.setText(str_operation);
            result.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(MainActivity.this, "No puedes ponerle dos puntos a un numero", Toast.LENGTH_LONG).show();
        }
        allow_point = false;
    }

    public void operationButton(View view){
        resultGiven = false;
        if (str_operation != "") {
            if(str_operation.charAt(str_operation.length() - 1) == '+' || str_operation.charAt(str_operation.length() - 1) == '-' || str_operation.charAt(str_operation.length() - 1) == 'x' || str_operation.charAt(str_operation.length() - 1) == '/') {
                Toast.makeText(MainActivity.this, "Agrega otro numero antes de hacer una operacion ", Toast.LENGTH_SHORT).show();
            }else{
                allow_point = true;
                Button b = (Button) view;
                String number_selected = b.getText().toString();
                str_operation += number_selected;
                result.setText(str_operation);
            }
        }else{
            Toast.makeText(MainActivity.this, "Escribe un numero antes de hacer una operacion", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteButton(View view){
        str_operation = "";
        allow_point = true;
        result.setText(str_operation);
    }

    public  void buttonResult(View view){
        ArrayList<String> separate = separateOperation(str_operation);
        float resul = pemdas(separate);
        str_operation = String.valueOf(resul);
        result.setText(str_operation);
        resultGiven = true;
    }

    private void delete(){
        str_operation = "";
        allow_point = true;
        result.setText(str_operation);
    }

    private ArrayList<String> separateOperation(String op){
        String  numToAdd = "";
        ArrayList<String> operation = new ArrayList<String>();
        for(int i = 0; i < op.length(); i++){
            if(op.charAt(i) == '+' || op.charAt(i) == '-'|| op.charAt(i) == 'x' || op.charAt(i) == '/'){
                operation.add(numToAdd);
                operation.add(Character.toString(op.charAt(i)));
                numToAdd = "";
            }else{
                numToAdd += op.charAt(i);
                if (i == op.length() - 1){
                    operation.add(numToAdd);
                }
            }
        }
        return operation;
    }

    private float pemdas(ArrayList<String> operation){
        float result;
        ArrayList<String> tempOperation = new ArrayList<String>();
        //first i need to look for the multiplications or divisions
        int i = 0;
        while(operation.contains("x") || operation.contains("/")){
            String thisOperation = "";
            if(operation.get(i).equals("x") || operation.get(i).equals("/")){
                //now i need to make the operation with the back and forward number
                if (operation.get(i).equals("x")){
                    thisOperation = String.valueOf(Float.parseFloat(operation.get(i - 1)) * Float.parseFloat(operation.get(i + 1)));
                }else{
                    thisOperation = String.valueOf(Float.parseFloat(operation.get(i - 1)) / Float.parseFloat(operation.get(i + 1)));
                }
                //now i need to make another array for the result
                tempOperation.clear();
                for(int x = 0;x < operation.size();x++){
                    if(x != i - 1){
                        tempOperation.add(operation.get(x));
                    }else{
                        tempOperation.add(thisOperation);
                        x += 2;
                    }
                }
                //now replace the base operation to the tempOperation
                operation.clear();
                operation.addAll(tempOperation);
                i = -1;
            }
            i++;
        }
        //now i need to do the same with the sum and subtract operation
        i = 0;
        while(operation.contains("+") || operation.contains("-")){
            String thisOperation = "";
            if(operation.get(i).equals("+") || operation.get(i).equals("-")){
                //now i need to make the operation with the back and forward number
                if (operation.get(i).equals("+")){
                    thisOperation = String.valueOf(Float.parseFloat(operation.get(i - 1)) + Float.parseFloat(operation.get(i + 1)));
                }else{
                    thisOperation = String.valueOf(Float.parseFloat(operation.get(i - 1)) - Float.parseFloat(operation.get(i + 1)));
                }
                //now i need to make another array for the result
                tempOperation.clear();
                for(int x = 0;x < operation.size();x++){
                    if(x != i - 1){
                        tempOperation.add(operation.get(x));
                    }else{
                        tempOperation.add(thisOperation);
                        x += 2;
                    }
                }
                //now replace the base operation to the tempOperation
                operation.clear();
                operation.addAll(tempOperation);
                i = -1;
            }
            i++;
        }
        result = Float.parseFloat(operation.get(0)) ;
        return result;
    }
}