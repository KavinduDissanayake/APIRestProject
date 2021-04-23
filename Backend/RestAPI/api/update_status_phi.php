<?php

  header('Access-Control-Allow-Origin: *');
  header('Content-Type: application/json');

  //access Control method PUT
  header('Access-Control-Allow-Methods: PUT');
  header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

  //initaliaztion our api 
  include_once('../core/initialize.php');


  //instantiate user 
  $phi = new  PHI($db);

  //get raw posted data
  $phi->nic = isset($_POST['nic']) ? $_POST['nic'] : die();
  $phi->status = isset($_POST['status']) ? $_POST['status'] : die();




//create user 
if($phi->user_status_update_phi()){

    echo json_encode(
        array('message' => "Update successful...!")
    );

}else {
    echo json_encode(
        array('message' => 'Update Failed...!')
    );
}


?>