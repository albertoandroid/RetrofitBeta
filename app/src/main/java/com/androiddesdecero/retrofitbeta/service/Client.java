package com.androiddesdecero.retrofitbeta.service;


import com.androiddesdecero.retrofitbeta.model.GitHubRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Client {

    /*
    Con retrofit podemos hacer nuestras conexiones HTTP de una forma sencilla a través de una interfaz, con todos los metodos que necesitamos.
    1.- Primero debemos anotar el metodo http que vamos a utilizar @GET, @POST, @PUT, @DELETE
    2.- Debemos anotar la ruta de acceso a nuestra api. El end-pint. Aunque puedes poner la ruta completa, lo ideal es excluir el dominio.
    3.- Cada función devuelve un objeto Call, que envuelve el objetio que queremos traernos con nuestro servicio. Es decir como viene la respuesta
    4.- Cada parametro de entrada tiene su propia anotación, para distinguir entre los diferentes parametros, como @Path,, @Query, @Body, Head.
    Lo bueno de retrofit es que vas a poder entender cualquier API de cualquier aplicación de Android y todo bien definido
     */

    /*
    @Path -> Una URL de solicitud puede ser actualizada dinámicamente utilizando bloques o parámetros de reemplazo en el metodo.
    Un bloque de reemplazo es un String rodeada por {data}. Hay que añadir la anotación @Path
     */
    @GET("/users/{user}/repos")
    Call<List<GitHubRepo>> reposForUser(@Path("user") String user);
}
