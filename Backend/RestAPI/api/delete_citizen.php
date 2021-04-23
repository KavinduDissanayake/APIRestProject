<?php

  header('Access-Control-Allow-Origin: *');
  header('Content-Type: application/json');

  //access Control method PUT
  header('Access-Control-Allow-Methods: DELETE');
  header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

  //initaliaztion our api 
  include_once('../core/initialize.php');


  //instantiate user 
  $cdc = new  CDC($db);

  //get raw posted data
  $cdc->nic = isset($_POST['nic']) ? $_POST['nic'] : die();




//create user 
if($cdc->citizens_delete()){

    echo json_encode(
        array('message' => "Delete successful...!")
    );

}else {
    echo json_encode(
        array('message' => 'Delete Failed...!')
    );
}


?>