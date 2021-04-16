<?php

  header('Access-Control-Allow-Origin: *');
  header('Content-Type: application/json');

  //access Control method PUT
  header('Access-Control-Allow-Methods: PUT');
  header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

  //initaliaztion our api 
  include_once('../core/initialize.php');


  //instantiate user 
  $user = new  User($db);

  //get raw posted data
  $user->nic = isset($_POST['nic']) ? $_POST['nic'] : die();
  $user->current_lat = isset($_POST['lat']) ? $_POST['lat'] : die();
  $user->current_lon = isset($_POST['lon']) ? $_POST['lon'] : die();



//create user 
if($user->user_update_current_location()){

    echo json_encode(
        array('message' => "Update successful...!")
    );

}else {
    echo json_encode(
        array('message' => 'Update Failed...!')
    );
}


?>