<?php

  header('Access-Control-Allow-Origin: *');
  header('Content-Type: application/json');
  header('Access-Control-Allow-Methods: POST');
  header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

  //initaliaztion our api 
  include_once('../core/initialize.php');


  //instantiate phi 


  $phi = new  phi($db);

  $phi->nic = isset($_POST['nic']) ? $_POST['nic'] : die();

  //blog phi  quary
  $result =$phi -> read_citizen_history();

  //get the row count


  $num =  $result->rowCount();

  if($num >0){

    $phi_arr= array();
    $phi_arr['phi_data']=array();

    while($row =$result ->fetch(PDO::FETCH_ASSOC)){
        extract($row);
        $phi_item= array(

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
            'history_lat' =>$history_lat,
            'history_lon' =>$history_lon,

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