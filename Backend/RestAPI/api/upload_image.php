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
  $user->uid = isset($_POST['uid']) ? $_POST['uid'] : die();
  $user->encodedImage = isset($_POST['image']) ? $_POST['image'] : die();
 


//create user 
if($user->user_upload_profile_image()){

    echo json_encode(
        array('message' => "Update successful...!")
    );

}else {
    echo json_encode(
        array('message' => 'Update Failed...!')
    );
}


?>