# SBAdmin
__Spigot/Bungee Administrator__: Plugin para administración de networks Minecraft, Soporte para BungeeCord 1.8.x o superior y Spigot 1.8.x o superior.
## Módulos
- __Sync__: Sincronización de Comandos por protocolo MQTT.
- **Bungee Report UI**: Reportes interactivos mediante interfaz.
- **Bungee Report**: Reportes mediante comando.  
## Futuros módulos
* OpControl: Control de comandos para op mediante cifrado y sanción
* CommandBlocker: Bloqueo de commandos.
* AuditControl: Auditar comandos que ejecuta el Staff o jugadores.
## Dependencias
Depende de un MQTT broker, si no posees este servicio, no podrás sacar provecho de este plugin.
Este plugin esta pensado para administrar __Networks__ de Minecraft por ende solo te será util si tienes una __VPS__ o __Servidor dedicado__. 
## Instalación
1. Colocar el plugin ya sea en servidor Bukkit/Spigot o Bungeecord
2. Iniciar el servidor
3. Una vez iniciado el servidor, en la carpeta del plugin se verá el archivo `config.yml`, para que el sincronizador funcione se debe modificar los siguientes parametros.

   ````yaml
   server_id: 0      #Numero entre 1 y 9999
   server_name: ''   #Nombre del servidor
   ````

   Inicialmente vendrán con `server_id` será igual a `0` y `server_name`  seá igual a `''` 

   Los dos atributos deben ser únicos en toda la network. Caso contrario si estos coinciden con otro servidor los dos dejarán de sincronizar.

4. Ya modificados los dos valores debemos ejecutar el siguiente comando

   **Caso:** Spigot/Bukkit Server

   ````bash
   /sadmin reload
   ````

   **Caso:** BungeeCord Server

   ````bash
   /badmin reload
   ````

5. Si en la consola se ve un mensaje donde afirma que el plugin está 100% operativo, entonces la configuración es correcta

## Permisos
#### BungeeCord
Lista de permisos para Bungee

| Permiso        | Descripción                                                  | Usuario |
| :------------- | ------------------------------------------------------------ |---------|
| `report.command` | Permite usar el comando `/report` en los servidores Bukkit/Spigot y Bungee.  | Todos |
| `report.receive` | Permite recibir reportes de los usuarios | Staff |
| `report.goto`    | Permite teletrasportar al jugador que recibe el reporte hasta el servidor origen del mismo. | Staff |
| `sync.command`   | Permite ejecuar el comando /sync en los servidores y Bungee. | Owner |
| `reload.command` | Recarga la config,  si quisieras ejecutar reload como un usuario y no por la consola. | Owner |
| `list.command` | Permite usar el comando `/blist` y listar todos los jugadores online, solo disponible en `bungee_redis = true`| Owner | 

#### Spigot / Bukkit
Lista de permisos para Spigot/Bukkit Server

| Permiso        | Descripción                                                  | Usuario |
| :------------- | ------------------------------------------------------------ |---------|
| `opc.command`  | Permisos para ejecutar comandos de OpControl | Admins |

## Comandos

| Comando              | Descripción                                                  |
| -------------------- | ------------------------------------------------------------ |
| `/report <nick> `    | Reporta jugadores mediante UI |
| `/breport <nick> <reason>` | Reporta jugadores mediante comando |
| `/sync  <command>`   | Sincroniza comando en todos los servidores Spigot. Ejem. `/sync /list` |
| `/sync -b <command>` | Sincroniza commandos en todos los servidores BungeeCord. Ejem. `/sync -b /glist` |
| `/sadmin reload`     | Recarga las config del Servidor Spigot                       |
| `/badmin reload`     | Recarga las config del Servidor BungeeCord                   |
| `/goto <server>`     | Teleport a cualquier server                                  |

----

Autor: [Detzer_G](https://www.facebook.com/detzerg)

 ![alt text](https://minotar.net/avatar/Detzer_G/40 "Detzer_G")