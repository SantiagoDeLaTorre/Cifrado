Cifrado
=======

Proyecto de clase, cifrado con claves simetricas y asimetricas.

Proyecto usado para probar github.

Enunciado Original:

AES – RSA - DSA

Se desea programar un servicio de red de descarga de información con transferencia segura. Necesitamos dos aplicaciones:

• Un servidor seguro, que almacenará archivos de texto.
• Un cliente seguro, que pedirá la descarga de archivos de texto.


El servidor almacenará una serie de archivos identificados por su nombre. Cuando el cliente se conecte, indicará el nombre del archivo que quiere descargar y, si existe, el servidor se los enviará. Los requisitos de seguridad son:

• La transferencia usará cifrado SIMETRICO AES. La clave de sesión la generará el cliente.
• Con esta clave simétrica, el cliente se la enviará al servidor de forma segura usando cifrado ASIMETRICO RSA, mediante un par de claves generadas en el 
servidor.
• Una vez que tanto el servidor como el cliente conocen la clave simétrica de cifrado, se enviará el fichero solicitado desde el servidor firmado con DSA.
• El cliente deberá verificar que el fichero recibido está correctamente firmado.
