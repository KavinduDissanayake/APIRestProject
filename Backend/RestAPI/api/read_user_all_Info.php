<?php

  header('Access-Control-Allow-Origin: *');
  header('Content-Type: application/json');
  header('Access-Control-Allow-Methods: POST');
  header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

  //initaliaztion our api 
  include_once('../core/initialize.php');


  //instantiate user 


  $user = new  User($db);


  //blog user  quary
  $result =$user -> read();

  //get the row count


  $num =  $result->rowCount();

  if($num >0){

    $user_arr= array();
    $user_arr['user_data']=array();

    while($row =$result ->fetch(PDO::FETCH_ASSOC)){
        extract($row);
        $user_item= array(

            'uid' =>$uid,
            'nic' =>$nic,
            'name' =>$name,
            'email' =>$email,
            'dob' =>$dob,
            'dob' =>$dob,
            'no' =>$no,
            'street' =>$street,
            'city' =>$city,
            'phone_num' =>$phone_num,
            'status' =>$status,
            'current_lat' =>$current_lat,
            'current_lon' =>$current_lon,
            'img_path' =>$img_path,

        );
    //    array_push($user_arr['user_data'],$user_item);
        array_push($user_arr['user_data'],$user_item);

    }

    //convet to json and output

    echo json_encode($user_arr);

    

  }else{


    echo json_encode(array('message'=>'no user found'));


  }




?>