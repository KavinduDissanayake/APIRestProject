<?php

  //headers
  header('Access-Control-Allow-Origin: *');
  header('Content-Type: application/json');
 

  //initaliaztion our api 
  include_once('../core/initialize.php');


  //instantiate user 


  $user = new  User($db);


  $user->nic = isset($_GET['nic']) ? $_GET['nic'] : die();

  $user->read_user_data_for_phi();



  $user_data_arr=array(

            'uid'=>$user->uid,
            'nic'=>$user->nic,
            'name'=>$user->name,
            'email'=>$user->email,
            'dob'=>$user->dob,
            'no'=>$user->no,
            'street'=>$user->street,
            'city'=>$user->city,
            'phone_num'=>$user->phone_num,
            'status'=>$user->status,
            'current_lat'=>$user->current_lat,
            'current_lon'=>$user->current_lon,
            'img_path'=>$user->img_path
  );


  //make a josn 
  print_r(json_encode($user_data_arr));
 
?>