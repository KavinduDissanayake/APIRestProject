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
  $user->name = isset($_POST['name']) ? $_POST['name'] : die();
  $user->email = isset($_POST['email']) ? $_POST['email'] : die();
  $user->dob = isset($_POST['dob']) ? $_POST['dob'] : die();
  $user->no = isset($_POST['no']) ? $_POST['no'] : die();
  $user->street = isset($_POST['street']) ? $_POST['street'] : die();
  $user->city = isset($_POST['city']) ? $_POST['city'] : die();
  $user->phone_num = isset($_POST['phone_num']) ? $_POST['phone_num'] : die();



//create user 
if($user->user_update_info()){

    echo json_encode(
        array('message' => "Update successful...!")
    );

}else {
    echo json_encode(
        array('message' => 'Update Failed...!')
    );
}


?>