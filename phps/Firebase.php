<?php

if(isset($_POST['token'])){
    $token=$_POST['token'];

    # Se declara la cabecera
    $cabecera = array(
    'Authorization:key=AAAAKVtpQig:APA91bFDDXl40YITlUg6TkocxvL1YVD21pZndBoEFhwznmzvCtqtzWxYLfJGZwoHKkNxJgJuWalhmZb2CQihC5oLCoNFFqY1qROK1Oy94C0OtlxvfwlKoGY_KJhTu0zXyFgwb_it0LKA',
    'Content-Type:application/json'
    );

    $msg= array(
    'to'=>$token,
    'notification'=> array(
        'body' => 'Foto anadida',
        'title' => 'Foto!',
        'icon' => 'ic_stat_ic_notification',
        'click_action'=>"AVISO")
    );
    # Se convierte a JSON
    $msgJSON= json_encode($msg);

    $ch = curl_init(); #inicializar el handler de curl
    #indicar el destino de la petición, el servicio FCM de google
    curl_setopt( $ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
    #indicar que la conexión es de tipo POST
    curl_setopt( $ch, CURLOPT_POST, true );
    #agregar las cabeceras
    curl_setopt( $ch, CURLOPT_HTTPHEADER, $cabecera);
    #Indicar que se desea recibir la respuesta a la conexión en forma de string
    curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
    #agregar los datos de la petición en formato JSON
    curl_setopt( $ch, CURLOPT_POSTFIELDS, $msgJSON );
    #ejecutar la llamada
    $resultado= curl_exec( $ch );
    #cerrar el handler de curl
    curl_close( $ch );

    if (curl_errno($ch)) {
    print curl_error($ch);
    }
    echo $resultado;}
    else{
    echo $resultado;
    }


?>