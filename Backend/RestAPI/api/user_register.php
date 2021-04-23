<?php

  header('Access-Control-Allow-Origin: *');
  header('Content-Type: application/json');
  header('Access-Control-Allow-Methods: POST');
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
  //use password encryption 
  $user->password = isset($_POST['password']) ? md5($_POST['password']) : die();


  $user->status = isset($_POST['status']) ? $_POST['status'] : die();
  $user->user_role = isset($_POST['user_role']) ? $_POST['user_role'] : die();


//create user 



 
// echo "[\n";

if($user->user_regsiter()){

    echo json_encode(
        array('message' => "Register successfully ...!")
    );

}else {
    echo json_encode(
        array('message' => 'Register Failed...!  nic number alrady have an account !')
    );
}

// echo "]\n";
?>