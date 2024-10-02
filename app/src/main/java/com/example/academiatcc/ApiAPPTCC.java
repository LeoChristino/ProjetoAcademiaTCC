package com.example.academiatcc;

public class ApiAPPTCC {
   private static final String ROOT_URL = "http://10.67.96.95/heroeapi/v1/Api.php?apicall="; //Local API!!!!!

    //Create
    private static final String URL_createPersonal = ROOT_URL + "createPersonal";
    private static final String URL_createAluno = ROOT_URL + "createAluno";
    private static final String URL_createLista = ROOT_URL + "createLista";
    private static final String URL_createTreino = ROOT_URL + "createTreino";
    private static final String URL_createExercicio = ROOT_URL + "createExercicio";

    //Get
    private static final String URL_getPersonal = ROOT_URL + "getPersonal";
    private static final String URL_getAluno = ROOT_URL + "getAluno";
    private static final String URL_getLista = ROOT_URL + "getLista";
    private static final String URL_gettreino = ROOT_URL + "getTreino";
    private static final String URL_getExercicio = ROOT_URL + "getExercicio";

    //Update
    private static final String URL_updateTreino = ROOT_URL + "updateTreino";
    private static final String URL_updateLista = ROOT_URL + "updateLista";
    private static final String URL_updateExercicio = ROOT_URL + "updateExercicio";
    private static final String URL_updatePersonal = ROOT_URL + "updatePersonal";
    private static final String URL_updateAluno = ROOT_URL + "updateAluno";

    //Delete
    private static final String URL_deleteTreino = ROOT_URL + "deleteTreino";
    private static final String URL_deleteExercicio = ROOT_URL + "deleteExercicio";
    private static final String URL_deleteLista = ROOT_URL + "deleteLista";
}
