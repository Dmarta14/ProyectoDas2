<?php


$DB_SERVER="localhost"; #la direccion del servidor
$DB_USER="Xdmarta002"; #el usuario para esa base de datos
$DB_PASS="LW5afdHR"; #la clave para ese usuario
$DB_DATABASE="Xdmarta002_Entrenadores"; #la base de datos a la que hay que conectarse
# Se establece la conexion:
$con = mysqli_connect($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);
// Escribir el mensaje en el archivo

if (mysqli_connect_errno()) {
    
echo 'Error de conexion: ' . mysqli_connect_error();
exit();
}

$parametros = json_decode( file_get_contents( 'php://input' ), true );
$nombre = $parametros["nombre"];
$apellido = $parametros["apellido"];
$usuario = $parametros["usuario"];
$contrasena = $parametros["contrasena"];
$direccion = $parametros["direccion"];
$telefono = $parametros["ntelefono"];
$email = $parametros["email"];
$club = $parametros["club"];
//Sentencia
$sql = "INSERT INTO Entrenador (Nombre,Apellido,Usuario,Contrasena,Direccion,NTelefono,Email,Club) VALUES ('$nombre','$apellido','$usuario','$contrasena','$direccion','$telefono','$email','$club')";

// Ejecucion de la consulta SQL
if ($con->query($sql) === TRUE) {
    echo "true";
    
    
} else {
    echo "Error: " . $sql . "<br>" . $con->error;
    
}



// Cierre de la conexion
$con->close();



?>