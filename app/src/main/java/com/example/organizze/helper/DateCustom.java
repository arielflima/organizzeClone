package com.example.organizze.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String dataAtual(){
        String dataString = new SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis());
        return dataString;
    }

    public static String mesAnoDataEscolhida(String data){
        String retornoData[] = data.split("/");
        String dia = retornoData[0];
        String mes = retornoData[1];
        String ano = retornoData[2];

        String mesAno = mes + ano;
        return mesAno;
    }
}
