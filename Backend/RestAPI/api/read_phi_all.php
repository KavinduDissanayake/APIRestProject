<?php

  header('Access-Control-Allow-Origin: *');
  header('Content-Type: application/json');
  header('Access-Control-Allow-Methods: POST');
  header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

  //initaliaztion our api 
  include_once('../core/initialize.php');


  //instantiate phi 


  $phi = new  PHI($db);


  //blog phi  quary
  $result =$phi -> read();

  //get the row count


  $num =  $result->rowCount();

  if($num >0){

    $phi_arr= array();
    $phi_arr['phi_data']=array();

    while($row =$result ->fetch(PDO::FETCH_ASSOC)){
        extract($row);
        $phi_item= array(

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
    //    array_push($phi_arr['phi_data'],$phi_item);
        array_push($phi_arr['phi_data'],$phi_item);

    }

    //convet to json and output

    echo json_encode($phi_arr);

    

  }else{


    echo json_encode(array('message'=>'no phi found'));


  }




?>