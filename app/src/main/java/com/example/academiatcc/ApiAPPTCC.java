package com.example.academiatcc;

public class ApiAPPTCC {
   private static final String ROOT_URL = "http://192.168.0.249/AcademiaAPI/v1/Api.php?apicall="; //Local API!!!!!

    //Create
    public static final String URL_CREATE_ALUNO = ROOT_URL + "createAluno";
    public static final String URL_GET_ALUNO = ROOT_URL + "getAluno";
    public static final String URL_UPDATE_ALUNO = ROOT_URL + "updateAluno";
}
