<?php


$DB_SERVER="localhost"; #la direccion del servidor
$DB_USER="Xdmarta002"; #el usuario para esa base de datos
$DB_PASS="LW5afdHR"; #la clave para ese usuario
$DB_DATABASE="Xdmarta002_Entrenadores"; #la base de datos a la que hay que conectarse
# Se establece la conexion:
$con = mysqli_connect($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);

if (mysqli_connect_errno()) {
    echo 'Error de conexion: ' . mysqli_connect_error();
    exit();
}

$parametros = json_decode( file_get_contents( 'php://input' ), true );

$usuario = $parametros["usuario"];
$contrasena = $parametros["contrasena"];

// Preparación de la consulta SQL
$sql = "SELECT * FROM Entrenador WHERE Usuario = '$usuario' AND Contrasena = '$contrasena'";
$result = mysqli_query($con, $sql);

// Ejecución de la consulta SQL
if (mysqli_num_rows($result) > 0) {
    echo "true";
} else {
    echo "error";
}
$con->close();

?>