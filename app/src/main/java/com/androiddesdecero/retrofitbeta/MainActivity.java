package com.androiddesdecero.retrofitbeta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androiddesdecero.retrofitbeta.model.GitHubRepo;
import com.androiddesdecero.retrofitbeta.service.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
    Retrofit es un cliente REST type save para Android y Java desarrollado por Square
    Permite hacer peticiones GET, POST, PUT, PATCH, DELETE y HEAD
    Retrofit no es la capa de networking, esta una por encima
    Retrofit usa OkHttp como capa de red
    Por defecto la implementación de retrofit incluye la de OkHttp -> si queremos otra versión la tendremos que poner
     */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Creamos una instancia del objeto retrofit con nuestra configuración Básica
        Retrofit retrofit = new Retrofit.Builder()
                //Hay que indicar la raíz del dominio. Esta es la manera más facil de que si cambiar de dominio no tengas que camibar todas las URL del servicio
                .baseUrl("https://api.github.com/")
                //Gson convierte Json objects en Java Objects
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Creamos la instancia Cliente con el objeto retrofit que hemos creado antes.
        Client client = retrofit.create(Client.class);

        //Creamos un objeto Call, que nos devolverá un objeto response del mismo tipo que tiene el Call
        Call<List<GitHubRepo>> call = client.reposForUser("albertoandroid");

        //fs-opensource

        //Nos permite hacer la solicitud en un hilo secundario. El callback se ejecutara una vez hayamos recibido respuesta desde el servidor, que puede ser onResponse si todo
        //ha salido correctamente o onFailure si ha habido algún tipo de fallo
        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                //vemos que recibimos la lista de objetos githburepo desde response.body();
                for(int i=0; i<response.body().size(); i++){
                    GitHubRepo repos = response.body().get(i);
                    Log.d("TAG", "Repositorio: " + i + " Nombre: " + repos.getName() +
                            " Avatar: " + repos.getOwner().getAvatarUrl() + " id: " + repos.getOwner().getLogin());

                }
                List<GitHubRepo> repos = response.body();

                Log.d("TAG", response.body().get(0).getName());
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

/*
El protocolo de tranferencia de hipertexto (HTTP) ha sido diseñado para habilitar las comunicaciones entre clientes y servidores
HTTP funciona como un protocolo de solicitud-respuesta entre el cliente y servidor. Es decir el cliente hace una solicitud y el servdor responde.
Ejemplo: un cliente (nuestra app) envía una solicitud HTTP al servidor, el servidor la procesa y devuelve un a respuesta al cliente. La respuesta
contine información de estado sobre la solictud y tambien puede contener contendido.

HTTP Methods:
GET
POST
PUT
HEAD
DELETE
PATCH
OPTIONS

The Get Method:
Get es uno de los metodos más comunes en HTTP.
Get se usa para solictud datos de un recurso específico
En get los parametros de consulta se envíen an la propia URL de la solictud GET. Son enviados en formato nombre/valor
Tenemos que tener en cuenta que no podemos utilizar GET cuando queremos enviar doatos confidenciales. Ejemplo Contraseñas y demás.
Get solo se utiliza para solicitud datos y no para modificarlos dentro del servidor.

El metodo Post
Post se utiliza para enviar datos a un servidor para crear o actualizar un recurso.
Cuando utilizamos Post los datos enviados al servidor se almacenana en el cuerpo de la solicutd HTTP a difirencia de GET que van en la URL.
Post es tambien uno de los metodos más utilizados en HTTP

El metodo PUT
Put se utilizar para enviar datos para actualizar un recurso o crear un recurso..
La diferencia entre POST y PUT esta en que las solicutudes PUT son Idempotentes y las Post no.
Idempotentes -> Quiere decir que cuando se hacen varias solicitud PUT el resultado siempre será el mismo. Por el contrario con Post cada vez que hacemos
una solicitud se creara el recurso varias veces.
PUT -> coloca un recurso en una URI espeficica. Si ya hay un recurso en dicha URI, PUT lo remplaza por el nuevo y si no hay un recurso lo crea.
POST -> envia un recurso a una URI espeficica y espera que el servidor maneje ese recurso. Es decir que es el servidor web el que determinar que acción tomar. Puede ser crear un nuevo recurso,
actualizar uno que ya existe. incluso puede borrar un recurso.
// Con PUT debes conocer la URL de destino que va a modificar, mientras que con POST no puedes conocer la URL y con los datos que le envias en Body haga lo que tiene que hacer.
https://stackoverflow.com/questions/107390/whats-the-difference-between-a-post-and-a-put-http-request

El metodo HEAD.
Head es identico al metodo GEt, pero con una peculiaridad no envia el cupero de respuesta.
Ejemplo: si el metodo get nos debuelve toda la lista de profesores, head nos devolvera la mimsa respuesta pero sin la lista de profesores
El uso más común es que antes de descargar un archivo pesado hagamos una solicitud HEAD y verificar si la respueta es la esperada y luego lanzar la petición GET.

El metodo DElete se utilizar para eliminar un recurso especificado.


https://www.w3schools.com/tags/ref_httpmessages.asp
https://developer.mozilla.org/es/docs/Web/HTTP/Status


Respuestas HTTP:
1.- Respuestas informativas: 1XX
2.- Peticiones correctas: 2XX
  200 OK -> Respuesta estándar para peticiones correctas
3.- Redirecciones 3XX
    El cliente tiene que tomar una acción adicional para completar la petición
4.- Errores del cliente 4XX
    400 Bad Request -> La solictud contiene sistaxis erronea
    401 Unauthorized -> No atuorizado al recurso
    403 Forbidden-> El cliente no tiene privilegios para hacerla.
    404 Not Found -> Recurso no encontrado
    405 Method Not Allowed -> Una Uri que espera un metodo Post no puedes acceder a ella a través de un metodo GET
5.- Errores del Servidor
    500 Internal Server Error -> El servidor se encuentra en un estado que no sabe como manejarlo


Cabecera HTTP -> Son los parametros que se envían en una petición o respueta a través del protocolo HTTP. Puden ser enviadas por el cliente o el servidor y proporcionana información
esencial sobre la transacción que se esta realizando.
La forma de transmitir esta información es a través de la sintaxix:
'Cabecera: Valor'
Cada petición HTTP tiene 2 Partes
1.-  Las cabeceras HEADERS
2.- El cuerpo del mensaje RESPONSE BODY   (Datos en formato JSON, o HTML, CSS, JAVASCRIPTT)

Extructura Cabecera HTTP en PETICIONES HTTP:
La primera línea de la solicitud HTTP tiene tres partes
1.- El metodo GET, Post, ETC
2.- La ruta o endPoint. Es lo que va despues del dominio. Por ejemplo https://www.udemy.com/curso-room-android-persistencia-de-datos-completa/
sería: /curso-room-android-persistencia-de-datos-completa/
3. El protocolo HTTP, en el que nos dice la versión:
Metodo         Ruta                                       Protocolo
GET /curso-room-android-persistencia-de-datos-completa/ http/1.1
El resto de la cabecera van en pares "NOMBRE: VALOR"
Principales Cabeceras:
Host: Debe indicar el dominio y subdominio si lo tuviera. Es decir la IP especifica
Host: udemy.com
User-Agent: Nombre del navegador y versión, Nombre sistema operativo y version.
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36
Accept: Indica el tipo de contenido que el cliente puede procesar. Ejemplo JSON, HTML, IMAGENES
Accept-Language: Lenguaje predeterminado por el usuario.
Accept-Language: es-ES,es;q=0.9
q valor de preferencia de legunaje por el usario.  0 minimo y 1 lo máximo
Accept-Encoding: gzip envia los datos del servidor comprimos y ocupando un 80% menos de su peso. Pero es el navegador el que le debe indicar en la cabecera que lo acepta. deflate y br son otros metodos
Accept-Encoding: gzip, deflate, br
Authorization: Contine las credenciales para autenticar a un usuario en el servidor.
Authorization: Basic YWxhZGRpbjpvcGVuc2VzYW1l


Cabeceras HTTP en Respuestas HTTP
Status: 200
Content-Type:  Indica el MIME-TYPE del cocumento. Es decir como son enviados los datos.
Content-Type: text/html; charset=UTF-8
Content-Type: application/json
Content-Encoding: gzip
WWW-Authenticate: Indica el metodo de autenticacion que se debe usar para tener acceso al rescurso. Va acompañado con un status 401
WWW-Authenticate: Basic




REST API teoria
API: Application Programing Inteface. Es algo que permite a una pieza de software hablar con otra.
REST API: REpresentacional State Transfer
Funcionamiento:
Cliente hace una llamada al Servidor a través del Protocolo HTTP
 */

